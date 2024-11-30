package com.example.myapplication2.service.auth
import com.example.myapplication2.service.auth.request.LoginRequest
import com.example.myapplication2.service.auth.request.SignupRequest
import com.example.myapplication2.service.auth.request.VerifyEmailRequest
import com.example.myapplication2.service.EmptyResponse
import com.example.myapplication2.service.auth.response.LoginResponse
import com.example.myapplication2.service.auth.response.SignupResponse
import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.auth.response.RefreshTokenResponse
import retrofit2.http.*

// Auth service interface
interface AuthService {
    // base url: https://api.example.com
    @POST("emails/verify")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): NetworkResponse<EmptyResponse>

    @POST("register")
    suspend fun register(@Body request: SignupRequest): NetworkResponse<SignupResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): NetworkResponse<LoginResponse>

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): NetworkResponse<EmptyResponse>

    @POST("tokens/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): NetworkResponse<RefreshTokenResponse>
}


