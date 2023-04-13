package com.talkingfox.composedtrabbithacker.modules

import android.content.Context
import androidx.room.Room
import com.talkingfox.composedtrabbithacker.dao.AppDatabase
import com.talkingfox.composedtrabbithacker.dao.HabitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun providesHabitDao(appDatabase: AppDatabase): HabitDao =
        appDatabase.habitDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "RssReader"
        ).build()
    }
}