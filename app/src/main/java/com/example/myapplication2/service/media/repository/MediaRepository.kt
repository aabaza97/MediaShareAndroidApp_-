package com.example.myapplication2.service.media.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.auth.repository.AuthRepository
import com.example.myapplication2.service.media.MediaService
import com.example.myapplication2.service.media.util.MediaType
import com.example.myapplication2.service.media.response.UploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MediaRepository (
    private val mediaService: MediaService,
    private val authRepository: AuthRepository,
) {

    // Upload image
    suspend
    fun uploadImage(image: File): Result<UploadResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val imagePart = image.toMultipartBody(MediaType.IMAGE)
                val token = authRepository.getAuthToken()
                when (val response = mediaService.uploadImage(imagePart, token)) {
                    is NetworkResponse.Success -> Result.success(response.data)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend
    fun uploadVideo(video: File): Result<UploadResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val videoPart = video.toMultipartBody(MediaType.VIDEO)
                val token = authRepository.getAuthToken()
                when (val response = mediaService.uploadVideo(videoPart, token)) {
                    is NetworkResponse.Success -> Result.success(response.data)
                    is NetworkResponse.Failure -> throw Exception(response.error?.message)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


    // Utility function to create MultipartBody.Part
    fun File.toMultipartBody(uploadType: MediaType): MultipartBody.Part {
        val requestBody = this.asRequestBody(uploadType.mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(uploadType.param, this.name, requestBody)
    }

}