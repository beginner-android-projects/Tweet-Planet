package com.hsmsample.tweetplanet.tweets.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.TweetsRemoteDataStore
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.model.TweetData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.Buffer
import java.nio.charset.Charset
import javax.inject.Inject

class TweetsRepository @Inject constructor(
    private val tweetsRemoteDataStore: TweetsRemoteDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val gson: Gson
) : TweetsRepositoryImpl {

    override fun getFilteredStream(): Flow<TweetData?> =
        flow {

            val response = tweetsRemoteDataStore.getFilteredStream()

            val body = response.body()
            try {

                val source = body?.source()
                val buffer = Buffer()


                while (source?.exhausted() != true) {
                    source?.read(buffer, 8192)
                    val data = buffer.readString(Charset.defaultCharset())

                    if (data.contains("\n")) {

                        val dataArray = data.split("\n")

                        for (dataItem in dataArray) {
                            val convertedItem = getTweetDataFromString(dataItem)
                            emit(convertedItem)
                        }

                    } else {
                        val convertedItem = getTweetDataFromString(data)
                        emit(convertedItem)
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                emit(null)
            } finally {
                getFilteredStream()
                emit(null)
                body?.close()
            }

        }.flowOn(dispatcherProvider.io)


    private fun getTweetDataFromString(jsonString: String): TweetData? =
        try {
            gson.fromJson(jsonString, TweetData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
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
}