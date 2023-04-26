package com.talkingfox.composedtrabbithacker.data

import com.talkingfox.composedtrabbithacker.data.doubletap.model.UuidDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.lenses
import com.talkingfox.composedtrabbithacker.data.room.TokenDAO
import com.talkingfox.composedtrabbithacker.data.room.model.HabitEntity
import com.talkingfox.composedtrabbithacker.repository.ShortHabitRemoteStore
import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class ShortHabitDataRepositoryImpl(private val localStore: com.talkingfox.composedtrabbithacker.data.room.HabitDAO,
                                   private val shortHabitRemoteStore: ShortHabitRemoteStore,
                                   private val tokenDAO: TokenDAO) : ShortHabitRepository {
    private fun fetchToken(): String = tokenDAO.getToken().first().token
    private fun transform(id: UUID, h: Habits.HabitData): HabitEntity =
        HabitEntity(id, h.name, h.description,
            h.priority.name, h.type.name, h.period.periodDays.periodDays,
            h.period.retries, h.creationDate)

    private fun backTransform(h: HabitEntity): Habits.ShortHabit =
        Habits.ShortHabit(
            h.uuid,
            Habits.HabitData(
                h.name,
                h.description,
                Habits.Priority.valueOf(h.priority),
                Habits.HabitType.valueOf(h.type),
                Habits.Period(h.retries, Habits.HabitPeriodDays(h.periodDays)!!),
                h.creationDate
            )
        )

    override suspend fun addHabit(data: Habits.HabitData) {
        val uuid = shortHabitRemoteStore.addHabit(lenses.habitDataDTO.to(data), fetchToken())
        localStore.insertHabit(transform(UUID.fromString(uuid.uid), data))
    }

    override suspend fun deleteHabit(id: UUID) {
        shortHabitRemoteStore.deleteHabit(UuidDTO(id.toString()), fetchToken())
        localStore.deleteHabit(id)
    }

    override suspend fun listHabits(): List<Habits.ShortHabit> = localStore.listHabits()
        .map { backTransform(it) }

    override fun habitsFlow(): Flow<List<Habits.ShortHabit>> = localStore.habitsFlow()
        .map { it.map { h -> backTransform(h) } }

    override suspend fun reloadCache() {
        val habits = shortHabitRemoteStore.habits(fetchToken())
        val transactHabits = habits
            .map {
                { localStore.insertHabit (
                    lenses.habitLens.from(it)
                        .let { transform(it.second, it.first) }
                    )
                }
            }
        localStore.transact(
            listOf { localStore.flushDatabase() } + transactHabits
        )
    }

    override suspend fun replaceHabit(id: UUID, data: Habits.HabitData): Unit {
        localStore.replaceHabit(transform(id, data))
        shortHabitRemoteStore.updateHabit(lenses.habitLens.to(Pair(data, id)), fetchToken())
    }
}