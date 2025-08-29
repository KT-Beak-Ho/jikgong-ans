package com.billcorea.jikgong.api.repository.join

import com.billcorea.jikgong.network.common.ApiResult
import com.billcorea.jikgong.api.models.auth.EmailValidationResponse
import com.billcorea.jikgong.api.models.auth.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.auth.PhoneValidationResponse
import com.billcorea.jikgong.api.models.auth.SmsVerificationResponse

/**
 * API 함수 이름 정의
 */
interface JoinRepository {
  /** sms 인증 코드 발송 요청 */
  suspend fun sendSmsVerification(phoneNumber: String): ApiResult<SmsVerificationResponse>
  /** 휴대폰 번호 등록 여부 검증 요청 */
  suspend fun validatePhone(phoneNumber: String): ApiResult<PhoneValidationResponse>
  /** Login ID 등록 여부 검증 요청 */
  suspend fun validateLoginId(id: String): ApiResult<LoginIdValidationResponse>
  /** Email 등록 여부 검증 요청 */
  suspend fun validateEmail(email: String): ApiResult<EmailValidationResponse>
}