package com.billcorea.jikgong.network.model.common

/**
 * 기본 API 응답 구조
 */
data class BaseResponse<T>(
  val success: Boolean,
  val code: String,
  val message: String,
  val data: T? = null,
  val timestamp: String = System.currentTimeMillis().toString()
)

/**
 * 페이징 응답 구조
 */
data class PagedResponse<T>(
  val success: Boolean,
  val code: String,
  val message: String,
  val data: PageData<T>,
  val timestamp: String = System.currentTimeMillis().toString()
)

/**
 * 페이지 데이터
 */
data class PageData<T>(
  val content: List<T>,
  val pageNumber: Int,
  val pageSize: Int,
  val totalElements: Long,
  val totalPages: Int,
  val first: Boolean,
  val last: Boolean,
  val empty: Boolean
)

/**
 * 기본 응답 (데이터 없음)
 */
data class DefaultResponse(
  val success: Boolean,
  val code: String,
  val message: String,
  val timestamp: String = System.currentTimeMillis().toString()
)

/**
 * 에러 응답
 */
data class ErrorResponse(
  val success: Boolean = false,
  val code: String,
  val message: String,
  val errors: List<FieldError>? = null,
  val timestamp: String = System.currentTimeMillis().toString()
)

/**
 * 필드 에러
 */
data class FieldError(
  val field: String,
  val value: Any?,
  val reason: String
)

