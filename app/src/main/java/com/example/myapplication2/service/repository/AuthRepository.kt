package com.example.myapplication2.service.repository


import com.example.myapplication2.service.AuthService
import com.example.myapplication2.service.TokenManager
import com.example.myapplication2.service.request.LoginRequest
import com.example.myapplication2.service.request.SignupRequest
import com.example.myapplication2.service.request.VerifyEmailRequest
import com.example.myapplication2.service.response.LoginResponse
import com.example.myapplication2.service.response.NetworkResponse
import com.example.myapplication2.service.response.RefreshTokenResponse
import com.example.myapplication2.service.response.SignupResponse
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
    ): Result<SignupResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = SignupRequest(email, otp)
                when (val response = authService.register(request)) {
                    is NetworkResponse.Success -> Result.success(response.value)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                when (val response = authService.login(request)) {
                    is NetworkResponse.Success -> {
                        // Store tokens after successful login
                        response.value.accessToken.let { tokenManager.saveAccessToken(it) }
                        response.value.refreshToken.let { tokenManager.saveRefreshToken(it) }
                        Result.success(response.value)
                    }
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
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

    suspend fun refreshToken(): Result<RefreshTokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val refreshToken = tokenManager.getRefreshToken() ?: return@withContext Result.failure(
                    IllegalStateException("No refresh token found")
                )
                when (val response = authService.refreshToken("Bearer $refreshToken")) {
                    is NetworkResponse.Success -> {
                        // Update tokens after successful refresh
                        response.value.accessToken.let { tokenManager.saveAccessToken(it) }
                        Result.success(response.value)
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