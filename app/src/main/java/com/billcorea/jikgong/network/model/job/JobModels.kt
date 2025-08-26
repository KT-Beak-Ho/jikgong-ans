package com.billcorea.jikgong.network.model.job

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.worker.WorkerData

/**
 * 일자리 관련 모델 (완전판)
 */

// ============================================
// 일자리 등록 데이터
// ============================================
data class JobRegistrationData(
  // ===== 기본 정보 =====
  val id: String,
  val projectId: String,
  val companyId: String,
  val title: String,
  val jobType: JobType,
  val isUrgent: Boolean = false,
  val deadline: String,
  val status: JobStatus,

  // ===== 근무 조건 =====
  val workDate: String,
  val workStartTime: String,
  val workEndTime: String,
  val location: String,
  val detailLocation: String? = null,
  val workIntensity: WorkIntensity,
  val riskLevel: RiskLevel,

  // ===== 요구사항 =====
  val experienceLevel: ExperienceLevel,
  val minExperienceYears: Int,
  val ageLimit: AgeRange? = null,
  val preferredGender: Gender? = null,
  val requiredDocuments: List<String>,
  val needInterview: Boolean = false,
  val needHealthCheck: Boolean = false,

  // ===== 제공 사항 =====
  val provisions: JobProvisions,

  // ===== 기타 =====
  val preparationItems: List<String>,
  val environmentPhotos: List<String>,
  val probationPeriod: Int? = null,
  val contactPerson: ContactPerson,
  val applicants: List<String> = emptyList(),
  val viewCount: Int = 0,
  val createdAt: String,
  val updatedAt: String
)

// ============================================
// 일자리 제공 사항
// ============================================
data class JobProvisions(
  val hasPickup: Boolean,
  val pickupLocation: String? = null,
  val parkingSpace: ParkingSpace,
  val parkingDescription: String? = null,
  val hasAccommodation: Boolean,
  val meals: MealProvision,
  val providesTools: Boolean,
  val providesWorkClothes: Boolean,
  val insurance: List<InsuranceType>
)

data class ParkingSpace(
  val available: Boolean,
  val isFree: Boolean,
  val description: String? = null
)

data class MealProvision(
  val breakfast: Boolean,
  val lunch: Boolean,
  val dinner: Boolean
)

data class ContactPerson(
  val name: String,
  val position: String,
  val phone: String,
  val email: String? = null
)

data class AgeRange(
  val min: Int,
  val max: Int
)

// ============================================
// JobAPI에서 이동된 모델들
// ============================================
data class JobStatusUpdate(
  val status: JobStatus,
  val reason: String?
)

data class JobApplicationRequest(
  val workerId: String,
  val coverLetter: String?,
  val expectedWage: Long?,
  val availableStartDate: String,
  val portfolio: List<String>?
)

data class JobApplication(
  val id: String,
  val jobId: String,
  val workerId: String,
  val workerName: String,
  val workerProfile: WorkerData?,
  val status: ApplicationStatus,
  val coverLetter: String?,
  val expectedWage: Long?,
  val availableStartDate: String,
  val portfolio: List<String>?,
  val appliedAt: String,
  val reviewedAt: String?,
  val decidedAt: String?
)

data class ApplicationStatusUpdate(
  val status: ApplicationStatus,
  val reason: String?,
  val interviewDate: String?
)

enum class ApplicationStatus {
  PENDING,
  REVIEWING,
  INTERVIEW_SCHEDULED,
  ACCEPTED,
  REJECTED,
  CANCELLED,
  WITHDRAWN
}

data class JobStats(
  val viewCount: Int,
  val applicationCount: Int,
  val bookmarkCount: Int,
  val averageMatchScore: Double,
  val fillRate: Double,
  val responseTime: Int  // 시간 단위
)

data class JobMarketStats(
  val totalJobs: Int,
  val newJobsThisPeriod: Int,
  val averageWage: Long,
  val demandIndex: Double,
  val supplyIndex: Double,
  val topJobTypes: List<JobTypeStats>,
  val wageDistribution: Map<String, Int>
)

data class JobTypeStats(
  val jobType: JobType,
  val count: Int,
  val averageWage: Long,
  val demandLevel: String
)

data class JobMatchResult(
  val jobId: String,
  val workerId: String,
  val matchScore: Double,
  val matchedAt: String,
  val status: MatchStatus
)

// MatchStatus enum 추가
enum class MatchStatus {
  PENDING,
  MATCHED,
  CONFIRMED,
  REJECTED,
  CANCELLED,
  COMPLETED
}

data class MatchCandidate(
  val worker: WorkerData,
  val matchScore: Double,
  val availability: Boolean,
  val distance: Double?,
  val matchReasons: List<String>
)

// JobAPI의 AutoMatchCriteria는 제거 (MatchingAPI에 있음)

data class JobNotificationRequest(
  val targetWorkers: List<String>,
  val message: String,
  val notificationType: NotificationType
)

enum class NotificationType {
  URGENT_HIRING,
  DEADLINE_REMINDER,
  WAGE_UPDATE,
  GENERAL
}

data class JobAlert(
  val id: String,
  val userId: String,
  val criteria: JobAlertCriteria,
  val frequency: AlertFrequency,
  val isActive: Boolean,
  val createdAt: String
)

data class JobAlertRequest(
  val criteria: JobAlertCriteria,
  val frequency: AlertFrequency
)

data class JobAlertCriteria(
  val jobTypes: List<JobType>?,
  val locations: List<String>?,
  val minWage: Long?,
  val keywords: List<String>?
)

enum class AlertFrequency {
  IMMEDIATE,
  DAILY,
  WEEKLY
}

data class JobCompletionRequest(
  val completedWorkers: List<String>,
  val actualEndDate: String,
  val totalCost: Long,
  val notes: String?
)

data class JobCompletion(
  val jobId: String,
  val completedAt: String,
  val totalWorkers: Int,
  val totalHours: Double,
  val totalCost: Long,
  val completionRate: Double
)

data class JobEvaluationRequest(
  val workerId: String,
  val rating: Int,
  val feedback: String?,
  val wouldRehire: Boolean
)

data class JobEvaluation(
  val id: String,
  val jobId: String,
  val workerId: String,
  val rating: Int,
  val feedback: String?,
  val wouldRehire: Boolean,
  val evaluatedAt: String
)