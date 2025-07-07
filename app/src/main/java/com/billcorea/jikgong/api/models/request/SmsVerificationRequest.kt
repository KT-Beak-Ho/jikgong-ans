package com.billcorea.jikgong.api.models.request

/**
 * Body
 * - SMS 인증번호 발송
 */
data class SmsVerificationRequest(
  val phone: String
)
