package tech.devbashar.interactivetaskmanager.presentation.screens.task.create

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tech.devbashar.interactivetaskmanager.domain.entity.Priority
import tech.devbashar.interactivetaskmanager.domain.model.getContainerColor
import tech.devbashar.interactivetaskmanager.domain.model.getContentColor
import tech.devbashar.interactivetaskmanager.utils.formatDate
import java.util.Date

@Composable
fun CreateTaskForm(
    viewModel: TaskCreateViewModel = hiltViewModel(),
    isEdit: Boolean,
    onTaskCreate: () -> Unit,
) {
    // Form fields
    val openDueDateDialog = remember { mutableStateOf(false) }
    val containerColor by animateColorAsState(
        targetValue = viewModel.priority.getContainerColor(),
        animationSpec = tween(durationMillis = 600),
    )
    val contentColor by animateColorAsState(
        targetValue = viewModel.priority.getContentColor(),
        animationSpec = tween(durationMillis = 600),
    )
    DueDateDialog(
        onDateSelected =
            {
                viewModel.updateDueDate(it)
                openDueDateDialog.value = false
            },
        openDateDialog = openDueDateDialog.value,
        onDismissRequest = { openDueDateDialog.value = false },
    )

    val localFocusManager = LocalFocusManager.current
    val titleFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }
    val dueDateFocusRequester = remember { FocusRequester() }
    val priorityFocusRequester = remember { FocusRequester() }
    val createdFocusRequester = remember { FocusRequester() }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                },
    ) {
        // Title
        TextField(
            modifier =
                Modifier
                    .padding(16.dp)
                    .focusRequester(titleFocusRequester)
                    .fillMaxWidth(),
            value = viewModel.title,
            singleLine = true,
            keyboardActions =
                KeyboardActions(
                    onDone = { descriptionFocusRequester.requestFocus() },
                ),
            isError = viewModel.titleError != null,
            supportingText = {
                viewModel.titleError?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            onValueChange = { viewModel.updateTitle(it) },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                ),
            label = {
                Text(
                    text = "Title",
                )
            },
        )

        // Description
        TextField(
            modifier =
                Modifier
                    .padding(16.dp)
                    .focusRequester(descriptionFocusRequester)
                    .fillMaxWidth(),
            value = viewModel.description,
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            onValueChange = { viewModel.updateDescription(it) },
            label = {
                Text(
                    text = "Description",
                )
            },
        )

        // Due Date
        Box {
            TextField(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .focusRequester(dueDateFocusRequester)
                        .fillMaxWidth(),
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                value = viewModel.dueDate,
                readOnly = true,
                singleLine = true,
                onValueChange = { },
                label = {
                    Text(
                        text = "Due Date",
                    )
                },
            )
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .alpha(0f)
                        .clickable(onClick = { openDueDateDialog.value = true }),
            )
        }

        Spacer(
            modifier = Modifier.padding(16.dp),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(
                        shape =
                            RoundedCornerShape(
                                topEnd = 46.dp,
                                topStart = 46.dp,
                            ),
                    )
                    .background(color = containerColor)
                    .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.headlineMedium,
                    color = contentColor,
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Row(
                    modifier = Modifier.focusRequester(priorityFocusRequester).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    repeat(Priority.entries.size) {
                        val priority = Priority.entries[it]
                        FilterChip(
                            onClick = { viewModel.updatePriority(priority) },
                            label = { Text(priority.name) },
                            selected = viewModel.priority == priority,
                            colors = FilterChipDefaults.filterChipColors(labelColor = contentColor),
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(32.dp))

                ElevatedButton(
                    onClick = onTaskCreate,
                    modifier = Modifier.fillMaxWidth().focusRequester(createdFocusRequester),
                    colors =
                        ButtonDefaults.elevatedButtonColors(
                            containerColor = contentColor,
                            contentColor = containerColor,
                        ),
                    contentPadding = PaddingValues(24.dp),
                ) {
                    Text(
                        if (isEdit) "Edit Task" else "Create Task",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DueDateDialog(
    openDateDialog: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit,
) {
    if (openDateDialog) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled =
            remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = {
                        val dueDate = Date(datePickerState.selectedDateMillis!!).formatDate()
                        onDateSelected.invoke(dueDate)
                    },
                    enabled = confirmEnabled.value,
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) { Text("Cancel") }
            },
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState()),
                title = {
                    Text("Select due date", modifier = Modifier.padding(PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)))
                },
            )
        }
    }
}
