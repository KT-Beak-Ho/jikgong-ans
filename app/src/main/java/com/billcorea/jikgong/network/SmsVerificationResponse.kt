package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class SmsVerificationResponse(
    var data: SmsVerificationData,
    var message: String
)