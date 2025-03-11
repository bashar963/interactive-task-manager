package tech.devbashar.interactivetaskmanager.presentation.screens.task.details

import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.model.getContainerColor
import tech.devbashar.interactivetaskmanager.domain.model.getContentColor
import tech.devbashar.interactivetaskmanager.presentation.components.dialogs.ConfirmDialog
import tech.devbashar.interactivetaskmanager.presentation.components.widgets.BouncyFABExtended
import tech.devbashar.interactivetaskmanager.presentation.navigation.Screen
import tech.devbashar.interactivetaskmanager.utils.formatDate
import kotlin.math.hypot

@Composable
fun TaskDetailsScreen(
    id: String,
    x: Int,
    y: Int,
    viewModel: TaskDetailsViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val task by viewModel.task.collectAsState()
    val scope = rememberCoroutineScope()
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = id) {
        viewModel.getTaskById(id)
    }

    ConfirmDialog(
        show = showConfirmDeleteDialog,
        title = "Delete Task",
        message = "Are you sure you want to delete this task?",
        onConfirm = {
            showConfirmDeleteDialog = false
            scope.launch {
                task?.let {
                    viewModel.deleteTask(it)
                    navController.popBackStack()
                }
            }
        },
        cancelText = "Cancel",
        confirmText = "Delete",
        onDismiss = { showConfirmDeleteDialog = false },
    )
    task?.let {
        TaskDetailsContent(
            task = it,
            x = x,
            y = y,
            onBackPressed = { navController.popBackStack() },
            onDeleteTask = { showConfirmDeleteDialog = true },
            onEditTask = { navController.navigate(Screen.TaskCreate.route + "/${it.id}") },
            onMarkAsCompleted = { scope.launch { viewModel.markAsCompleted(it) } },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsContent(
    task: TaskModel,
    x: Int,
    y: Int,
    onBackPressed: () -> Unit,
    onMarkAsCompleted: () -> Unit,
    onDeleteTask: () -> Unit,
    onEditTask: () -> Unit,
) {
    var showComposeWidget by remember { mutableStateOf(false) }
    LaunchedEffect(showComposeWidget) {
        if (!showComposeWidget) {
            delay(600)
            showComposeWidget = true
        }
    }

    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                setBackgroundColor(task.priority.getContainerColor().toArgb())
                post {
                    val finalRadius = hypot(width.toFloat(), height.toFloat())
                    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0f, finalRadius)
                    anim.duration = 800
                    anim.start()
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    )

    if (showComposeWidget) {
        Scaffold(
            containerColor = task.priority.getContainerColor(),
            contentColor = task.priority.getContentColor(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Task Details")
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = task.priority.getContainerColor(),
                            titleContentColor = task.priority.getContentColor(),
                            navigationIconContentColor = task.priority.getContentColor(),
                            actionIconContentColor = task.priority.getContentColor(),
                        ),
                    navigationIcon = {
                        IconButton(onBackPressed) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onDeleteTask) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = onEditTask) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    },
                )
            },
            floatingActionButton = {
                if (!task.isCompleted) {
                    BouncyFABExtended(onClick = onMarkAsCompleted) {
                        Text(text = "Mark as Completed")
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Mark as Completed",
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
            ) {
                TaskDetails(task = task)
            }
        }
    }
}

@Composable
fun TaskDetails(task: TaskModel) {
    Box(
        modifier =
            Modifier
                .padding(16.dp)
                .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.verticalScroll(state = rememberScrollState()),
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Due Date: ")
                        }
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(task.dueDate?.formatDate() ?: "No Due Date")
                        }
                    },
                fontSize = 12.sp,
            )
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Status: ")
                        }
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(if (task.isCompleted) "Completed" else "Pending")
                        }
                    },
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = task.description ?: "")
        }
    }
}
