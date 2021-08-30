package com.hsmsample.tweetplanet.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.di.dispatchers.StandardDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): DispatcherProvider = StandardDispatcher()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()
}