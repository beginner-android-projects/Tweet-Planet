package com.hsmsample.tweetplanet.maps.repository

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.model.TweetData
import com.hsmsample.tweetplanet.tweets.repository.TweetsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTweetsRepository(
    private val dispatcherProvider: DispatcherProvider
): TweetsRepositoryImpl {
    override fun getFilteredStream(): Flow<String?> {

    }

    override suspend fun addRule(keyword: String): Result<JsonObject> {

    }

    override suspend fun deleteExistingRules(ruleId: String): Result<JsonObject> {

    }

    override suspend fun retrieveRules(): Result<List<MatchingRule>> {

    }

}