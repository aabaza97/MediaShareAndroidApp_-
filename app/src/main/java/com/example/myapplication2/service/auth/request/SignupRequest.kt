package com.example.myapplication2.service.auth.request

data class SignupRequest(
    val email: String,
   val otp: String
)