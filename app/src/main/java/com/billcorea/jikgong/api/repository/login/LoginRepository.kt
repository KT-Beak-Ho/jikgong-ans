package com.billcorea.jikgong.api.repository.login

import com.billcorea.jikgong.api.models.common.ApiResult
import com.billcorea.jikgong.api.models.auth.LoginResponse


/**
 * API 함수 이름 정의
 */
interface LoginRepository {
  /** Login 요청 */
  suspend fun requestLogin(loginIdOrPhone: String, password: String, deviceToken: String): ApiResult<LoginResponse>
}

