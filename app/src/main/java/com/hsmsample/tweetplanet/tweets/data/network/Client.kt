package com.hsmsample.tweetplanet.tweets.data.network

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.common.ApiResponse
import com.hsmsample.tweetplanet.tweets.data.model.MatchingRule
import com.hsmsample.tweetplanet.tweets.data.model.request.AddDeleteRuleRequest
import com.hsmsample.tweetplanet.utils.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface Client {

    @POST("$TWEETS_$SEARCH_$STREAM_$RULES")
    suspend fun submitRulesForStream(@Body addRule: AddDeleteRuleRequest): Response<JsonObject>

    @GET("$TWEETS_$SEARCH_$STREAM_$RULES")
    suspend fun getRulesForStream(): Response<ApiResponse<List<MatchingRule>>>

    @Streaming
    @GET("$TWEETS_$SEARCH_$STREAM")
    suspend fun getFilteredStream(
        @Query("tweet.fields") tweetFields: String = TWEET_FIELDS_VALUES,
        @Query("expansions") expansions: String = EXPANSIONS_VALUES,
        @Query("place.fields") placeFields: String = PLACE_FIELD_VALUES,
        @Query("user.fields") userFields: String = USER_FIELD_VALUES
    ): Response<ResponseBody>

}