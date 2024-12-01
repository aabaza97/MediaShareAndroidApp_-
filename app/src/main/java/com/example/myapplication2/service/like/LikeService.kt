package com.example.myapplication2.service.like

import com.example.myapplication2.service.EmptyResponse
import com.example.myapplication2.service.NetworkResponse
import com.example.myapplication2.service.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Singleton

interface LikeService {
    @POST("{id}")
    suspend fun like (
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): NetworkResponse<LikeResponse>

    @DELETE("{id}")
    suspend fun disLike (
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): NetworkResponse<EmptyResponse>
}
