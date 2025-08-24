package com.billcorea.jikgong.api.models.request

/**
 * Body
 * - 전화번호 중복 확인
 */
data class PhoneValidationRequest(
  val phone: String
)