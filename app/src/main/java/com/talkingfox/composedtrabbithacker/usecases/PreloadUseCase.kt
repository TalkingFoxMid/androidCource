package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.domain.HabitRepository

interface PreloadUseCase {
    suspend fun preloadCache(): Boolean
}

class PreloadUseCaseImpl(private val habitRepository: HabitRepository): PreloadUseCase {
    override suspend fun preloadCache(): Boolean {
        return try {
            habitRepository.reloadCache()
            true
        } catch(e: Throwable) {
            false
        }
    }

}