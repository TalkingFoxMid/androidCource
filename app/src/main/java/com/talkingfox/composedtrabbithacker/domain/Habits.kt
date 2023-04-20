package com.talkingfox.composedtrabbithacker.domain

import androidx.compose.ui.graphics.Color
import java.util.UUID
import kotlin.time.Duration


object Habits {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    enum class HabitType {
        BAD, GOOD, NEUTRAL
    }

    data class Habit(
        val id: UUID,
        val data: HabitData
    )

    data class HabitData(
        val name: String,
        val description: String,
        val priority: Priority,
        val type: HabitType,
        val period: Period
    )

    data class Period(
        val retries: Int,
        val periodDays: Int
    )
}