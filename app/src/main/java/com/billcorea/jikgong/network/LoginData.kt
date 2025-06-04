package com.billcorea.jikgong.network

import com.google.gson.annotations.SerializedName

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val role: String,
)