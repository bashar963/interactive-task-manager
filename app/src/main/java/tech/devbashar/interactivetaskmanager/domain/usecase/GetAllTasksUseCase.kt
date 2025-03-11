package tech.devbashar.interactivetaskmanager.domain.usecase

import kotlinx.coroutines.flow.Flow
import tech.devbashar.interactivetaskmanager.domain.model.FilterOptions
import tech.devbashar.interactivetaskmanager.domain.model.SortOptions
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.repository.TasksRepository
import javax.inject.Inject

class GetAllTasksUseCase
    @Inject
    constructor(
        private val tasksRepository: TasksRepository,
    ) {
        operator fun invoke(
            filterOption: FilterOptions,
            sortOption: SortOptions,
        ): Flow<List<TaskModel>> {
            return tasksRepository.getTasks(filterOption, sortOption)
        }
    }
