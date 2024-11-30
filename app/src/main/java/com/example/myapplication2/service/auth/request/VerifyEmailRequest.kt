package com.example.myapplication2.service.auth.request

import com.google.gson.annotations.SerializedName

data class VerifyEmailRequest(
    val email: String,
    val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
)