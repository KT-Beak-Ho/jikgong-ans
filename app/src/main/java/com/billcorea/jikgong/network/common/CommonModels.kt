package com.billcorea.jikgong.network.common

// ==================== 공통 응답 모델 ====================

data class DefaultResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null,
    val timestamp: Long = System.currentTimeMillis()
)

// ==================== API 결과 wrapper ====================

/**
 * 공통사용
 * API Call 결과 정규화
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
    data class HttpError(val code: Int, val message: String, val errorBody: String?) : ApiResult<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): ApiResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (Throwable) -> Unit): ApiResult<T> {
        if (this is Error) action(exception)
        return this
    }

    inline fun onHttpError(action: (Int, String, String?) -> Unit): ApiResult<T> {
        if (this is HttpError) action(code, message, errorBody)
        return this
    }
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