package com.example.myapplication2.service.media.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("download_url") val downloadURL: String,
)