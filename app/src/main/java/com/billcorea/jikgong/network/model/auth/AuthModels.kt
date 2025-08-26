package com.billcorea.jikgong.network.model.auth

import com.google.gson.JsonElement

/**
 * Auth 관련 모델 통합 파일
 * 모든 인증 관련 데이터 모델 포함
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
  val role: String,
  val userId: String? = null,
  val userName: String? = null
)

data class LoginErrorData(
  val status: String,
  val code: String,
  val errorMessage: String
)

// ============================================
// Token 관련
// ============================================
data class RefreshTokenRequest(
  val refreshToken: String
)

data class TokenData(
  val accessToken: String,
  val refreshToken: String,
  val expiresIn: Long
)

// ============================================
// Validation 관련
// ============================================
data class LoginIdValidationRequest(
  val loginId: String
)

data class LoginIdValidationResponse(
  val data: ValidationResult?,
  val message: String
)

data class PhoneValidationRequest(
  val phone: String
)

data class PhoneValidationResponse(
  val data: ValidationResult?,
  val message: String
)

data class EmailValidationRequest(
  val email: String
)

data class EmailValidationResponse(
  val data: ValidationResult?,
  val message: String
)

data class ValidationResult(
  val isValid: Boolean,
  val message: String? = null
)

// ============================================
// SMS 인증 관련
// ============================================
data class SmsVerificationRequest(
  val phone: String
)

data class SmsVerificationData(
  val authCode: String,
  val expiresAt: String? = null
)

data class SmsVerificationResponse(
  val data: SmsVerificationData,
  val message: String
)

data class SmsVerifyRequest(
  val phone: String,
  val authCode: String
)

data class SmsVerifyResponse(
  val verified: Boolean,
  val message: String
)

// ============================================
// 비밀번호 관련
// ============================================
data class ResetPasswordRequest(
  val email: String? = null,
  val phone: String? = null
)

data class ChangePasswordRequest(
  val currentPassword: String,
  val newPassword: String
)

// ============================================
// 비자 관련
// ============================================
data class VisaExpiryDateRequest(
  val birthDate: String,
  val country: String,
  val nationality: String,
  val passportNo: String
)

// ============================================
// 회원 탈퇴
// ============================================
data class WithdrawRequest(
  val reason: String? = null,
  val password: String
)

// ============================================
// 에러 응답 (공통으로 사용)
// ============================================
data class RegisterWorkerErrorResponse(
  val status: String,
  val code: String,
  val errorMessage: String
)

// ============================================
// 기본 응답
// ============================================
data class DefaultResponse(
  val message: String,
  val success: Boolean = true
)