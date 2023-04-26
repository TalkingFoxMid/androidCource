package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.infra.Clock
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository
import java.util.UUID

interface CompleteHabitUseCase {
    suspend fun completeHabit(uuid: UUID): Unit
}

class CompleteHabitUseCaseImpl(private val completionsRepository: HabitCompletionsRepository, private val clock: Clock): CompleteHabitUseCase {
    override suspend fun completeHabit(uuid: UUID): Unit {
        completionsRepository.completeHabit(uuid, clock.currentTime())
    }
}