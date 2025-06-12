package com.billcorea.jikgong.presentation.company.auth.join.shared

sealed class CompanyJoinSharedEvent {
    // Page 1 이벤트
    data class UpdatePhoneNumber(val phoneNumber: String) : CompanyJoinSharedEvent()
    data class UpdateVerificationCode(val code: String) : CompanyJoinSharedEvent()
    object RequestVerificationCode : CompanyJoinSharedEvent()
    object VerifyPhoneNumber : CompanyJoinSharedEvent()

    // Page 2 이벤트 (추후 확장)
    data class UpdateCompanyName(val companyName: String) : CompanyJoinSharedEvent()
    data class UpdateBusinessNumber(val businessNumber: String) : CompanyJoinSharedEvent()
    data class UpdateRepresentativeName(val name: String) : CompanyJoinSharedEvent()
    data class UpdateEmail(val email: String) : CompanyJoinSharedEvent()

    // Page 3 이벤트 (추후 확장)
    data class UpdateCompanyAddress(val address: String) : CompanyJoinSharedEvent()
    data class UpdateDetailAddress(val detailAddress: String) : CompanyJoinSharedEvent()
    object SearchCompanyAddress : CompanyJoinSharedEvent()

    // Page 4 이벤트 (추후 확장)
    data class UpdateBusinessType(val businessType: String) : CompanyJoinSharedEvent()
    data class UpdateEmployeeCount(val count: String) : CompanyJoinSharedEvent()
    data class UpdateEstablishedYear(val year: String) : CompanyJoinSharedEvent()

    // Page 5 이벤트 (추후 확장)
    data class UpdateCompanyDescription(val description: String) : CompanyJoinSharedEvent()
    data class UpdateWebsite(val website: String) : CompanyJoinSharedEvent()

    // Page 6 이벤트 (추후 확장)
    data class UpdateTermsAgreement(val agree: Boolean) : CompanyJoinSharedEvent()
    data class UpdatePrivacyAgreement(val agree: Boolean) : CompanyJoinSharedEvent()
    data class UpdateMarketingAgreement(val agree: Boolean) : CompanyJoinSharedEvent()
    object SubmitCompanyJoinData : CompanyJoinSharedEvent()

    // 네비게이션 이벤트
    object NextPage : CompanyJoinSharedEvent()
    object PreviousPage : CompanyJoinSharedEvent()
    data class NavigateToPage(val page: Int) : CompanyJoinSharedEvent()
    object ClearError : CompanyJoinSharedEvent()
    // 네비게이션 이벤트 - 해당 페이지 입력 값 초기화
    object ResetJoin1Flow : CompanyJoinSharedEvent()
}

sealed class CompanyJoinNavigationEvent {
    data class NavigateToPage(val page: Int) : CompanyJoinNavigationEvent()
    data class JoinSuccess(val companyId: String) : CompanyJoinNavigationEvent()
    object NavigateBack : CompanyJoinNavigationEvent()
}