package com.hsmsample.tweetplanet.tweets.data.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("username")
    var username: String? = null
)