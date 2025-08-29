package com.billcorea.jikgong.presentation.company.auth.login.shared

import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent

sealed class CompanyLoginSharedEvent {
  /** 초기화 */
  object ResetLogin1Flow : CompanyLoginSharedEvent()
  object ClearError : CompanyLoginSharedEvent()

  /** 네비게이션 이벤트 */
  //  다음 페이지
  object NextPage : CompanyLoginSharedEvent()
  //  이전 페이지
  object PreviousPage : CompanyLoginSharedEvent()

  /** Page 1 이벤트 */
  //  사용자 로그인 ID 입력
  data class UpdateId(val id: String) : CompanyLoginSharedEvent()
  //  사용자 로그인 PWD 입력
  data class UpdatePassword(val password: String) : CompanyLoginSharedEvent()
  //  로그인 시도
  object RequestLogin : CompanyLoginSharedEvent()
}