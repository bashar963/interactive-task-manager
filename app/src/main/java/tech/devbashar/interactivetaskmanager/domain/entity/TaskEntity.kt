package tech.devbashar.interactivetaskmanager.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val createdAt: Long,
    val dueDate: Long?,
    val priority: Priority,
)

enum class Priority {
    HIGH,
    MEDIUM,
    LOW,
}
