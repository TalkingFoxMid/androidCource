package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.infra.Clock
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository
import java.util.UUID

interface CompleteHabitUseCase {
    suspend fun completeHabit(uuid: UUID, periodDays: Habits.HabitPeriodDays): Unit
}

class CompleteHabitUseCaseImpl(private val completionsRepository: HabitCompletionsRepository,
private val curPeriod: CurrentPeriodProvider): CompleteHabitUseCase {
    override suspend fun completeHabit(uuid: UUID, periodDays: Habits.HabitPeriodDays): Unit {
        completionsRepository.completeHabit(uuid, curPeriod.currentPeriod(periodDays))
    }
}