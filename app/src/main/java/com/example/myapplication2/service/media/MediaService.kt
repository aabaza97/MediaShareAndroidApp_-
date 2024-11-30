package com.example.myapplication2.service.media

import com.example.myapplication2.service.EmptyResponse
import com.example.myapplication2.service.NetworkResponse
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.PUT
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.File

interface MediaService {
    // Improved multipart image upload
    @Multipart
    @PUT("images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): NetworkResponse<ImageUploadResponse>
}


data class ImageUploadResponse(
    @SerializedName("download_url") val downloadURL: String,
)

// Utility function to create MultipartBody.Part
fun File.toMultipartBody(uploadType: MediaType): MultipartBody.Part {
    val requestBody = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(uploadType.param, this.name, requestBody)
}

enum class MediaType(val param: String) {
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    DOCUMENT("document"),
    OTHER("other")
}