package com.billcorea.jikgong.api.repository.login

import com.billcorea.jikgong.network.common.ApiResult
import com.billcorea.jikgong.network.auth.LoginResponse


/**
 * API 함수 이름 정의
 */
interface LoginRepository {
  /** Login 요청 */
  suspend fun requestLogin(loginIdOrPhone: String, password: String, deviceToken: String): ApiResult<LoginResponse>
}

