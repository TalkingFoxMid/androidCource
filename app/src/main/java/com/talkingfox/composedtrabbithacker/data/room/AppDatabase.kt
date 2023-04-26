package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.talkingfox.composedtrabbithacker.data.room.model.HabitEntity
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntity
import com.talkingfox.composedtrabbithacker.data.room.model.TokenEntity

@Database(entities = [(HabitEntity::class), (TokenEntity::class), (HabitCompletionEntity::class)], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDAO
    abstract fun tokenDao(): TokenDAO

    abstract fun habitCompletionsDao(): HabitCompletionsDAO
}