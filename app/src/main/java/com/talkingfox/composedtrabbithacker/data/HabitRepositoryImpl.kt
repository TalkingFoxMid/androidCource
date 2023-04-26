package com.talkingfox.composedtrabbithacker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.talkingfox.composedtrabbithacker.data.doubletap.model.UuidDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.lenses
import com.talkingfox.composedtrabbithacker.data.room.TokenDAO
import com.talkingfox.composedtrabbithacker.data.room.model.Habit
import com.talkingfox.composedtrabbithacker.domain.HabitRemoteStore
import com.talkingfox.composedtrabbithacker.domain.HabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.*

class HabitJuggler (private val localStore: com.talkingfox.composedtrabbithacker.data.room.HabitDAO,
                    private val habitRemoteStore: HabitRemoteStore,
private val tokenDAO: TokenDAO) : HabitRepository {
    private fun fetchToken(): String = tokenDAO.getToken().first().token
    private fun transform(h: Habits.Habit): Habit =
        Habit(h.id, h.data.name, h.data.description, h.data.priority.name, h.data.type.name, h.data.period.periodDays, h.data.period.retries, h.data.creationDate)

    private fun backTransform(h: Habit): Habits.Habit =
        Habits.Habit(
            h.uuid,
            Habits.HabitData(
                h.name,
                h.description,
                Habits.Priority.valueOf(h.priority),
                Habits.HabitType.valueOf(h.type),
                Habits.Period(h.periodDays, h.retries),
                h.creationDate
            )
        )

    override suspend fun addHabit(h: Habits.Habit) {
        habitRemoteStore.addHabit(lenses.habitDataDTO.to(h.data), fetchToken())
        localStore.insertHabit(transform(h))
    }

    override suspend fun deleteHabit(id: UUID) {
        habitRemoteStore.deleteHabit(UuidDTO(id.toString()), fetchToken())
        localStore.deleteHabit(id)
    }

    override fun habits(): LiveData<List<Habits.Habit>> = localStore.habits()
        .map { it.map { h -> backTransform(h) } }

    override suspend fun reloadCache() {
        val habits = habitRemoteStore.habits(fetchToken())
        val transactHabits = habits
            .map {
                { localStore.insertHabit(transform(lenses.habitLens.from(it))) }
            }
        println("HABITS: ${habits}")
        localStore.transact(
            listOf(
                { localStore.flushDatabase() },
            ) + transactHabits
        )
    }

    override suspend fun replaceHabit(newHabit: Habits.Habit): Unit {
        localStore.replaceHabit(transform(newHabit))
        habitRemoteStore.updateHabit(lenses.habitLens.to(newHabit), fetchToken())
    }
}