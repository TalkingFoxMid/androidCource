package com.talkingfox.composedtrabbithacker.repository

import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ShortHabitRepository {
    suspend fun addHabit(data: Habits.HabitData): Unit

    suspend fun deleteHabit(id: UUID): Unit

    suspend fun replaceHabit(id: UUID, data: Habits.HabitData): Unit
    suspend fun listHabits(): List<Habits.ShortHabit>
    fun habitsFlow(): Flow<List<Habits.ShortHabit>>


    suspend fun reloadCache(): Unit
}

