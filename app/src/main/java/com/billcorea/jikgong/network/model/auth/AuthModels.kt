package com.billcorea.jikgong.network.model.auth

import com.billcorea.jikgong.network.model.common.UserRole

/**
 * 로그인 요청
 */
data class LoginRequest(
  val loginId: String,
  val password: String,
  val deviceToken: String? = null,
  val platform: String = "ANDROID"
)

/**
 * 로그인 응답
 */
data class LoginResponse(
  val accessToken: String,
  val refreshToken: String,
  val tokenType: String = "Bearer",
  val expiresIn: Long,
  val user: LoginUserData
)

/**
 * 로그인 사용자 데이터
 */
data class LoginUserData(
  val id: String,
  val loginId: String,
  val name: String,
  val role: UserRole,
  val profileImageUrl: String? = null,
  val lastLoginAt: String? = null
)

/**
 * 로그인 에러 데이터
 */
data class LoginErrorData(
  val errorCode: String,
  val errorMessage: String,
  val remainingAttempts: Int? = null
)

/**
 * 토큰 갱신 요청
 */
data class RefreshTokenRequest(
  val refreshToken: String
)

/**
 * 토큰 데이터
 */
data class TokenData(
  val accessToken: String,
  val refreshToken: String,
  val tokenType: String = "Bearer",
  val expiresIn: Long
)

/**
 * 아이디 중복 확인 요청
 */
data class LoginIdValidationRequest(
  val loginId: String
)

/**
 * 아이디 중복 확인 응답
 */
data class LoginIdValidationResponse(
  val available: Boolean,
  val message: String
)

/**
 * 전화번호 인증 요청
 */
data class PhoneValidationRequest(
  val phone: String
)

/**
 * 전화번호 인증 응답
 */
data class PhoneValidationResponse(
  val success: Boolean,
  val message: String,
  val verificationId: String? = null
)

/**
 * SMS 인증 요청
 */
data class SmsVerificationRequest(
  val phone: String,
  val verificationCode: String,
  val verificationId: String
)

/**
 * SMS 인증 응답
 */
data class SmsVerificationResponse(
  val success: Boolean,
  val message: String,
  val verified: Boolean
)

/**
 * 비자 만료일 요청
 */
data class VisaExpiryDateRequest(
  val nationality: String,
  val visaType: String
)
