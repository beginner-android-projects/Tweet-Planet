package com.hsmsample.tweetplanet.tweets

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.data.remote.ApiResponse
import com.hsmsample.tweetplanet.data.remote.ErrorUtils
import com.hsmsample.tweetplanet.data.remote.Result
import com.hsmsample.tweetplanet.tweets.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.model.TweetData
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject

private const val TWEETS_ = "tweets/"
private const val SEARCH_ = "search/"
private const val STREAM_ = "stream/"
private const val STREAM = "stream"
private const val RULES = "rules"
private const val TWEET_FIELDS_VALUES = "id,text,geo"
private const val EXPANSIONS_VALUES = "author_id,geo.place_id"
private const val PLACE_FIELD_VALUES = "geo"
private const val USER_FIELD_VALUES = "id,name,username"


class TweetsRemoteDataStore @Inject constructor(
    private val retrofit: Retrofit
) {

    /**
     * Use the same function for adding or deleting rules,
     * the JSON structure for each of them are as follows:-
     * 1. For Adding a rule
     * {
     *  "add": [
     *      {
     *       "value": "androiddev",
     *       "tag": "keyword"
     *      }
     *   ]
     * }
     *
     * 2. For Deleting a rule
     * {
     *  "delete": {
     *      "ids": [
     *          "1430802598467043331"
     *      ]
     *   }
     * }
     *
     * @param payload will be the value for the tag in case of [forAdd] is true
     * else the value will be the value of the rule that the user wants to delete
     */
    suspend fun submitRulesForStream(forAdd: Boolean, payload: String): Response<JsonObject> {

        val map = HashMap<Any?, Any?>()

        if (forAdd) {

            val valueTag = hashMapOf(
                "value" to payload,
                "tag" to "keyword"
            )

            map["add"] = listOf(valueTag)

        } else {

            val idsToDelete = hashMapOf(
                "ids" to listOf(payload)
            )

            map["delete"] = idsToDelete
        }

        return retrofit.create(Client::class.java).submitRulesForStream(JSONObject(map))
    }

    suspend fun retrieveRules(): Response<ApiResponse<List<MatchingRule>>> =
        retrofit.create(Client::class.java).getRulesForStream()


    suspend fun getFilteredStream(): Result<TweetData> =
        getResponse(
            request = {
                retrofit.create(Client::class.java).getFilteredStream(
                    TWEET_FIELDS_VALUES,
                    EXPANSIONS_VALUES,
                    PLACE_FIELD_VALUES,
                    USER_FIELD_VALUES
                )
            },
            "Something went wrong"
        )


    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }


}

private interface Client {

    @POST("$TWEETS_$SEARCH_$STREAM_$RULES")
    suspend fun submitRulesForStream(@Body addRule: JSONObject): Response<JsonObject>

    @GET("$TWEETS_$SEARCH_$STREAM_$RULES")
    suspend fun getRulesForStream(): Response<ApiResponse<List<MatchingRule>>>

    @GET("$TWEETS_$SEARCH_$STREAM")
    suspend fun getFilteredStream(
        @Query("tweet.fields") tweetFields: String,
        @Query("expansions") expansions: String,
        @Query("place.fields") placeFields: String,
        @Query("user.fields") userFields: String
    ): Response<TweetData>

}