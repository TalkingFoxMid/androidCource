package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.R
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.ui.views.main.PreloadStateHolder
import com.talkingfox.composedtrabbithacker.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val detailedHabitListUseCase: DetailedHabitListUseCase,
    private val completeHabitUseCase: CompleteHabitUseCase,
    private val preloadStateHolder: PreloadStateHolder,
    private val habitsReloadCacheUseCase: HabitsReloadCacheUseCase,
    private val habitsCRUDUseCase: HabitsCRUDUseCase
) : ViewModel() {
    private fun makeToastEvent(retries: Int, completionsCount: Int, habitType: Habits.HabitType): REvent.ToastType {
        val overflowCount: Int = completionsCount - retries  +1

        fun buildToastMessage(overflow: REvent.ToastType, underflow: (Int) -> REvent.ToastType.ToastUnderflow): REvent.ToastType =
            if(overflowCount >= 0) overflow else underflow(-overflowCount)

        return when(habitType) {
            Habits.HabitType.GOOD -> buildToastMessage(REvent.ToastType.TooMuchGood) {
                i -> REvent.ToastType.UnderflowGood(i)
            }
            Habits.HabitType.BAD -> buildToastMessage(REvent.ToastType.TooMuchBad) {
                    i -> REvent.ToastType.UnderflowBad(i)
            }
        }
    }

    fun reduce(intent: Event): Unit {
        when(intent) {
            is Event.DeleteHabit -> {
                viewModelScope.launch(Dispatchers.IO) {
                    habitsCRUDUseCase.deleteHabit(intent.uuid)
                }.run {  }
            }
            is Event.PreloadHabits -> {
                if(!preloadStateHolder.isPreloaded()) {
                    viewModelScope.launch (Dispatchers.IO) {
                        habitsReloadCacheUseCase.reloadCache()
                    }
                    preloadStateHolder.preload()
                }
            }
            is Event.ForceReloadHabits -> {
                viewModelScope.launch (Dispatchers.IO) {
                    habitsReloadCacheUseCase.reloadCache()
                    intent.cb()
                }
            }
            is Event.Complete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val detailedHabit = detailedHabitListUseCase.habits()
                        .find {
                            it.shortHabit.id == intent.habit.shortHabit.id
                        }
                    detailedHabit?.let {
                        completeHabitUseCase.completeHabit(intent.habit.shortHabit.id, detailedHabit.shortHabit.data.period.periodDays)

                        mToastState.value = makeToastEvent(
                            it.shortHabit.data.period.retries,
                            it.completion?.tries?.toInt() ?: 0,
                            it.shortHabit.data.type
                        )
                    }
                }
            }
            Event.FlushToast -> {
                mToastState.value = REvent.ToastType.Empty
            }
        }
    }

    val state: Flow<HomeScreenState> = detailedHabitListUseCase.habitsFlow()
        .map {
            HomeScreenState(it)
        }
    private val mToastState = MutableStateFlow<REvent.ToastType>(REvent.ToastType.Empty)

    val toastState: Flow<REvent.ToastType> = mToastState
}

