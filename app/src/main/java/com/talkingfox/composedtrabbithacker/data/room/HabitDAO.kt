package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.*
import com.talkingfox.composedtrabbithacker.data.room.model.HabitEntity
import com.talkingfox.composedtrabbithacker.data.room.model.params
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface HabitDAO {
    @Insert
    fun insertHabit(habitEntity: HabitEntity)

    @Query("SELECT * FROM ${params.habitsDataTableName}")
    fun habitsFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM ${params.habitsDataTableName}")
    fun listHabits(): List<HabitEntity>

    @Update(entity = HabitEntity::class)
    fun replaceHabit(h: HabitEntity)

    @Query("DELETE FROM ${params.habitsDataTableName} WHERE uuid = :uuid")
    fun deleteHabit(uuid: UUID)

    @Query("DELETE FROM ${params.habitsDataTableName}")
    fun flushDatabase()

    @Transaction
    fun transact(actions: List<() -> Unit>): Unit {
        actions.forEach {
            it()
        }
    }
}