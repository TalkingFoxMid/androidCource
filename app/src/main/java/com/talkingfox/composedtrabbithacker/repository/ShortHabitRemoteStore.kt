package com.talkingfox.composedtrabbithacker.repository

import com.talkingfox.composedtrabbithacker.data.doubletap.model.HabitDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.UuidDTO

interface ShortHabitRemoteStore {
    suspend fun habits(token: String): List<HabitDTO>
    suspend fun addHabit(habit: HabitDTO, token: String): UuidDTO
    suspend fun updateHabit(habit: HabitDTO, token: String): UuidDTO
    suspend fun deleteHabit(uuid: UuidDTO, token: String): Unit
}