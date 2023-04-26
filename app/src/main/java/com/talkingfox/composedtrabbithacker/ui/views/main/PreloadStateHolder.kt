package com.talkingfox.composedtrabbithacker.ui.views.main

class PreloadStateHolder {
    private var preloaded: Boolean = false
    fun isPreloaded(): Boolean = preloaded
    fun preload(): Unit {
        preloaded = true
    }
}