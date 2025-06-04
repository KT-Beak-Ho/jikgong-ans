package com.billcorea.jikgong.network

import com.google.gson.annotations.SerializedName

data class LoginErrorData(
    val status: String,
    val code: String,
    val errorMessage: String
)