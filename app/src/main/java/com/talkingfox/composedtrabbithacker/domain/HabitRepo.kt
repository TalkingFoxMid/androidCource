package com.talkingfox.composedtrabbithacker.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import java.util.*

interface HabitRepo {
    fun addHabit(h: Habits.Habit): Unit

    fun deleteHabit(id: UUID): Unit

    fun replaceHabit(id: UUID, newHabit: Habits.Habit): Unit

    fun liveData(): LiveData<List<Habits.Habit>>
}

class HabitRepoInMemoryImpl : HabitRepo {
    private val list = mutableStateListOf<Habits.Habit>()

    override fun addHabit(h: Habits.Habit) {
        list.add(h)
    }

    override fun deleteHabit(id: UUID) {
        list.removeIf {
            it.id == id
        }
    }

    override fun replaceHabit(id: UUID, newHabit: Habits.Habit): Unit {
        val maybe = list.find { it.id == id }
        maybe?.name = newHabit.name
        maybe?.description = newHabit.description
        maybe?.period = newHabit.period
        maybe?.priority = newHabit.priority
        maybe?.type = newHabit.type
    }


    override fun liveData(): LiveData<List<Habits.Habit>> =
        MutableLiveData(list)
}