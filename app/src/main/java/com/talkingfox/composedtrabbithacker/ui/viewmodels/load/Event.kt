package com.talkingfox.composedtrabbithacker.ui.viewmodels.load

sealed class Event {
    object TryLoad: Event()
    data class EditToken(val s: String): Event()
    object Authorize: Event()
}