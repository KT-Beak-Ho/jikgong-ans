package com.billcorea.jikgong.network.model.auth

// LoginRequest.kt 내용
data class LoginRequest(
  val loginId: String,
  val password: String
)

// LoginResponse.kt 내용
data class LoginResponse(
  val code: String,
  val message: String,
  val data: LoginData? = null
)

// LoginData.kt 내용
data class LoginData(
  val memberId: String,
  val memberType: String,
  val memberName: String,
  val accessToken: String,
  val refreshToken: String
)

// LoginErrorData.kt 내용
data class LoginErrorData(
  val errorCode: String,
  val errorMessage: String
)