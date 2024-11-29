package com.example.myapplication2.service.response

// Network Response wrapper
sealed class NetworkResponse<out T> {
    data class Success<out T>(val status: Int, val message: String, val value: T) : NetworkResponse<T>()
    data class Failure<out T>(val error: NetworkError? = null) : NetworkResponse<T>()

    enum class NetworkError(val status: Int, val message: String) {
        TIMEOUT(408, "Request Timeout"),
        UNAUTHORIZED(401, "Unauthorized"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        override fun toString(): String {
            return "Error $status: $message"
        }
    }
}