package com.roman.mydemoapp.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/** Raw DataStore access; stores the roast level as its ordinal, or null when nothing is saved. */
@Singleton
class SettingsDataSource @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.applicationContext.settingsDataStore

    val roastLevelOrdinal: Flow<Int?> = dataStore.data.map { it[ROAST_LEVEL] }

    suspend fun setRoastLevelOrdinal(ordinal: Int) {
        dataStore.edit { it[ROAST_LEVEL] = ordinal }
    }

    private companion object {
        val ROAST_LEVEL = intPreferencesKey("roast_level")
    }
}
