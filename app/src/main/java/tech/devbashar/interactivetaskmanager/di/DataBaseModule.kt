package tech.devbashar.interactivetaskmanager.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tech.devbashar.interactivetaskmanager.data.local.TaskDao
import tech.devbashar.interactivetaskmanager.data.local.TasksDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(
        @ApplicationContext context: Context,
    ): TasksDatabase {
        return Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks.db",
        ).build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(tasksDatabase: TasksDatabase): TaskDao = tasksDatabase.getTaskDao()
}
