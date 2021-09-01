package com.hsmsample.tweetplanet.tweets.data.model.request


import com.google.gson.annotations.SerializedName

data class Add(
    @SerializedName("tag")
    var tag: String = "keyword",
    @SerializedName("value")
    var value: String? = null
)