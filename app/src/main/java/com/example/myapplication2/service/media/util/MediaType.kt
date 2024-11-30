package com.example.myapplication2.service.media.util
enum class MediaType(val param: String, val mimeType: String) {
    IMAGE("image", "image/jpeg"),
    VIDEO("movie", "video/mp4"),
    AUDIO("audio", "audio/mpeg"),
    DOCUMENT("document", "application/pdf"),
    OTHER("other", "application/octet-stream")
}