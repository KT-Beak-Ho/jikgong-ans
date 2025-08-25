package com.billcorea.jikgong.network.config

/**
 * 네트워크 관련 상수 설정
 */
object NetworkConfig {
  // API Base URLs
  const val BASE_URL = "https://api.jikgong.com/api/v1/"
  const val DEV_BASE_URL = "https://dev-api.jikgong.com/api/v1/"
  const val LOCAL_BASE_URL = "http://10.0.2.2:8080/api/v1/"

  // Timeout 설정 (초 단위)
  const val CONNECT_TIMEOUT = 30L
  const val READ_TIMEOUT = 30L
  const val WRITE_TIMEOUT = 30L

  // 재시도 설정
  const val MAX_RETRY_COUNT = 3
  const val RETRY_DELAY_MILLIS = 1000L

  // 헤더 키
  const val HEADER_AUTHORIZATION = "Authorization"
  const val HEADER_CONTENT_TYPE = "Content-Type"
  const val HEADER_ACCEPT = "Accept"
  const val HEADER_USER_AGENT = "User-Agent"
  const val HEADER_DEVICE_ID = "X-Device-Id"
  const val HEADER_APP_VERSION = "X-App-Version"
  const val HEADER_PLATFORM = "X-Platform"

  // 헤더 값
  const val CONTENT_TYPE_JSON = "application/json"
  const val CONTENT_TYPE_FORM = "application/x-www-form-urlencoded"
  const val CONTENT_TYPE_MULTIPART = "multipart/form-data"
  const val PLATFORM_ANDROID = "android"

  // API 응답 코드
  const val SUCCESS_CODE = "0000"
  const val UNAUTHORIZED_CODE = "1001"
  const val TOKEN_EXPIRED_CODE = "1002"
  const val INVALID_REQUEST_CODE = "2001"
  const val SERVER_ERROR_CODE = "5001"

  // SharedPreferences 키
  const val PREF_ACCESS_TOKEN = "access_token"
  const val PREF_REFRESH_TOKEN = "refresh_token"
  const val PREF_USER_ID = "user_id"
  const val PREF_USER_ROLE = "user_role"
  const val PREF_DEVICE_ID = "device_id"

  // 기타 설정
  const val PAGE_SIZE = 20
  const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB
  const val IMAGE_QUALITY = 80
}