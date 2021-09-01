package com.hsmsample.tweetplanet.common

import retrofit2.Response

/**
 * Generic class for holding success response, error response and loading status
 */
data class Result<out T>(val status: Status, val data: T?, val error: Error?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: Error?): Result<T> {
            return Result(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): Result<T> {
            return Result(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}

private suspend fun <T> getResponse(
    request: suspend () -> Response<T>,
    defaultErrorMessage: String
): Result<T> {
    return try {
        val result = request.invoke()
        if (result.isSuccessful) {
            return Result.success(result.body())
        } else {
            val errorResponse = ErrorUtils.parseError(result, null)
            Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
        }
    } catch (e: Throwable) {
        Result.error("Unknown Error", null)
    }
}