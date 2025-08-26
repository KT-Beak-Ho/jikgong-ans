package com.billcorea.jikgong.network.interceptor

import android.content.SharedPreferences
import com.billcorea.jikgong.network.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 인증 토큰 인터셉터
 * API 요청에 자동으로 인증 토큰을 추가
 */
class AuthInterceptor(
  private val sharedPreferences: SharedPreferences
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    // 로그인, 회원가입 등 인증이 필요없는 API는 토큰 추가 안함
    val skipAuthPaths = listOf(
      "/auth/login",
      "/auth/register",
      "/auth/validate-id",
      "/auth/validate-phone",
      "/auth/send-sms",
      "/auth/verify-sms",
      "/auth/reset-password"
    )

    val requestPath = originalRequest.url.encodedPath
    val shouldSkipAuth = skipAuthPaths.any { requestPath.contains(it) }

    if (shouldSkipAuth) {
      return chain.proceed(originalRequest)
    }

    // SharedPreferences에서 토큰 가져오기
    val accessToken = sharedPreferences.getString(NetworkConfig.PREF_ACCESS_TOKEN, null)

    // 토큰이 없으면 원본 요청 그대로 진행
    if (accessToken.isNullOrEmpty()) {
      return chain.proceed(originalRequest)
    }

    // 토큰을 헤더에 추가
    val modifiedRequest = originalRequest.newBuilder()
      .header(NetworkConfig.HEADER_AUTHORIZATION, "Bearer $accessToken")
      .build()

    return chain.proceed(modifiedRequest)
  }
}