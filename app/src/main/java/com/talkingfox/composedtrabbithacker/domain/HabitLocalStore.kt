package com.talkingfox.composedtrabbithacker.domain

import androidx.lifecycle.LiveData
import java.util.*

interface HabitLocalStore {
    suspend fun addHabit(h: Habits.Habit): Unit

    suspend fun deleteHabit(id: UUID): Unit

    suspend fun replaceHabit(newHabit: Habits.Habit): Unit

    fun habits(): LiveData<List<Habits.Habit>>
}