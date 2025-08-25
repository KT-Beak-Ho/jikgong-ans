package com.billcorea.jikgong.network.model.common

/**
 * 모든 API 응답의 기본 구조
 */
data class BaseResponse<T>(
  val success: Boolean,
  val code: Int,
  val message: String,
  val data: T? = null,
  val timestamp: Long = System.currentTimeMillis()
)

/**
 * 페이징 처리된 응답
 */
data class PagedResponse<T>(
  val success: Boolean,
  val code: Int,
  val message: String,
  val data: PagedData<T>? = null,
  val timestamp: Long = System.currentTimeMillis()
)

data class PagedData<T>(
  val content: List<T>,
  val pageNumber: Int,
  val pageSize: Int,
  val totalElements: Long,
  val totalPages: Int,
  val isFirst: Boolean,
  val isLast: Boolean
)

/**
 * 에러 응답
 */
data class ErrorResponse(
  val code: String,
  val message: String,
  val details: Map<String, String>? = null,
  val timestamp: Long = System.currentTimeMillis()
)

/**
 * 기본 응답 (성공/실패만 반환)
 */
data class DefaultResponse(
  val success: Boolean,
  val message: String
)

