package tech.devbashar.interactivetaskmanager.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tech.devbashar.interactivetaskmanager.data.repository.UserPrefRepositoryImpl
import tech.devbashar.interactivetaskmanager.domain.repository.UserPrefRepository
import tech.devbashar.interactivetaskmanager.utils.settingsDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserPrefRepository(
        @ApplicationContext context: Context,
    ): UserPrefRepository {
        return UserPrefRepositoryImpl(dataStore = context.settingsDataStore)
    }
}
