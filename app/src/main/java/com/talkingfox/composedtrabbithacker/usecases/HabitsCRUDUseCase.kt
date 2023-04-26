package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.UUID

interface HabitsCRUDUseCase {
    suspend fun addHabit(data: Habits.HabitData): Unit
    suspend fun deleteHabit(uuid: UUID): Unit
    suspend fun fetchHabits(): List<Habits.ShortHabit>
}

class HabitsCRUDUseCaseImpl(private val repo: ShortHabitRepository): HabitsCRUDUseCase {
    override suspend fun addHabit(data: Habits.HabitData): Unit {
        try {
            repo.addHabit(data)
        } catch (e: Throwable)  {
            repo.reloadCache()
        }
    }

    override suspend fun deleteHabit(uuid: UUID): Unit {
        try {
            repo.deleteHabit(uuid)
        } catch (e: Throwable)  {
            repo.reloadCache()
        }
    }

    override suspend fun fetchHabits(): List<Habits.ShortHabit> {
        return repo.listHabits()
    }

}