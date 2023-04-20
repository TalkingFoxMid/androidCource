package com.talkingfox.composedtrabbithacker.utils.lenses

interface Iso<A, B> {
    fun to(a: A): B
    fun from(a: B): A
}