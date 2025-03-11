package tech.devbashar.interactivetaskmanager.presentation.screens.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tech.devbashar.interactivetaskmanager.domain.repository.UserPrefRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val userPrefRepository: UserPrefRepository,
    ) : ViewModel() {
        val primaryColor: StateFlow<Color> =
            userPrefRepository.getPrimaryColor()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = Color.Unspecified,
                )
        val isDarkMode: StateFlow<Boolean> =
            userPrefRepository.useDarkMode()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = false,
                )

        val useSystemDefaults: StateFlow<Boolean> =
            userPrefRepository.useSystemDefault()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = false,
                )

        fun setDarkMode(isDarkMode: Boolean) {
            viewModelScope.launch {
                userPrefRepository.setDarkMode(isDarkMode)
            }
        }

        fun setPrimaryColor(color: Color) {
            viewModelScope.launch {
                userPrefRepository.setPrimaryColor(color)
            }
        }

        fun setUseSystemDefault(useSystemDefault: Boolean) {
            viewModelScope.launch {
                userPrefRepository.setSystemDefault(useSystemDefault)
            }
        }
    }
