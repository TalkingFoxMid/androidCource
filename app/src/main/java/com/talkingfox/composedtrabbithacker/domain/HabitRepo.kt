package com.talkingfox.composedtrabbithacker.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface HabitRepo {
    fun addHabit(h: Habits.Habit): Unit

    fun deleteHabit(id: UUID): Unit

    fun replaceHabit(id: UUID, newHabit: Habits.Habit): Unit

    fun habits(): List<Habits.Habit>
}

@Singleton
class HabitRepoInMemoryImpl @Inject constructor() : HabitRepo {
    private val list = mutableListOf<Habits.Habit>()

    override fun addHabit(h: Habits.Habit) {
        list.add(h)
    }

    override fun deleteHabit(id: UUID) {
        list.removeIf {
            it.id == id
        }
    }

    override fun habits(): List<Habits.Habit> = list

    override fun replaceHabit(id: UUID, newHabit: Habits.Habit): Unit {
        val maybe = list.find { it.id == id }
        maybe?.name = newHabit.name
        maybe?.description = newHabit.description
        maybe?.period = newHabit.period
        maybe?.priority = newHabit.priority
        maybe?.type = newHabit.type
    }
}