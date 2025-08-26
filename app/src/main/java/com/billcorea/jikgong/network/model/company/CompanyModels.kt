// ============================================
// CompanyModels.kt - 기업 관련 모델 (RegisterCompanyResponse 추가)
// ============================================
package com.billcorea.jikgong.network.model.company

import com.billcorea.jikgong.network.model.common.*
import com.google.gson.annotations.SerializedName

// ===== 기업 기본 데이터 =====
data class CompanyData(
  val id: String? = null,
  val loginId: String,
  val password: String? = null,
  val role: UserRole = UserRole.COMPANY,
  val companyName: String,
  val businessNumber: String,
  val representativeName: String,
  val businessType: String,
  val businessCategory: String,
  val establishedDate: String,
  val employeeCount: Int? = null,
  val phone: String,
  val email: String,
  val fax: String? = null,
  val website: String? = null,
  val address: String,
  val detailAddress: String? = null,
  val latitude: Double,
  val longitude: Double,
  val postalCode: String? = null,
  val businessLicense: BusinessLicense,
  val constructionLicense: ConstructionLicense? = null,
  val certifications: List<CompanyCertification> = emptyList(),
  val rating: CompanyRating? = null,
  val description: String? = null,
  val logoUrl: String? = null,
  val bankName: String,
  val bankAccount: String,
  val accountHolder: String,
  val stats: CompanyStats? = null,
  val notifications: NotificationInfo? = null,
  val totalProjects: Int = 0,
  val ongoingProjects: Int = 0,
  val completedProjects: Int = 0,
  val totalHiredWorkers: Int = 0,
  val deviceToken: String? = null,
  val isNotification: Boolean = true,
  val privacyConsent: Boolean = true,
  val termsConsent: Boolean = true,
  val marketingConsent: Boolean = false,
  val memo: String? = null,
  val isActive: Boolean = true,
  val createdAt: String? = null,
  val updatedAt: String? = null
)

// ===== 기업 등록 응답 =====
data class RegisterCompanyResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("data")
  val data: RegisterCompanyData? = null
)

data class RegisterCompanyData(
  @SerializedName("companyId")
  val companyId: String,

  @SerializedName("loginId")
  val loginId: String,

  @SerializedName("companyName")
  val companyName: String,

  @SerializedName("businessNumber")
  val businessNumber: String
)

// ===== 사업자등록증 =====
data class BusinessLicense(
  val number: String,
  val issueDate: String,
  val imageUrl: String? = null,
  val verified: Boolean = false
)

// ===== 건설업 면허 =====
data class ConstructionLicense(
  val number: String,
  val type: String,
  val grade: String,
  val issueDate: String,
  val expiryDate: String? = null,
  val imageUrl: String? = null
)

// ===== 기업 인증 =====
data class CompanyCertification(
  val name: String,
  val issuer: String,
  val issueDate: String,
  val expiryDate: String? = null,
  val certificateNumber: String
)

// ===== 기업 평가 =====
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

// ===== 기업 통계 =====
data class CompanyStats(
  val automatedDocs: StatItem,
  val matchedWorkers: StatItem,
  val completedProjects: StatItem,
  val activeConstructionSites: StatItem
)

data class StatItem(
  val icon: String,
  val label: String,
  val value: Int,
  val unit: String,
  val isActive: Boolean = false,
  val trendText: String,
  val trendPercentage: Double? = null
)

// ===== 알림 정보 =====
data class NotificationInfo(
  val unreadCount: Int = 0,
  val totalCount: Int = 0,
  val lastNotificationTime: String? = null
)

// ===== CompanyAPI에서 사용되는 추가 모델들 =====
data class CompanyVerification(
  val businessNumber: String,
  val representativeName: String,
  val verificationCode: String
)

data class BlacklistRequest(
  val workerId: String,
  val reason: String,
  val incidentDate: String?
)

data class BlacklistedWorker(
  val workerId: String,
  val workerName: String,
  val reason: String,
  val blacklistedDate: String,
  val incidentDate: String?
)

data class FinancialStats(
  val revenue: Long,
  val expense: Long,
  val profit: Long,
  val projectCosts: Long,
  val laborCosts: Long,
  val growthRate: Double,
  val profitMargin: Double
)

data class PerformanceMetrics(
  val totalProjects: Int,
  val completedProjects: Int,
  val ongoingProjects: Int,
  val completionRate: Double,
  val averageProjectDuration: Int,
  val workerSatisfactionRate: Double,
  val clientSatisfactionRate: Double,
  val revenuePerProject: Long
)

data class CompanyReviewRequest(
  val rating: Int,
  val title: String,
  val content: String,
  val projectId: String?,
  val isAnonymous: Boolean
)

data class CompanyReview(
  val id: String,
  val companyId: String,
  val reviewerId: String,
  val reviewerName: String?,
  val rating: Int,
  val title: String,
  val content: String,
  val projectId: String?,
  val isAnonymous: Boolean,
  val createdAt: String
)

data class CompanyDocument(
  val id: String,
  val type: String,
  val name: String,
  val url: String,
  val description: String?,
  val uploadedAt: String,
  val expiryDate: String?
)

data class CompanyNotificationSettings(
  val newProject: Boolean,
  val workerApplication: Boolean,
  val paymentDue: Boolean,
  val documentExpiry: Boolean,
  val reviewReceived: Boolean,
  val emergencyAlert: Boolean
)

data class PartnerCompany(
  val id: String,
  val name: String,
  val businessNumber: String,
  val partnershipType: String,
  val startDate: String
)