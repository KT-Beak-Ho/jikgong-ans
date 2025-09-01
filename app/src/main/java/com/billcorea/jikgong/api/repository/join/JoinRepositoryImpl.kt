package com.billcorea.jikgong.api.repository.join

import com.billcorea.jikgong.api.models.auth.EmailValidationRequest
import com.billcorea.jikgong.api.models.auth.LoginIdValidationRequest
import com.billcorea.jikgong.api.models.auth.PhoneValidationRequest
import com.billcorea.jikgong.api.models.auth.SmsVerificationRequest
import com.billcorea.jikgong.network.common.ApiResult
import com.billcorea.jikgong.api.models.auth.EmailValidationResponse
import com.billcorea.jikgong.api.models.auth.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.auth.PhoneValidationResponse
import com.billcorea.jikgong.api.models.auth.SmsVerificationResponse
import com.billcorea.jikgong.api.service.JoinApi
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
    } catch (e: HttpException) {
      ApiResult.HttpError(e.code(), e.message() ?: "HTTP Error", null)
    } catch (e: SocketTimeoutException) {
      ApiResult.Error(Exception("연결 시간이 초과되었습니다. 네트워크 상태를 확인해주세요."))
    } catch (e: UnknownHostException) {
      ApiResult.Error(Exception("서버에 연결할 수 없습니다. 인터넷 연결을 확인해주세요."))
    } catch (e: ConnectException) {
      ApiResult.Error(Exception("서버 연결에 실패했습니다."))
    } catch (e: IOException) {
      ApiResult.Error(Exception("네트워크 오류가 발생했습니다."))
    } catch (e: Throwable) {  // Exception 대신 Throwable 사용
      ApiResult.Error(Exception("알 수 없는 오류가 발생했습니다: ${e.message}"))
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