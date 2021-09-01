package com.hsmsample.tweetplanet.tweets.data.model


import com.google.gson.annotations.SerializedName

data class Includes(
    @SerializedName("places")
    var places: List<Place>? = null,
    @SerializedName("users")
    var users: List<User>? = null
)