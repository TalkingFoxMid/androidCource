package com.talkingfox.composedtrabbithacker.views.create

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.domain.HabitRepoInMemoryImpl
import com.talkingfox.composedtrabbithacker.domain.Habits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class CreateScreenViewModel @Inject constructor(private val habitRepo: HabitRepoInMemoryImpl) : ViewModel() {
    companion object CreateScreenViewModel {
        sealed class OperationType {
            data class EditHabit(val id: UUID): OperationType()
            object CreateHabit: OperationType()
        }

        sealed class State {
            data class Modifying(
                val nameTextField: String,
                val descriptionTextField: String,
                val prioritySelect: Habits.Priority,
                val typeSelect: Habits.HabitType,
                val period: Habits.Period,
                val opType: OperationType
            ): State()

            data class ReadyToEdit(val id: UUID): State()
        }


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

    fun habitFromState(id: UUID, s: State.Modifying): Habits.Habit =
        Habits.Habit(id,
        Habits.HabitData(s.nameTextField, s.descriptionTextField,
            s.prioritySelect, s.typeSelect, s.period))

    fun reduce(e: Event): Unit {
        val st = mutableState.value
        when (st) {
            null -> {
                when(e) {
                    is Event.Initialize -> {
                        println("INITIALIZE")
                        if(!mutableState.isInitialized) {
                            if(e.habbitId == null) {
                                mutableState.value = State.Modifying(
                                    "",
                                    "",
                                    Habits.Priority.LOW,
                                    Habits.HabitType.NEUTRAL,
                                    Habits.Period(0, 0),
                                    OperationType.CreateHabit
                                )
                            } else {
                                mutableState.value = State.ReadyToEdit(e.habbitId)
                            }
                        } else {}

                    }
                    else -> {}
                }
            }
            is State.Modifying -> {
                when(e) {
                    is Event.SetName -> {
                        mutableState.value = st.copy(nameTextField = e.name)
                    }
                    is Event.SetDesc -> mutableState.value = st.copy(descriptionTextField = e.desc)
                    is Event.SetPriority -> mutableState.value = st.copy(prioritySelect = e.priority)
                    is Event.SetType -> mutableState.value = st.copy(typeSelect = e.type)
                    is Event.SetPeriod -> mutableState.value = st.copy(period = e.period)

                    is Event.DoneEdit -> {
                        when (val opType = st.opType) {
                            is OperationType.EditHabit -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    habitRepo.replaceHabit(habitFromState(opType.id, st))
                                }
                            }
                            is OperationType.CreateHabit -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    habitRepo.addHabit(
                                        habitFromState(UUID.randomUUID(), st)
                                    )
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }
    private val mutableState = MutableLiveData<State>()
    fun modifiedState(): LiveData<State> {
        val data = habitRepo.habits().map {
            list -> when(val st = mutableState.value) {
            is State.ReadyToEdit -> {
                list.find { h -> h.id == st.id }?.let { ha ->
                    mutableState.value = State.Modifying(
                        ha.data.name,
                        ha.data.description,
                        ha.data.priority,
                        ha.data.type,
                        ha.data.period,
                        OperationType.EditHabit(ha.id)
                    )
                }
            }
            else -> {}
        }
            list
        }
        return data.switchMap { _ -> mutableState }
    }

    val state: LiveData<State> = modifiedState()
}