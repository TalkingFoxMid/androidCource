package com.talkingfox.composedtrabbithacker.data.doubletap.model


data class CreateRequest (
    val count: Int,
    val date: Int,
    val description: String,
    val priority: Int,
    val frequency: Int,
    val title: String,
    val type: Int
)