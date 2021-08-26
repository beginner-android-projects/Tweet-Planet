package com.hsmsample.tweetplanet.maps.repository

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.repository.TweetsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTweetsRepository(
    private val dispatcherProvider: DispatcherProvider
): TweetsRepositoryImpl {
    override suspend fun getFilteredStream(): Flow<List<JsonObject>> = flow {  }

    override fun getRandomData(): String = "ahsdjkfasldkjfhasdjlkf lkjdfshjklasdf"
}