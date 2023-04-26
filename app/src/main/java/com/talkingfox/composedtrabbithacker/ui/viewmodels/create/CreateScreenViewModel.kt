package com.talkingfox.composedtrabbithacker.ui.viewmodels.create

import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateScreenViewModel @Inject constructor(private val habitRepo: ShortHabitRepository, private val clock: com.talkingfox.composedtrabbithacker.infra.Clock) : ViewModel() {
    fun habitDataFromState(s: ModelState.Modifying): Habits.HabitData =
        Habits.HabitData(s.nameTextField, s.descriptionTextField,
            s.prioritySelect, s.typeSelect, s.period, clock.currentTime())

    fun reduce(e: Event): Unit {
        val st = mutableState.value
        when (st) {
            ModelState.Uninitialized -> {
                when(e) {
                    is Event.Initialize -> {
                        e.habbitId?.let {
                            viewModelScope.launch(Dispatchers.IO) {
                                val habits = habitRepo.listHabits()
                                habits.find { h -> h.id == it }?.let { ha ->
                                    viewModelScope.launch {
                                        mutableState.value = ModelState.Modifying(
                                            ha.data.name,
                                            ha.data.description,
                                            ha.data.priority,
                                            ha.data.type,
                                            ha.data.period,
                                            ModelState.OperationType.EditHabit(ha.id)
                                        )
                                    }
                                }
                            }
                        } ?: run {
                            mutableState.value = ModelState.Modifying(
                                "",
                                "",
                                Habits.Priority.LOW,
                                Habits.HabitType.BAD,
                                Habits.Period(0, Habits.HabitPeriodDays.one()),
                                ModelState.OperationType.CreateHabit
                            )
                        }
                    }
                    else -> {}
                }
            }
            is ModelState.Modifying -> {
                when(e) {
                    is Event.SetName -> {
                        mutableState.value = st.copy(nameTextField = e.name)
                    }
                    is Event.SetDesc -> mutableState.value = st.copy(descriptionTextField = e.desc)
                    is Event.SetPriority -> mutableState.value = st.copy(prioritySelect = e.priority)
                    is Event.SetType -> mutableState.value = st.copy(typeSelect = e.type)
                    is Event.SetPeriod -> mutableState.value = st.copy(period = e.period)

                    is Event.DoneEdit -> {
                        if(isValidToCreate()) {
                            when (val opType = st.opType) {
                                is ModelState.OperationType.EditHabit -> {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        habitDataFromState(st).let {
                                            habitRepo.replaceHabit(opType.id, it)
                                        }
                                    }
                                }
                                is ModelState.OperationType.CreateHabit -> {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        habitRepo.addHabit(
                                            habitDataFromState(st)
                                        )
                                    }
                                }
                            }
                            e.cb(DoneEditResultSuccess)
                        } else e.cb(DoneEditResultError("error"))
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }
    fun isValidToCreate(): Boolean = when(val st = mutableState.value) {
        is ModelState.Modifying -> st.nameTextField.isNotEmpty() && st.descriptionTextField.isNotEmpty()
        else -> false
    }

    private val mutableState = MutableStateFlow<ModelState>(ModelState.Uninitialized)

    val state: Flow<ModelState> = mutableState
}