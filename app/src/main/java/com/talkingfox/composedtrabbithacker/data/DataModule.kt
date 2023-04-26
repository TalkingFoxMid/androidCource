package com.talkingfox.composedtrabbithacker.data

import com.talkingfox.composedtrabbithacker.data.room.TokenDAO
import com.talkingfox.composedtrabbithacker.domain.HabitRemoteStore
import com.talkingfox.composedtrabbithacker.domain.HabitRepository
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
                               remoteStore: HabitRemoteStore,
                               fetchToken: TokenDAO): HabitRepository {
        return HabitJuggler(habitDAO, remoteStore, fetchToken)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(tokenDAO: TokenDAO): TokenRepository =
        TokenRepositoryImpl(tokenDAO)
}