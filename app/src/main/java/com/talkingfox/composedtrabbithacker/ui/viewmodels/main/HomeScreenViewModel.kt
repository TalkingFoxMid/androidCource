package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.domain.Habits
import com.talkingfox.composedtrabbithacker.ui.views.main.PreloadStateHolder
import com.talkingfox.composedtrabbithacker.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    fun makeToastMessage(retries: Int, completionsCount: Int, habitType: Habits.HabitType): String {
        fun buildToastMessage(overFlowMessage: String, underFlowMessage: (Int) -> String, overflow: Int): String =
            if(overflow >= 0) overFlowMessage else underFlowMessage(overflow)

        val overflowCount: Int = completionsCount - retries  +1

        val toastMessage: String =
            when(habitType) {
                Habits.HabitType.BAD ->
                    buildToastMessage("Хватит это делать!!!", {"Можете выполнить еще ${-it} раз"}, overflowCount)
                Habits.HabitType.GOOD ->
                    buildToastMessage("Мега хорош", {"Стоит выполнить еще ${-it} раз"}, overflowCount)
            }
        return toastMessage
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
                            it.shortHabit.id == intent.uuid
                        }
                    completeHabitUseCase.completeHabit(intent.uuid)
                    detailedHabit?.let {
                        val toastMessage =
                            makeToastMessage(
                                it.shortHabit.data.period.retries,
                                it.completions.size,
                                it.shortHabit.data.type
                            )
                        intent.cb(toastMessage)
                    }
                }
            }
        }
    }

    val state: Flow<HomeScreenState> = detailedHabitListUseCase.habitsFlow()
        .map {
            HomeScreenState(it)
        }
}

