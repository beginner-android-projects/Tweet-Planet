package com.hsmsample.tweetplanet.tweets.model


import com.google.gson.annotations.SerializedName

data class GeoGsonData (
    @SerializedName("bbox")
    var bbox: List<Double>? = null,
    @SerializedName("properties")
    var properties: Properties? = null,
    @SerializedName("type")
    var type: String? = null
)