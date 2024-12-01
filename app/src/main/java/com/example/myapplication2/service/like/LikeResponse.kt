package com.example.myapplication2.service.like

import com.google.gson.annotations.SerializedName

data class LikeResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("upload_id") val uploadId: Int
)
