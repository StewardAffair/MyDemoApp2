package com.roman.mydemoapp.settings.domain.usecase

import com.roman.mydemoapp.settings.domain.model.RoastLevel
import com.roman.mydemoapp.settings.domain.repository.SettingsRepository

class SetRoastLevelUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(level: RoastLevel) = repository.setRoastLevel(level)
}
