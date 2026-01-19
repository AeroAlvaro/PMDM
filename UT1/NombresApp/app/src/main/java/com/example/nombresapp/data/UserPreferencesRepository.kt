package com.example.nombresapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private val IS_SORT_ASC = booleanPreferencesKey("is_sort_asc")

    val isSortAsc: Flow<Boolean> = dataStore.data
        .catch {
            if(it is IOException) emit(emptyPreferences()) else throw it
        }
        .map { preferences ->
            preferences[IS_SORT_ASC] ?: true
        }

    suspend fun saveSortPreference(isAsc: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_SORT_ASC] = isAsc
        }
    }
}