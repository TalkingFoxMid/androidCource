package com.talkingfox.composedtrabbithacker.utils.lenses

interface Lens<From, To> {
    fun to(f: From): To
}