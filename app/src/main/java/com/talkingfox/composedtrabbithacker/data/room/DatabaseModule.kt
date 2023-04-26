package com.talkingfox.composedtrabbithacker.data.room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun providesHabitDao(appDatabase: AppDatabase): HabitDAO =
        appDatabase.habitDao()

    @Provides
    fun providesTokenDao(appDatabase: AppDatabase): TokenDAO =
        appDatabase.tokenDao()

    @Provides
    fun provideHabitCompletionDao(appDatabase: AppDatabase): HabitCompletionsDAO =
        appDatabase.habitCompletionsDao()
}