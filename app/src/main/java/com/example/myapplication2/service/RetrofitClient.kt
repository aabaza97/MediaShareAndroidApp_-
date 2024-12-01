package com.example.myapplication2.service

import com.example.myapplication2.service.auth.AuthService
import com.example.myapplication2.service.like.LikeService
import com.example.myapplication2.service.media.MediaService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    enum class APIResource(val id: String) {
        AUTH("auth/"),
        MEDIA("media/"),
        LIKE("likes/")
    }

    enum class APIVersion(val id: String) {
        V1("v1/")
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val gson = GsonBuilder()
        .registerTypeAdapterFactory(NetworkResponseTypeAdapterFactory())
        .create()

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /** Auth service */
    val authApi: AuthService = Retrofit.Builder()
        .baseUrl(getBaseUrl(forResource = APIResource.AUTH))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
        .create(AuthService::class.java)


    /** Media service */
    val mediaApi: MediaService = Retrofit.Builder()
        .baseUrl(getBaseUrl(forResource = APIResource.MEDIA))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
        .create(MediaService::class.java)

    /** Like service */
    val likeApi: LikeService = Retrofit.Builder()
        .baseUrl(getBaseUrl(forResource = APIResource.LIKE))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
        .create(LikeService::class.java)

    /** Helper function to get the base URL */
    private
    fun getBaseUrl(
        withVersion: APIVersion = APIVersion.V1,
        forResource: APIResource
    ): String {
        return BASE_URL + withVersion.id + forResource.id
    }
}
