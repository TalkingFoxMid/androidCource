package com.talkingfox.composedtrabbithacker.data.doubletap

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
    @InstallIn(SingletonComponent::class)
    @Module
    class RetrofitModule {
        @Provides
        @Singleton
        fun provideDoubleTapRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://droid-test-server.doubletapp.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}