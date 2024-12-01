package com.example.myapplication2.model

import com.example.myapplication2.service.media.response.GetUploadsResponse
import com.example.myapplication2.service.media.util.MediaType

/**
 * Data class that captures the content card information
 */
data class ContentCard (
    val id: Int,
    val title: String,
    val mediaUrl: String,
    val isVideo: Boolean,
    var isLiked: Boolean = false
) {
    constructor(media: GetUploadsResponse.MediaUpload) : this(
        media.id,
        media.name,
        media.downloadURL,
        media.type.lowercase() == MediaType.VIDEO.name.lowercase()
    )
}