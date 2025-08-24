package com.billcorea.jikgong.presentation.company.auth.login.shared

sealed class CompanyLoginSharedEvent {
  /** 초기화 */
  object ResetJoin1Flow : CompanyLoginSharedEvent()
  object ClearError : CompanyLoginSharedEvent()

  /** 네비게이션 이벤트 */
  object PreviousPage : CompanyLoginSharedEvent()

  /** 로그인 이벤트 */
  data class UpdateId(val id: String) : CompanyLoginSharedEvent()
  data class UpdatePassword(val password: String) : CompanyLoginSharedEvent()
  object Login : CompanyLoginSharedEvent()
}