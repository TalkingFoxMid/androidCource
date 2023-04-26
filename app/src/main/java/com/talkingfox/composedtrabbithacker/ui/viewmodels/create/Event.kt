package com.talkingfox.composedtrabbithacker.ui.viewmodels.create

import com.talkingfox.composedtrabbithacker.domain.Habits
import java.util.*

sealed class Event {
    data class SetName(val name: String): Event()
    data class SetDesc(val desc: String): Event()
    data class SetPriority(val priority: Habits.Priority): Event()
    data class SetType(val type: Habits.HabitType): Event()
    data class SetPeriod(val period: Habits.Period): Event()
    data class Initialize(val habbitId: UUID?): Event()
    data class DoneEdit(val cb: (DoneEditResult) -> Unit): Event()
}

sealed class DoneEditResult
object DoneEditResultSuccess : DoneEditResult()
data class DoneEditResultError(val msg: String) : DoneEditResult()