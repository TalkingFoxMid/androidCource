package com.talkingfox.composedtrabbithacker.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.talkingfox.composedtrabbithacker.data.room.HabitDao
import com.talkingfox.composedtrabbithacker.data.room.model.Habit
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface HabitRepo {
    suspend fun addHabit(h: Habits.Habit): Unit

    suspend fun deleteHabit(id: UUID): Unit

    suspend fun replaceHabit(newHabit: Habits.Habit): Unit

    fun habits(): LiveData<List<Habits.Habit>>
}

@Singleton
class HabitRepoInMemoryImpl @Inject constructor(private val dao: HabitDao) : HabitRepo {
    private fun transform(h: Habits.Habit): Habit =
        Habit(h.id, h.data.name, h.data.description, h.data.priority.name, h.data.type.name, h.data.period.periodDays, h.data.period.retries)

    private fun backTransform(h: Habit): Habits.Habit =
        Habits.Habit(
            h.uuid,
            Habits.HabitData(
                h.name,
                h.description,
                Habits.Priority.valueOf(h.priority),
                Habits.HabitType.valueOf(h.type),
                Habits.Period(h.periodDays, h.retries)
            )
        )

    override suspend fun addHabit(h: Habits.Habit) {
        dao.insertHabit(transform(h))
    }

    override suspend fun deleteHabit(id: UUID) {
        dao.deleteHabit(id)
    }

    override fun habits(): LiveData<List<Habits.Habit>> = dao.habits().map {
        it.map { el -> backTransform(el) }
    }

    override suspend fun replaceHabit(newHabit: Habits.Habit): Unit {
        dao.replaceHabit(transform(newHabit))
    }
}