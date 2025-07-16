// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/data/CompanyInfoData.kt
package com.billcorea.jikgong.presentation.company.main.info.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// 회사 정보 데이터 모델
data class CompanyInfoData(
    val companyName: String = "",
    val businessNumber: String = "",
    val savedWorkersCount: Int = 0,
    val profileImageUrl: String? = null
)

// 메뉴 아이템 데이터 모델
data class InfoMenuItem(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val badge: String? = null, // 알림 개수 등
    val onClick: () -> Unit = {}
)

// 기본 메뉴 아이템들
object CompanyInfoMenuItems {
    fun getDefaultMenuItems(): List<InfoMenuItem> {
        return listOf(
            InfoMenuItem(
                id = "saved_workers",
                icon = Icons.Filled.People,
                title = "저장한 인부",
                subtitle = "15명",
                badge = null
            ),
            InfoMenuItem(
                id = "notifications",
                icon = Icons.Filled.Notifications,
                title = "알림 설정",
                subtitle = "푸시 알림, 이메일 알림 설정"
            ),
            InfoMenuItem(
                id = "events",
                icon = Icons.Filled.CalendarToday,
                title = "이벤트",
                subtitle = "진행 중인 이벤트 확인",
                badge = "2"
            ),
            InfoMenuItem(
                id = "announcements",
                icon = Icons.Filled.Campaign,
                title = "공지사항",
                subtitle = "새로운 소식과 업데이트"
            ),
            InfoMenuItem(
                id = "customer_service",
                icon = Icons.Filled.Help,
                title = "고객센터",
                subtitle = "문의하기, FAQ"
            ),
            InfoMenuItem(
                id = "policies",
                icon = Icons.Filled.Description,
                title = "약관 및 정책",
                subtitle = "서비스 이용약관, 개인정보처리방침"
            )
        )
    }
}

// 샘플 데이터
object CompanyInfoSampleData {
    fun getSampleCompanyInfo(): CompanyInfoData {
        return CompanyInfoData(
            companyName = "(주)대한건설",
            businessNumber = "123-45-67890",
            savedWorkersCount = 15,
            profileImageUrl = null
        )
    }
}