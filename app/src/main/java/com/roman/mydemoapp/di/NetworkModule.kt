package com.roman.mydemoapp.di

import com.roman.mydemoapp.BuildConfig
import com.roman.mydemoapp.roast.data.ClaudeService
import com.roman.mydemoapp.roast.data.YoutubeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CLAUDE_BASE_URL = "https://api.anthropic.com/"
private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/"

/** Shared OkHttp client, JSON config and the Retrofit services built on them. */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .callTimeout(90, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideClaudeService(client: OkHttpClient, json: Json): ClaudeService =
        retrofit(CLAUDE_BASE_URL, client, json).create(ClaudeService::class.java)

    @Provides
    @Singleton
    fun provideYoutubeService(client: OkHttpClient, json: Json): YoutubeService =
        retrofit(YOUTUBE_BASE_URL, client, json).create(YoutubeService::class.java)

    @Provides
    @AnthropicApiKey
    fun provideAnthropicApiKey(): String = BuildConfig.ANTHROPIC_API_KEY

    @Provides
    @YoutubeApiKey
    fun provideYoutubeApiKey(): String = BuildConfig.YOUTUBE_API_KEY

    private fun retrofit(baseUrl: String, client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
