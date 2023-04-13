package com.talkingfox.composedtrabbithacker.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.talkingfox.composedtrabbithacker.dao.model.Habit

@Database(entities = [(Habit::class)], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao
}