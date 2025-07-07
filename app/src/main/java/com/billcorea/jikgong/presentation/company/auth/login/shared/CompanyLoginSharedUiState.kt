package com.billcorea.jikgong.presentation.company.auth.login.shared

data class CompanyLoginSharedUiState (
  /** Page 1: 로그인 */
  val id:String = "",         //  로그인 ID
  val password:String = "",   //  로그인 PWD
  /** 공통 상태 */
  val currentPage: Int = 1,
  val errorMessage: String? = null,
  val validationErrors: Map<String, String> = emptyMap(),
){
  /** 각 페이지 별 완료 상태 확인 */
  val isPage1Complete: Boolean
    get() = true
}
