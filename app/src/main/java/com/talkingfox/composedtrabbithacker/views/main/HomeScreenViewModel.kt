package com.talkingfox.composedtrabbithacker.views.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.talkingfox.composedtrabbithacker.domain.HabitRepo
import com.talkingfox.composedtrabbithacker.domain.HabitRepoInMemoryImpl
import com.talkingfox.composedtrabbithacker.domain.Habits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val habitRepo: HabitRepoInMemoryImpl) : ViewModel() {
    companion object HomeScreenViewModel {
        data class HomeScreenState(
            val habits: List<Habits.Habit>
        )
        sealed class Events {
            data class DeleteHabit(val uuid: UUID) : Events()
        }
    }
    fun reduce(intent: Events) = when(intent) {
        is Events.DeleteHabit -> {
            viewModelScope.launch(Dispatchers.IO) {
                habitRepo.deleteHabit(intent.uuid)
            }
        }
    }

    val state: LiveData<HomeScreenState> = habitRepo.habits()
        .map {
            HomeScreenState(it)
        }
}

