package com.talkingfox.composedtrabbithacker.views.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.talkingfox.composedtrabbithacker.domain.HabitRepo
import com.talkingfox.composedtrabbithacker.domain.HabitRepoInMemoryImpl
import com.talkingfox.composedtrabbithacker.domain.Habits
import dagger.hilt.android.lifecycle.HiltViewModel
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
    fun reloadState() {
        val newList = mutableStateListOf<Habits.Habit>()
        newList.addAll(habitRepo.habits())
        mutableState.value = HomeScreenState(newList)
    }
    fun reduce(intent: Events) = when(intent) {
        is Events.DeleteHabit -> {habitRepo.deleteHabit(intent.uuid); reloadState()}
    }

    private val mutableState = MutableLiveData(HomeScreenState(listOf()))
    val state: LiveData<HomeScreenState> = mutableState
}

