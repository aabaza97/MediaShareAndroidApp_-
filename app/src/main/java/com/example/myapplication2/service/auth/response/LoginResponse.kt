package com.example.myapplication2.service.auth.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val id: Int,
    val email: String,
    @SerializedName("first_name")  val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)


