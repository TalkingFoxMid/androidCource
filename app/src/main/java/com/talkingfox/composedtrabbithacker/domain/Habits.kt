package com.talkingfox.composedtrabbithacker.domain

import java.util.UUID


object Habits {
    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    enum class HabitType {
        BAD, GOOD
    }

    data class ShortHabit(
        val id: UUID,
        val data: HabitData,
    )

    data class DetailedHabit(
        val shortHabit: ShortHabit,
        val completions: List<HabitCompletion>
    )

    data class HabitData(
        val name: String,
        val description: String,
        val priority: Priority,
        val type: HabitType,
        val period: Period,
        val creationDate: Long,
    )

    data class Period(
        val retries: Int,
        val periodDays: Int
    )

    data class HabitCompletion(
        val uuid: UUID,
        val date: Long
    )
}