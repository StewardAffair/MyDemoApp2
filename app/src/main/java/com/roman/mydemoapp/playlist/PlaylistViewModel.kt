package com.roman.mydemoapp.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.mydemoapp.navigation.Routes
import com.roman.mydemoapp.roast.domain.model.RoastError
import com.roman.mydemoapp.roast.domain.model.RoastException
import com.roman.mydemoapp.roast.domain.model.RoastedPlaylist
import com.roman.mydemoapp.roast.domain.usecase.RoastPlaylistUseCase
import com.roman.mydemoapp.roast.domain.usecase.RoastScreenshotUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PlaylistSource {
    data class Link(val url: String) : PlaylistSource
    data class Screenshot(val uri: String) : PlaylistSource

    companion object {
        fun from(savedStateHandle: SavedStateHandle): PlaylistSource {
            val uri = savedStateHandle.get<String>(Routes.SCREENSHOT_ARG)
            return if (uri != null) {
                Screenshot(uri)
            } else {
                Link(savedStateHandle.get<String>(Routes.PLAYLIST_ARG).orEmpty())
            }
        }
    }
}

data class PlaylistUiState(
    val fromScreenshot: Boolean = false,
    val isLoading: Boolean = true,
    val roasted: RoastedPlaylist? = null,
    val error: RoastError? = null
)

sealed interface PlaylistEvent {
    data class Share(val text: String) : PlaylistEvent
}

class PlaylistViewModel(
    private val source: PlaylistSource,
    private val roastPlaylist: RoastPlaylistUseCase,
    private val roastScreenshot: RoastScreenshotUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState(fromScreenshot = source is PlaylistSource.Screenshot))
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    private val _events = Channel<PlaylistEvent>(Channel.BUFFERED)
    val events: Flow<PlaylistEvent> = _events.receiveAsFlow()

    private var roastJob: Job? = null

    init {
        roast()
    }

    fun onShareClick() {
        _uiState.value.roasted?.let { _events.trySend(PlaylistEvent.Share(it.text)) }
    }

    fun onRoastAgainClick() = roast()

    private fun roast() {
        roastJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }
        roastJob = viewModelScope.launch {
            try {
                val roasted = when (source) {
                    is PlaylistSource.Link -> roastPlaylist(source.url)
                    is PlaylistSource.Screenshot -> roastScreenshot(source.uri)
                }
                _uiState.update { it.copy(isLoading = false, roasted = roasted) }
            } catch (e: RoastException) {
                _uiState.update { it.copy(isLoading = false, roasted = null, error = e.error) }
            }
        }
    }
}
