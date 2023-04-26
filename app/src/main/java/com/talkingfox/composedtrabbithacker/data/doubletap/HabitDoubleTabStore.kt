package com.talkingfox.composedtrabbithacker.data.doubletap

import com.talkingfox.composedtrabbithacker.data.doubletap.model.HabitDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.UuidDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.lenses
import com.talkingfox.composedtrabbithacker.domain.HabitRemoteStore
import com.talkingfox.composedtrabbithacker.domain.Habits
import retrofit2.Retrofit
import java.util.UUID

class HabitDoubleTapStore(private val rf: Retrofit): HabitRemoteStore {
    private val api = rf.create(HabitApi::class.java)

    override suspend fun habits(token: String): List<HabitDTO> =
        api.getHabits(token)

    override suspend fun addHabit(habit: HabitDTO, token: String): UuidDTO =
        api.putHabits(token, habit)

    override suspend fun updateHabit(habit: HabitDTO, token: String): UuidDTO {
        println("${habit}")
        return api.putHabits(token, habit)
    }

    override suspend fun deleteHabit(uuid: UuidDTO, token: String): Unit =
        api.deleteHabit(token, uuid)

}