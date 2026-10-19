package com.roman.mydemoapp.settings

import com.roman.mydemoapp.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private class FakeStore(initial: RoastLevel) : SettingsStore {
        val level = MutableStateFlow(initial)
        override val roastLevel: Flow<RoastLevel> = level
        override suspend fun setRoastLevel(level: RoastLevel) {
            this.level.value = level
        }
    }

    @Test
    fun loadsSavedLevel() {
        val viewModel = SettingsViewModel(FakeStore(RoastLevel.WellDone))
        assertEquals(RoastLevel.WellDone, viewModel.uiState.value.level)
    }

    @Test
    fun levelChangeUpdatesStateAndPersists() {
        val store = FakeStore(RoastLevel.Medium)
        val viewModel = SettingsViewModel(store)
        viewModel.onLevelChange(RoastLevel.Rare.ordinal)
        assertEquals(RoastLevel.Rare, viewModel.uiState.value.level)
        assertEquals(RoastLevel.Rare, store.level.value)
    }

    @Test
    fun outOfRangeLevelIsIgnored() {
        val viewModel = SettingsViewModel(FakeStore(RoastLevel.Medium))
        viewModel.onLevelChange(99)
        assertEquals(RoastLevel.Medium, viewModel.uiState.value.level)
    }
}
