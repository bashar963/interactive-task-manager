package tech.devbashar.interactivetaskmanager.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    val primaryColor = viewModel.primaryColor.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val useSystemDefault by viewModel.useSystemDefaults.collectAsState()
    var showColorPicker by remember { mutableStateOf(false) }
    ColorPickerDialog(
        show = showColorPicker,
        onDismissRequest = { showColorPicker = false },
        type = ColorPickerType.Circle(),
    ) {
        scope.launch {
            viewModel.setPrimaryColor(it)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            ListItem(
                headlineContent = {
                    Text("Use device theme mode")
                },
                leadingContent = {
                    Checkbox(
                        checked = useSystemDefault,
                        onCheckedChange = { viewModel.setUseSystemDefault(it) },
                    )
                },
            )
            ListItem(
                headlineContent = {
                    Text("Dark Mode")
                },
                trailingContent = {
                    Switch(
                        checked = isDarkMode,
                        enabled = !useSystemDefault,
                        onCheckedChange = { viewModel.setDarkMode(it) },
                    )
                },
            )
            ListItem(
                modifier = Modifier.clickable { showColorPicker = true },
                headlineContent = {
                    Text("Primary Color")
                },
                leadingContent = {
                    Box(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .background(
                                    if (primaryColor.value == Color.Unspecified) MaterialTheme.colorScheme.primary else primaryColor.value,
                                )
                                .size(36.dp),
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Primary Color",
                    )
                },
            )
        }
    }
}
