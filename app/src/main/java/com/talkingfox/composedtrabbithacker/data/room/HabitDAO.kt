package com.talkingfox.composedtrabbithacker.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.talkingfox.composedtrabbithacker.data.room.model.Habit
import com.talkingfox.composedtrabbithacker.data.room.model.params
import java.util.*

@Dao
interface HabitDAO {
    @Insert
    fun insertHabit(habit: Habit)

    @Query("SELECT * FROM ${params.tableName}")
    fun habits(): LiveData<List<Habit>>

    @Update(entity = Habit::class)
    fun replaceHabit(h: Habit)

    @Query("DELETE FROM ${params.tableName} WHERE uuid = :uuid")
    fun deleteHabit(uuid: UUID)

    @Query("DELETE FROM ${params.tableName}")
    fun flushDatabase()

    @Transaction
    fun transact(actions: List<() -> Unit>): Unit {
        actions.forEach {
            it()
        }
    }
}