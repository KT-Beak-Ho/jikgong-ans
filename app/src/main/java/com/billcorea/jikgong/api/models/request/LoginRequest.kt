package com.billcorea.jikgong.api.models.request

/**
 * Body
 * - Login 인증정보
 */
data class LoginRequest(
  val loginIdOrPhone: String,
  val password: String,
  val deviceToken: String
)