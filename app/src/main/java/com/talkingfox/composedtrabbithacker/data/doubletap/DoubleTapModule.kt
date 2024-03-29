package com.talkingfox.composedtrabbithacker.data.doubletap

import com.talkingfox.composedtrabbithacker.repository.ShortHabitRemoteStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DoubleTapModule {

    @Provides
    @Singleton
    fun remoteStore(): ShortHabitRemoteStore =
        ShortHabitDoubleTapStore(
            Retrofit.Builder()
                .baseUrl("https://droid-test-server.doubletapp.ru/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        )

}