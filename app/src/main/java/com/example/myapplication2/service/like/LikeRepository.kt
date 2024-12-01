package com.example.myapplication2.service.like

import com.example.myapplication2.service.EmptyResponse
import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

class LikeRepository(
    private val service: LikeService,
    private val authRepository: AuthRepository
) {
    // Like Uploaded Item
    suspend
    fun like(uploadId: Int): Result<LikeResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val token = authRepository.getAuthToken()
                when(val response = service.like(token, uploadId)) {
                    is NetworkResponse.Success -> Result.success(response.data)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Dislike Uploaded Item
    suspend
    fun disLike(uploadId: Int): Result<EmptyResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val token = authRepository.getAuthToken()
                when(val response = service.disLike(token, uploadId)) {
                    is NetworkResponse.Success -> Result.success(response.data)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}