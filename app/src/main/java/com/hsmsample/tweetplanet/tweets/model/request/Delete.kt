package com.hsmsample.tweetplanet.tweets.model.request


import com.google.gson.annotations.SerializedName

data class Delete(
    @SerializedName("ids")
    var ids: List<String>? = null
)