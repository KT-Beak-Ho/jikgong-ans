package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class SmsVerificationData(
    var authCode: String
)