package tech.devbashar.interactivetaskmanager.data.mapper

import tech.devbashar.interactivetaskmanager.domain.entity.TaskEntity
import tech.devbashar.interactivetaskmanager.domain.model.TaskModel
import java.util.Date

object TaskMapper {
    fun entityToModel(taskEntity: TaskEntity): TaskModel {
        return TaskModel(
            id = taskEntity.id,
            title = taskEntity.title,
            description = taskEntity.description,
            isCompleted = taskEntity.isCompleted,
            createdAt = Date(taskEntity.createdAt),
            dueDate = taskEntity.dueDate?.let { Date(it) },
            priority = taskEntity.priority,
        )
    }

    fun modelToEntity(taskModel: TaskModel): TaskEntity {
        return TaskEntity(
            id = taskModel.id,
            title = taskModel.title,
            description = taskModel.description,
            isCompleted = taskModel.isCompleted,
            createdAt = taskModel.createdAt.time,
            dueDate = taskModel.dueDate?.time,
            priority = taskModel.priority,
        )
    }
}
