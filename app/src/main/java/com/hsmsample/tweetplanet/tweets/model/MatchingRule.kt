package com.hsmsample.tweetplanet.tweets.model


import com.google.gson.annotations.SerializedName

data class MatchingRule(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("tag")
    var tag: String? = null,
    @SerializedName("value")
    var value: String? = null
)