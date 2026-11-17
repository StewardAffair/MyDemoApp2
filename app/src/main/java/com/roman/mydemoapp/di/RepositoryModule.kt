package com.roman.mydemoapp.di

import com.roman.mydemoapp.history.data.HistoryRepositoryImpl
import com.roman.mydemoapp.history.domain.repository.HistoryRepository
import com.roman.mydemoapp.roast.data.PlaylistRepositoryImpl
import com.roman.mydemoapp.roast.data.RoastRepositoryImpl
import com.roman.mydemoapp.roast.domain.repository.PlaylistRepository
import com.roman.mydemoapp.roast.domain.repository.RoastRepository
import com.roman.mydemoapp.settings.data.SettingsRepositoryImpl
import com.roman.mydemoapp.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository

    @Binds
    abstract fun bindPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    @Binds
    abstract fun bindRoastRepository(impl: RoastRepositoryImpl): RoastRepository
}
