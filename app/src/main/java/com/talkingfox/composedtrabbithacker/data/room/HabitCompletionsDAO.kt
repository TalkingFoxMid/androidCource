package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface HabitCompletionsDAO {
    @Insert
    fun complete(h: HabitCompletionEntity): Unit

    @Query("SELECT * FROM habitsCompletion where uuid = :uuid and date > :currentDate - :periodDays * 8640000")
    fun completions(uuid: UUID, periodDays: Int, currentDate: Long): List<HabitCompletionEntity>

    @Query("SELECT * FROM habitsCompletion where uuid = :uuid and date > :currentDate - :periodDays * 8640000")
    fun completionsFlow(uuid: UUID, periodDays: Int, currentDate: Long): Flow<List<HabitCompletionEntity>>
}