package tech.devbashar.interactivetaskmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.devbashar.interactivetaskmanager.domain.entity.TaskEntity

@Database(version = 1, entities = [TaskEntity::class], exportSchema = true)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}
