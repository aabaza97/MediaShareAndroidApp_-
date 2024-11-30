package com.example.myapplication2.service

import com.example.myapplication2.service.auth.AuthService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val AUTH_BASE_URL = "http://10.0.2.2:3000/api/v1/auth/"
    private const val MEDIA_BASE_URL = "http://10.0.2.2:3000/api/v1/media/"

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
        .baseUrl(AUTH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
        .create(AuthService::class.java)


}
