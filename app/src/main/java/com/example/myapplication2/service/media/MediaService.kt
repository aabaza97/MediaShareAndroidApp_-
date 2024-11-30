package com.example.myapplication2.service.media

import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.media.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File

interface MediaService {
    @Multipart
    @PUT("images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): NetworkResponse<UploadResponse>

    @Multipart
    @PUT("movies")
    suspend fun uploadVideo(
        @Part movie: MultipartBody.Part,
        @Header("Authorization") token: String
    ): NetworkResponse<UploadResponse>
}


