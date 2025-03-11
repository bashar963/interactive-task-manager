package tech.devbashar.interactivetaskmanager.domain.repository

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow

interface UserPrefRepository {
    fun getPrimaryColor(): Flow<Color>

    suspend fun setPrimaryColor(color: Color)

    fun useDarkMode(): Flow<Boolean>

    suspend fun setDarkMode(isDarkMode: Boolean)

    fun useSystemDefault(): Flow<Boolean>

    suspend fun setSystemDefault(isSystemDefault: Boolean)
}
