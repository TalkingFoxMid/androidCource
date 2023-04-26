package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.infra.Clock
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface DetailedHabitListUseCase {
    fun habitsFlow(): Flow<List<Habits.DetailedHabit>>
    suspend fun habits(): List<Habits.DetailedHabit>
}

class DetailedHabitListUseCaseImpl(private val shortHabitRepository: ShortHabitRepository,
                                   private val completionsRepository: HabitCompletionsRepository,
                                   private val clock: Clock
): DetailedHabitListUseCase {
    @OptIn(FlowPreview::class)
    override fun habitsFlow(): Flow<List<Habits.DetailedHabit>> {
        return shortHabitRepository.habitsFlow().flatMapConcat {
                habits -> combine(
            habits.map {
                    shortHabit ->
                completionsRepository.habitCompletionFlow(
                    shortHabit.id, clock.currentTime(),
                    shortHabit.data.period.periodDays)
                    .map { Habits.DetailedHabit(shortHabit, it) }
            }
        )  { x -> x.toList() }
        }
    }

    override suspend fun habits(): List<Habits.DetailedHabit> {
        val habits = shortHabitRepository.listHabits()
        return habits.map { shortHabit ->
            Habits.DetailedHabit(
                shortHabit, completionsRepository.habitCompletion(shortHabit.id, clock.currentTime(), shortHabit.data.period.periodDays)
            )
        }
    }

}