package com.talkingfox.composedtrabbithacker.infra

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import com.talkingfox.composedtrabbithacker.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "RssReader"
        ).build()
    }

    @Provides
    @Singleton
    fun provideClock(@ApplicationContext appContext: Context): Clock {
        return object: Clock {
            override fun currentTime(): Long {
                return System.currentTimeMillis()
            }

        }
    }
}