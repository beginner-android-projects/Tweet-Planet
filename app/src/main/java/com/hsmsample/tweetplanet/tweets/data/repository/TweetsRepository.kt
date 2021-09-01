package com.hsmsample.tweetplanet.tweets.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.data.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.data.model.TweetData
import com.hsmsample.tweetplanet.tweets.data.model.request.Add
import com.hsmsample.tweetplanet.tweets.data.model.request.AddDeleteRuleRequest
import com.hsmsample.tweetplanet.tweets.data.model.request.Delete
import com.hsmsample.tweetplanet.tweets.data.network.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.Buffer
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject

class TweetsRepository @Inject constructor(
    private val clientApi: Client,
    private val dispatcherProvider: DispatcherProvider,
    private val gson: Gson
) : TweetsRepositoryImpl {

    override fun getFilteredStream(): Flow<TweetData?> =
        flow {

            val response = clientApi.getFilteredStream()

            val body = response.body()
            try {

                val source = body?.source()
                val buffer = Buffer()


                while (source?.exhausted() != true) {
                    source?.read(buffer, 8192)
                    val data = buffer.readString(Charset.defaultCharset())

                    if (data.contains('\n')) {

                        val dataArray = data.split('\n')

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

            val request = AddDeleteRuleRequest(
                add = listOf(
                    Add(
                        value = keyword
                    )
                )
            )

            val response = clientApi.submitRulesForStream(request)

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

            val request = AddDeleteRuleRequest(
                delete =
                Delete(
                    ids = listOf(ruleId)
                )
            )

            val response = clientApi.submitRulesForStream(request)

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
                clientApi.getRulesForStream()
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