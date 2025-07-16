// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/shared/CompanyInfoSharedUiState.kt
package com.billcorea.jikgong.presentation.company.main.info.shared

import com.billcorea.jikgong.presentation.company.main.info.data.CompanyInfoData
import com.billcorea.jikgong.presentation.company.main.info.data.InfoMenuItem

data class CompanyInfoSharedUiState(
    // 회사 정보
    val companyInfo: CompanyInfoData = CompanyInfoData(),

    // 메뉴 아이템들
    val menuItems: List<InfoMenuItem> = emptyList(),

    // 로딩 상태
    val isLoading: Boolean = false,

    // 에러 메시지
    val errorMessage: String? = null,

    // 네비게이션 상태
    val shouldNavigateToLogin: Boolean = false,
    val selectedMenuId: String? = null
) {
    // 데이터 로드 완료 상태
    val isDataLoaded: Boolean
        get() = companyInfo.companyName.isNotEmpty() && menuItems.isNotEmpty()
}