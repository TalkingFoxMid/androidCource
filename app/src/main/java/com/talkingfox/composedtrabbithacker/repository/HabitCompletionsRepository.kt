package com.talkingfox.composedtrabbithacker.repository

import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface HabitCompletionsRepository {

    fun habitCompletionFlow(uuid: UUID, time: Long, periodDays: Int): Flow<List<Habits.HabitCompletion>>

    suspend fun habitCompletion(uuid: UUID, time: Long, periodDays: Int): List<Habits.HabitCompletion>

    suspend fun completeHabit(uuid: UUID, time: Long)
}