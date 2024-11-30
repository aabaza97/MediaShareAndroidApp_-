package com.example.myapplication2.service.auth

import com.example.myapplication2.service.auth.response.LoginResponse
import com.example.myapplication2.service.auth.response.SignupResponse
import com.squareup.moshi.Json

// Data class to represent user information
data class UserInfo(
    val id: Int,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name")val lastName: String,
    val email: String
) {
    constructor(from: LoginResponse) : this(
        from.id,
        from.firstName,
        from.lastName,
        from.email
    )

    constructor(from: SignupResponse) : this(
        from.id,
        from.firstName,
        from.lastName,
        from.email
    )
}