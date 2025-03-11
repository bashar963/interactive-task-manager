package tech.devbashar.interactivetaskmanager.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.devbashar.interactivetaskmanager.data.local.TaskDao
import tech.devbashar.interactivetaskmanager.data.repository.TasksRepositoryImpl
import tech.devbashar.interactivetaskmanager.domain.repository.TasksRepository
import tech.devbashar.interactivetaskmanager.domain.usecase.CreateTaskUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.DeleteTaskUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.GetAllTasksUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.GetTaskByIdUseCase
import tech.devbashar.interactivetaskmanager.domain.usecase.UpdateTaskUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskRepositoryModule {
    @Singleton
    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TasksRepository = TasksRepositoryImpl(taskDao)

    @Provides
    fun provideGetAllTasksUseCase(repository: TasksRepository): GetAllTasksUseCase = GetAllTasksUseCase(repository)

    @Provides
    fun provideUpdateTaskUseCase(repository: TasksRepository): UpdateTaskUseCase = UpdateTaskUseCase(repository)

    @Provides
    fun provideGetTaskByIdUseCase(repository: TasksRepository): GetTaskByIdUseCase = GetTaskByIdUseCase(repository)

    @Provides
    fun provideDeleteTaskUseCase(repository: TasksRepository): DeleteTaskUseCase = DeleteTaskUseCase(repository)

    @Provides
    fun provideCreateTaskUseCase(repository: TasksRepository): CreateTaskUseCase = CreateTaskUseCase(repository)
}
