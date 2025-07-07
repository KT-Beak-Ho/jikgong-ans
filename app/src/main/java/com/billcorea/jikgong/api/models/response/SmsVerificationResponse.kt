package com.billcorea.jikgong.api.models.response

import com.google.gson.annotations.SerializedName
/**
 * SMS 인증번호 요청 Response
 */
data class SmsVerificationResponse(
  @SerializedName("success")
  val success: Boolean,
  @SerializedName("message")
  val message: String,
  @SerializedName("verificationId")
  val verificationId: String?
)