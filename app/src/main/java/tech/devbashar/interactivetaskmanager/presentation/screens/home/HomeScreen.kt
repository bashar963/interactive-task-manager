package tech.devbashar.interactivetaskmanager.presentation.screens.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.devbashar.interactivetaskmanager.presentation.navigation.NavigationStack
import tech.devbashar.interactivetaskmanager.presentation.theme.AppTheme

@AndroidEntryPoint
class HomeScreen : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val primaryColor = viewModel.primaryColor.collectAsState()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val useSystemDefault by viewModel.useSystemDefault.collectAsState()
            AppTheme(
                seedColor = primaryColor.value,
                darkTheme = if (useSystemDefault) isSystemInDarkTheme() else isDarkMode,
            ) {
                NavigationStack(navController)
            }
        }
    }
}
