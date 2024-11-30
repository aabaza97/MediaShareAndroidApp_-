package com.example.myapplication2.service.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class UserInfoManager(private val sharedPreferences: SharedPreferences ) {
    companion object {
        private const val USER_INFO_KEY = "user_info"
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val userInfoAdapter = moshi.adapter(UserInfo::class.java)

    fun saveUserInfo(userInfo: UserInfo) {
        val userInfoJson = userInfoAdapter.toJson(userInfo)
        sharedPreferences.edit { 
            putString(USER_INFO_KEY, userInfoJson) 
        }
    }

    fun getUserInfo(): UserInfo? {
        val userInfoJson = sharedPreferences.getString(USER_INFO_KEY, null)
        return userInfoJson?.let { 
            userInfoAdapter.fromJson(it) 
        }
    }

    fun clearUserInfo() {
        sharedPreferences.edit { 
            remove(USER_INFO_KEY) 
        }
    }

    fun updateUserInfo(update: (UserInfo) -> UserInfo) {
        val currentInfo = getUserInfo()
        currentInfo?.let {
            saveUserInfo(update(it))
        }
    }

    fun isUserLoggedIn(): Boolean {
        return getUserInfo() != null
    }
}
