package com.roman.mydemoapp.di

import androidx.room.Room
import com.roman.mydemoapp.BuildConfig
import com.roman.mydemoapp.history.data.AppDatabase
import com.roman.mydemoapp.history.data.HistoryRepositoryImpl
import com.roman.mydemoapp.history.domain.repository.HistoryRepository
import com.roman.mydemoapp.history.domain.usecase.DeleteRoastUseCase
import com.roman.mydemoapp.history.domain.usecase.GetSavedRoastUseCase
import com.roman.mydemoapp.history.domain.usecase.ObserveRecentRoastsUseCase
import com.roman.mydemoapp.history.domain.usecase.SaveRoastUseCase
import com.roman.mydemoapp.mainscreen.MainViewModel
import com.roman.mydemoapp.navigation.LinkViewModel
import com.roman.mydemoapp.playlist.PlaylistViewModel
import com.roman.mydemoapp.roast.data.ClaudeApi
import com.roman.mydemoapp.roast.data.ClaudeService
import com.roman.mydemoapp.roast.data.PlaylistRepositoryImpl
import com.roman.mydemoapp.roast.data.RoastRepositoryImpl
import com.roman.mydemoapp.roast.data.ScreenshotLoader
import com.roman.mydemoapp.roast.data.YoutubeApi
import com.roman.mydemoapp.roast.data.YoutubeService
import com.roman.mydemoapp.roast.domain.repository.PlaylistRepository
import com.roman.mydemoapp.roast.domain.repository.RoastRepository
import com.roman.mydemoapp.roast.domain.usecase.RoastPlaylistUseCase
import com.roman.mydemoapp.roast.domain.usecase.RoastScreenshotUseCase
import com.roman.mydemoapp.settings.data.SettingsDataSource
import com.roman.mydemoapp.settings.data.SettingsRepositoryImpl
import com.roman.mydemoapp.settings.domain.repository.SettingsRepository
import com.roman.mydemoapp.settings.domain.usecase.GetRoastLevelUseCase
import com.roman.mydemoapp.settings.domain.usecase.SetRoastLevelUseCase
import com.roman.mydemoapp.settings.presentation.SettingsViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val CLAUDE_BASE_URL = "https://api.anthropic.com/"
private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/"

/** Shared OkHttp client, JSON config and the Retrofit services built on them. */
internal val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
        }
    }
    single {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)
            .build()
    }
    single {
        ClaudeApi(
            service = retrofit(CLAUDE_BASE_URL, get(), get()).create(ClaudeService::class.java),
            apiKey = BuildConfig.ANTHROPIC_API_KEY
        )
    }
    single {
        YoutubeApi(
            service = retrofit(YOUTUBE_BASE_URL, get(), get()).create(YoutubeService::class.java),
            apiKey = BuildConfig.YOUTUBE_API_KEY
        )
    }
}

internal val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "playlist_roast.db").build()
    }
    single { get<AppDatabase>().roastDao() }
    single { SettingsDataSource(androidContext()) }
    single { ScreenshotLoader(androidContext()) }

    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
    singleOf(::HistoryRepositoryImpl) bind HistoryRepository::class
    singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class
    singleOf(::RoastRepositoryImpl) bind RoastRepository::class
}

internal val domainModule = module {
    factoryOf(::GetRoastLevelUseCase)
    factoryOf(::SetRoastLevelUseCase)
    factoryOf(::RoastPlaylistUseCase)
    factoryOf(::RoastScreenshotUseCase)
    factoryOf(::ObserveRecentRoastsUseCase)
    factoryOf(::GetSavedRoastUseCase)
    factoryOf(::SaveRoastUseCase)
    factoryOf(::DeleteRoastUseCase)
}

internal val presentationModule = module {
    viewModelOf(::LinkViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    // Resolves its SavedStateHandle (the nav arguments) from the NavBackStackEntry.
    viewModelOf(::PlaylistViewModel)
}

val appModules = listOf(networkModule, dataModule, domainModule, presentationModule)

private fun retrofit(baseUrl: String, client: OkHttpClient, json: Json): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
