package com.roman.mydemoapp.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.roman.mydemoapp.navigation.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

// Playlist details are placeholder until the link is parsed; only the received [url] is real.
data class PlaylistUiState(val url: String = "")

sealed interface PlaylistEvent {
    data class Share(val text: String) : PlaylistEvent
}

class PlaylistViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PlaylistUiState(url = savedStateHandle.get<String>(Routes.PLAYLIST_ARG).orEmpty())
    )
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    private val _events = Channel<PlaylistEvent>(Channel.BUFFERED)
    val events: Flow<PlaylistEvent> = _events.receiveAsFlow()

    fun onShareClick() {
        _events.trySend(PlaylistEvent.Share(_uiState.value.url))
    }
}
