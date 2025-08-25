package com.billcorea.jikgong.network.interceptor

import com.billcorea.jikgong.network.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * 에러 처리 인터셉터
 * API 응답의 에러를 통일된 방식으로 처리
 */
class ErrorInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()

    try {
      val response = chain.proceed(request)

      // 응답 코드 확인
      when (response.code) {
        401 -> {
          // 인증 실패 - 토큰 만료 등
          handleUnauthorized(response)
        }
        403 -> {
          // 권한 없음
          handleForbidden(response)
        }
        404 -> {
          // 리소스를 찾을 수 없음
          handleNotFound(response)
        }
        500, 502, 503, 504 -> {
          // 서버 에러
          handleServerError(response)
        }
      }

      return response

    } catch (e: IOException) {
      // 네트워크 에러
      throw NetworkException("네트워크 연결을 확인해주세요.", e)
    } catch (e: Exception) {
      // 기타 에러
      throw UnknownException("알 수 없는 오류가 발생했습니다.", e)
    }
  }

  private fun handleUnauthorized(response: Response) {
    val responseBody = response.peekBody(Long.MAX_VALUE).string()

    try {
      val json = JSONObject(responseBody)
      val code = json.optString("code", "")

      when (code) {
        NetworkConfig.TOKEN_EXPIRED_CODE -> {
          // 토큰 만료 - 리프레시 토큰으로 갱신 필요
          // EventBus나 LiveData를 통해 로그인 화면으로 이동 이벤트 발생
        }
        NetworkConfig.UNAUTHORIZED_CODE -> {
          // 인증 실패 - 다시 로그인 필요
        }
      }
    } catch (e: Exception) {
      // JSON 파싱 실패
    }
  }

  private fun handleForbidden(response: Response) {
    // 권한 없음 처리
  }

  private fun handleNotFound(response: Response) {
    // 리소스를 찾을 수 없음 처리
  }

  private fun handleServerError(response: Response) {
    // 서버 에러 처리
  }
}

/**
 * 네트워크 예외
 */
class NetworkException(message: String, cause: Throwable? = null) : IOException(message, cause)

/**
 * 알 수 없는 예외
 */
class UnknownException(message: String, cause: Throwable? = null) : Exception(message, cause)