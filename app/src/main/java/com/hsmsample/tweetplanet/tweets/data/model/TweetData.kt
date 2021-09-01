package com.hsmsample.tweetplanet.tweets.data.model


import com.google.gson.annotations.SerializedName

data class TweetData(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("includes")
    var includes: Includes? = null,
    @SerializedName("matching_rules")
    var matchingRules: List<MatchingRule>? = null
)