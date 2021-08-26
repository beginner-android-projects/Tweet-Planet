package com.hsmsample.tweetplanet.tweets.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.TweetsRemoteDataStore
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class TweetsRepository @Inject constructor(
    private val tweetsRemoteDataStore: TweetsRemoteDataStore,
    private val dispatcherProvider: DispatcherProvider
) : TweetsRepositoryImpl {

    override suspend fun getFilteredStream(searchKeyword: String): Flow<JsonObject> = flow {


        if (searchKeyword.isNotEmpty()) {

        }

    }

    override suspend fun addRule(keyword: String): Result<JsonObject> {
        return try {

            val response = tweetsRemoteDataStore.submitRulesForStream(true, keyword)

            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Throwable("Something went wrong"))

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    }


    override suspend fun deleteExistingRules(ruleId: String): Result<JsonObject> {
        return try {

            val response = tweetsRemoteDataStore.submitRulesForStream(false, ruleId)

            if (response.isSuccessful && response.body() != null)
                Result.success(response.body()!!)
            else
                Result.failure(Throwable("Something went wrong"))

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    }

    override suspend fun retrieveRules(): Result<List<MatchingRule>> {

        return try {

            val response = withContext(dispatcherProvider.io) {
                tweetsRemoteDataStore.retrieveRules()
            }

            if (response.isSuccessful)
                Result.success(response.body()?.data.orEmpty())
            else
                Result.failure(Throwable("Something went wrong"))

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    }


    override fun getRandomData(): String = "lkadjhfalkdhf ksadkljfh kfakljdhfk"

}