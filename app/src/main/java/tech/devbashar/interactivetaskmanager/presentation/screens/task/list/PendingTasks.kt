package tech.devbashar.interactivetaskmanager.presentation.screens.task.list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import tech.devbashar.interactivetaskmanager.R
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.model.getContainerColor
import tech.devbashar.interactivetaskmanager.domain.model.getContentColor
import tech.devbashar.interactivetaskmanager.utils.formatDate

@Composable
fun PendingTasks(
    tasks: List<TaskModel>,
    onTaskDetailSelected: (TaskModel, Int, Int) -> Unit,
) {
    Box(modifier = Modifier.animateContentSize()) {
        if (tasks.isEmpty()) return
        Column(
            modifier = Modifier.animateContentSize(),
        ) {
            Text(
                "Pending Tasks",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding =
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                    ),
            ) {
                items(tasks.size) { index ->
                    PendingTaskItem(task = tasks[index], onTaskDetailSelected = onTaskDetailSelected)
                }
            }
        }
    }
}

@Composable
fun PendingTaskItem(
    task: TaskModel,
    onTaskDetailSelected: (TaskModel, Int, Int) -> Unit,
) {
    var coordinates by remember { mutableStateOf(IntOffset.Zero) }
    Card(
        modifier =
            Modifier.size(
                width = 160.dp,
                height = 160.dp,
            )
                .onGloballyPositioned { layoutCoordinates ->
                    val position = layoutCoordinates.boundsInWindow()
                    coordinates = IntOffset(position.left.toInt(), position.top.toInt())
                }
                .clickable {
                    onTaskDetailSelected(task, coordinates.x, coordinates.y)
                },
        colors =
            CardDefaults.cardColors(
                containerColor = task.priority.getContainerColor(),
                contentColor = Color.White,
            ),
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_task_illustration),
                contentDescription = null,
                modifier = Modifier.offset(x = 60.dp),
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = task.priority.getContentColor(),
                )
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Text(
                    "Due Date: ${task.dueDate?.formatDate() ?: "No Due Date"}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = task.priority.getContentColor(),
                )
            }
        }
    }
}
