package com.billcorea.jikgong.network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val loginIdOrPhone: String,
    val password: String,
    val deviceToken: String
)