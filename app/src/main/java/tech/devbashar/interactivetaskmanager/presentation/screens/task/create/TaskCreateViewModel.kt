package tech.devbashar.interactivetaskmanager.presentation.screens.task.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.devbashar.interactivetaskmanager.domain.entity.Priority
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.usecase.CreateTaskUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.GetTaskByIdUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.UpdateTaskUseCase
import tech.devbashar.interactivetaskmanager.utils.formatDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TaskCreateViewModel
    @Inject
    constructor(
        private val createTaskUseCase: CreateTaskUseCase,
        private val getTaskByIdUseCase: GetTaskByIdUseCase,
        private val updateTaskUseCase: UpdateTaskUseCase,
    ) : ViewModel() {
        private val _task = MutableStateFlow<TaskModel?>(null)
        val task: StateFlow<TaskModel?> = _task

        var titleError by mutableStateOf<String?>(null)
            private set

        var title by mutableStateOf("")
            private set

        fun updateTitle(input: String) {
            title = input
        }

        var description by mutableStateOf("")
            private set

        fun updateDescription(input: String) {
            description = input
        }

        var dueDate by mutableStateOf(Date().formatDate())
            private set

        fun updateDueDate(input: String) {
            dueDate = input
        }

        var priority by mutableStateOf(Priority.LOW)
            private set

        fun updatePriority(input: Priority) {
            priority = input
        }

        fun getTask(id: String) {
            getTaskByIdUseCase.invoke(id.toInt())
                .onEach {
                    _task.value = it
                    if (it != null) {
                        title = it.title
                        description = it.description ?: ""
                        dueDate = it.dueDate?.formatDate() ?: ""
                        priority = it.priority
                    }
                }
                .launchIn(viewModelScope)
        }

        suspend fun createTask(): Boolean {
            if (title.isEmpty()) {
                titleError = "Title is required"
                return false
            } else {
                titleError = null
            }

            val task =
                TaskModel(
                    title = title,
                    description = description,
                    dueDate = dueDate.formatDate(),
                    priority = priority,
                    isCompleted = false,
                    createdAt = Date(),
                )
            createTaskUseCase.invoke(task)
            return true
        }

        suspend fun editTask(): Boolean {
            if (title.isEmpty()) {
                titleError = "Title is required"
                return false
            } else {
                titleError = null
            }
            updateTaskUseCase.invoke(
                _task.value!!.copy(
                    title = title,
                    description = description,
                    dueDate = dueDate.formatDate(),
                    priority = priority,
                ),
            )
            return true
        }
    }
