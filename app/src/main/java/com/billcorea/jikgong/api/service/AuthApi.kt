package com.billcorea.jikgong.api.service

import com.billcorea.jikgong.api.models.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi{
  /**
   * 로그인
   */
  @POST("api/login")
  suspend fun login(
    @Body body: com.billcorea.jikgong.api.models.request.LoginRequest
  ): Response<LoginResponse>
}