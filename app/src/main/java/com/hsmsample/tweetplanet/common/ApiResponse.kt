package com.hsmsample.tweetplanet.common

import com.google.gson.annotations.SerializedName

class ApiResponse<T> {

    @SerializedName("data")
    var data: T? = null

}