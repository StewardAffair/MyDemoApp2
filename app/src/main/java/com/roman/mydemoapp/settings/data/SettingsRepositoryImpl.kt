package com.roman.mydemoapp.settings.data

import com.roman.mydemoapp.settings.domain.model.DEFAULT_LEVEL
import com.roman.mydemoapp.settings.domain.model.RoastLevel
import com.roman.mydemoapp.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val dataSource: SettingsDataSource) : SettingsRepository {
    override val roastLevel: Flow<RoastLevel> = dataSource.roastLevelOrdinal.map { ordinal ->
        RoastLevel.entries.getOrNull(ordinal ?: -1) ?: DEFAULT_LEVEL
    }

    override suspend fun setRoastLevel(level: RoastLevel) {
        dataSource.setRoastLevelOrdinal(level.ordinal)
    }
}
