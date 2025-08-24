package com.billcorea.jikgong.api.models.request

/**
 * Body
 * - 로그인 ID 중복 확인
 */
data class LoginIdValidationRequest(
  val loginId: String
)