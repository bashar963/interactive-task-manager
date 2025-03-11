package tech.devbashar.interactivetaskmanager.presentation.screens.task.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun TaskCreateScreen(
    viewModel: TaskCreateViewModel = hiltViewModel(),
    id: String?,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = id) {
        id?.let { viewModel.getTask(it) }
    }
    Scaffold(
        topBar = {
            CreateTaskAppBar(
                navController = navController,
                title = if (id == null) "Create New Task" else "Edit Task",
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CreateTaskForm(
                isEdit = id != null,
                onTaskCreate = {
                    scope.launch {
                        if (id != null) {
                            if (viewModel.editTask()) {
                                navController.popBackStack()
                            }
                        } else {
                            if (viewModel.createTask()) {
                                navController.popBackStack()
                            }
                        }
                    }
                },
            )
        }
    }
}
