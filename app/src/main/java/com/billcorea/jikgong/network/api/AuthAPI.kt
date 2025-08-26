package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.auth.*
import com.billcorea.jikgong.network.model.common.BaseResponse
import com.billcorea.jikgong.network.model.common.DefaultResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * 인증 관련 API 인터페이스
 * - 로그인/로그아웃
 * - 토큰 관리
 * - 회원가입 검증
 * - SMS 인증
 * - 비밀번호 관리
 */
interface AuthAPI {

  // ===== 로그인/로그아웃 =====

  @POST("api/auth/login")
  suspend fun login(
    @Body request: LoginRequest
  ): Response<BaseResponse<LoginResponse>>

  @POST("api/auth/logout")
  suspend fun logout(
    @Header("Authorization") token: String
  ): Response<DefaultResponse>

  // ===== 토큰 관리 =====

  @POST("api/auth/refresh")
  suspend fun refreshToken(
    @Body request: RefreshTokenRequest
  ): Response<BaseResponse<TokenData>>

  // ===== 회원가입 검증 =====

  @POST("api/auth/validate-id")
  suspend fun validateLoginId(
    @Body request: LoginIdValidationRequest
  ): Response<LoginIdValidationResponse>

  @POST("api/auth/validate-phone")
  suspend fun validatePhone(
    @Body request: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  @POST("api/auth/validate-email")
  suspend fun validateEmail(
    @Body request: EmailValidationRequest
  ): Response<EmailValidationResponse>

  // ===== SMS 인증 =====

  @POST("api/auth/send-sms")
  suspend fun sendSmsVerification(
    @Body request: SmsVerificationRequest
  ): Response<SmsVerificationResponse>

  @POST("api/auth/verify-sms")
  suspend fun verifySms(
    @Body request: SmsVerifyRequest
  ): Response<BaseResponse<SmsVerifyResponse>>

  // ===== 비밀번호 관리 =====

  @POST("api/auth/reset-password")
  suspend fun resetPassword(
    @Body request: ResetPasswordRequest
  ): Response<DefaultResponse>

  @POST("api/auth/change-password")
  suspend fun changePassword(
    @Header("Authorization") token: String,
    @Body request: ChangePasswordRequest
  ): Response<DefaultResponse>

  // ===== 비자 만료일 (외국인 근로자용) =====

  @POST("api/auth/visa-expiry")
  suspend fun updateVisaExpiryDate(
    @Header("Authorization") token: String,
    @Body request: VisaExpiryDateRequest
  ): Response<DefaultResponse>

  // ===== 회원 탈퇴 =====

  @DELETE("api/auth/withdraw")
  suspend fun withdrawAccount(
    @Header("Authorization") token: String,
    @Body request: WithdrawRequest
  ): Response<DefaultResponse>
}