package com.billcorea.jikgong.presentation.company.auth.join.shared

sealed class CompanyJoinSharedEvent {
  /**
   * Page 1 이벤트
   */
  //  사용자 휴대폰 번호 입력
  data class UpdatePhoneNumber(val phoneNumber: String) : CompanyJoinSharedEvent()
  //  사용자 인증 번호 입력
  data class UpdateVerificationCode(val code: String) : CompanyJoinSharedEvent()
  //  인증번호 요청
  object RequestVerificationCode : CompanyJoinSharedEvent()
  //  인증번호 확인
  object VerifyCode : CompanyJoinSharedEvent()

  /**
   * Page 2 이벤트
   */
  // 사용자 이름 입력
  data class UpdateUserName(val name: String) : CompanyJoinSharedEvent()
  // 사용자 ID 입력
  data class UpdateUserId(val id:String): CompanyJoinSharedEvent()
  // 사용자 비밀번호 입력
  data class UpdateUserPassword(val password: String) : CompanyJoinSharedEvent()
  // 사용자 비밀번호 입력확인
  data class UpdateUserPasswordConfirm(val password: String) : CompanyJoinSharedEvent()
  // 사용자 Mail 입력
  data class UpdateUserMail(val email: String) : CompanyJoinSharedEvent()
  // 사업자 등록번호
  data class UpdateBusinessNumber(val businessNumber: String) : CompanyJoinSharedEvent()
  // 회사명
  data class UpdateCompanyName(val companyName: String) : CompanyJoinSharedEvent()
  // 문의 사항
  data class UpdateInquiry(val inquiry: String) : CompanyJoinSharedEvent()
  // ID 검증
  object RequestVerificationID: CompanyJoinSharedEvent()
  // Email 검증
  object RequestVerificationEmail: CompanyJoinSharedEvent()

  /**
   * 회원가입 최종 제출
   */
  object SubmitRegistration : CompanyJoinSharedEvent()

  /**
   * 네비게이션 이벤트
   */
  //  다음 페이지
  object NextPage : CompanyJoinSharedEvent()
  //  이전 페이지
  object PreviousPage : CompanyJoinSharedEvent()
  //  홈페이지
  object HomePage : CompanyJoinSharedEvent()

  /**
   * 초기화
   */
  //  page 1 초기화
  object ResetJoin1Flow : CompanyJoinSharedEvent()
  //  page 2 초기화
  object ResetJoin2Flow : CompanyJoinSharedEvent()
  //  page 3 초기화
  object ResetJoin3Flow : CompanyJoinSharedEvent()
  // 에러 초기화
  object ClearError : CompanyJoinSharedEvent()
}
