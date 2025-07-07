package com.billcorea.jikgong.presentation.company.auth.login.shared

import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent

sealed class CompanyLoginSharedEvent {
  /** 초기화 */
  //  page 1 초기화
  object ResetJoin1Flow : CompanyLoginSharedEvent()
  // 에러 초기화
  object ClearError : CompanyLoginSharedEvent()

  /** 네비게이션 이벤트 */
  //  이전 페이지
  object PreviousPage : CompanyLoginSharedEvent()
}