package com.billcorea.jikgong.network.common

// ==================== 공통 응답 모델 ====================

data class DefaultResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null,
    val timestamp: Long = System.currentTimeMillis()
)

// ==================== API 결과 wrapper ====================

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val exception: Exception) : ApiResult<T>()
    data class HttpError<T>(
        val code: Int, 
        val message: String, 
        val errorBody: String?
    ) : ApiResult<T>()
}

// ==================== 페이지네이션 ====================

data class PaginatedResponse<T>(
    val data: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

// ==================== 공통 에러 모델 ====================

data class ErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, Any>? = null
)