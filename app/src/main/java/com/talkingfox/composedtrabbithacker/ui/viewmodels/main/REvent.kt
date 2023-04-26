package com.talkingfox.composedtrabbithacker.ui.viewmodels.main

object REvent {
    sealed interface ToastType {
        object TooMuchBad: ToastType
        object TooMuchGood: ToastType

        sealed interface ToastUnderflow: ToastType {
            val underflow: Int
        }
        data class UnderflowBad(override val underflow: Int): ToastUnderflow
        data class UnderflowGood(override val underflow: Int): ToastUnderflow

        object Empty: ToastType
    }
}