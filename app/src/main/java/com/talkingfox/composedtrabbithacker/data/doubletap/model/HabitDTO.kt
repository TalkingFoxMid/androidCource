package com.talkingfox.composedtrabbithacker.data.doubletap.model

import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.UUID

data class HabitDTO(
    val count: Int,
    val date: Long,
    val description: String,
    val priority: Int,
    val frequency: Int,
    val title: String,
    val type: Int,
    val uid: String?
)
