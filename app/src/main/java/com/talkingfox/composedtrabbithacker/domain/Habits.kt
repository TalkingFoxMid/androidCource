package com.talkingfox.composedtrabbithacker.domain

import androidx.compose.ui.graphics.Color
import kotlin.time.Duration


object Habits {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    enum class HabitType {
        BAD, GOOD, NEUTRAL
    }

    data class Habit(
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