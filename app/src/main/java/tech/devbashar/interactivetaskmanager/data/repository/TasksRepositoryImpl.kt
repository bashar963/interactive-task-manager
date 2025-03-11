package tech.devbashar.interactivetaskmanager.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import tech.devbashar.interactivetaskmanager.data.local.TaskDao
import tech.devbashar.interactivetaskmanager.data.mapper.TaskMapper
import tech.devbashar.interactivetaskmanager.domain.model.FilterOptions
import tech.devbashar.interactivetaskmanager.domain.model.SortOptions
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import tech.devbashar.interactivetaskmanager.domain.repository.TasksRepository
import javax.inject.Inject

class TasksRepositoryImpl
    @Inject
    constructor(
        private val taskDao: TaskDao,
    ) : TasksRepository {
        override fun getTasks(
            filterOption: FilterOptions,
            sortOption: SortOptions,
        ): Flow<List<TaskModel>> {
            return taskDao.getAll(filter = filterOption.name, sort = sortOption.name).transform { taskEntities ->
                val taskModels = taskEntities.map { TaskMapper.entityToModel(it) }
                emit(taskModels)
            }
        }

        override fun getTask(id: Int): Flow<TaskModel?> {
            return taskDao.getById(id).transform { taskEntity ->
                val taskModel = taskEntity?.let { TaskMapper.entityToModel(it) }
                emit(taskModel)
            }
        }

        override suspend fun addTask(task: TaskModel) {
            val taskEntity = TaskMapper.modelToEntity(task)
            taskDao.insert(taskEntity)
        }

        override suspend fun updateTask(task: TaskModel) {
            val taskEntity = TaskMapper.modelToEntity(task)
            taskDao.update(taskEntity)
        }

        override suspend fun deleteTask(task: TaskModel) {
            val taskEntity = TaskMapper.modelToEntity(task)
            taskDao.deleteTask(taskEntity)
        }
    }
