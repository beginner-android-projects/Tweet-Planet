package com.hsmsample.tweetplanet.data.remote

import com.google.gson.annotations.SerializedName

class ApiResponse<T> {

    @SerializedName("data")
    var data: T? = null

}