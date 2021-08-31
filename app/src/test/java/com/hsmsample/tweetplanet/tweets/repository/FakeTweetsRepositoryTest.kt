package com.hsmsample.tweetplanet.tweets.repository

import com.google.gson.JsonObject
import com.hsmsample.tweetplanet.tweets.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

import org.junit.Test

class FakeTweetsRepositoryTest : TweetsRepositoryImpl {
    override fun getFilteredStream(): Flow<TweetData?> = flow {
        emit(FAKE_ITEM_1)
    }

    override suspend fun addRule(keyword: String): Result<JsonObject> {
        return Result.success(JsonObject())
    }

    override suspend fun deleteExistingRules(ruleId: String): Result<JsonObject> {
        return Result.success(JsonObject())
    }

    override suspend fun retrieveRules(): Result<List<MatchingRule>> {
        return Result.success(emptyList<MatchingRule>())
    }


}

val FAKE_ITEM_1 = TweetData(
    data = Data(
        authorId = "87947798",
        geo = Geo(
            placeId = "07d9cff2c3c86002"
        ),
        id = "1430823813126762499",
        text = "#androiddev"
    ),
    includes = Includes(
        users = listOf(
            User(
                id = "87947798",
                name = "Husain Mukadam",
                username = "hsm59"
            )
        ),
        places = listOf(
            Place(
                id = "07d9cff2c3c86002",
                fullName = "Lebanese Grill Retaurant",
                geo = GeoGsonData(
                    bbox = listOf(
                        55.34511026786179,
                        25.27484105721577,
                        55.34511026786179,
                        25.27484105721577
                    ),
                    type = "feature",
                    properties = null
                )
            )
        )
    )
)
