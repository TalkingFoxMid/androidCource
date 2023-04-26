package com.talkingfox.composedtrabbithacker.repository

import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface HabitCompletionsRepository {
    fun habitCompletionFlow(): Flow<Map<UUID, HabitCompletionsRepository_.HabitCompletionWithPeriod>>
    suspend fun habitCompletion(uuid: UUID): HabitCompletionsRepository_.HabitCompletionWithPeriod?
    suspend fun completeHabit(uuid: UUID, periodDays: Long)
}

object HabitCompletionsRepository_ {
    data class HabitCompletionWithPeriod(
        val uuid: UUID,
        val tries: Long,
        val completedPeriod: Long,
        val period: Long
    )
}