package com.billcorea.jikgong.api.repository

import com.billcorea.jikgong.api.models.request.EmailValidationRequest
import com.billcorea.jikgong.api.models.request.LoginIdValidationRequest
import com.billcorea.jikgong.api.models.request.PhoneValidationRequest
import com.billcorea.jikgong.api.models.request.SmsVerificationRequest
import com.billcorea.jikgong.api.models.response.ApiResult
import com.billcorea.jikgong.api.models.response.EmailValidationResponse
import com.billcorea.jikgong.api.models.response.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.response.PhoneValidationResponse
import com.billcorea.jikgong.api.models.response.SmsVerificationResponse
import com.billcorea.jikgong.api.service.JoinApi

/**
 * API 함수 내용 override
 */
class JoinRepositoryImpl(
  private val joinApi: JoinApi
) : JoinRepository {

  override suspend fun sendSmsVerification(phoneNumber: String): ApiResult<SmsVerificationResponse> {
    return try {
      val response = joinApi.smsVerification(SmsVerificationRequest(phoneNumber))
      when {
        response.isSuccessful -> {
          response.body()?.let { body ->
            ApiResult.Success(body)
          } ?: run {
            ApiResult.Error(Exception("Response body is null"))
          }
        }
        else -> {
          val errorBody = response.errorBody()?.string()
          ApiResult.HttpError(response.code(), response.message(), errorBody)
        }
      }
    } catch (e: Exception) {
      ApiResult.Error(e)
    }
  }

  override suspend fun validatePhone(phoneNumber: String): ApiResult<PhoneValidationResponse> {
    return try {
      val response = joinApi.phoneValidation(PhoneValidationRequest(phoneNumber))
      when {
        response.isSuccessful -> {
          response.body()?.let { body ->
            ApiResult.Success(body)
          } ?: run {
            ApiResult.Error(Exception("Response body is null"))
          }
        }
        else -> {
          val errorBody = response.errorBody()?.string()
          ApiResult.HttpError(response.code(), response.message(), errorBody)
        }
      }
    } catch (e: Exception) {
      ApiResult.Error(e)
    }
  }

  override suspend fun validateLoginId(loginId: String): ApiResult<LoginIdValidationResponse> {
    return try {
      val response = joinApi.loginIdValidation(LoginIdValidationRequest(loginId))
      when {
        response.isSuccessful -> {
          response.body()?.let { body ->
            ApiResult.Success(body)
          } ?: run {
            ApiResult.Error(Exception("Response body is null"))
          }
        }
        else -> {
          val errorBody = response.errorBody()?.string()
          ApiResult.HttpError(response.code(), response.message(), errorBody)
        }
      }
    } catch (e: Exception) {
      ApiResult.Error(e)
    }
  }

  override suspend fun validateEmail(email: String): ApiResult<EmailValidationResponse> {
    return try {
      val response = joinApi.emailValidation(EmailValidationRequest(email))
      when {
        response.isSuccessful -> {
          response.body()?.let { body ->
            ApiResult.Success(body)
          } ?: run {
            ApiResult.Error(Exception("Response body is null"))
          }
        }
        else -> {
          val errorBody = response.errorBody()?.string()
          ApiResult.HttpError(response.code(), response.message(), errorBody)
        }
      }
    } catch (e: Exception) {
      ApiResult.Error(e)
    }
  }
}

//Phone validation fail code: 409
//Phone validation fail message:
//Phone validation fail errorBody: {"data":{"status":"CONFLICT","code":"MEMBER-005","errorMessage":"이미 등록된 핸드폰 번호입니다."},"message":"커스텀 예외 반환"}
//onHttpError - code: 409
//onHttpError - message:
//onHttpError - errorBody: {"data":{"status":"CONFLICT","code":"MEMBER-005","errorMessage":"이미 등록된 핸드폰 번호입니다."},"message":"커스텀 예외 반환"}
//Phone validation HTTP error: 이미 등록된 전화번호입니다