package com.billcorea.jikgong.network.util

/**
 * 네트워크 요청 결과를 나타내는 sealed class
 */
sealed class NetworkResult<T> {
  data class Success<T>(val data: T) : NetworkResult<T>()
  data class Error<T>(val exception: Exception) : NetworkResult<T>()
  class Loading<T> : NetworkResult<T>()

  /**
   * 성공 여부 확인
   */
  val isSuccess: Boolean
    get() = this is Success

  /**
   * 에러 여부 확인
   */
  val isError: Boolean
    get() = this is Error

  /**
   * 로딩 중 여부 확인
   */
  val isLoading: Boolean
    get() = this is Loading

  /**
   * 데이터 가져오기 (null safe)
   */
  fun getOrNull(): T? {
    return when (this) {
      is Success -> data
      is Error -> null
      is Loading -> null
    }
  }

  /**
   * 데이터 가져오기 (기본값 제공)
   */
  fun getOrDefault(default: T): T {
    return when (this) {
      is Success -> data
      is Error -> default
      is Loading -> default
    }
  }

  /**
   * 성공 시 콜백 실행
   */
  inline fun onSuccess(action: (value: T) -> Unit): NetworkResult<T> {
    if (this is Success) {
      action(data)
    }
    return this
  }

  /**
   * 에러 시 콜백 실행
   */
  inline fun onError(action: (exception: Exception) -> Unit): NetworkResult<T> {
    if (this is Error) {
      action(exception)
    }
    return this
  }

  /**
   * 로딩 시 콜백 실행
   */
  inline fun onLoading(action: () -> Unit): NetworkResult<T> {
    if (this is Loading) {
      action()
    }
    return this
  }

  /**
   * 결과 변환
   */
  inline fun <R> map(transform: (value: T) -> R): NetworkResult<R> {
    return when (this) {
      is Success -> Success(transform(data))
      is Error -> Error(exception)
      is Loading -> Loading()
    }
  }

  /**
   * 결과 flat map
   */
  inline fun <R> flatMap(transform: (value: T) -> NetworkResult<R>): NetworkResult<R> {
    return when (this) {
      is Success -> transform(data)
      is Error -> Error(exception)
      is Loading -> Loading()
    }
  }
}

/**
 * NetworkResult 생성 헬퍼 함수
 */
fun <T> networkResultSuccess(value: T): NetworkResult<T> = NetworkResult.Success(value)

fun <T> networkResultError(exception: Exception): NetworkResult<T> = NetworkResult.Error(exception)

fun <T> networkResultLoading(): NetworkResult<T> = NetworkResult.Loading()