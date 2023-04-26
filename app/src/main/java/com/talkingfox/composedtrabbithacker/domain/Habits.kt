package com.talkingfox.composedtrabbithacker.domain

import java.util.UUID


object Habits {
    @JvmInline
    value class HabitPeriodDays private constructor(val periodDays: Int) {
        override fun toString(): String {
            return periodDays.toString()
        }
        companion object {
            operator fun invoke(_periodDays: Int): HabitPeriodDays? =
                if(_periodDays <= 0) null else HabitPeriodDays(_periodDays)

            fun one(): HabitPeriodDays = HabitPeriodDays(1)
        }
    }
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

    data class CompletableHabit(
        val shortHabit: ShortHabit,
        val completion: Completion?
    )

    data class Completion(val tries: Long)



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
        val periodDays: HabitPeriodDays
    )

}