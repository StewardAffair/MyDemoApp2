package com.roman.mydemoapp.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.mydemoapp.history.domain.model.SavedRoast
import com.roman.mydemoapp.history.domain.usecase.GetSavedRoastUseCase
import com.roman.mydemoapp.history.domain.usecase.SaveRoastUseCase
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

    /** A roast already stored in history; shown from the database without any network call. */
    data class Saved(val id: Long) : PlaylistSource

    companion object {
        fun from(savedStateHandle: SavedStateHandle): PlaylistSource {
            savedStateHandle.get<Long>(Routes.SAVED_ARG)?.let { return Saved(it) }
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
    val error: RoastError? = null,
    val canRoastAgain: Boolean = true
)

sealed interface PlaylistEvent {
    data class Share(val text: String) : PlaylistEvent
}

class PlaylistViewModel(
    private val source: PlaylistSource,
    private val roastPlaylist: RoastPlaylistUseCase,
    private val roastScreenshot: RoastScreenshotUseCase,
    private val saveRoast: SaveRoastUseCase,
    private val getSavedRoast: GetSavedRoastUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState(fromScreenshot = source is PlaylistSource.Screenshot))
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    private val _events = Channel<PlaylistEvent>(Channel.BUFFERED)
    val events: Flow<PlaylistEvent> = _events.receiveAsFlow()

    private var roastJob: Job? = null

    // What "roast again" re-runs; null for a saved screenshot, whose image is gone.
    private var roastSource: PlaylistSource? = source.takeUnless { it is PlaylistSource.Saved }
    private var savedId: Long? = (source as? PlaylistSource.Saved)?.id

    init {
        if (source is PlaylistSource.Saved) loadSaved(source.id) else roast()
    }

    fun onShareClick() {
        _uiState.value.roasted?.let { _events.trySend(PlaylistEvent.Share(it.text)) }
    }

    fun onRoastAgainClick() = roast()

    private fun loadSaved(id: Long) {
        viewModelScope.launch {
            val saved = getSavedRoast(id)
            if (saved == null) {
                _uiState.update { it.copy(isLoading = false, error = RoastError.Unknown, canRoastAgain = false) }
                return@launch
            }
            roastSource = saved.sourceRef?.let { PlaylistSource.Link(it) }
            _uiState.update {
                it.copy(
                    fromScreenshot = saved.fromScreenshot,
                    isLoading = false,
                    roasted = saved.roasted,
                    canRoastAgain = roastSource != null
                )
            }
        }
    }

    private fun roast() {
        val target = roastSource ?: return
        roastJob?.cancel()
        _uiState.update { it.copy(isLoading = true, error = null) }
        roastJob = viewModelScope.launch {
            try {
                val roasted = when (target) {
                    is PlaylistSource.Link -> roastPlaylist(target.url)
                    is PlaylistSource.Screenshot -> roastScreenshot(target.uri)
                    is PlaylistSource.Saved -> throw RoastException(RoastError.Unknown)
                }
                _uiState.update { it.copy(isLoading = false, roasted = roasted) }
                savedId = saveRoast(
                    SavedRoast(
                        id = savedId ?: 0,
                        roasted = roasted,
                        fromScreenshot = target is PlaylistSource.Screenshot,
                        sourceRef = (target as? PlaylistSource.Link)?.url,
                        createdAt = System.currentTimeMillis()
                    )
                )
            } catch (e: RoastException) {
                _uiState.update { it.copy(isLoading = false, roasted = null, error = e.error) }
            }
        }
    }
}
