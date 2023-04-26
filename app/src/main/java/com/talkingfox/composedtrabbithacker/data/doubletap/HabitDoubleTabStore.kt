package com.talkingfox.composedtrabbithacker.data.doubletap

import com.talkingfox.composedtrabbithacker.data.doubletap.model.HabitDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.UuidDTO
import com.talkingfox.composedtrabbithacker.repository.ShortHabitRemoteStore
import retrofit2.Retrofit

class ShortHabitDoubleTapStore(private val rf: Retrofit): ShortHabitRemoteStore {
    private val api = rf.create(HabitApi::class.java)

    override suspend fun habits(token: String): List<HabitDTO> =
        api.getHabits(token)

    override suspend fun addHabit(habit: HabitDTO, token: String): UuidDTO =
        api.putHabits(token, habit)

    override suspend fun updateHabit(habit: HabitDTO, token: String): UuidDTO {
        return api.putHabits(token, habit)
    }

    override suspend fun deleteHabit(uuid: UuidDTO, token: String): Unit =
        api.deleteHabit(token, uuid)

}