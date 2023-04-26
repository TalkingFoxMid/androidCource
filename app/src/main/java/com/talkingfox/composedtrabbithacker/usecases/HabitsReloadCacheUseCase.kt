package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository

interface HabitsReloadCacheUseCase {
    suspend fun reloadCache(): Unit
}

class HabitsReloadCacheUseCaseImpl(private val habitsRepository: ShortHabitRepository): HabitsReloadCacheUseCase {
    override suspend fun reloadCache() {
        habitsRepository.reloadCache()
    }
}