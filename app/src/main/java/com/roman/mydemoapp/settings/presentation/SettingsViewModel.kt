package com.roman.mydemoapp.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.mydemoapp.settings.domain.model.DEFAULT_LEVEL
import com.roman.mydemoapp.settings.domain.model.RoastLevel
import com.roman.mydemoapp.settings.domain.usecase.GetRoastLevelUseCase
import com.roman.mydemoapp.settings.domain.usecase.SetRoastLevelUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(val level: RoastLevel = DEFAULT_LEVEL)

class SettingsViewModel(
    private val getRoastLevel: GetRoastLevelUseCase,
    private val setRoastLevel: SetRoastLevelUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = getRoastLevel().first()
            _uiState.update { it.copy(level = saved) }
        }
    }

    fun onLevelChange(index: Int) {
        val level = RoastLevel.entries.getOrNull(index) ?: return
        _uiState.update { it.copy(level = level) }
        viewModelScope.launch { setRoastLevel(level) }
    }
}
