package com.hsmsample.tweetplanet.tweets.data.model


import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("full_name")
    var fullName: String? = null,
    @SerializedName("geo")
    var geo: GeoGsonData? = null,
    @SerializedName("id")
    var id: String? = null
)