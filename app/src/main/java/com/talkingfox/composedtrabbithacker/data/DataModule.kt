package com.talkingfox.composedtrabbithacker.data

import com.talkingfox.composedtrabbithacker.data.room.HabitCompletionsDAO
import com.talkingfox.composedtrabbithacker.data.room.TokenDAO
import com.talkingfox.composedtrabbithacker.repository.ShortHabitRemoteStore
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
class DataModule {

    @Provides
    @Singleton
    fun provideHabitRepository(habitDAO: com.talkingfox.composedtrabbithacker.data.room.HabitDAO,
                               remoteStore: ShortHabitRemoteStore,
                               fetchToken: TokenDAO): ShortHabitRepository {
        return ShortHabitDataRepositoryImpl(habitDAO, remoteStore, fetchToken)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(tokenDAO: TokenDAO): TokenRepository =
        TokenRepositoryImpl(tokenDAO)

    @Provides
    @Singleton
    fun provideCompletionRepository(completionDAO: HabitCompletionsDAO): HabitCompletionsRepository =
        HabitCompletionsRepositoryImpl(completionDAO)
}