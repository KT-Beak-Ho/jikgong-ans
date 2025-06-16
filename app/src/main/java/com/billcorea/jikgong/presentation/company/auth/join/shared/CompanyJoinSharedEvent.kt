package com.billcorea.jikgong.presentation.company.auth.join.shared

sealed class CompanyJoinSharedEvent {
  // Page 1 이벤트
  data class UpdatePhoneNumber(val phoneNumber: String) : CompanyJoinSharedEvent()  //  사용자 휴대폰 번호 입력
  data class UpdateVerificationCode(val code: String) : CompanyJoinSharedEvent()    //  사용자 인증 번호 입력
  object RequestVerificationCode : CompanyJoinSharedEvent()                         //  인증번호 검증

  // Page 2 이벤트
  data class UpdateUserName(val name: String) : CompanyJoinSharedEvent()                  // 사용자 이름 입력
  data class UpdateUserId(val id:String): CompanyJoinSharedEvent()                        // 사용자 ID 입력
  data class UpdateUserPassword(val password: String) : CompanyJoinSharedEvent()          // 사용자 비밀번호 입력
  data class UpdateUserPasswordConfirm(val password: String) : CompanyJoinSharedEvent()   // 사용자 비밀번호 입력확인
  data class UpdateUserMail(val email: String) : CompanyJoinSharedEvent()                 // 사용자 Mail 입력
  data class UpdateBusinessNumber(val businessNumber: String) : CompanyJoinSharedEvent()  // 사업자 등록번호
  data class UpdateCompanyName(val companyName: String) : CompanyJoinSharedEvent()        // 회사명
  data class UpdateInquiry(val inquiry: String) : CompanyJoinSharedEvent()                // 문의 사항

  // 네비게이션 이벤트
  object NextPage : CompanyJoinSharedEvent()
  object PreviousPage : CompanyJoinSharedEvent()
  object HomePage : CompanyJoinSharedEvent()
  object ClearError : CompanyJoinSharedEvent()

  // 네비게이션 이벤트 - 해당 페이지 입력 값 초기화
  object ResetJoin1Flow : CompanyJoinSharedEvent()
  object ResetJoin2Flow : CompanyJoinSharedEvent()
  object ResetJoin3Flow : CompanyJoinSharedEvent()
}
