package com.billcorea.jikgong.network

import java.time.LocalDateTime

/**
 * 회사 정보 데이터 모델
 * network 폴더에 다른 데이터 모델들과 함께 위치
 */
data class CompanyData(
  val id: String = "company_001",
  val name: String = "김직공건설",
  val type: CompanyType = CompanyType.PREMIUM,
  val status: CompanyStatus = CompanyStatus.ACTIVE,
  val statusText: String = "기업회원 • 활성 사용자",

  // 재무 정보
  val monthlySavings: Long = 3540000L,
  val previousMonthGrowth: Int = 28,
  val targetAchievementRate: Int = 112,

  // 통계
  val stats: CompanyStats = CompanyStats(),

  // 인력 관리
  val savedWorkersCount: Int = 32,

  // 알림
  val notifications: NotificationInfo = NotificationInfo(),

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class CompanyStats(
  val automatedDocs: StatItem = StatItem(
    icon = "📄",
    label = "서류 자동화",
    value = 312,
    unit = "건",
    trendText = "100%"
  ),
  val matchedWorkers: StatItem = StatItem(
    icon = "👷",
    label = "매칭 인력",
    value = 156,
    unit = "명",
    trendText = "98.5%"
  ),
  val completedProjects: StatItem = StatItem(
    icon = "✅",
    label = "완료 프로젝트",
    value = 23,
    unit = "개",
    trendText = "100%"
  ),
  val activeConstructionSites: StatItem = StatItem(
    icon = "🏗️",
    label = "시공 현장",
    value = 8,
    unit = "곳",
    isActive = true,
    trendText = "활성"
  )
)

data class StatItem(
  val icon: String,
  val label: String,
  val value: Int,
  val unit: String,
  val isActive: Boolean = false,
  val trendText: String
)

data class NotificationInfo(
  val unreadCount: Int = 3,
  val totalCount: Int = 15
)

enum class CompanyType { BASIC, PREMIUM, ENTERPRISE }
enum class CompanyStatus { ACTIVE, INACTIVE, SUSPENDED }