package com.talkingfox.composedtrabbithacker.infra

interface Clock {
    fun currentTime(): Long
}