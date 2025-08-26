package com.billcorea.jikgong.network.model.auth

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

// ===== 로그인 관련 =====
data class LoginRequest(
  @SerializedName("loginId")
  val loginId: String,

  @SerializedName("password")
  val password: String,

  @SerializedName("deviceToken")
  val deviceToken: String? = null,

  @SerializedName("deviceType")
  val deviceType: String = "ANDROID"
)

data class LoginResponse(
  @SerializedName("data")
  val data: JsonElement,

  @SerializedName("message")
  val message: String
)

data class LoginData(
  @SerializedName("accessToken")
  val accessToken: String,

  @SerializedName("refreshToken")
  val refreshToken: String,

  @SerializedName("userId")
  val userId: String,

  @SerializedName("userType")
  val userType: String,

  @SerializedName("name")
  val name: String
)

data class LoginErrorData(
  @SerializedName("status")
  val status: String,

  @SerializedName("code")
  val code: String,

  @SerializedName("errorMessage")
  val errorMessage: String
)

// ===== SMS 인증 관련 =====
data class SmsVerificationRequest(
  @SerializedName("phoneNumber")
  val phoneNumber: String,

  @SerializedName("purpose")
  val purpose: String = "SIGNUP"
)

data class SmsVerificationResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("verificationId")
  val verificationId: String? = null,

  @SerializedName("expiresIn")
  val expiresIn: Int = 180
)

// SmsVerificationData는 SmsVerificationResponse와 동일한 구조이므로 별칭으로 처리
typealias SmsVerificationData = SmsVerificationResponse

data class SmsVerifyRequest(
  @SerializedName("phoneNumber")
  val phoneNumber: String,

  @SerializedName("verificationCode")
  val verificationCode: String,

  @SerializedName("verificationId")
  val verificationId: String? = null
)

data class SmsVerifyResponse(
  @SerializedName("verified")
  val verified: Boolean,

  @SerializedName("token")
  val token: String? = null
)

// ===== 검증 관련 =====
data class LoginIdValidationRequest(
  @SerializedName("loginId")
  val loginId: String
)

data class LoginIdValidationResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("isValid")
  val isValid: Boolean
)

data class PhoneValidationRequest(
  @SerializedName("phoneNumber")
  val phoneNumber: String
)

data class PhoneValidationResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("isValid")
  val isValid: Boolean
)

data class EmailValidationRequest(
  @SerializedName("email")
  val email: String
)

data class EmailValidationResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("isValid")
  val isValid: Boolean
)

// ===== 비밀번호 관련 =====
data class ResetPasswordRequest(
  @SerializedName("phoneNumber")
  val phoneNumber: String,

  @SerializedName("newPassword")
  val newPassword: String,

  @SerializedName("verificationToken")
  val verificationToken: String
)

data class ChangePasswordRequest(
  @SerializedName("currentPassword")
  val currentPassword: String,

  @SerializedName("newPassword")
  val newPassword: String
)

// ===== 비자 만료일 =====
data class VisaExpiryDateRequest(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("visaType")
  val visaType: String,

  @SerializedName("expiryDate")
  val expiryDate: String
)

// ===== 회원 탈퇴 =====
data class WithdrawRequest(
  @SerializedName("reason")
  val reason: String,

  @SerializedName("password")
  val password: String,

  @SerializedName("feedback")
  val feedback: String? = null
)

// ===== 토큰 관련 =====
data class RefreshTokenRequest(
  @SerializedName("refreshToken")
  val refreshToken: String
)

data class TokenData(
  @SerializedName("accessToken")
  val accessToken: String,

  @SerializedName("refreshToken")
  val refreshToken: String,

  @SerializedName("expiresIn")
  val expiresIn: Long
)