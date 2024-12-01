package com.example.myapplication2.service.media

import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.media.response.GetUploadsResponse
import com.example.myapplication2.service.media.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

interface MediaService {
    /** Upload an image */
    @Multipart
    @PUT("images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): NetworkResponse<UploadResponse>

    /** Upload a video */
    @Multipart
    @PUT("movies")
    suspend fun uploadVideo(
        @Part movie: MultipartBody.Part,
        @Header("Authorization") token: String
    ): NetworkResponse<UploadResponse>

    /** Get uploads with pagination */
    @GET("{page}")
    suspend fun getUploads(
        @Header("Authorization") token: String,
        @Path("page") page: Int
    ): NetworkResponse<GetUploadsResponse>
}


