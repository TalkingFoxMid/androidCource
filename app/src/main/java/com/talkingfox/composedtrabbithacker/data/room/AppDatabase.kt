package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.talkingfox.composedtrabbithacker.data.room.model.Habit
import com.talkingfox.composedtrabbithacker.data.room.model.Token

@Database(entities = [(Habit::class), (Token::class)], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDAO
    abstract fun tokenDao(): TokenDAO
}