package com.roman.mydemoapp.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsStore {
    val roastLevel: Flow<RoastLevel>
    suspend fun setRoastLevel(level: RoastLevel)
}

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreSettingsStore(context: Context) : SettingsStore {
    private val dataStore = context.applicationContext.settingsDataStore

    override val roastLevel: Flow<RoastLevel> = dataStore.data.map { prefs ->
        RoastLevel.entries.getOrNull(prefs[ROAST_LEVEL] ?: -1) ?: DEFAULT_LEVEL
    }

    override suspend fun setRoastLevel(level: RoastLevel) {
        dataStore.edit { it[ROAST_LEVEL] = level.ordinal }
    }

    private companion object {
        val ROAST_LEVEL = intPreferencesKey("roast_level")
    }
}

val DEFAULT_LEVEL = RoastLevel.Medium
