package com.hsmsample.tweetplanet.tweets.repository

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
 * Defines the contract for [TweetsRepository]
 */
interface TweetsRepositoryImpl {

    suspend fun getFilteredStream(searchKeyword: String): Flow<JsonObject>

    suspend fun addRule(keyword: String): Result<JsonObject>

    suspend fun deleteExistingRules(ruleId: String): Result<JsonObject>

    suspend fun retrieveRules(): Result<List<MatchingRule>>

    fun getRandomData(): String

}