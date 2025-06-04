package com.billcorea.jikgong.network

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: JsonElement,
    val message: String
)