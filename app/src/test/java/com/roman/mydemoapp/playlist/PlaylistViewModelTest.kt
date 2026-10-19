package com.roman.mydemoapp.playlist

import androidx.lifecycle.SavedStateHandle
import com.roman.mydemoapp.navigation.Routes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaylistViewModelTest {
    private val url = "https://music.youtube.com/playlist?list=PL123"

    @Test
    fun readsUrlFromSavedStateHandle() {
        val viewModel = PlaylistViewModel(SavedStateHandle(mapOf(Routes.PLAYLIST_ARG to url)))
        assertEquals(url, viewModel.uiState.value.url)
    }

    @Test
    fun shareEmitsEventWithUrl() = runTest {
        val viewModel = PlaylistViewModel(SavedStateHandle(mapOf(Routes.PLAYLIST_ARG to url)))
        viewModel.onShareClick()
        assertEquals(PlaylistEvent.Share(url), viewModel.events.first())
    }
}
