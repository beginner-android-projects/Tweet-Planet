package com.hsmsample.tweetplanet.tweets.data.repository

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.tweets.data.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.data.model.TweetData
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for [TweetsRepository]
 */
interface TweetsRepositoryImpl {

    fun getFilteredStream(): Flow<TweetData?>

    suspend fun addRule(keyword: String): Result<JsonObject>

    suspend fun deleteExistingRules(ruleId: String): Result<JsonObject>

    suspend fun retrieveRules(): Result<List<MatchingRule>>

}