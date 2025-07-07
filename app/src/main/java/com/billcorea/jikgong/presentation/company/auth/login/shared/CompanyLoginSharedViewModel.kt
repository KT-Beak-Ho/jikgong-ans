package com.billcorea.jikgong.presentation.company.auth.login.shared

import androidx.lifecycle.ViewModel
import com.billcorea.jikgong.api.repository.login.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CompanyLoginSharedViewModel(
  private val loginRepository: LoginRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyLoginSharedUiState())
  val uiState: StateFlow<CompanyLoginSharedUiState> = _uiState.asStateFlow()

  /**
   * 네비게이션 이벤트
   */
  private val _shouldNavigateBack = MutableStateFlow(false)
  val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack.asStateFlow()

  /**
   * 네비게이션 이벤트 클리어
   */
  fun clearNavigationEvents() {
//    _shouldNavigateToNextPage.value = false
    _shouldNavigateBack.value = false
//    _shouldNavigateHome.value = false
  }

  /**
   * 전체 이벤트 처리
   */
  fun onEvent(event: CompanyLoginSharedEvent) {
    when (event) {
      /**
       * 공통 이벤트
       */
      /**
       * 페이지 뒤로 가기
       */
      is CompanyLoginSharedEvent.PreviousPage -> {
        val currentPage = _uiState.value.currentPage
        //  현재 page가 1 page 이상인 경우에만 뒤로가기 가능
        _uiState.value = _uiState.value.copy(
          currentPage = currentPage - 1
        )
        _shouldNavigateBack.value = currentPage > 0
      }
      /**
       * 페이지 다음 으로 가기
       */
//      is CompanyLoginSharedEvent.NextPage -> {
//        if (canNavigateToNextPage()) {
//          val currentPage = _uiState.value.currentPage
//          _uiState.value = _uiState.value.copy(
//            currentPage = currentPage + 1
//          )
//          _shouldNavigateToNextPage.value = true
//        }
//      }
      /**
       * main 페이지로 돌아가기
       */
//      is CompanyLoginSharedEvent.HomePage -> {
//        val currentPage = _uiState.value.currentPage
//        _shouldNavigateHome.value = currentPage == TOTAL_PAGES
//      }
      /**
       * 1 Page 입력값 초기화
       */
      is CompanyLoginSharedEvent.ResetJoin1Flow -> {
        _uiState.value = _uiState.value.copy()
      }
      /**
       * 공통
       */
      /**
       * 에러 초기화
       */
      CompanyLoginSharedEvent.ClearError -> {
        _uiState.value = _uiState.value.copy(
          validationErrors = emptyMap(), //  현재 페이지의 모든 에러 제거
          errorMessage = null
        )
      }
    }
  }
}