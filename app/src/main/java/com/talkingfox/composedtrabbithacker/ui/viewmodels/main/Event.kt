package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.*

sealed class Event {
    object PreloadHabits: Event()
    data class ForceReloadHabits(val cb: () -> Unit): Event()
    data class DeleteHabit(val uuid: UUID) : Event()
    data class Complete(val uuid: UUID, val cb: (String) -> Unit): Event()
}