package com.billcorea.jikgong.api.repository

import com.billcorea.jikgong.api.models.response.ApiResult
import com.billcorea.jikgong.api.models.response.EmailValidationResponse
import com.billcorea.jikgong.api.models.response.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.response.PhoneValidationResponse
import com.billcorea.jikgong.api.models.response.SmsVerificationResponse

/**
 * API 함수 이름 정의
 */
interface JoinRepository {
  //  sms 인증 코드 발송 요청
  suspend fun sendSmsVerification(phoneNumber: String): ApiResult<SmsVerificationResponse>
  //  휴대폰 번호 등록 여부 검증 요청
  suspend fun validatePhone(phoneNumber: String): ApiResult<PhoneValidationResponse>
  //  Login ID 등록 여부 검증 요청
  suspend fun validateLoginId(id: String): ApiResult<LoginIdValidationResponse>
  //  Email 등록 여부 검증 요청
  suspend fun validateEmail(email: String): ApiResult<EmailValidationResponse>
}