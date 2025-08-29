package com.billcorea.jikgong.presentation.worker.login.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import com.billcorea.jikgong.api.models.auth.LoginData
import com.billcorea.jikgong.api.models.auth.LoginErrorData
import com.billcorea.jikgong.api.models.auth.LoginRequest
import com.billcorea.jikgong.network.service.RetrofitAPI
import com.billcorea.jikgong.api.models.auth.LoginResponse
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants.TOTAL_PAGES
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkerLoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerLoginUiState())
    val uiState: StateFlow<WorkerLoginUiState> = _uiState.asStateFlow()

    /**
     * 네비게이션 이벤트
     */
    private val _shouldNavigateToProjectListPage = MutableStateFlow(false)
    val shouldNavigateToNextPage: StateFlow<Boolean> = _shouldNavigateToProjectListPage.asStateFlow()

    /**
     * 네비게이션 이벤트 클리어
     */
    fun clearNavigationEvents() {
        _shouldNavigateToProjectListPage.value = false
    }

    /**
     * 다음 페이지 이동 검증 함수
     */
    private fun canNavigateToProjectListPage(): Boolean {
        val state = _uiState.value

        // return state.getLoginToken.isNotEmpty()
        return true
    }

    /**
     * 전화번호 양식 검증 함수
     */
    private fun isEmptyLoginIdOrPhone(loginIdOrPhone: String) {
        val errors = _uiState.value.validationErrors.toMutableMap()
        when {
            loginIdOrPhone.isEmpty() -> {
                errors["loginIdOrPhone"] = "아이디를 입력해주세요"
                false
            }

            else -> {
                errors.remove("loginIdOrPhone")
                true
            }
        }
        _uiState.value = _uiState.value.copy(
            validationErrors = errors
        )
    }

    private fun isEmptyPassword(password: String) {
        val errors = _uiState.value.validationErrors.toMutableMap()
        when {
            password.isEmpty() -> {
                errors["password"] = "비밀번호를 입력해주세요"
                false
            }

            /* 비밀번호 정규식
            !password.matches(Regex("")) -> {
                errors["password"] = "비밀번호 형식이 아닙니다"
                false
            }
            */

            else -> {
                errors.remove("password")
                true
            }
        }
        _uiState.value = _uiState.value.copy(
            validationErrors = errors,
        )
    }

    /**
     * 인증코드 발송 요청
     */
    private fun doLogin(loginIdOrPhone: String, password: String, deviceToken: String) {
        val body = LoginRequest(loginIdOrPhone, password, deviceToken)
        RetrofitAPI.create().login(body).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val loginResponse = response.body()
                val loginDataElement = loginResponse?.data
                if (response.isSuccessful) {
                    try {
                        // LoginData는 이미 파싱된 상태
                        val loginData = loginDataElement!!
                        onEvent(WorkerLoginSharedEvent.updateLoginToken(loginData.accessToken, loginData.refreshToken))
                        onEvent(WorkerLoginSharedEvent.updateRole(loginData.userRole))
                        Log.d("LOGIN", "로그인 성공: ${loginData}")
                        onEvent(WorkerLoginSharedEvent.toProjectListPage)
                    } catch (e: Exception) {
                        onEvent(WorkerLoginSharedEvent.updateErrorMessage("로그인 실패"))
                    }

                } else {
                    val loginErrorBodyString = response.errorBody()?.string()

                    try {
                        val loginErrorJson = Gson().fromJson(loginErrorBodyString, LoginResponse::class.java)
                        onEvent(WorkerLoginSharedEvent.updateErrorMessage(loginErrorJson.message))
                    } catch (e: Exception) {
                        Log.e("LOGIN", "LoginErrorData 파싱 실패: ${e.localizedMessage}")
                        onEvent(WorkerLoginSharedEvent.updateErrorMessage("로그인 실패 (에러 파싱 실패)"))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Log.e("", "error ${t.localizedMessage}")
                // 실패 시에도 isWaiting = false로 변경
                _uiState.value = _uiState.value.copy(errorMessage = t.localizedMessage)
            }
        })
    }

    /**
     * 전체 이벤트 처리
     */
    fun onEvent(event: WorkerLoginSharedEvent) {
        when (event) {
            /**
             * 공통 이벤트
             */

            /**
             * ProjectList 페이지로 가기
             */
            is WorkerLoginSharedEvent.toProjectListPage -> {
                if (canNavigateToProjectListPage()) {
                    _shouldNavigateToProjectListPage.value = true
                }
            }

            /**
             * Page 1 (CompanyJoinPage1Screen) 이벤트
             */
            /**
             * ID 입력시 실시간 업데이트
             */
            is WorkerLoginSharedEvent.updateLoginIdOrPhone -> {
                _uiState.value = _uiState.value.copy(
                    loginIdOrPhone = event.loginIdOrPhone,
                )
                // 실시간 유효성 검증
                isEmptyLoginIdOrPhone(event.loginIdOrPhone)
            }
            /**
             * PW 입력시 실시간 업데이트
             */
            is WorkerLoginSharedEvent.updatePassword -> {
                _uiState.value = _uiState.value.copy(
                    password = event.password,
                )
                // 실시간 유효성 검증
                isEmptyPassword(event.password)
            }
            /**
             * Login
             */
            is WorkerLoginSharedEvent.RequestLogin -> {
                // Login
                if(_uiState.value.isLoginEmpty) {
                    doLogin(_uiState.value.loginIdOrPhone,_uiState.value.password,_uiState.value.deviceToken)
                }
                else {
                    isEmptyLoginIdOrPhone(_uiState.value.loginIdOrPhone)
                    isEmptyPassword(_uiState.value.password)
                }
            }
            /**
             * ErrorMessage
             */
            is WorkerLoginSharedEvent.updateErrorMessage -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = event.errorMessage
                )
            }

            /**
             * Update AccessToken
             */
            is WorkerLoginSharedEvent.updateLoginToken -> {
                _uiState.value = _uiState.value.copy(
                    accessToken = event.accessToken,
                    refreshToken = event.refreshToken
                )
            }

            /**
             * update Role
             */
            is WorkerLoginSharedEvent.updateRole -> {
                _uiState.value = _uiState.value.copy(
                    role = event.role
                )
            }
            /**
             * 1 Page 입력값 초기화
             */
            is WorkerLoginSharedEvent.InitLogin -> {
                _uiState.value = _uiState.value.copy(
                    loginIdOrPhone = "",
                    password = "",
                    errorMessage = null
                )
            }

            /**
             * 공통
             */
            /**
             * 에러 초기화
             */
            WorkerLoginSharedEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(
                    validationErrors = emptyMap() //  현재 페이지의 모든 에러 제거
                )
            }
        }
    }
}