package com.billcorea.jikgong.presentation.company.auth.join.shared

//  기업 회원 가입 절차간 입력 받는 모든 사용자 입력 저장
//  각 페이지 에서 모두 참조 가능

data class CompanyJoinSharedUiState(
    // Page 1: 전화 번호 인증
    val phoneNumber: String = "",               //  전화번호
    val verificationCode: String = "",          //  인증코드(사용자 입력)
    val isValidPhoneNumber: Boolean = false,    //  전화번호 양식 일치
    val isPhoneVerified: Boolean = false,       //  전화번호 검증 완료
    val isSecurityStepActive: Boolean = false,  //  인증절차 단계
    val isWaiting: Boolean = false,             //  인증 진행중

    // Page 2: 기본 정보
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    // Page 3: 주소 정보
    val zipCode: String = "",
    val address: String = "",
    val detailAddress: String = "",

    // Page 4: 직업 정보
    val jobCategory: String = "",
    val experience: String = "",
    val skills: List<String> = emptyList(),
    val preferredWorkTime: String = "",

    // Page 5: 추가 정보
    val profileImage: String? = null,
    val introduction: String = "",
    val portfolioUrls: List<String> = emptyList(),

    // Page 6: 약관 동의
    val agreeToTerms: Boolean = false,
    val agreeToPrivacy: Boolean = false,
    val agreeToMarketing: Boolean = false,

    // 공통 상태
    val currentPage: Int = 1,
    val errorMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap(),

) {
    // 각 페이지 별 완료 상태 확인
    val isPage1Complete: Boolean
        get() = isPhoneVerified && phoneNumber.isNotEmpty()

    val isPage2Complete: Boolean
        get() = name.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                password == passwordConfirm

    val isPage3Complete: Boolean
        get() = address.isNotEmpty() && detailAddress.isNotEmpty()

    val isPage4Complete: Boolean
        get() = jobCategory.isNotEmpty() && experience.isNotEmpty()

    val isPage5Complete: Boolean
        get() = true // 선택사항이므로 항상 true

    val isPage6Complete: Boolean
        get() = agreeToTerms && agreeToPrivacy

    // 전체 완료 상태
    val isAllDataComplete: Boolean
        get() = isPage1Complete &&
                isPage2Complete &&
                isPage3Complete &&
                isPage4Complete &&
                isPage5Complete &&
                isPage6Complete
}