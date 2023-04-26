package com.talkingfox.composedtrabbithacker.ui.viewmodels.create

import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.domain.HabitRepository
import com.talkingfox.composedtrabbithacker.domain.Habits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CreateScreenViewModel @Inject constructor(private val habitRepo: HabitRepository, private val clock: com.talkingfox.composedtrabbithacker.infra.Clock) : ViewModel() {
    fun habitFromState(id: UUID, s: ModelState.Modifying): Habits.Habit =
        Habits.Habit(id,
        Habits.HabitData(s.nameTextField, s.descriptionTextField,
            s.prioritySelect, s.typeSelect, s.period, clock.currentTime()))

    fun reduce(e: Event): Unit {
        val st = mutableState.value
        when (st) {
            null -> {
                when(e) {
                    is Event.Initialize -> {
                        if(!mutableState.isInitialized) {
                            if(e.habbitId == null) {
                                mutableState.value = ModelState.Modifying(
                                    "",
                                    "",
                                    Habits.Priority.LOW,
                                    Habits.HabitType.BAD,
                                    Habits.Period(0, 0),
                                    ModelState.OperationType.CreateHabit
                                )
                            } else {
                                mutableState.value = ModelState.ReadyToEdit(e.habbitId)
                            }
                        } else {}

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
                                        println(opType.id)
                                        habitRepo.replaceHabit(habitFromState(opType.id, st))
                                    }
                                }
                                is ModelState.OperationType.CreateHabit -> {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        habitRepo.addHabit(
                                            habitFromState(UUID.randomUUID(), st)
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

    private val mutableState = MutableLiveData<ModelState>()
    fun modifiedState(): LiveData<ModelState> {
        val data = habitRepo.habits().map {
            list -> when(val st = mutableState.value) {
            is ModelState.ReadyToEdit -> {
                list.find { h -> h.id == st.id }?.let { ha ->
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
            else -> {}
        }
            list
        }
        return data.switchMap { _ -> mutableState }
    }

    val state: LiveData<ModelState> = modifiedState()
}