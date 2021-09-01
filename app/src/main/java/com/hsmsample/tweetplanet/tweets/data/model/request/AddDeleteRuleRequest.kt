package com.hsmsample.tweetplanet.tweets.data.model.request


import com.google.gson.annotations.SerializedName

data class AddDeleteRuleRequest(
    @SerializedName("add")
    var add: List<Add>? = null,
    @SerializedName("delete")
    var delete: Delete? = null
)