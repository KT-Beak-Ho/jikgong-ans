package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.auth.*
import com.billcorea.jikgong.network.model.common.BaseResponse
import com.billcorea.jikgong.network.model.common.DefaultResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * 인증 관련 API 인터페이스
 */
interface AuthAPI {

  @POST("auth/login")
  suspend fun login(
    @Body request: LoginRequest
  ): Response<BaseResponse<LoginResponse>>

  @POST("auth/logout")
  suspend fun logout(
    @Header("Authorization") token: String
  ): Response<DefaultResponse>

  @POST("auth/refresh")
  suspend fun refreshToken(
    @Body request: RefreshTokenRequest
  ): Response<BaseResponse<TokenData>>

  @POST("auth/validate-id")
  suspend fun validateLoginId(
    @Body request: LoginIdValidationRequest
  ): Response<LoginIdValidationResponse>

  @POST("auth/validate-phone")
  suspend fun validatePhone(
    @Body request: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  @POST("auth/send-sms")
  suspend fun sendSmsVerification(
    @Body request: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  @POST("auth/verify-sms")
  suspend fun verifySms(
    @Body request: SmsVerificationRequest
  ): Response<SmsVerificationResponse>

  @POST("auth/reset-password")
  suspend fun resetPassword(
    @Body email: String
  ): Response<DefaultResponse>

  @POST("auth/change-password")
  suspend fun changePassword(
    @Header("Authorization") token: String,
    @Body request: ChangePasswordRequest
  ): Response<DefaultResponse>
}

data class ChangePasswordRequest(
  val currentPassword: String,
  val newPassword: String
)
