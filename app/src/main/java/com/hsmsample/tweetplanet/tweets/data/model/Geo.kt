package com.hsmsample.tweetplanet.tweets.data.model


import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("place_id")
    var placeId: String? = null
)