package com.billcorea.jikgong.presentation.worker.login.shared

data class WorkerLoginUiState (

  // Login Request
  var loginIdOrPhone: String = "",
  var password: String = "",
  val deviceToken: String = "",

  // Login Response
  var accessToken: String = "",
  var refreshToken: String = "",
  var role: String = "",

  // Login Response Error
  var status: String = "",
  var code: String = "",
  var errorMessage: String ?= null,

  val message: String = "",

  // Shared
  val validationErrors: Map<String, String> = emptyMap(),
) {

  // 로그인 입력 확인
  val isLoginEmpty: Boolean
    get() = loginIdOrPhone.isNotEmpty() &&
      password.isNotEmpty()

  // 로그인 에러 메세지 반환
  val getErrorMessage: String?
    get() = errorMessage

  // 로그인 토큰 반환
  val getLoginToken: String
    get() = accessToken
}