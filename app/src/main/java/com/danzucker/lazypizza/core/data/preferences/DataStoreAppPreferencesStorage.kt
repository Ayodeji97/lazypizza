package com.danzucker.lazypizza.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.danzucker.lazypizza.core.domain.preferences.AppPreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreAppPreferencesStorage(
    private val dataStore: DataStore<Preferences>
) : AppPreferencesStorage {

    override suspend fun hasSeededData(): Boolean {
        return try {
            dataStore.data.map { preferences ->
                preferences[KEY_HAS_SEEDED_DATA] ?: false
            }.first()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun setHasSeededData(value: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[KEY_HAS_SEEDED_DATA] = value
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val KEY_HAS_SEEDED_DATA = booleanPreferencesKey("key_has_seeded_data")
    }
}