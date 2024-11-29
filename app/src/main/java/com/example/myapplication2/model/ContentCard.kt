package com.example.myapplication2.model

/**
 * Data class that captures the content card information
 */
data class ContentCard (
    val title: String,
    val mediaUrl: String,
    val isVideo: Boolean,
    var isLiked: Boolean = false
)