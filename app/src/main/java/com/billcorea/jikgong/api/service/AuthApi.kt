package com.billcorea.jikgong.api.service

import com.billcorea.jikgong.api.models.response.LoginResponse
import com.billcorea.jikgong.network.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi{
  /**
   * 로그인
   */
  @POST("api/login")
  suspend fun login(
    @Body body: LoginRequest
  ): Response<LoginResponse>
}