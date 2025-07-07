package com.billcorea.jikgong.api.models.request

/**
 * Body
 * - 사용자 EMail 중복 확인
 */
data class EmailValidationRequest(
  val loginId: String
)