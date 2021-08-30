package com.hsmsample.tweetplanet.di

import com.google.gson.Gson
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.TweetsRemoteDataStore
import com.hsmsample.tweetplanet.tweets.repository.TweetsRepository
import com.hsmsample.tweetplanet.tweets.repository.TweetsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object TweetStreamModule {

    @Provides
    @ViewModelScoped
    fun provideTweetsRemoteDataStore(retrofit: Retrofit): TweetsRemoteDataStore
    = TweetsRemoteDataStore(retrofit)

    @Provides
    @ViewModelScoped
    fun provideTweetsRepository(
        tweetsRemoteDataStore: TweetsRemoteDataStore,
        dispatchers: DispatcherProvider,
        gson: Gson
    ): TweetsRepositoryImpl =
        TweetsRepository(tweetsRemoteDataStore, dispatchers, gson) as TweetsRepositoryImpl

}