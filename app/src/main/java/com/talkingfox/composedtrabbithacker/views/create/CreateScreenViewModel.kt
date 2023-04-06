package com.talkingfox.composedtrabbithacker.views.create

import android.graphics.Path.Op
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.talkingfox.composedtrabbithacker.domain.HabitRepo
import com.talkingfox.composedtrabbithacker.domain.HabitRepoInMemoryImpl
import com.talkingfox.composedtrabbithacker.domain.Habits
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CreateScreenViewModel @Inject constructor(private val habitRepo: HabitRepoInMemoryImpl) : ViewModel() {
    companion object CreateScreenViewModel {
        sealed class OperationType {
            data class EditHabit(val id: UUID): OperationType()
            object CreateHabit: OperationType()
        }

        data class State(
            val nameTextField: String,
            val descriptionTextField: String,
            val prioritySelect: Habits.Priority,
            val typeSelect: Habits.HabitType,
            val period: Habits.Period,
            val opType: OperationType
        )

        sealed class Event {
            data class SetName(val name: String): Event()
            data class SetDesc(val desc: String): Event()
            data class SetPriority(val priority: Habits.Priority): Event()
            data class SetType(val type: Habits.HabitType): Event()
            data class SetPeriod(val period: Habits.Period): Event()
            data class Initialize(val habbitId: UUID?): Event()
            object DoneEdit: Event()
        }
    }

    fun habitFromState(s: State): Habits.Habit =
        Habits.Habit(UUID.randomUUID(), s.nameTextField, s.descriptionTextField,
            s.prioritySelect, s.typeSelect, s.period)

    fun reduce(e: Event): Unit = when(e) {
        is Event.SetName -> {
            println("REDUCED SET NAME")
            println(mutableState.value)
            println(e.name)
            mutableState.value = mutableState.value?.copy(nameTextField = e.name)
            println(mutableState.value)

        }
        is Event.SetDesc -> mutableState.value = mutableState.value?.copy(descriptionTextField = e.desc)
        is Event.SetPriority -> mutableState.value = mutableState.value?.copy(prioritySelect = e.priority)
        is Event.SetType -> mutableState.value = mutableState.value?.copy(typeSelect = e.type)
        is Event.SetPeriod -> mutableState.value = mutableState.value?.copy(period = e.period)
        is Event.Initialize -> if(!mutableState.isInitialized){
          val habitMaybe = e.habbitId.let { id -> habitRepo.habits().find { it.id == id } }
            if(habitMaybe != null) {
                mutableState.value = State(
                    habitMaybe.name,
                    habitMaybe.description,
                    habitMaybe.priority,
                    habitMaybe.type,
                    habitMaybe.period,
                    OperationType.EditHabit(habitMaybe.id)
                )
            } else {
                mutableState.value = State(
                    "",
                    "",
                    Habits.Priority.LOW,
                    Habits.HabitType.NEUTRAL,
                    Habits.Period(0, 0),
                    OperationType.CreateHabit
                )
            }
        } else {}

        is Event.DoneEdit -> {
            when(val realValue = mutableState.value) {
                is State -> when(val opType = realValue.opType) {
                    is OperationType.EditHabit -> {
                       habitRepo.replaceHabit(opType.id, habitFromState(realValue))
                    }
                    is OperationType.CreateHabit -> {
                        habitRepo.addHabit(
                            habitFromState(realValue)
                        )
                    }
                }
                else -> {}
            }
        }
    }
    private val mutableState = MutableLiveData<State>()
    val state: LiveData<State> = mutableState
}