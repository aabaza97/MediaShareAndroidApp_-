package com.example.myapplication2.service.auth.repository


import android.util.Log
import com.example.myapplication2.service.auth.AuthService
import com.example.myapplication2.service.auth.TokenManager
import com.example.myapplication2.service.auth.request.LoginRequest
import com.example.myapplication2.service.auth.request.SignupRequest
import com.example.myapplication2.service.auth.request.VerifyEmailRequest
import com.example.myapplication2.service.auth.response.LoginResponse
import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.auth.response.RefreshTokenResponse
import com.example.myapplication2.service.auth.response.SignupResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) {
    suspend fun verifyEmail(email: String, password: String, firstName: String, lastName: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = VerifyEmailRequest(email,password, firstName, lastName)
                when (val response = authService.verifyEmail(request)) {
                    is NetworkResponse.Success -> Result.success(Unit)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun register(
        email: String,
        otp: String
    ): Result<SignupResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val request = SignupRequest(email, otp)
                when (val response = authService.register(request)) {
                    is NetworkResponse.Success -> Result.success(response.data)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                val response = authService.login(request)

                Log.d("AuthRepository", "Login response: $response")
                when (response) {
                    is NetworkResponse.Success -> {
                        // Store tokens after successful login
                        response.data?.accessToken?.let { tokenManager.saveAccessToken(it) }
                        response.data?.refreshToken?.let { tokenManager.saveRefreshToken(it) }
                        Result.success(response.data)
                    }
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "Login failed", e)
                Result.failure(e)
            }
        }
    }

    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Get access token or refresh access token using refresh token
                val token = tokenManager.getAccessToken() ?: refreshToken().getOrNull()?.accessToken
                    ?: return@withContext Result.failure(IllegalStateException("No access token found"))

                // Logout using access token
                when (val response = authService.logout("Bearer $token")) {
                    is NetworkResponse.Success -> {
                        // Clear tokens after successful logout
                        tokenManager.clearTokens()
                        Result.success(Unit)
                    }
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun refreshToken(): Result<RefreshTokenResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val refreshToken = tokenManager.getRefreshToken() ?: return@withContext Result.failure(
                    IllegalStateException("No refresh token found")
                )
                when (val response = authService.refreshToken("Bearer $refreshToken")) {
                    is NetworkResponse.Success -> {
                        // Update tokens after successful refresh
                        response.data?.accessToken?.let { tokenManager.saveAccessToken(it) }
                        Result.success(response.data)
                    }
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return tokenManager.getRefreshToken() != null
    }
}