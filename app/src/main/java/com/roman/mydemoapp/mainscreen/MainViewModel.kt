package com.roman.mydemoapp.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.mydemoapp.history.domain.model.SavedRoast
import com.roman.mydemoapp.history.domain.usecase.DeleteRoastUseCase
import com.roman.mydemoapp.history.domain.usecase.ObserveRecentRoastsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val RECENT_LIMIT = 5

data class MainUiState(val recent: List<SavedRoast> = emptyList())

class MainViewModel(
    observeRecent: ObserveRecentRoastsUseCase,
    private val deleteRoast: DeleteRoastUseCase
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = observeRecent(RECENT_LIMIT)
        .map { MainUiState(recent = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState())

    fun onDeleteClick(id: Long) {
        viewModelScope.launch { deleteRoast(id) }
    }
}
