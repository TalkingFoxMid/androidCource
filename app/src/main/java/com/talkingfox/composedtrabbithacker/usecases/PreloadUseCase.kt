package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository

interface PreloadUseCase {
    suspend fun preloadCache(): Boolean
}

class PreloadUseCaseImpl(private val shortHabitRepository: ShortHabitRepository): PreloadUseCase {
    override suspend fun preloadCache(): Boolean {
        return try {
            shortHabitRepository.reloadCache()
            true
        } catch(e: Throwable) {
            e.printStackTrace()
            false
        }
    }

}