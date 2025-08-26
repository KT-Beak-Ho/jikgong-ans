package com.billcorea.jikgong.network.model.worker

import com.billcorea.jikgong.network.model.common.*

/**
 * 노동자 관련 모델 (완전판)
 */

// ============================================
// 노동자 전체 데이터 모델
// ============================================
data class WorkerData(
  // ===== 기본 정보 =====
  val id: String? = null,
  val loginId: String,
  val password: String? = null,
  val role: UserRole = UserRole.WORKER,

  // ===== 개인 정보 =====
  val workerName: String,
  val birth: String,
  val gender: Gender,
  val nationality: String = "한국",
  val age: Int? = null,
  val profileImageUrl: String? = null,

  // ===== 연락처 =====
  val phone: String,
  val email: String,
  val emergencyContact: String? = null,
  val emergencyContactName: String? = null,

  // ===== 주소 정보 =====
  val address: String,
  val detailAddress: String? = null,
  val latitude: Double,
  val longitude: Double,
  val postalCode: String? = null,

  // ===== 계좌 정보 =====
  val accountHolder: String,
  val account: String,
  val bank: String,

  // ===== 자격/인증 =====
  val workerCardNumber: String? = null,
  val hasVisa: Boolean = false,
  val visaExpiryDate: String? = null,
  val certificates: List<Certificate> = emptyList(),
  val safetyEducation: SafetyEducation? = null,
  val driverLicense: DriverLicense? = null,

  // ===== 경력 정보 =====
  val workExperienceRequest: List<WorkExperience>,
  val totalExperienceMonths: Int? = null,
  val mainJobType: JobType? = null,

  // ===== 근무 조건 =====
  val preferredAreas: List<String> = emptyList(),
  val preferredJobTypes: List<JobType> = emptyList(),
  val desiredWage: WageInfo? = null,
  val availableStartDate: String? = null,
  val preferredWorkType: WorkType = WorkType.DAILY,
  val hasVehicle: Boolean = false,

  // ===== 스카우트 정보 =====
  val isScoutRegistered: Boolean = false,
  val scoutProfile: ScoutProfile? = null,

  // ===== 활동 이력 =====
  val totalWorkDays: Int = 0,
  val completedProjects: Int = 0,
  val ongoingProjects: Int = 0,
  val lastWorkDate: String? = null,
  val totalEarnings: Long = 0L,

  // ===== 평가 정보 =====
  val rating: WorkerRating? = null,

  // ===== 기술/능력 =====
  val skills: List<String> = emptyList(),
  val languages: List<Language> = emptyList(),

  // ===== 건강/안전 =====
  val healthStatus: HealthStatus? = null,
  val hasHealthCertificate: Boolean = false,
  val healthCertificateDate: String? = null,
  val accidentHistory: List<AccidentRecord> = emptyList(),

  // ===== 알림 설정 =====
  val deviceToken: String? = null,
  val isNotification: Boolean = true,
  val notificationSettings: NotificationSettings? = null,

  // ===== 동의 정보 =====
  val privacyConsent: Boolean = true,
  val credentialLiabilityConsent: Boolean = true,
  val termsConsent: Boolean = true,
  val marketingConsent: Boolean = false,

  // ===== 기타 =====
  val blacklistedCompanies: List<String> = emptyList(),
  val favoriteCompanies: List<String> = emptyList(),
  val memo: String? = null,
  val isActive: Boolean = true,
  val createdAt: String? = null,
  val updatedAt: String? = null
)

// ============================================
// 경력 정보
// ============================================
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

// ============================================
// 자격증 정보
// ============================================
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

// ============================================
// WorkerAPI에서 사용되는 추가 모델들
// ============================================

// 등록 관련
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
  val errors: List<FieldError>?
)

data class FieldError(
  val field: String,
  val message: String
)

// 검증 관련
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

enum class WorkerAccountStatus {
  ACTIVE,
  INACTIVE,
  SUSPENDED,
  VACATION,
  DELETED
}

data class CertificateVerification(
  val certificateId: String,
  val certificateNumber: String,
  val issuerCode: String
)

// 프로필 관련
data class RecommendedWorker(
  val worker: WorkerData,
  val recommendationScore: Double,
  val reasons: List<String>,
  val matchingFactors: Map<String, Double>
)

data class WorkerProfile(
  val workerId: String,
  val basicInfo: BasicWorkerInfo,
  val workInfo: WorkInfo,
  val skills: List<Skill>,
  val portfolio: List<PortfolioItem>,
  val introduction: String?,
  val completedProjects: Int,
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

data class PhotoUpdateResponse(
  val photoUrl: String,
  val uploadedAt: String
)

data class PublicWorkerProfile(
  val workerId: String,
  val name: String,
  val jobType: JobType,
  val experienceYears: Int,
  val rating: Double,
  val completedProjects: Int,
  val introduction: String?,
  val skills: List<String>,
  val certifications: List<String>,
  val isVerified: Boolean
)

// 스카우트 관련
data class ScoutProfile(
  val isPublic: Boolean,
  val title: String,
  val introduction: String,
  val requiredBenefits: List<String> = emptyList(),
  val desiredWage: Long,
  val portfolioUrls: List<String> = emptyList(),
  val availableImmediately: Boolean = false
)

data class ScoutProfileData(
  val workerId: String,
  val worker: WorkerData,
  val scoutProfile: ScoutProfile,
  val viewCount: Int,
  val contactCount: Int,
  val lastActive: String
)

// 평가 관련
data class WorkerRating(
  val averageScore: Double,
  val totalReviews: Int,
  val attendanceRate: Double,
  val punctualityRate: Double,
  val reemploymentRate: Double,
  val skillLevel: Int,
  val teamworkScore: Double,
  val safetyScore: Double
)

data class WorkerReviewRequest(
  val projectId: String,
  val rating: WorkerReviewRating,
  val comment: String?,
  val wouldWorkAgain: Boolean
)

data class WorkerReviewRating(
  val overall: Int,
  val skill: Int,
  val punctuality: Int,
  val attitude: Int,
  val communication: Int
)

data class WorkerPerformance(
  val attendanceRate: Double,
  val punctualityRate: Double,
  val completionRate: Double,
  val reemploymentRate: Double,
  val averageRating: Double,
  val performanceTrend: String,
  val monthlyPerformance: List<MonthlyPerformance>
)

data class MonthlyPerformance(
  val month: String,
  val workDays: Int,
  val rating: Double,
  val earnings: Long
)

// 통계 관련
data class WorkerStatistics(
  val totalWorkDays: Int,
  val totalProjects: Int,
  val totalEarnings: Long,
  val averageMonthlyEarnings: Long,
  val topJobTypes: List<JobTypeStatistic>,
  val topCompanies: List<CompanyStatistic>,
  val yearlyTrend: List<YearlyStatistic>
)

data class JobTypeStatistic(
  val jobType: JobType,
  val projectCount: Int,
  val totalDays: Int,
  val totalEarnings: Long
)

data class CompanyStatistic(
  val companyId: String,
  val companyName: String,
  val projectCount: Int,
  val lastWorkDate: String
)

data class YearlyStatistic(
  val year: Int,
  val workDays: Int,
  val earnings: Long,
  val projectCount: Int
)

data class IncomeStatistics(
  val year: Int,
  val month: Int?,
  val totalIncome: Long,
  val regularIncome: Long,
  val overtimeIncome: Long,
  val bonusIncome: Long,
  val taxDeduction: Long,
  val netIncome: Long,
  val dailyBreakdown: List<DailyIncome>?
)

data class DailyIncome(
  val date: String,
  val projectId: String,
  val amount: Long,
  val hours: Double
)

data class WorkHistory(
  val id: String,
  val projectId: String,
  val projectName: String,
  val companyName: String,
  val position: String,
  val startDate: String,
  val endDate: String,
  val totalDays: Int,
  val totalEarnings: Long,
  val rating: Double?
)

// 설정 관련
data class WorkerPreferences(
  val preferredJobTypes: List<JobType>,
  val preferredLocations: List<String>,
  val desiredWageRange: WageRange,
  val workSchedule: WorkSchedulePreference,
  val benefits: List<String>,
  val avoidCompanies: List<String>
)

data class WorkSchedulePreference(
  val preferredDays: List<String>,
  val preferredStartTime: String,
  val preferredEndTime: String,
  val canWorkWeekends: Boolean,
  val canWorkNights: Boolean,
  val canWorkOvertime: Boolean
)

data class WageRange(
  val min: Long,
  val max: Long
)

data class WorkerDocument(
  val id: String,
  val workerId: String,
  val type: String,
  val name: String,
  val url: String,
  val description: String?,
  val uploadedAt: String,
  val expiryDate: String?
)

// 블랙리스트 관련
data class BlacklistStatus(
  val isBlacklisted: Boolean,
  val blacklistedBy: List<BlacklistEntry>,
  val totalReports: Int
)

data class BlacklistEntry(
  val companyId: String,
  val companyName: String,
  val reason: String,
  val date: String
)

data class BlacklistCheckResult(
  val workerId: String,
  val isBlacklisted: Boolean,
  val blacklistCount: Int
)

// 기타 클래스들
data class SafetyEducation(
  val completionDate: String,
  val educationType: String,
  val certificateNumber: String,
  val expiryDate: String,
  val institutor: String? = null
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

data class WorkerActivity(
  val workerId: String,
  val date: String,
  val projectId: String,
  val projectName: String,
  val workHours: Double,
  val wage: Long,
  val status: String
)