package com.billcorea.jikgong.api.service

import com.billcorea.jikgong.api.models.auth.EmailValidationRequest
import com.billcorea.jikgong.api.models.auth.LoginIdValidationRequest
import com.billcorea.jikgong.api.models.auth.PhoneValidationRequest
import com.billcorea.jikgong.api.models.auth.SmsVerificationRequest
import com.billcorea.jikgong.api.models.auth.EmailValidationResponse
import com.billcorea.jikgong.api.models.auth.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.auth.PhoneValidationResponse
import com.billcorea.jikgong.api.models.auth.SmsVerificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 회원가입 관련 API 인터페이스
 */
interface JoinApi {

  /**
   * SMS 인증번호 발송
   */
  @POST("api/join/sms-verification")
  suspend fun smsVerification(
    @Body body: SmsVerificationRequest
  ): Response<SmsVerificationResponse>

  /**
   * 전화번호 중복 확인
   */
  @POST("api/join/validation-phone")
  suspend fun phoneValidation(
    @Body body: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  /**
   * 로그인 ID 중복 확인
   */
  @POST("api/join/validation-loginId")
  suspend fun loginIdValidation(
    @Body body: LoginIdValidationRequest
  ): Response<LoginIdValidationResponse>

  /**
   * 사용자 Email 중복 확인 ( 수정 필요 )
   */
  @POST("api/join/validation-email")
  suspend fun emailValidation(
    @Body body: EmailValidationRequest
  ): Response<EmailValidationResponse>
}