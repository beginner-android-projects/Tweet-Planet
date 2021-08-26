package com.hsmsample.tweetplanet.tweets.model


import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("place_id")
    var placeId: String? = null
)