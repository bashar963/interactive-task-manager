package tech.devbashar.interactivetaskmanager.presentation.navigation

sealed class Screen(val route: String) {
    data object TaskList : Screen("task_list_screen")

    data object TaskDetail : Screen("task_detail_screen")

    data object TaskCreate : Screen("task_create_screen")

    data object Settings : Screen("settings_screen")
}
