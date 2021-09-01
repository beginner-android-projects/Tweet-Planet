package com.hsmsample.tweetplanet.di

import com.google.gson.Gson
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.data.network.Client
import com.hsmsample.tweetplanet.tweets.data.repository.TweetsRepository
import com.hsmsample.tweetplanet.tweets.data.repository.TweetsRepositoryImpl
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
    fun provideClientApi(retrofit: Retrofit): Client = retrofit.create(Client::class.java)

    @Provides
    @ViewModelScoped
    fun provideTweetsRepository(
        clientApi: Client,
        dispatchers: DispatcherProvider,
        gson: Gson
    ): TweetsRepositoryImpl =
        TweetsRepository(clientApi, dispatchers, gson) as TweetsRepositoryImpl

}