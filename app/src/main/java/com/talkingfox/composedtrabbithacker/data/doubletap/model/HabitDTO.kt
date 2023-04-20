package com.talkingfox.composedtrabbithacker.data.doubletap.model

import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.UUID

data class HabitDTO(
    val count: Int,
    val date: Int,
    val description: String,
    val priority: Int,
    val frequency: Int,
    val title: String,
    val type: Int,
    val uuid: String
)