package com.example.myapplication2.service
import com.example.myapplication2.service.request.LoginRequest
import com.example.myapplication2.service.request.SignupRequest
import com.example.myapplication2.service.request.VerifyEmailRequest
import com.example.myapplication2.service.response.EmptyResponse
import com.example.myapplication2.service.response.LoginResponse
import com.example.myapplication2.service.response.SignupResponse
import com.example.myapplication2.service.response.NetworkResponse
import com.example.myapplication2.service.response.RefreshTokenResponse
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


