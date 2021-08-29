package com.hsmsample.tweetplanet.tweets.repository

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.di.dispatchers.DispatcherProvider
import com.hsmsample.tweetplanet.tweets.TweetsRemoteDataStore
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.model.TweetData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.Buffer
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject

class TweetsRepository @Inject constructor(
    private val tweetsRemoteDataStore: TweetsRemoteDataStore,
    private val dispatcherProvider: DispatcherProvider,
) : TweetsRepositoryImpl {

    override fun getFilteredStream(): Flow<String?> =
        flow {

            try{
                val response = tweetsRemoteDataStore.getFilteredStream()

                Timber.d("----------------response in repo ${response.body()}")
                Timber.d("----------------response in repo ${response.code()}")
                Timber.d("----------------response in repo ${response.raw()}")
                Timber.d("----------------response in repo ${response.errorBody()}")
                Timber.d("----------------response in repo ${response.message()}")

                val source = response.body()?.source()
                val buffer = Buffer()


                while(source?.exhausted() != true){
                    response.body()?.source()?.read(buffer, 8192)
                    val data = buffer.readString(Charset.defaultCharset())

                    emit(data)
                }


            } catch (e: Exception) {
                emit(null)
            }

        }.flowOn(dispatcherProvider.io)

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