package tech.devbashar.interactivetaskmanager.presentation.screens.task.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.SwipeToReveal
import androidx.wear.compose.foundation.rememberRevealState
import compose.icons.FeatherIcons
import compose.icons.feathericons.CheckCircle
import compose.icons.feathericons.Filter
import compose.icons.feathericons.List
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.devbashar.interactivetaskmanager.domain.model.DraggableItem
import tech.devbashar.interactivetaskmanager.domain.model.FilterOptions
import tech.devbashar.interactivetaskmanager.domain.model.SortOptions
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.model.getContainerColor
import tech.devbashar.interactivetaskmanager.domain.model.getContentColor
import tech.devbashar.interactivetaskmanager.presentation.components.widgets.CircleCheckbox

@Composable
fun TaskList(
    tasks: List<TaskModel>,
    viewModel: TaskListViewModel = hiltViewModel(),
    onAddTask: () -> Unit,
    onSelectTask: (TaskModel, Int, Int) -> Unit,
) {
    val stateList = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val selectedFilter by viewModel.filterOption.collectAsState()
    val selectedSort by viewModel.sortOption.collectAsState()
    val hapticFeedback = LocalHapticFeedback.current

    Column(modifier = Modifier.animateContentSize()) {
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            FilterTaskButton(selectedFilter) {
                viewModel.updateFilterOption(it)
            }
            SortTaskButton(selectedSort) {
                viewModel.updateSortOption(it)
            }
        }

        Crossfade(targetState = tasks.isEmpty()) { first ->
            if (first) {
                EmptyTaskState(onAddTask = onAddTask)
            } else {
                var delta: Float by remember {
                    mutableFloatStateOf(0f)
                }
                var draggingItem: LazyListItemInfo? by remember {
                    mutableStateOf(null)
                }
                var draggingItemIndex: Int? by remember {
                    mutableStateOf(null)
                }
                val onMove = { fromIndex: Int, toIndex: Int ->
                    val newOrderTasks = tasks.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
                    viewModel.updateTask(newOrderTasks)
                }
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    state = stateList,
                    modifier =
                        Modifier
                            .height(600.dp)
                            .pointerInput(key1 = stateList) {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = { offset ->
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        stateList.layoutInfo.visibleItemsInfo
                                            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                                            ?.also {
                                                (it.contentType as? DraggableItem)?.let { draggableItem ->
                                                    draggingItem = it
                                                    draggingItemIndex = draggableItem.index
                                                }
                                            }
                                    },
                                    onDragEnd = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        draggingItem = null
                                        draggingItemIndex = null
                                        delta = 0f
                                    },
                                    onDragCancel = {
                                        draggingItem = null
                                        draggingItemIndex = null
                                        delta = 0f
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        delta += dragAmount.y

                                        val currentDraggingItemIndex =
                                            draggingItemIndex ?: return@detectDragGesturesAfterLongPress
                                        val currentDraggingItem =
                                            draggingItem ?: return@detectDragGesturesAfterLongPress

                                        val startOffset = currentDraggingItem.offset + delta
                                        val endOffset =
                                            currentDraggingItem.offset + currentDraggingItem.size + delta
                                        val middleOffset = startOffset + (endOffset - startOffset) / 2

                                        val targetItem =
                                            stateList.layoutInfo.visibleItemsInfo.find { item ->
                                                middleOffset.toInt() in item.offset..item.offset + item.size &&
                                                    currentDraggingItem.index != item.index &&
                                                    item.contentType is DraggableItem
                                            }

                                        if (targetItem != null) {
                                            val targetIndex = (targetItem.contentType as DraggableItem).index
                                            onMove(currentDraggingItemIndex, targetIndex)
                                            draggingItemIndex = targetIndex
                                            draggingItem = targetItem
                                            delta += currentDraggingItem.offset - targetItem.offset
                                        }
                                    },
                                )
                            },
                ) {
                    itemsIndexed(
                        items = tasks,
                        contentType = { index, _ -> DraggableItem(index = index) },
                    ) { index, task ->
                        var isRemoved by remember { mutableStateOf(false) }
                        val modifier =
                            if (draggingItemIndex == index) {
                                Modifier
                                    .zIndex(1f)
                                    .graphicsLayer {
                                        translationY = delta
                                    }
                            } else {
                                Modifier
                            }
                        TaskItem(
                            modifier = modifier,
                            isRemoved = isRemoved,
                            task = task,
                            onSelectTask = onSelectTask,
                            onDeleteTask = {
                                isRemoved = true
                                scope.launch {
                                    delay(300)
                                    viewModel.deleteTask(tasks[index])
                                }
                            },
                            onCompleteTask = {
                                scope.launch {
                                    viewModel.toggleTaskCompletion(tasks[index])
                                }
                            },
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(120.dp))
    }
}

@Composable
fun TaskItem(
    modifier: Modifier,
    isRemoved: Boolean,
    task: TaskModel,
    onSelectTask: (TaskModel, Int, Int) -> Unit,
    onDeleteTask: () -> Unit,
    onCompleteTask: () -> Unit,
) {
    AnimatedVisibility(
        visible = !isRemoved,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
    ) {
        TaskListItem(
            task = task,
            onSelectTask = onSelectTask,
            onDeleteTask = onDeleteTask,
            onCompleteTask = onCompleteTask,
        )
    }
}

@Composable
fun SortTaskButton(
    selectedSort: SortOptions,
    onSortSelected: (SortOptions) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = FeatherIcons.List,
                contentDescription = "Sort",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            repeat(SortOptions.entries.size) {
                val sortOptions = SortOptions.entries[it]
                val isSelected = selectedSort == sortOptions
                DropdownMenuItem(
                    text = { Text(sortOptions.name) },
                    leadingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = FeatherIcons.CheckCircle,
                                contentDescription = "Sort",
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onSortSelected(sortOptions)
                    },
                )
            }
        }
    }
}

@Composable
fun FilterTaskButton(
    selectedFilter: FilterOptions,
    onFilterSelected: (FilterOptions) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = FeatherIcons.Filter,
                contentDescription = "Filter",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            repeat(FilterOptions.entries.size) {
                val filterOption = FilterOptions.entries[it]
                val isSelected = selectedFilter == filterOption
                DropdownMenuItem(
                    text = { Text(filterOption.name) },
                    leadingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = FeatherIcons.CheckCircle,
                                contentDescription = "Filter",
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onFilterSelected(filterOption)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun TaskListItem(
    task: TaskModel,
    onDeleteTask: () -> Unit,
    onCompleteTask: () -> Unit,
    onSelectTask: (TaskModel, Int, Int) -> Unit,
) {
    val revealState = rememberRevealState()
    var coordinates by remember { mutableStateOf(IntOffset.Zero) }
    SwipeToReveal(
        modifier =
            Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    val position = layoutCoordinates.boundsInWindow()
                    coordinates = IntOffset(position.left.toInt(), position.top.toInt())
                }
                .clickable {
                    onSelectTask(task, coordinates.x, coordinates.y)
                },
        state = revealState,
        primaryAction = {
            FilledIconButton(
                onClick = onDeleteTask,
                colors =
                    IconButtonDefaults.filledIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                )
            }
        },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .padding(8.dp)
                        .clip(shape = CircleShape)
                        .background(task.priority.getContainerColor()),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = task.priority.name.first().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = task.priority.getContentColor(),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = task.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                )
            }
            CircleCheckbox(checked = task.isCompleted, onCheckedChange = onCompleteTask)
        }
    }
}
