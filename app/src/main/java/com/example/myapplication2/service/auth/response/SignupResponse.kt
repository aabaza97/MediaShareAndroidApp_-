package com.example.myapplication2.service.auth.response

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)