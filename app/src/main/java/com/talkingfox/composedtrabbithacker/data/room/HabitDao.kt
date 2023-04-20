package com.talkingfox.composedtrabbithacker.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.talkingfox.composedtrabbithacker.data.room.model.Habit
import java.util.*

@Dao
interface HabitDao {
    @Insert
    fun insertHabit(habit: Habit)

    @Query("SELECT * FROM habits")
    fun habits(): LiveData<List<Habit>>

    @Update(entity = Habit::class)
    fun replaceHabit(h: Habit)

    @Query("DELETE FROM habits WHERE uuid = :uuid")
    fun deleteHabit(uuid: UUID)
}