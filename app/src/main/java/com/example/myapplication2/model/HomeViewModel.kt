package com.example.myapplication2.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.service.media.repository.MediaRepository
import com.example.myapplication2.service.media.response.GetUploadsResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val mediaRepository: MediaRepository): ViewModel() {
    var uploads = MutableLiveData<GetUploadsResponse?>()

    // Companion object factory method
    companion object {
        fun provideFactory(mediaRepository: MediaRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(mediaRepository) as T
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
}