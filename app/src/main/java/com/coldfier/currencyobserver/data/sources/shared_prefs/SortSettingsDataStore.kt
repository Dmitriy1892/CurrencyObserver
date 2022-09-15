package com.coldfier.currencyobserver.data.sources.shared_prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import com.coldfier.currencyobserver.di.SortDatastore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SortSettingsDataStore @Inject constructor(
    @SortDatastore private val dataStore: DataStore<Preferences>
) {
    companion object {
        val SORT_BY = stringPreferencesKey("sortBy")
    }

    val sortSettingsFlow: Flow<SortSettings> = dataStore.data
        .map { preferences ->
            val savedSettings = preferences[SORT_BY] ?: ""

            SortSettings.values().firstOrNull { it.name == savedSettings } ?: SortSettings.NONE
        }
        .catch { emit(SortSettings.NONE) }

    suspend fun saveSettings(sortSettings: SortSettings) {
        dataStore.edit { preferences ->
            preferences[SORT_BY] = sortSettings.name
        }
    }
}