package com.talkingfox.composedtrabbithacker.ui.viewmodels.load

sealed class State {
    object Loading: State()
    data class NeedAuthorization(val bufferToken: String): State()
    object Ready: State()
}