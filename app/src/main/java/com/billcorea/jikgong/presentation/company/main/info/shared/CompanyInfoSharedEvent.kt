// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/shared/CompanyInfoSharedEvent.kt
package com.billcorea.jikgong.presentation.company.main.info.shared

sealed class CompanyInfoSharedEvent {
    // 회사 정보 로드
    object LoadCompanyInfo : CompanyInfoSharedEvent()

    // 메뉴 아이템 클릭
    data class OnMenuItemClick(val menuId: String) : CompanyInfoSharedEvent()

    // 로그아웃
    object Logout : CompanyInfoSharedEvent()

    // 에러 처리
    object ClearError : CompanyInfoSharedEvent()

    // 로딩 상태 업데이트
    data class UpdateLoading(val isLoading: Boolean) : CompanyInfoSharedEvent()
}