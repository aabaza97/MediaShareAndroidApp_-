package com.example.myapplication2.service.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)


