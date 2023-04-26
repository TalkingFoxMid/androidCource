package com.talkingfox.composedtrabbithacker.data

import com.talkingfox.composedtrabbithacker.data.room.HabitCompletionsDAO
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntity
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class HabitCompletionsRepositoryImpl(private val completionDAO: HabitCompletionsDAO): HabitCompletionsRepository {
    fun transform(completionDTO: HabitCompletionEntity): Habits.HabitCompletion =
        Habits.HabitCompletion(
            completionDTO.habitUUID,
            completionDTO.completeDate
        )

    override fun habitCompletionFlow(uuid: UUID, time: Long, periodDays: Int): Flow<List<Habits.HabitCompletion>> =
        completionDAO.completionsFlow(uuid, periodDays, time)
            .map { it.map { h -> transform(h) } }

    override suspend fun habitCompletion(uuid: UUID, time: Long, periodDays: Int): List<Habits.HabitCompletion> =
        completionDAO.completions(uuid, periodDays, time)
            .map { transform(it) }

    override suspend fun completeHabit(uuid: UUID, time: Long) {
        println(uuid)
        println(time)
        completionDAO.complete(HabitCompletionEntity(uuid, time))
    }
}