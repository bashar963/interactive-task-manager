package tech.devbashar.interactivetaskmanager.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.devbashar.interactivetaskmanager.domain.repository.UserPrefRepository
import javax.inject.Inject

class UserPrefRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : UserPrefRepository {
        private companion object {
            val PRIMARY_COLOR = intPreferencesKey("primary_color")
            val DARK_MODE = booleanPreferencesKey("dark_mode")
            val SYSTEM_DEFAULT = booleanPreferencesKey("system_default")
        }

        override fun getPrimaryColor(): Flow<Color> =
            dataStore.data.map { preferences ->
                preferences[PRIMARY_COLOR]?.let { Color(it) } ?: Color.Unspecified
            }

        override suspend fun setPrimaryColor(color: Color) {
            dataStore.edit { preferences ->
                preferences[PRIMARY_COLOR] = color.toArgb()
            }
        }

        override fun useDarkMode(): Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[DARK_MODE] ?: false
            }

        override suspend fun setDarkMode(isDarkMode: Boolean) {
            dataStore.edit { preferences ->
                preferences[DARK_MODE] = isDarkMode
            }
        }

        override fun useSystemDefault(): Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[SYSTEM_DEFAULT] ?: false
            }

        override suspend fun setSystemDefault(isSystemDefault: Boolean) {
            dataStore.edit { preferences ->
                preferences[SYSTEM_DEFAULT] = isSystemDefault
            }
        }
    }
