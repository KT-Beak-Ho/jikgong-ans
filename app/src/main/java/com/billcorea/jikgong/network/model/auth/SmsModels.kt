package com.billcorea.jikgong.network.model.auth

// SmsVerificationRequest.kt 내용
data class SmsVerificationRequest(
  val phoneNumber: String,
  val verificationCode: String
)

// SmsVerificationResponse.kt 내용
data class SmsVerificationResponse(
  val code: String,
  val message: String,
  val data: SmsVerificationData? = null
)

// SmsVerificationData.kt 내용
data class SmsVerificationData(
  val verificationId: String,
  val phoneNumber: String,
  val isVerified: Boolean
)