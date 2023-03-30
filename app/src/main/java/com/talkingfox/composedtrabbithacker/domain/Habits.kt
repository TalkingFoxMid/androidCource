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
        var id: UUID,
        var name: String,
        var description: String,
        var priority: Priority,
        var type: HabitType,
        var period: Period
    )

    data class Period(
        val retries: Int,
        val periodDays: Int
    )
}