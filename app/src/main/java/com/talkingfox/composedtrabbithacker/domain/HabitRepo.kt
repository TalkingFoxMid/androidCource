package com.talkingfox.composedtrabbithacker.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talkingfox.composedtrabbithacker.dao.HabitDao
import com.talkingfox.composedtrabbithacker.dao.model.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface HabitRepo {
    fun addHabit(h: Habits.Habit): Unit

    fun deleteHabit(id: UUID): Unit

    fun replaceHabit(newHabit: Habits.Habit): Unit

    fun habits(): LiveData<List<Habits.Habit>>
}

@Singleton
class HabitRepoInMemoryImpl @Inject constructor(private val dao: HabitDao) : HabitRepo {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private fun transform(h: Habits.Habit): Habit =
        Habit(h.id, h.name, h.description, h.priority.name, h.type.name, h.period.periodDays, h.period.retries)

    private fun backTransform(h: Habit): Habits.Habit =
        Habits.Habit(
            h.uuid,
            h.name,
            h.description,
            Habits.Priority.valueOf(h.priority),
            Habits.HabitType.valueOf(h.type),
            Habits.Period(h.periodDays, h.retries)
        )

    override fun addHabit(h: Habits.Habit) {
        coroutineScope.launch(Dispatchers.IO) {
            dao.insertHabit(transform(h))
        }
    }

    override fun deleteHabit(id: UUID) {
        coroutineScope.launch(Dispatchers.IO) {
            dao.deleteHabit(id)
        }
    }

    override fun habits(): LiveData<List<Habits.Habit>> = dao.habits().map { it.map { el -> backTransform(el) } }

    override fun replaceHabit(newHabit: Habits.Habit): Unit {
        coroutineScope.launch(Dispatchers.IO) {
            dao.replaceHabit(transform(newHabit))
        }
    }
}