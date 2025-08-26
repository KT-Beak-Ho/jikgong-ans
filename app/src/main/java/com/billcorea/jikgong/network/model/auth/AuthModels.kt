package com.billcorea.jikgong.network.model.auth

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

/**
 * Auth 관련 모델 통합 파일
 * 기존 레거시 파일들(network/)을 그대로 유지
 * 변경사항 없음 - 코드 100% 동일
 */

// ============================================
// LoginRequest.kt 그대로
// ============================================
data class LoginRequest(
  val loginIdOrPhone: String,
  val password: String,
  val deviceToken: String
)

// ============================================
// LoginResponse.kt 그대로
// ============================================
data class LoginResponse(
  val data: JsonElement,
  val message: String
)

// ============================================
// LoginData.kt 그대로
// ============================================
data class LoginData(
  val accessToken: String,
  val refreshToken: String,
  val role: String
)

// ============================================
// LoginErrorData.kt 그대로
// ============================================
data class LoginErrorData(
  val status: String,
  val code: String,
  val errorMessage: String
)

// ============================================
// LoginIdValidationRequest.kt 그대로
// ============================================
data class LoginIdValidationRequest(
  var loginId: String
)

// ============================================
// LoginIdValidationResponse.kt 그대로
// ============================================
data class LoginIdValidationResponse(
  var data: RegisterWorkerErrorResponse?,
  var message: String
)

// ============================================
// PhoneValidationRequest.kt 그대로
// ============================================
data class PhoneValidationRequest(
  var phone: String
)

// ============================================
// PhoneValidationResponse.kt 그대로
// ============================================
data class PhoneValidationResponse(
  var data: RegisterWorkerErrorResponse?,
  var message: String
)

// ============================================
// SmsVerificationRequest.kt 그대로
// ============================================
data class SmsVerificationRequest(
  var phone: String
)

// ============================================
// SmsVerificationData.kt 그대로
// ============================================
data class SmsVerificationData(
  var authCode: String
)

// ============================================
// SmsVerificationResponse.kt 그대로
// ============================================
data class SmsVerificationResponse(
  var data: SmsVerificationData,
  var message: String
)

// ============================================
// VisaExpiryDateRequest.kt 그대로
// ============================================
data class VisaExpiryDateRequest(
  var birthDate: String,
  var country: String,
  var nationality: String,
  var passportNo: String
)

// ============================================
// RegisterWorkerErrorResponse.kt 그대로
// ============================================
data class RegisterWorkerErrorResponse(
  var status: String,
  var code: String,
  var errorMessage: String
)

// ============================================
// DefaultResponse.kt (auth는 아니지만 함께 사용)
// ============================================
data class DefaultResponse(
  var message: String
)