package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntity
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntityWithPeriod
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface HabitCompletionsDAO {
    @Query("""INSERT INTO habitsCompletion (uuid, periodNumber, tries) VALUES (:uuid, :currentPeriod, 1) ON CONFLICT (uuid) DO UPDATE SET tries = tries + 1 where uuid = :uuid""")
    fun complete(uuid: UUID, currentPeriod: Long): Unit

    @Query("DELETE FROM habitsCompletion WHERE periodNumber != :currentPeriod and uuid = :uuid")
    fun reloadCompletions(uuid: UUID, currentPeriod: Long): Unit

    @Query("SELECT * FROM habitsCompletion hc inner join habits h on h.uuid = hc.uuid where tries > 0")
    fun completionsFlow(): Flow<List<HabitCompletionEntityWithPeriod>>

    @Query("SELECT * FROM habitsCompletion hc inner join habits h on h.uuid = hc.uuid where hc.uuid = :uuid limit 1")
    fun completion(uuid: UUID): HabitCompletionEntityWithPeriod?
}