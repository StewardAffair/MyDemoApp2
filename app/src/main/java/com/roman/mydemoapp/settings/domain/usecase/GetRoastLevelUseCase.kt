package com.roman.mydemoapp.settings.domain.usecase

import com.roman.mydemoapp.settings.domain.model.RoastLevel
import com.roman.mydemoapp.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetRoastLevelUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<RoastLevel> = repository.roastLevel
}
