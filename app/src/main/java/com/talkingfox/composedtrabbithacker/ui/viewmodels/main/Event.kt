package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

import java.util.*

sealed class Event {
    object PreloadHabits: Event()
    data class ForceReloadHabits(val cb: () -> Unit): Event()
    data class DeleteHabit(val uuid: UUID) : Event()
}