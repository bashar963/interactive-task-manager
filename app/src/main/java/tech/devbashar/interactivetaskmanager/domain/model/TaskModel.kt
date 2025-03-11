package tech.devbashar.interactivetaskmanager.domain.model

import androidx.compose.ui.graphics.Color
import tech.devbashar.interactivetaskmanager.domain.entity.Priority
import java.util.Date

data class TaskModel(
    val id: Int? = null,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val createdAt: Date,
    val dueDate: Date?,
    val priority: Priority,
)

fun Priority.getContainerColor(): Color {
    return when (this) {
        Priority.LOW -> Color(0xFFDEF8CB)
        Priority.MEDIUM -> Color(0xFFFDC56D)
        Priority.HIGH -> Color(0xFFF476A3)
    }
}

fun Priority.getContentColor(): Color {
    return when (this) {
        Priority.LOW -> Color(0xFF000000)
        Priority.MEDIUM -> Color(0xFFFFFFFF)
        Priority.HIGH -> Color(0xFFFFFFFF)
    }
}
