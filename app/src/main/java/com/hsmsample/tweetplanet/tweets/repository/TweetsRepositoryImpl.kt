package com.hsmsample.tweetplanet.tweets.repository

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.model.TweetData
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

/**
 * Defines the contract for [TweetsRepository]
 */
interface TweetsRepositoryImpl {

    fun getFilteredStream(): Flow<ResponseBody?>

    suspend fun addRule(keyword: String): Result<JsonObject>

    suspend fun deleteExistingRules(ruleId: String): Result<JsonObject>

    suspend fun retrieveRules(): Result<List<MatchingRule>>

    fun getRandomData(): String

}