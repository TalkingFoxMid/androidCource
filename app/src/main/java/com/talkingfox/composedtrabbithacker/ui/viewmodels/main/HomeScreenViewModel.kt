package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.domain.HabitRepository
import com.talkingfox.composedtrabbithacker.repository.TokenRepository
import com.talkingfox.composedtrabbithacker.ui.views.main.PreloadStateHolder
import com.talkingfox.composedtrabbithacker.usecases.TokenUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val habitRepo: HabitRepository,
                                              private val preloadStateHolder: PreloadStateHolder,
                                              private val tokenUseCases: TokenUseCases
) : ViewModel() {
    fun reduce(intent: Event): Unit {
        when(intent) {
            is Event.DeleteHabit -> {
                viewModelScope.launch(Dispatchers.IO) {
                    habitRepo.deleteHabit(intent.uuid)
                }.run {  }
            }
            is Event.PreloadHabits -> {
                if(!preloadStateHolder.isPreloaded()) {
                    viewModelScope.launch (Dispatchers.IO) {
                        habitRepo.reloadCache()
                    }
                    preloadStateHolder.preload()
                }
            }
            is Event.ForceReloadHabits -> {
                viewModelScope.launch (Dispatchers.IO) {
                    habitRepo.reloadCache()
                    intent.cb()
                }
            }
        }
    }

    val state: LiveData<HomeScreenState> = habitRepo.habits()
        .map {
            HomeScreenState(it)
        }
}

