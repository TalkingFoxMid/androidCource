package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.infra.Clock
import com.talkingfox.composedtrabbithacker.repository.ShortHabitRepository
import com.talkingfox.composedtrabbithacker.repository.HabitCompletionsRepository
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
    fun providePreloadUseCase(repo: ShortHabitRepository): PreloadUseCase =
        PreloadUseCaseImpl(repo)

    @Provides
    @Singleton
    fun provideHabitFlowUseCase(repo: ShortHabitRepository): HabitFlowUseCase =
        HabitFlowUseCaseImpl(repo)

    @Provides
    @Singleton
    fun provideHabitReloadCacheUseCase(repo: ShortHabitRepository): HabitsReloadCacheUseCase =
        HabitsReloadCacheUseCaseImpl(repo)

    @Provides
    @Singleton
    fun provideHabitsCRUDUseCase(repo: ShortHabitRepository): HabitsCRUDUseCase =
        HabitsCRUDUseCaseImpl(repo)

    @Provides
    @Singleton
    fun provideDetailedHabitFlowUseCase(comRepo: HabitCompletionsRepository,
                                        repo: ShortHabitRepository, clock: Clock
    ): DetailedHabitListUseCase =
        DetailedHabitListUseCaseImpl(repo, comRepo, clock)

    @Provides
    @Singleton
    fun provideCompleteHabitUseCase(comRepo: HabitCompletionsRepository, clock: Clock): CompleteHabitUseCase =
        CompleteHabitUseCaseImpl(comRepo, clock)
}