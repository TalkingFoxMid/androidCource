package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.domain.HabitRepository
import com.talkingfox.composedtrabbithacker.repository.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UseCasesModule {
    @Provides
    @Singleton
    fun provideTokenUseCases(repo: TokenRepository): TokenUseCases =
        TokenUseCasesImpl(repo)

    @Provides
    @Singleton
    fun providePreloadUseCase(repo: HabitRepository): PreloadUseCase =
        PreloadUseCaseImpl(repo)
}