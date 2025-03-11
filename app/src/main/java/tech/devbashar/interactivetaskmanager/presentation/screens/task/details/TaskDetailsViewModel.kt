package tech.devbashar.interactivetaskmanager.presentation.screens.task.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.usecase.DeleteTaskUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.GetTaskByIdUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.UpdateTaskUseCase
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel
    @Inject
    constructor(
        private val getTaskByIdUseCase: GetTaskByIdUseCase,
        private val deleteTaskUseCase: DeleteTaskUseCase,
        private val updateTaskUseCase: UpdateTaskUseCase,
    ) : ViewModel() {
        private val _task = MutableStateFlow<TaskModel?>(null)
        val task: StateFlow<TaskModel?> = _task

        fun getTaskById(id: String) {
            getTaskByIdUseCase.invoke(id.toInt())
                .onEach {
                    _task.value = it
                }
                .launchIn(viewModelScope)
        }

        suspend fun deleteTask(it: TaskModel) {
            deleteTaskUseCase.invoke(it)
        }

        suspend fun markAsCompleted(task: TaskModel) {
            updateTaskUseCase.invoke(task.copy(isCompleted = true))
        }
    }
