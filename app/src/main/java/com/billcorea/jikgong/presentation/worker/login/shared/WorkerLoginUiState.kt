package com.billcorea.jikgong.presentation.worker.login.shared

data class WorkerLoginUiState (

  // Login Request
  val loginIdOrPhone: String = "",
  val password: String = "",
  val deviceToken: String = "",

  // Login Response
  val accessToken: String = "",
  val refreshToken: String = "",
  val role: String = "",

  // Loigin Response Error
  val status: String = "",
  val code: String = "",
  val errorMessage: String ?= null,
) {
}