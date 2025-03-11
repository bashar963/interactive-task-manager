package tech.devbashar.interactivetaskmanager.presentation.screens.task.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import tech.devbashar.interactivetaskmanager.presentation.components.widgets.ShimmerLoading
import tech.devbashar.interactivetaskmanager.presentation.navigation.Screen
import tech.devbashar.interactivetaskmanager.presentation.screens.home.AppTopBar

@Composable
fun TaskListScreen(
    navController: NavHostController,
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    val tasks by viewModel.tasks.collectAsState()
    val progressTasks by viewModel.progressTasks.collectAsState()
    val pendingTasks by viewModel.pendingTasks.collectAsState()
    val isLoaded by viewModel.isLoaded.collectAsState()
    Scaffold(
        topBar = {
            AppTopBar(
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag("create_task_fab"),
                onClick = {
                    navController.navigate(Screen.TaskCreate.route + "/null")
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background,
            ) {
                Icon(
                    imageVector = FeatherIcons.Plus,
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Add",
                )
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                if (!isLoaded) {
                    TaskHeader(
                        completedTasks = progressTasks.count { it.isCompleted },
                        totalTasks = progressTasks.size,
                    )
                    PendingTasks(tasks = pendingTasks) { task, x, y ->
                        navController.navigate(Screen.TaskDetail.route + "/${task.id}/$x/$y")
                    }
                    TaskList(
                        tasks = tasks,
                        onAddTask = {
                            navController.navigate(Screen.TaskCreate.route + "/null")
                        },
                        onSelectTask = { task, x, y ->
                            navController.navigate(Screen.TaskDetail.route + "/${task.id}/$x/$y")
                        },
                    )
                } else {
                    HomeLoading()
                }
            }
        }
    }
}

@Composable
fun HomeLoading() {
    Column {
        ShimmerLoading(
            modifier =
                Modifier
                    .padding(16.dp)
                    .size(140.dp),
        )
        Row {
            ShimmerLoading(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .size(140.dp),
            )
            ShimmerLoading(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .size(140.dp),
            )
            ShimmerLoading(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .size(140.dp),
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            ShimmerLoading(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .size(60.dp),
            )
            ShimmerLoading(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .size(60.dp),
            )
            ShimmerLoading(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .size(60.dp),
            )
        }
    }
}
