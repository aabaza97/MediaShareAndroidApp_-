package com.example.myapplication2.service

import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val LAST_TOKEN_REFRESH = "last_token_refresh"

        private const val TTL: Long = 1000 * 60 * 3 // 3 minutes
    }

    fun saveAccessToken(token: String) {
        sharedPreferences.edit { putString(ACCESS_TOKEN_KEY, token) }
        sharedPreferences.edit { putLong(LAST_TOKEN_REFRESH, System.currentTimeMillis()) }
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit { putString(REFRESH_TOKEN_KEY, token) }
    }

    fun getAccessToken(): String? {
        val lastRefresh = sharedPreferences.getLong(LAST_TOKEN_REFRESH, 0)
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastRefresh > TTL) {
            clearTokens()
            return null
        }

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun hasValidToken(): Boolean {
        return getAccessToken() != null
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    fun clearTokens() {
        sharedPreferences.edit {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
        }
    }
}