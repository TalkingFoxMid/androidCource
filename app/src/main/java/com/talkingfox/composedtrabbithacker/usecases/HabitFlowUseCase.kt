package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.flow.Flow

interface HabitFlowUseCase {
    fun habitsFlow(): Flow<List<Habits.ShortHabit>>
}

class HabitFlowUseCaseImpl(private val shortHabitRepository: ShortHabitRepository): HabitFlowUseCase {
    override fun habitsFlow(): Flow<List<Habits.ShortHabit>> =
        shortHabitRepository.habitsFlow()
}