package com.billcorea.jikgong.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * 네트워크 관련 헬퍼 함수 모음
 */
object NetworkHelper {

  /**
   * 안전한 API 호출
   */
  suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
  ): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
      try {
        val response = apiCall()
        if (response.isSuccessful) {
          val body = response.body()
          if (body != null) {
            NetworkResult.Success(body)
          } else {
            NetworkResult.Error(Exception("Response body is null"))
          }
        } else {
          NetworkResult.Error(
            ApiException(
              code = response.code(),
              message = response.message() ?: "Unknown error"
            )
          )
        }
      } catch (e: IOException) {
        NetworkResult.Error(NetworkException("네트워크 연결을 확인해주세요.", e))
      } catch (e: Exception) {
        NetworkResult.Error(UnknownException("알 수 없는 오류가 발생했습니다.", e))
      }
    }
  }

  /**
   * 안전한 API 호출 (커스텀 에러 처리)
   */
  suspend fun <T, E> safeApiCallWithError(
    apiCall: suspend () -> Response<T>,
    errorHandler: (Response<T>) -> E
  ): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
      try {
        val response = apiCall()
        if (response.isSuccessful) {
          val body = response.body()
          if (body != null) {
            NetworkResult.Success(body)
          } else {
            NetworkResult.Error(Exception("Response body is null"))
          }
        } else {
          val error = errorHandler(response)
          NetworkResult.Error(Exception(error.toString()))
        }
      } catch (e: IOException) {
        NetworkResult.Error(NetworkException("네트워크 연결을 확인해주세요.", e))
      } catch (e: Exception) {
        NetworkResult.Error(UnknownException("알 수 없는 오류가 발생했습니다.", e))
      }
    }
  }

  /**
   * 여러 API 호출 병렬 실행
   */
  suspend fun <T1, T2> parallel(
    call1: suspend () -> NetworkResult<T1>,
    call2: suspend () -> NetworkResult<T2>
  ): Pair<NetworkResult<T1>, NetworkResult<T2>> {
    return coroutineScope {
      val result1 = async(Dispatchers.IO) { call1() }
      val result2 = async(Dispatchers.IO) { call2() }

      Pair(result1.await(), result2.await())
    }
  }

  /**
   * 재시도 로직이 포함된 API 호출
   */
  suspend fun <T> retryApiCall(
    times: Int = 3,
    initialDelay: Long = 100,
    maxDelay: Long = 1000,
    factor: Double = 2.0,
    apiCall: suspend () -> Response<T>
  ): NetworkResult<T> {
    var currentDelay = initialDelay
    repeat(times - 1) {
      val result = safeApiCall(apiCall)
      if (result is NetworkResult.Success) {
        return result
      }

      delay(currentDelay)
      currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }

    return safeApiCall(apiCall) // 마지막 시도
  }

  /**
   * 네트워크 연결 상태 확인
   */
  fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(
      Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
      activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
      activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
      activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
      else -> false
    }
  }
}

/**
 * API 예외
 */
class ApiException(
  val code: Int,
  override val message: String
) : Exception(message)

/**
 * 네트워크 예외
 */
class NetworkException(
  override val message: String,
  override val cause: Throwable? = null
) : IOException(message, cause)

/**
 * 알 수 없는 예외
 */
class UnknownException(
  override val message: String,
  override val cause: Throwable? = null
) : Exception(message, cause)