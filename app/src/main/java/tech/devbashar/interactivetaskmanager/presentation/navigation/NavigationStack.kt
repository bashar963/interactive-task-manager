package tech.devbashar.interactivetaskmanager.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import tech.devbashar.interactivetaskmanager.presentation.screens.settings.SettingsScreen
import tech.devbashar.interactivetaskmanager.presentation.screens.task.create.TaskCreateScreen
import tech.devbashar.interactivetaskmanager.presentation.screens.task.details.TaskDetailsScreen
import tech.devbashar.interactivetaskmanager.presentation.screens.task.list.TaskListScreen

@Composable
fun NavigationStack(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route,
    ) {
        composable(route = Screen.TaskList.route) {
            TaskListScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(
            route = Screen.TaskDetail.route + "/{taskId}/{x}/{y}",
            arguments =
                listOf(
                    navArgument("taskId") {
                        type = NavType.StringType
                        nullable = false
                    },
                    navArgument("x") { type = NavType.IntType },
                    navArgument("y") { type = NavType.IntType },
                ),
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: return@composable
            val x = backStackEntry.arguments?.getInt("x") ?: 0
            val y = backStackEntry.arguments?.getInt("y") ?: 0

            TaskDetailsScreen(id = taskId, x = x, y = y, navController = navController)
        }
        composable(
            route = Screen.TaskCreate.route + "/{taskId}",
            enterTransition = {
                scaleIn(
                    initialScale = 0f,
                    animationSpec = tween(600),
                ) + fadeIn(animationSpec = tween(600))
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0f,
                    animationSpec = tween(600),
                ) + fadeOut(animationSpec = tween(600))
            },
            arguments =
                listOf(
                    navArgument("taskId") {
                        type = NavType.StringType
                        nullable = true
                    },
                ),
        ) {
            TaskCreateScreen(id = it.arguments?.getString("taskId"), navController = navController)
        }
    }
}
