package com.example.myapplication2.service.media.response

import com.google.gson.annotations.SerializedName

data class GetUploadsResponse(
    val media: List<MediaUpload>
) {
    data class MediaUpload(
        val id: Int,
        val name: String,
        val type: String,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("download_url") val downloadURL: String,
        @SerializedName("is_liked") val isLiked: Boolean
    )
}
