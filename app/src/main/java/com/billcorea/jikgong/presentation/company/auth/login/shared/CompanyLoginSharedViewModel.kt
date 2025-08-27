package com.billcorea.jikgong.presentation.company.auth.login.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants.TOTAL_PAGES
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyLoginSharedViewModel(
  private val loginRepository: LoginRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyLoginSharedUiState())
  val uiState: StateFlow<CompanyLoginSharedUiState> = _uiState.asStateFlow()

  /**
   * 네비게이션 이벤트
   */
  private val _shouldNavigateToNextPage = MutableStateFlow(false)
  val shouldNavigateToNextPage: StateFlow<Boolean> = _shouldNavigateToNextPage.asStateFlow()
  private val _shouldNavigateBack = MutableStateFlow(false)
  val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack.asStateFlow()
  private val _shouldNavigateToProjectList = MutableStateFlow(false)
  val shouldNavigateToProjectList: StateFlow<Boolean> = _shouldNavigateToProjectList.asStateFlow()

  /**
   * 네비게이션 이벤트 클리어
   */
  fun clearNavigationEvents() {
    _shouldNavigateToNextPage.value = false
    _shouldNavigateBack.value = false
    _shouldNavigateToProjectList.value = false
  }

  /**
   * 다음 페이지 이동 검증 함수
   */
  private fun canNavigateToNextPage(): Boolean {
    val state = _uiState.value
    val currentPage = state.currentPage

    val pageCompletionMap = mapOf(
      1 to state.isPage1Complete,
    )
    return pageCompletionMap[currentPage] ?: false
  }

  /**
   * ID 유효성 검증 함수
   */
  private fun validateId(id: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    val isValid = when {
      id.isEmpty() -> {
        errors["id"] = "아이디를 입력해주세요"
        false
      }
      else -> {
        errors.remove("id")
        true
      }
    }
    _uiState.value = _uiState.value.copy(
      validationErrors = errors
    )
  }

  /**
   * 비밀번호 유효성 검증 함수
   */
  private fun validatePassword(password: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    val isValid = when {
      password.isEmpty() -> {
        errors["password"] = "비밀번호를 입력해주세요"
        false
      }
      else -> {
        errors.remove("password")
        true
      }
    }
    _uiState.value = _uiState.value.copy(
      validationErrors = errors
    )
  }

  /**
   * 로그인 처리
   */
  private fun requestLogin() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoggingIn = true)
      _uiState.value = _uiState.value.copy(
        isLoggingIn = false,
        errorMessage = null
      )
      _shouldNavigateToProjectList.value = true

//      loginRepository.requestLogin(_uiState.value.id, _uiState.value.password, _uiState.value.deviceToken)
//        .onSuccess { response ->
//          _uiState.value = _uiState.value.copy(
//            isLoggingIn = false,
//            errorMessage = null
//          )
//          _shouldNavigateToProjectList.value = true
//        }
//        .onError { error ->
//          _uiState.value = _uiState.value.copy(
//            isLoggingIn = false,
//            isLoading=false,
//            errorMessage = "네트워크 오류: ${error.message}"
//          )
//        }
//        .onHttpError { code, message, errorBody ->
//          val errorMessage = when (code) {
//            401 -> "아이디 또는 비밀번호가 잘못되었습니다"
//            404 -> "존재하지 않는 계정입니다"
//            408 -> "네트워크 오류"
//            500 -> "서버 오류가 발생했습니다"
//            else -> "로그인 실패: HTTP $code"
//          }
//          _uiState.value = _uiState.value.copy(
//            isLoggingIn = false,
//            isLoading=false,
//            errorMessage = errorMessage
//          )
//        }
    }
  }

  /**
   * 전체 이벤트 처리
   */
  fun onEvent(event: CompanyLoginSharedEvent) {
    when (event) {
      /**
       * 페이지 뒤로 가기
       */
      is CompanyLoginSharedEvent.PreviousPage -> {
        _shouldNavigateBack.value = true
      }
      /**
       * 페이지 다음 으로 가기
       */
      is CompanyLoginSharedEvent.NextPage -> {
        if (canNavigateToNextPage()) {
          val currentPage = _uiState.value.currentPage
          _uiState.value = _uiState.value.copy(
            currentPage = currentPage + 1
          )
          _shouldNavigateToNextPage.value = true
        }
      }

      /**
       * ID 입력
       */
      is CompanyLoginSharedEvent.UpdateId -> {
        _uiState.value = _uiState.value.copy(id = event.id)
        validateId(event.id)
      }

      /**
       * 비밀번호 입력
       */
      is CompanyLoginSharedEvent.UpdatePassword -> {
        _uiState.value = _uiState.value.copy(password = event.password)
        validatePassword(event.password)
      }

      /**
       * 로그인 시도
       */
      is CompanyLoginSharedEvent.RequestLogin -> {
        if (_uiState.value.canLogin) {
          requestLogin()
        }
      }

      /**
       * 1 Page 입력값 초기화
       */
      is CompanyLoginSharedEvent.ResetLogin1Flow -> {
        _uiState.value = _uiState.value.copy(
          id = "",
          password = "",
          currentPage = 1,
          validationErrors = emptyMap(),
          errorMessage = null,
          shouldNavigateBack = false,
          shouldNavigateToMain = false,
          isLoading = false,
          isLoggingIn = false
        )
      }

      /**
       * 에러 초기화
       */
      CompanyLoginSharedEvent.ClearError -> {
        _uiState.value = _uiState.value.copy(
          validationErrors = emptyMap(),
          errorMessage = null
        )
      }
    }
  }
}