package com.roman.mydemoapp.settings.domain.repository

import com.roman.mydemoapp.settings.domain.model.RoastLevel
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val roastLevel: Flow<RoastLevel>
    suspend fun setRoastLevel(level: RoastLevel)
}
