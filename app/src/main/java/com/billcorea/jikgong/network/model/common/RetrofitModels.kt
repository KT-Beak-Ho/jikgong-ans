package com.billcorea.jikgong.network.model.common

/**
 * RetrofitAPI에서 사용하는 추가 모델들
 * 다른 도메인 모델에 속하지 않는 공통/유틸리티 모델
 *
 * 주의: 다음 모델들은 이미 다른 파일에 정의되어 있으므로 제외
 * - DefaultResponse (AuthModels.kt)
 * - EmailValidationRequest, EmailValidationResponse (AuthModels.kt)
 * - VisaExpiryDateRequest (AuthModels.kt 또는 WorkerModels.kt)
 * - RegionData (LocationModels.kt)
 */

// ============================================
// 좌표 변환 응답
// ============================================
data class CoordinateConversionResponse(
  val meta: Meta,
  val documents: List<ConversionDocument>
)

data class ConversionDocument(
  val x: Double,
  val y: Double
)

data class Meta(
  val total_count: Int
)

// ============================================
// 키워드 검색 응답 (Kakao)
// ============================================
data class KeywordSearchResponse(
  val meta: KeywordSearchMeta,
  val documents: List<KeywordDocument>
)

data class KeywordSearchMeta(
  val total_count: Int,
  val pageable_count: Int,
  val is_end: Boolean,
  val same_name: RegionInfo?
)

data class RegionInfo(
  val region: List<String>,
  val keyword: String,
  val selected_region: String
)

data class KeywordDocument(
  val id: String,
  val place_name: String,
  val category_name: String,
  val category_group_code: String,
  val category_group_name: String,
  val phone: String,
  val address_name: String,
  val road_address_name: String,
  val x: String,
  val y: String,
  val place_url: String,
  val distance: String
)

// ============================================
// 회사 등록 응답
// ============================================
data class RegisterCompanyResponse(
  val code: String,
  val message: String,
  val data: RegisterCompanyData? = null
)

data class RegisterCompanyData(
  val companyId: String,
  val loginId: String,
  val companyName: String
)

// ============================================
// 파일 관련
// ============================================
data class FileUploadResponse(
  val fileId: String,
  val fileName: String,
  val fileUrl: String,
  val fileSize: Long,
  val mimeType: String,
  val uploadedAt: String
)

data class FileData(
  val fileId: String,
  val fileName: String,
  val fileUrl: String,
  val fileSize: Long,
  val mimeType: String,
  val uploadedAt: String,
  val uploadedBy: String
)

// ============================================
// 앱 설정 관련
// ============================================
data class AppVersionInfo(
  val currentVersion: String,
  val minimumVersion: String,
  val latestVersion: String,
  val updateUrl: String,
  val forceUpdate: Boolean,
  val releaseNotes: String,
  val releasedAt: String
)

data class TermsData(
  val type: String,
  val title: String,
  val content: String,
  val version: String,
  val effectiveDate: String,
  val updatedAt: String
)

data class NoticeData(
  val id: String,
  val title: String,
  val content: String,
  val type: NoticeType,
  val isImportant: Boolean,
  val isPinned: Boolean,
  val viewCount: Int,
  val attachments: List<String>?,
  val createdAt: String,
  val updatedAt: String
)

enum class NoticeType {
  GENERAL,
  EVENT,
  UPDATE,
  MAINTENANCE,
  URGENT
}

data class BannerData(
  val id: String,
  val title: String,
  val imageUrl: String,
  val linkUrl: String?,
  val position: BannerPosition,
  val priority: Int,
  val startDate: String,
  val endDate: String,
  val isActive: Boolean
)

enum class BannerPosition {
  HOME_TOP,
  HOME_MIDDLE,
  HOME_BOTTOM,
  WORKER_MAIN,
  COMPANY_MAIN
}

data class FAQData(
  val id: String,
  val category: FAQCategory,
  val question: String,
  val answer: String,
  val viewCount: Int,
  val isHelpful: Int,
  val createdAt: String
)

enum class FAQCategory {
  GENERAL,
  ACCOUNT,
  PAYMENT,
  MATCHING,
  PROJECT,
  TECHNICAL
}

// ============================================
// 푸시 알림 관련
// ============================================
data class PushTokenRequest(
  val token: String,
  val deviceType: DeviceType,
  val deviceId: String
)

enum class DeviceType {
  ANDROID,
  IOS,
  WEB
}

data class NotificationData(
  val id: String,
  val type: NotificationType,
  val title: String,
  val message: String,
  val data: Map<String, String>?,
  val isRead: Boolean,
  val createdAt: String
)

enum class NotificationType {
  GENERAL,
  MATCHING,
  PAYMENT,
  PROJECT,
  URGENT,
  SYSTEM
}

// ============================================
// 지역 관련 (LocationModels와 충돌 방지)
// ============================================
data class RegionApiData(  // 이름 변경
  val code: String,
  val name: String,
  val level: Int,
  val parentCode: String?,
  val fullName: String,
  val shortName: String,
  val latitude: Double?,
  val longitude: Double?
)

// ============================================
// 통계 관련
// ============================================
data class DashboardStats(
  val userType: String,
  val period: String,
  val summary: DashboardSummary,
  val charts: DashboardCharts,
  val recentActivities: List<ActivityData>
)

data class DashboardSummary(
  val totalProjects: Int,
  val activeProjects: Int,
  val totalEarnings: Long,
  val monthlyEarnings: Long,
  val totalWorkDays: Int,
  val thisMonthWorkDays: Int,
  val averageRating: Double,
  val completionRate: Double
)

data class DashboardCharts(
  val earningsChart: List<ChartDataPoint>,
  val projectChart: List<ChartDataPoint>,
  val performanceChart: List<ChartDataPoint>
)

data class ChartDataPoint(
  val label: String,
  val value: Double,
  val date: String?
)

data class ActivityData(
  val id: String,
  val type: String,
  val title: String,
  val description: String,
  val timestamp: String
)

data class MarketStats(
  val totalJobs: Int,
  val totalWorkers: Int,
  val averageWage: Long,
  val demandIndex: Double,
  val supplyIndex: Double,
  val topJobTypes: List<JobTypeMarketData>,
  val regionalStats: List<RegionalMarketData>
)

data class JobTypeMarketData(
  val jobType: String,
  val jobCount: Int,
  val workerCount: Int,
  val averageWage: Long,
  val demandLevel: String
)

data class RegionalMarketData(
  val region: String,
  val jobCount: Int,
  val workerCount: Int,
  val averageWage: Long,
  val growthRate: Double
)