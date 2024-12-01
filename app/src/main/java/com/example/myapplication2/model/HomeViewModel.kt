package com.example.myapplication2.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.service.like.LikeRepository
import com.example.myapplication2.service.media.repository.MediaRepository
import com.example.myapplication2.service.media.response.GetUploadsResponse
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mediaRepository: MediaRepository,
    private val likeRepository: LikeRepository
): ViewModel() {
    var uploads = MutableLiveData<GetUploadsResponse?>()

    // Companion object factory method
    companion object {
        fun provideFactory(mediaRepository: MediaRepository, likeRepository: LikeRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(mediaRepository, likeRepository) as T
                }
            }
        }
    }

    // Get uploads
    fun getUploads(page: Int = 0) {
        viewModelScope.launch {
            try {
                val result =  mediaRepository.getUploads(page)
                Log.d("HomeViewModel", "Received uploads: $result")
                uploads.value = if (result.isSuccess) {
                    result.getOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to get uploads", e)
                uploads = MutableLiveData()
            }
        }
    }

    // Like Uploaded Item
    fun like(uploadId: Int) {
        viewModelScope.launch {
            try {
                val result = likeRepository.like(uploadId)
                Log.d("HomeViewModel", "Liked upload: $result")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to like upload", e)
            }
        }
    }

    // Dislike Uploaded Item
    fun disLike(uploadId: Int) {
        viewModelScope.launch {
            try {
                val result = likeRepository.disLike(uploadId)
                Log.d("HomeViewModel", "Disliked upload: $result")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to dislike upload", e)
            }
        }
    }
}