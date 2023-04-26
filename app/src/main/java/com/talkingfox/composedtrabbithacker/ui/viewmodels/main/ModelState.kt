package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

import com.talkingfox.composedtrabbithacker.domain.Habits

data class HomeScreenState(
    val shortHabits: List<Habits.CompletableHabit>
)