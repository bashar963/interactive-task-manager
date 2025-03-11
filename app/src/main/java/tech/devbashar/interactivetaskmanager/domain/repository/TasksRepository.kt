package tech.devbashar.interactivetaskmanager.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.devbashar.interactivetaskmanager.domain.model.FilterOptions
import tech.devbashar.interactivetaskmanager.domain.model.SortOptions
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel

interface TasksRepository {
    fun getTasks(
        filterOption: FilterOptions,
        sortOption: SortOptions,
    ): Flow<List<TaskModel>>

    fun getTask(id: Int): Flow<TaskModel?>

    suspend fun addTask(task: TaskModel)

    suspend fun updateTask(task: TaskModel)

    suspend fun deleteTask(task: TaskModel)
}
