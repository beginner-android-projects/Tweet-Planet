package com.hsmsample.tweetplanet.tweets.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("author_id")
    var authorId: String? = null,
    @SerializedName("geo")
    var geo: Geo? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("text")
    var text: String? = null
)