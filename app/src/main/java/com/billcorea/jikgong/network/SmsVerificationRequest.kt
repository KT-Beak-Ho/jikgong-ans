package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class SmsVerificationRequest(
    var phone: String
)