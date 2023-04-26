package com.talkingfox.composedtrabbithacker.data

import com.talkingfox.composedtrabbithacker.data.room.HabitCompletionsDAO
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntity
import com.talkingfox.composedtrabbithacker.data.room.model.HabitCompletionEntityWithPeriod
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository_
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class HabitCompletionsRepositoryImpl(private val completionDAO: HabitCompletionsDAO): HabitCompletionsRepository {
    private fun transform(completionDTO: HabitCompletionEntityWithPeriod): HabitCompletionsRepository_.HabitCompletionWithPeriod =
        HabitCompletionsRepository_.HabitCompletionWithPeriod(
            completionDTO.uuid,
            completionDTO.tries,
            completionDTO.periodNumber,
            completionDTO.periodDays
        )

    override fun habitCompletionFlow(): Flow<Map<UUID, HabitCompletionsRepository_.HabitCompletionWithPeriod>> =
        completionDAO.completionsFlow()
            .map { comEntities ->
                comEntities.groupBy { it.uuid }.mapValues { x ->
                    transform(x.value.first())
                }
            }

    override suspend fun habitCompletion(uuid: UUID): HabitCompletionsRepository_.HabitCompletionWithPeriod? =
        completionDAO.completion(uuid)?.let {
            transform(it)
        }


    override suspend fun completeHabit(uuid: UUID, periodDays: Long) {
        println(periodDays)
        completionDAO.reloadCompletions(uuid, periodDays)
        completionDAO.complete(
            uuid, periodDays
        )
    }
}