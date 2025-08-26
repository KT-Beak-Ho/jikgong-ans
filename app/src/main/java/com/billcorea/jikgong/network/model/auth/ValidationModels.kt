package com.billcorea.jikgong.network.model.auth

// LoginIdValidationRequest.kt 내용
data class LoginIdValidationRequest(
  val loginId: String
)

// LoginIdValidationResponse.kt 내용
data class LoginIdValidationResponse(
  val code: String,
  val message: String,
  val isValid: Boolean
)

// PhoneValidationRequest.kt 내용
data class PhoneValidationRequest(
  val phoneNumber: String
)

// PhoneValidationResponse.kt 내용
data class PhoneValidationResponse(
  val code: String,
  val message: String,
  val isValid: Boolean
)