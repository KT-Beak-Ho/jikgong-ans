package com.billcorea.jikgong.presentation.company.auth.login.shared

data class CompanyLoginSharedUiState (
  /** Page 1: 로그인 */
  val id: String = "",         //  로그인 ID
  val password: String = "",   //  로그인 PWD

  /** 공통 상태 */
  val currentPage: Int = 1,
  val errorMessage: String? = null,
  val validationErrors: Map<String, String> = emptyMap(),

  /** 네비게이션 상태 */
  val shouldNavigateBack: Boolean = false,
  val shouldNavigateToMain: Boolean = false,

  /** 로딩 상태 */
  val isLoading: Boolean = false,
  val isLoggingIn: Boolean = false
) {
  /** 각 페이지 별 완료 상태 확인 */
  val isPage1Complete: Boolean
    get() = id.isNotEmpty() && password.isNotEmpty()

  /** 로그인 가능 여부 */
  val canLogin: Boolean
    get() = isPage1Complete && !isLoggingIn
}