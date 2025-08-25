package com.billcorea.jikgong.network.model.company

import com.billcorea.jikgong.network.model.common.*

/**
 * 기업 전체 데이터 모델
 */
data class CompanyData(
  // ===== 기본 정보 =====
  val id: String,
  val loginId: String,
  val password: String? = null,
  val role: UserRole = UserRole.COMPANY,

  // ===== 회사 정보 =====
  val name: String,
  val businessNumber: String,
  val representativeName: String,
  val establishedDate: String,
  val companyType: CompanyType,
  val status: CompanyStatus,
  val statusText: String,

  // ===== 연락처 =====
  val phone: String,
  val email: String,
  val managerName: String,
  val managerPhone: String,
  val fax: String? = null,
  val website: String? = null,

  // ===== 위치 정보 =====
  val region: String,
  val address: String,
  val latitude: Double,
  val longitude: Double,
  val postalCode: String? = null,

  // ===== 규모 정보 =====
  val employeeCount: Int,
  val annualRevenue: Long,
  val capitalAmount: Long? = null,

  // ===== 자격/인증 =====
  val businessLicense: BusinessLicense,
  val constructionLicense: ConstructionLicense? = null,
  val certifications: List<CompanyCertification> = emptyList(),
  val companySeal: String? = null,
  val creditRating: String? = null,

  // ===== 사업 정보 =====
  val businessFields: List<String>,
  val registeredProjects: List<String> = emptyList(),
  val jobRegistrations: List<String> = emptyList(),
  val completedProjectCount: Int = 0,
  val partnerCompanies: List<String> = emptyList(),

  // ===== 인력 관리 =====
  val usedWorkers: List<String> = emptyList(),
  val savedWorkers: List<String> = emptyList(),
  val savedWorkersCount: Int = 0,
  val preferredWorkerTypes: List<JobType> = emptyList(),
  val blacklistedWorkers: List<String> = emptyList(),

  // ===== 평가/신뢰도 =====
  val rating: CompanyRating? = null,

  // ===== 복지/보험 =====
  val providedInsurances: List<InsuranceType> = emptyList(),
  val providedBenefits: List<String> = emptyList(),

  // ===== 재무 정보 =====
  val monthlySavings: Long = 0L,
  val previousMonthGrowth: Int = 0,
  val targetAchievementRate: Int = 0,

  // ===== 통계 =====
  val stats: CompanyStats? = null,

  // ===== 알림 =====
  val notifications: NotificationInfo? = null,
  val deviceToken: String? = null,
  val isNotification: Boolean = true,

  // ===== 동의 정보 =====
  val privacyConsent: Boolean = true,
  val termsConsent: Boolean = true,
  val marketingConsent: Boolean = false,

  // ===== 기타 =====
  val memo: String? = null,
  val isActive: Boolean = true,
  val createdAt: String? = null,
  val updatedAt: String? = null
)

/**
 * 사업자등록증
 */
data class BusinessLicense(
  val number: String,
  val issueDate: String,
  val imageUrl: String? = null,
  val verified: Boolean = false
)

/**
 * 건설업 면허
 */
data class ConstructionLicense(
  val number: String,
  val type: String,
  val grade: String,
  val issueDate: String,
  val expiryDate: String? = null,
  val imageUrl: String? = null
)

/**
 * 기업 인증
 */
data class CompanyCertification(
  val name: String,
  val issuer: String,
  val issueDate: String,
  val expiryDate: String? = null,
  val certificateNumber: String
)

/**
 * 기업 평가
 */
data class CompanyRating(
  val averageScore: Double,
  val totalReviews: Int,
  val wageDelayRate: Double,
  val averagePaymentDays: Int,
  val safetyAccidentRate: Double,
  val reemploymentRate: Double,
  val workEnvironmentScore: Double,
  val communicationScore: Double
)

/**
 * 기업 통계
 */
data class CompanyStats(
  val automatedDocs: StatItem,
  val matchedWorkers: StatItem,
  val completedProjects: StatItem,
  val activeConstructionSites: StatItem
)

/**
 * 통계 항목
 */
data class StatItem(
  val icon: String,
  val label: String,
  val value: Int,
  val unit: String,
  val isActive: Boolean = false,
  val trendText: String,
  val trendPercentage: Double? = null
)

/**
 * 알림 정보
 */
data class NotificationInfo(
  val unreadCount: Int = 0,
  val totalCount: Int = 0,
  val lastNotificationTime: String? = null
)
