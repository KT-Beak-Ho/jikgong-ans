package com.billcorea.jikgong.network.model.worker

import com.billcorea.jikgong.network.model.common.*

/**
 * 노동자 전체 데이터 모델
 */
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






/**
 * 경력 정보
 */
data class WorkExperience(
  val tech: String,
  val experienceMonths: Int,
  val description: String? = null
)

/**
 * 자격증 정보
 */
data class Certificate(
  val name: String,
  val issuer: String,
  val issueDate: String,
  val expiryDate: String? = null,
  val certificateNumber: String,
  val imageUrl: String? = null
)

/**
 * 안전교육 정보
 */
data class SafetyEducation(
  val completionDate: String,
  val educationType: String,
  val certificateNumber: String,
  val expiryDate: String,
  val institutor: String? = null
)

/**
 * 운전면허 정보
 */
data class DriverLicense(
  val type: String,
  val issueDate: String,
  val expiryDate: String,
  val licenseNumber: String
)

/**
 * 희망 임금 정보
 */
data class WageInfo(
  val type: WageType,
  val amount: Long,
  val negotiable: Boolean = false,
  val includeOvertime: Boolean = false
)

/**
 * 스카우트 프로필
 */
data class ScoutProfile(
  val isPublic: Boolean,
  val title: String,
  val introduction: String,
  val requiredBenefits: List<String> = emptyList(),
  val desiredWage: Long,
  val portfolioUrls: List<String> = emptyList(),
  val availableImmediately: Boolean = false
)

/**
 * 노동자 평점
 */
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

/**
 * 언어 능력
 */
data class Language(
  val language: String,
  val proficiency: LanguageProficiency
)

/**
 * 건강 상태
 */
data class HealthStatus(
  val level: HealthLevel,
  val fitnessLevel: FitnessLevel,
  val lastCheckupDate: String? = null,
  val restrictions: List<String> = emptyList(),
  val hasChronicDisease: Boolean = false
)

/**
 * 사고 기록
 */
data class AccidentRecord(
  val date: String,
  val type: String,
  val severity: String,
  val description: String,
  val compensationReceived: Boolean = false
)

/**
 * 알림 설정
 */
data class NotificationSettings(
  val jobMatch: Boolean = true,
  val payment: Boolean = true,
  val urgent: Boolean = true,
  val chat: Boolean = true,
  val marketing: Boolean = false,
  val nightTime: Boolean = false,
  val weekend: Boolean = true
)

/**
 * 노동자 활동 이력
 */
data class WorkerActivity(
  val workerId: String,
  val date: String,
  val projectId: String,
  val projectName: String,
  val workHours: Double,
  val wage: Long,
  val status: String
)
