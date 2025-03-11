package tech.devbashar.interactivetaskmanager.domain.usecase

import kotlinx.coroutines.flow.Flow
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.repository.TasksRepository
import javax.inject.Inject

class GetTaskByIdUseCase
    @Inject
    constructor(
        private val tasksRepository: TasksRepository,
    ) {
        operator fun invoke(taskId: Int): Flow<TaskModel?> {
            return tasksRepository.getTask(taskId)
        }
    }
