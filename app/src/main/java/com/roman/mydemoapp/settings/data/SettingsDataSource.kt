package com.roman.mydemoapp.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/** Raw DataStore access; stores the roast level as its ordinal, or null when nothing is saved. */
class SettingsDataSource(context: Context) {
    private val dataStore = context.applicationContext.settingsDataStore

    val roastLevelOrdinal: Flow<Int?> = dataStore.data.map { it[ROAST_LEVEL] }

    suspend fun setRoastLevelOrdinal(ordinal: Int) {
        dataStore.edit { it[ROAST_LEVEL] = ordinal }
    }

    private companion object {
        val ROAST_LEVEL = intPreferencesKey("roast_level")
    }
}
