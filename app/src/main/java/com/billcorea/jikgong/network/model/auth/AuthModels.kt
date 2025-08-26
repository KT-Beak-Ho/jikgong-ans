package com.billcorea.jikgong.network.model.auth

import com.google.gson.JsonElement

/**
 * Auth 관련 모델 통합 파일
 * 기존 레거시 파일들(network/)을 그대로 유지
 * 변경사항 없음 - 코드 100% 동일
 */

// ============================================
// Login 관련
// ============================================
data class LoginRequest(
  val loginIdOrPhone: String,
  val password: String,
  val deviceToken: String
)

data class LoginResponse(
  val data: JsonElement,
  val message: String
)

data class LoginData(
  val accessToken: String,
  val refreshToken: String,
  val role: String
)

data class LoginErrorData(
  val status: String,
  val code: String,
  val errorMessage: String
)

// ============================================
// Validation 관련
// ============================================
data class LoginIdValidationRequest(
  var loginId: String
)

data class LoginIdValidationResponse(
  var data: RegisterWorkerErrorResponse?,
  var message: String
)

data class PhoneValidationRequest(
  var phone: String
)

data class PhoneValidationResponse(
  var data: RegisterWorkerErrorResponse?,
  var message: String
)

// ============================================
// SMS 인증 관련
// ============================================
data class SmsVerificationRequest(
  var phone: String
)

data class SmsVerificationData(
  var authCode: String
)

data class SmsVerificationResponse(
  var data: SmsVerificationData,
  var message: String
)

// ============================================
// 비자 관련
// ============================================
data class VisaExpiryDateRequest(
  var birthDate: String,
  var country: String,
  var nationality: String,
  var passportNo: String
)

// ============================================
// 에러 응답 (공통으로 사용)
// ============================================
data class RegisterWorkerErrorResponse(
  var status: String,
  var code: String,
  var errorMessage: String
)

// ============================================
// 기본 응답
// ============================================
data class DefaultResponse(
  var message: String
)