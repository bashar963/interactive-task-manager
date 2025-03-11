package tech.devbashar.interactivetaskmanager.presentation.screens.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.devbashar.interactivetaskmanager.domain.model.FilterOptions
import tech.devbashar.interactivetaskmanager.domain.model.SortOptions
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.usecase.DeleteTaskUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.GetAllTasksUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.UpdateTaskUseCase
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel
    @Inject
    constructor(
        private val getAllTasksUseCase: GetAllTasksUseCase,
        private val updateTaskUseCase: UpdateTaskUseCase,
        private val deleteTaskUseCase: DeleteTaskUseCase,
    ) : ViewModel() {
        private val _isLoaded = MutableStateFlow(false)
        val isLoaded: StateFlow<Boolean> = _isLoaded

        private val _filterOption = MutableStateFlow(FilterOptions.All)
        val filterOption: StateFlow<FilterOptions> = _filterOption

        private val _sortOption = MutableStateFlow(SortOptions.Title)
        val sortOption: StateFlow<SortOptions> = _sortOption

        private val _progressTasks = MutableStateFlow<List<TaskModel>>(emptyList())
        val progressTasks: StateFlow<List<TaskModel>> = _progressTasks

        private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
        val tasks: StateFlow<List<TaskModel>> = _tasks

        private val _pendingTasks = MutableStateFlow<List<TaskModel>>(emptyList())
        val pendingTasks: StateFlow<List<TaskModel>> = _pendingTasks

        init {
            loadProgressTasks()
            viewModelScope.launch {
                loadTasks()
            }
        }

        private fun loadProgressTasks() {
            getAllTasksUseCase.invoke(
                FilterOptions.All,
                SortOptions.Title,
            )
                .onEach { _progressTasks.value = it }
                .launchIn(
                    viewModelScope,
                )
        }

        private suspend fun loadTasks() {
            _isLoaded.value = true
            getAllTasks()
            getPendingTasks()
            delay(800)
            _isLoaded.value = false
        }

        private fun getPendingTasks() {
            getAllTasksUseCase.invoke(
                FilterOptions.Pending,
                SortOptions.Title,
            )
                .onEach { _pendingTasks.value = it }
                .launchIn(
                    viewModelScope,
                )
        }

        private fun getAllTasks() {
            getAllTasksUseCase.invoke(_filterOption.value, _sortOption.value)
                .onEach {
                    _tasks.value = it
                }
                .launchIn(viewModelScope)
        }

        fun updateTask(tasks: List<TaskModel>) {
            _tasks.value = tasks
        }

        suspend fun toggleTaskCompletion(task: TaskModel) {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTaskUseCase.invoke(updatedTask)
        }

        fun updateFilterOption(filter: FilterOptions) {
            _filterOption.value = filter
            getAllTasks()
        }

        fun updateSortOption(sort: SortOptions) {
            _sortOption.value = sort
            getAllTasks()
        }

        suspend fun deleteTask(taskModel: TaskModel) {
            deleteTaskUseCase.invoke(taskModel)
        }
    }
