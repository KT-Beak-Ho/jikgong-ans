package com.billcorea.jikgong.api.models.response

import com.google.gson.JsonElement

/**
 * 로그인 요청 결과
 */
data class LoginResponse(
  val data: JsonElement,
  val message: String
)