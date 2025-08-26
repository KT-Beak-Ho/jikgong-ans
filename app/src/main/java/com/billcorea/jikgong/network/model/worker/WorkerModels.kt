// ============================================
// WorkerModels.kt - Worker 관련 모델 (완전판 v2)
// ============================================
package com.billcorea.jikgong.network.model.worker

import com.billcorea.jikgong.network.model.common.*
import com.google.gson.annotations.SerializedName

// ===== Worker 기본 데이터 (기존 유지) =====
data class WorkerData(
  val id: String? = null,
  val loginId: String,
  val password: String? = null,
  val role: UserRole = UserRole.WORKER,
  val workerName: String,
  val birth: String,
  val gender: Gender,
  val nationality: String = "한국",
  val age: Int? = null,
  val profileImageUrl: String? = null,
  val phone: String,
  val email: String,
  val emergencyContact: String? = null,
  val emergencyContactName: String? = null,
  val address: String,
  val detailAddress: String? = null,
  val latitude: Double,
  val longitude: Double,
  val postalCode: String? = null,
  val accountHolder: String,
  val account: String,
  val bank: String,
  val workerCardNumber: String? = null,
  val hasVisa: Boolean = false,
  val visaExpiryDate: String? = null,
  val certificates: List<Certificate> = emptyList(),
  val safetyEducation: SafetyEducation? = null,
  val driverLicense: DriverLicense? = null,
  val workExperienceRequest: List<WorkExperience>,
  val totalExperienceMonths: Int? = null,
  val mainJobType: JobType? = null,
  val preferredAreas: List<String> = emptyList(),
  val preferredJobTypes: List<JobType> = emptyList(),
  val desiredWage: WageInfo? = null,
  val availableStartDate: String? = null,
  val preferredWorkType: WorkType = WorkType.DAILY,
  val hasVehicle: Boolean = false,
  val isScoutRegistered: Boolean = false,
  val scoutProfile: ScoutProfile? = null,
  val totalWorkDays: Int = 0,
  val completedProjects: Int = 0,
  val ongoingProjects: Int = 0,
  val lastWorkDate: String? = null,
  val totalEarnings: Long = 0L,
  val rating: WorkerRating? = null,
  val skills: List<String> = emptyList(),
  val languages: List<Language> = emptyList(),
  val healthStatus: HealthStatus? = null,
  val hasHealthCertificate: Boolean = false,
  val healthCertificateDate: String? = null,
  val accidentHistory: List<AccidentRecord> = emptyList(),
  val deviceToken: String? = null,
  val isNotification: Boolean = true,
  val notificationSettings: NotificationSettings? = null,
  val privacyConsent: Boolean = true,
  val credentialLiabilityConsent: Boolean = true,
  val termsConsent: Boolean = true,
  val marketingConsent: Boolean = false,
  val blacklistedCompanies: List<String> = emptyList(),
  val favoriteCompanies: List<String> = emptyList(),
  val memo: String? = null,
  val isActive: Boolean = true,
  val createdAt: String? = null,
  val updatedAt: String? = null
)

// ===== 등록 요청/응답 =====
data class RegisterWorkerRequest(
  val loginId: String,
  val password: String,
  val phone: String,
  val email: String,
  val role: String = "WORKER",
  val privacyConsent: Boolean,
  val deviceToken: String,
  val isNotification: Boolean,
  val workerName: String,
  val birth: String,
  val gender: String,
  val nationality: String,
  val accountHolder: String,
  val account: String,
  val bank: String,
  val workerCardNumber: String,
  val hasVisa: Boolean,
  val credentialLiabilityConsent: Boolean,
  val workExperienceRequest: List<WorkExperience>,
  val address: String,
  val latitude: Double,
  val longitude: Double
)

data class RegisterWorkerResponse(
  val code: String,
  val message: String,
  val data: RegisterWorkerData? = null
)

data class RegisterWorkerData(
  val workerId: String,
  val loginId: String,
  val name: String
)

data class RegisterWorkerErrorResponse(
  val code: String,
  val message: String,
  val errors: List<FieldError>? = null
)

// ===== 추천 근로자 =====
data class RecommendedWorker(
  @SerializedName("worker")
  val worker: WorkerData,

  @SerializedName("recommendationScore")
  val recommendationScore: Double,

  @SerializedName("reasons")
  val reasons: List<String>,

  @SerializedName("matchingFactors")
  val matchingFactors: Map<String, Double>
)

// ===== 근로자 프로필 =====
data class WorkerProfile(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("basicInfo")
  val basicInfo: BasicWorkerInfo,

  @SerializedName("workInfo")
  val workInfo: WorkInfo,

  @SerializedName("skills")
  val skills: List<Skill>,

  @SerializedName("portfolio")
  val portfolio: List<PortfolioItem>,

  @SerializedName("introduction")
  val introduction: String?,

  @SerializedName("completedProjects")
  val completedProjects: Int,

  @SerializedName("totalWorkDays")
  val totalWorkDays: Int
)

data class BasicWorkerInfo(
  val name: String,
  val age: Int,
  val gender: Gender,
  val phone: String,
  val email: String,
  val address: String,
  val profileImageUrl: String?
)

data class WorkInfo(
  val mainJobType: JobType,
  val experienceYears: Int,
  val desiredWage: Long,
  val availableStartDate: String,
  val preferredAreas: List<String>,
  val hasVehicle: Boolean,
  val canStayOnSite: Boolean
)

data class Skill(
  val name: String,
  val level: SkillLevel,
  val yearsOfExperience: Int
)

enum class SkillLevel {
  BEGINNER,
  INTERMEDIATE,
  ADVANCED,
  EXPERT
}

data class PortfolioItem(
  val id: String,
  val title: String,
  val description: String,
  val imageUrls: List<String>,
  val projectDate: String
)

// ===== 사진 업데이트 응답 =====
data class PhotoUpdateResponse(
  @SerializedName("photoUrl")
  val photoUrl: String,

  @SerializedName("uploadedAt")
  val uploadedAt: String
)

// ===== 공개 프로필 =====
data class PublicWorkerProfile(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("jobType")
  val jobType: JobType,

  @SerializedName("experienceYears")
  val experienceYears: Int,

  @SerializedName("rating")
  val rating: Double,

  @SerializedName("completedProjects")
  val completedProjects: Int,

  @SerializedName("introduction")
  val introduction: String?,

  @SerializedName("skills")
  val skills: List<String>,

  @SerializedName("certifications")
  val certifications: List<String>,

  @SerializedName("isVerified")
  val isVerified: Boolean
)

// ===== 스카우트 프로필 데이터 =====
data class ScoutProfileData(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("isPublic")
  val isPublic: Boolean,

  @SerializedName("title")
  val title: String,

  @SerializedName("introduction")
  val introduction: String,

  @SerializedName("requiredBenefits")
  val requiredBenefits: List<String>,

  @SerializedName("preferredCompanySize")
  val preferredCompanySize: String?,

  @SerializedName("portfolioUrls")
  val portfolioUrls: List<String>,

  @SerializedName("viewCount")
  val viewCount: Int,

  @SerializedName("contactCount")
  val contactCount: Int
)

// ===== 자격증 검증 =====
data class CertificateVerification(
  @SerializedName("certificateId")
  val certificateId: String,

  @SerializedName("certificateNumber")
  val certificateNumber: String,

  @SerializedName("issuerCode")
  val issuerCode: String
)

// ===== 근로자 활동 =====
data class WorkerActivity(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("date")
  val date: String,

  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("projectName")
  val projectName: String,

  @SerializedName("workHours")
  val workHours: Double,

  @SerializedName("wage")
  val wage: Long,

  @SerializedName("status")
  val status: String
)

// ===== 근로자 리뷰 =====
data class WorkerReviewRequest(
  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("rating")
  val rating: Int,

  @SerializedName("punctualityScore")
  val punctualityScore: Int,

  @SerializedName("skillScore")
  val skillScore: Int,

  @SerializedName("attitudeScore")
  val attitudeScore: Int,

  @SerializedName("safetyScore")
  val safetyScore: Int,

  @SerializedName("comment")
  val comment: String
)

// ===== 근로자 성과 =====
data class WorkerPerformance(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("period")
  val period: String,

  @SerializedName("attendanceRate")
  val attendanceRate: Double,

  @SerializedName("completionRate")
  val completionRate: Double,

  @SerializedName("qualityScore")
  val qualityScore: Double,

  @SerializedName("safetyIncidents")
  val safetyIncidents: Int,

  @SerializedName("commendations")
  val commendations: Int
)

// ===== 근로자 통계 =====
data class WorkerStatistics(
  @SerializedName("totalWorkDays")
  val totalWorkDays: Int,

  @SerializedName("totalProjects")
  val totalProjects: Int,

  @SerializedName("totalEarnings")
  val totalEarnings: Long,

  @SerializedName("averageDailyWage")
  val averageDailyWage: Long,

  @SerializedName("mostWorkedJobType")
  val mostWorkedJobType: String,

  @SerializedName("yearlyStats")
  val yearlyStats: List<YearlyWorkerStats>
)

data class YearlyWorkerStats(
  val year: Int,
  val workDays: Int,
  val projects: Int,
  val earnings: Long
)

// ===== 수입 통계 =====
data class IncomeStatistics(
  @SerializedName("period")
  val period: String,

  @SerializedName("totalIncome")
  val totalIncome: Long,

  @SerializedName("regularIncome")
  val regularIncome: Long,

  @SerializedName("overtimeIncome")
  val overtimeIncome: Long,

  @SerializedName("bonusIncome")
  val bonusIncome: Long,

  @SerializedName("deductions")
  val deductions: Long,

  @SerializedName("netIncome")
  val netIncome: Long
)

// ===== 근무 이력 =====
data class WorkHistory(
  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("projectName")
  val projectName: String,

  @SerializedName("companyName")
  val companyName: String,

  @SerializedName("startDate")
  val startDate: String,

  @SerializedName("endDate")
  val endDate: String?,

  @SerializedName("jobType")
  val jobType: String,

  @SerializedName("totalDays")
  val totalDays: Int,

  @SerializedName("totalEarnings")
  val totalEarnings: Long,

  @SerializedName("evaluation")
  val evaluation: Double?
)

// ===== 근로자 선호 설정 =====
data class WorkerPreferences(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("preferredJobTypes")
  val preferredJobTypes: List<String>,

  @SerializedName("preferredLocations")
  val preferredLocations: List<String>,

  @SerializedName("minWage")
  val minWage: Long,

  @SerializedName("maxDistance")
  val maxDistance: Int,

  @SerializedName("workDays")
  val workDays: List<String>,

  @SerializedName("startTime")
  val startTime: String,

  @SerializedName("endTime")
  val endTime: String,

  @SerializedName("avoidCompanies")
  val avoidCompanies: List<String>
)

// ===== 근로자 문서 =====
data class WorkerDocument(
  @SerializedName("id")
  val id: String,

  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("type")
  val type: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("url")
  val url: String,

  @SerializedName("uploadedAt")
  val uploadedAt: String,

  @SerializedName("expiryDate")
  val expiryDate: String?,

  @SerializedName("verified")
  val verified: Boolean
)

// ===== 블랙리스트 관련 =====
data class BlacklistStatus(
  @SerializedName("isBlacklisted")
  val isBlacklisted: Boolean,

  @SerializedName("reason")
  val reason: String?,

  @SerializedName("blacklistedBy")
  val blacklistedBy: String?,

  @SerializedName("blacklistedAt")
  val blacklistedAt: String?
)

data class BlacklistCheckResult(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("isBlacklisted")
  val isBlacklisted: Boolean,

  @SerializedName("blacklistCount")
  val blacklistCount: Int,

  @SerializedName("companies")
  val companies: List<String>
)

// ===== 기존 모델들 (유지) =====
data class WorkExperience(
  val id: String? = null,
  val tech: String,
  val experienceMonths: Int,
  val description: String? = null,
  val companyName: String? = null,
  val projectName: String? = null,
  val startDate: String? = null,
  val endDate: String? = null
)

data class Certificate(
  val id: String? = null,
  val name: String,
  val issuer: String,
  val issueDate: String,
  val expiryDate: String? = null,
  val certificateNumber: String,
  val imageUrl: String? = null,
  val verified: Boolean = false
)

data class SafetyEducation(
  val completedDate: String,
  val expiryDate: String,
  val certificateNumber: String,
  val institution: String
)

data class DriverLicense(
  val type: String,
  val issueDate: String,
  val expiryDate: String,
  val licenseNumber: String
)

data class WageInfo(
  val type: WageType,
  val amount: Long,
  val negotiable: Boolean = false,
  val includeOvertime: Boolean = false
)

data class Language(
  val language: String,
  val proficiency: LanguageProficiency
)

enum class LanguageProficiency {
  BASIC,
  INTERMEDIATE,
  ADVANCED,
  NATIVE
}

data class HealthStatus(
  val level: HealthLevel,
  val fitnessLevel: FitnessLevel,
  val lastCheckupDate: String? = null,
  val restrictions: List<String> = emptyList(),
  val hasChronicDisease: Boolean = false
)

enum class HealthLevel {
  EXCELLENT,
  GOOD,
  NORMAL,
  POOR
}

enum class FitnessLevel {
  VERY_FIT,
  FIT,
  AVERAGE,
  BELOW_AVERAGE
}

data class AccidentRecord(
  val date: String,
  val type: String,
  val severity: String,
  val description: String,
  val compensationReceived: Boolean = false
)

data class NotificationSettings(
  val jobMatch: Boolean = true,
  val payment: Boolean = true,
  val urgent: Boolean = true,
  val chat: Boolean = true,
  val marketing: Boolean = false,
  val nightTime: Boolean = false,
  val weekend: Boolean = true
)

data class WorkerRating(
  val averageScore: Double,
  val totalReviews: Int,
  val punctualityScore: Double,
  val skillScore: Double,
  val attitudeScore: Double,
  val safetyScore: Double
)

data class ScoutProfile(
  val isPublic: Boolean,
  val title: String,
  val introduction: String,
  val requiredBenefits: List<String> = emptyList(),
  val preferredCompanySize: String? = null,
  val portfolioUrls: List<String> = emptyList()
)

enum class WorkerAccountStatus {
  ACTIVE,
  INACTIVE,
  SUSPENDED,
  VACATION,
  DELETED
}

data class WorkerVerification(
  val idNumber: String,
  val name: String,
  val phone: String,
  val verificationCode: String?
)

data class WorkerStatusUpdate(
  val status: WorkerAccountStatus,
  val reason: String?,
  val effectiveDate: String?
)