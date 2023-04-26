package com.talkingfox.composedtrabbithacker.ui.views.main

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MainScreenModule {
    @Provides
    @Singleton
    fun providePreloadStateHolder(): PreloadStateHolder {
        return PreloadStateHolder()
    }
}