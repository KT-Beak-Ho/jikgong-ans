package com.billcorea.jikgong.api.repository.login

import com.billcorea.jikgong.api.models.request.LoginRequest
import com.billcorea.jikgong.api.models.response.ApiResult
import com.billcorea.jikgong.api.models.response.LoginResponse
import com.billcorea.jikgong.api.service.AuthApi

/**
 * API 함수 내용 override
 */
class LoginRepositoryImpl(
  private val authApi: AuthApi
) : LoginRepository {

  override suspend fun requestLogin(
    loginIdOrPhone: String,
    password: String,
    deviceToken: String
  ): ApiResult<LoginResponse> {
    return try {
      val response = authApi.login(LoginRequest(loginIdOrPhone, password, deviceToken))
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