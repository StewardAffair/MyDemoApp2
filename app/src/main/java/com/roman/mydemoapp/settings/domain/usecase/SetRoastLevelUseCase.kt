package com.roman.mydemoapp.settings.domain.usecase

import com.roman.mydemoapp.settings.domain.model.RoastLevel
import com.roman.mydemoapp.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetRoastLevelUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(level: RoastLevel) = repository.setRoastLevel(level)
}
