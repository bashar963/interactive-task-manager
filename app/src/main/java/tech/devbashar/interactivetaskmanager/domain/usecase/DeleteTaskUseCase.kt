package tech.devbashar.interactivetaskmanager.domain.usecase

import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase
    @Inject
    constructor(
        private val tasksRepository: TasksRepository,
    ) {
        suspend operator fun invoke(task: TaskModel) {
            tasksRepository.deleteTask(task)
        }
    }
