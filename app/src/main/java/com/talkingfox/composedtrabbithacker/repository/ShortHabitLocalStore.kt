package com.talkingfox.composedtrabbithacker.repository

import androidx.lifecycle.LiveData
import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.*

interface ShortHabitLocalStore {
    suspend fun addHabit(h: Habits.ShortHabit): Unit

    suspend fun deleteHabit(id: UUID): Unit

    suspend fun replaceHabit(newShortHabit: Habits.ShortHabit): Unit

    fun habits(): LiveData<List<Habits.ShortHabit>>
}