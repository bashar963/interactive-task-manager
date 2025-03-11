package tech.devbashar.interactivetaskmanager.presentation.screens.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import tech.devbashar.interactivetaskmanager.domain.repository.UserPrefRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        userPrefRepository: UserPrefRepository,
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
        val useSystemDefault: StateFlow<Boolean> =
            userPrefRepository.useSystemDefault()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = false,
                )
    }
