package com.talkingfox.composedtrabbithacker.data.doubletap

import com.talkingfox.composedtrabbithacker.data.doubletap.model.HabitDTO
import com.talkingfox.composedtrabbithacker.data.doubletap.model.UuidDTO
import retrofit2.http.*

interface HabitApi {
    @Headers("Accept: application/json")
    @GET("habit")
    suspend fun getHabits(@Header("Authorization") token: String): List<HabitDTO>

    @Headers("Accept: application/json")
    @PUT("habit")
    suspend fun putHabits(@Header("Authorization") token: String, @Body habit: HabitDTO): UuidDTO

    @Headers("Accept: application/json")
    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Header("Authorization") token: String, @Body uuidDTO: UuidDTO): Unit
}
