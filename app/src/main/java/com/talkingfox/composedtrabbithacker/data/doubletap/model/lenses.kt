package com.talkingfox.composedtrabbithacker.data.doubletap.model

import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.utils.lenses.Iso
import com.talkingfox.composedtrabbithacker.utils.lenses.Lens
import java.util.*

object lenses {
    data class TransformationException(override val message: String) : java.lang.Exception(message)

    val habitPriorityLens: Iso<Habits.Priority, Int> = object: Iso<Habits.Priority, Int> {
        override fun to(a: Habits.Priority): Int =
            when(a) {
                Habits.Priority.LOW -> 0
                Habits.Priority.MEDIUM -> 1
                Habits.Priority.HIGH -> 2
            }

        override fun from(a: Int): Habits.Priority =
            when(a) {
                0 -> Habits.Priority.LOW
                1 -> Habits.Priority.MEDIUM
                2 -> Habits.Priority.HIGH
                else -> throw TransformationException("Unknown priority with id $a")
            }
    }

    val habitTypeLens: Iso<Habits.HabitType, Int> = object: Iso<Habits.HabitType, Int> {
        override fun to(a: Habits.HabitType): Int =
            when(a) {
                Habits.HabitType.BAD -> 0
                Habits.HabitType.GOOD -> 1
            }

        override fun from(a: Int): Habits.HabitType =
            when(a) {
                0 -> Habits.HabitType.BAD
                1 -> Habits.HabitType.GOOD
                else -> throw TransformationException("Unknown type with id $a")
            }

    }

    val habitLens: Iso<Habits.Habit, HabitDTO> = object: Iso<Habits.Habit, HabitDTO> {
        override fun to(a: Habits.Habit): HabitDTO =
            HabitDTO(
                uid = a.id.toString(),
                type = habitTypeLens.to(a.data.type),
                priority = habitPriorityLens.to(a.data.priority),
                frequency = a.data.period.periodDays,
                count = a.data.period.retries,
                description = a.data.description,
                title = a.data.name,
                date = a.data.creationDate
            )

        override fun from(a: HabitDTO): Habits.Habit {
            return Habits.Habit(
                id = UUID.fromString(a.uid),
                Habits.HabitData(
                    name = a.title,
                    description = a.description,
                    priority = habitPriorityLens.from(a.priority),
                    type = habitTypeLens.from(a.type),
                    period = Habits.Period(a.count, a.frequency),
                    creationDate = a.date
                )
            )
        }
    }

    val habitDataDTO: Lens<Habits.HabitData, HabitDTO> = object: Lens<Habits.HabitData, HabitDTO> {
        override fun to(f: Habits.HabitData): HabitDTO =
            HabitDTO(
                type = habitTypeLens.to(f.type),
                priority = habitPriorityLens.to(f.priority),
                frequency = f.period.periodDays,
                count = f.period.retries,
                description = f.description,
                title = f.name,
                date = 0,
                uid = null
            )
    }

    val createRequestLens: Lens<Habits.HabitData, CreateRequest> = object: Lens<Habits.HabitData, CreateRequest> {
        override fun to(f: Habits.HabitData): CreateRequest =
            CreateRequest(
                title = f.name,
                description = f.description,
                priority = habitPriorityLens.to(f.priority),
                type = habitTypeLens.to(f.type),
                frequency = f.period.periodDays,
                count = f.period.retries,
                date = 0
            )

    }

}