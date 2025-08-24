package com.billcorea.jikgong.presentation.company.auth.login.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.repository.login.LoginRepository
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
   * 네비게이션 이벤트 클리어
   */
  fun clearNavigationEvents() {
    _uiState.value = _uiState.value.copy(
      shouldNavigateBack = false,
      shouldNavigateToMain = false
    )
  }

  /**
   * 로그인 처리
   */
  private fun performLogin() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoggingIn = true)

      try {
        val currentState = _uiState.value
        // 실제로는 API 호출
        // val result = loginRepository.login(currentState.id, currentState.password)

        // 임시로 성공 처리
        _uiState.value = _uiState.value.copy(
          isLoggingIn = false,
          shouldNavigateToMain = true
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(
          isLoggingIn = false,
          errorMessage = "로그인에 실패했습니다: ${e.message}"
        )
      }
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
        _uiState.value = _uiState.value.copy(shouldNavigateBack = true)
      }

      /**
       * ID 입력
       */
      is CompanyLoginSharedEvent.UpdateId -> {
        _uiState.value = _uiState.value.copy(id = event.id)
      }

      /**
       * 비밀번호 입력
       */
      is CompanyLoginSharedEvent.UpdatePassword -> {
        _uiState.value = _uiState.value.copy(password = event.password)
      }

      /**
       * 로그인 시도
       */
      is CompanyLoginSharedEvent.Login -> {
        performLogin()
      }

      /**
       * 1 Page 입력값 초기화
       */
      is CompanyLoginSharedEvent.ResetJoin1Flow -> {
        _uiState.value = _uiState.value.copy(
          id = "",
          password = "",
          currentPage = 1
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