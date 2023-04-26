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
    fun habitsFlow(): Flow<List<Habits.CompletableHabit>>
    suspend fun habits(): List<Habits.CompletableHabit>
}

class DetailedHabitListUseCaseImpl(private val shortHabitRepository: ShortHabitRepository,
                                   private val completionsRepository: HabitCompletionsRepository,
                                   private val currentPeriodProvider: CurrentPeriodProvider
): DetailedHabitListUseCase {
    @OptIn(FlowPreview::class)
    override fun habitsFlow(): Flow<List<Habits.CompletableHabit>> =
            shortHabitRepository.habitsFlow().combine(completionsRepository.habitCompletionFlow()) {
                    v, g ->
                v.map {
                    shortHabit ->
                        val currentPeriodForHabit = currentPeriodProvider.currentPeriod(shortHabit.data.period.periodDays)
                        val completion = g[shortHabit.id]?.let {
                            x -> if(currentPeriodForHabit == x.completedPeriod)
                            Habits.Completion(x.tries) else null
                        }
                Habits.CompletableHabit(shortHabit, completion)
            }
        }

    override suspend fun habits(): List<Habits.CompletableHabit> {
        val habits = shortHabitRepository.listHabits()
        return habits.map { shortHabit ->
            val currentPeriodForHabit = currentPeriodProvider.currentPeriod(shortHabit.data.period.periodDays)
            val completion = completionsRepository.habitCompletion(shortHabit.id)
            Habits.CompletableHabit(
                shortHabit,
                completion?.let {
                    if(currentPeriodForHabit == it.completedPeriod)
                    Habits.Completion(it.tries) else null
                }
            )
        }
    }

}