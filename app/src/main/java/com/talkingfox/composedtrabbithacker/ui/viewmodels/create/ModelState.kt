package com.talkingfox.composedtrabbithacker.ui.viewmodels.create

import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.*

sealed class ModelState {
    data class Modifying(
        val nameTextField: String,
        val descriptionTextField: String,
        val prioritySelect: Habits.Priority,
        val typeSelect: Habits.HabitType,
        val period: Habits.Period,
        val opType: OperationType
    ): ModelState()

    data class ReadyToEdit(val id: UUID): ModelState()

    sealed class OperationType {
        data class EditHabit(val id: UUID): OperationType()
        object CreateHabit: OperationType()
    }
}