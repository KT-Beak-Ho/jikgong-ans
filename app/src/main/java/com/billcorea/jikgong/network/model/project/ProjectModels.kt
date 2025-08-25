package com.billcorea.jikgong.network.model.project

import com.billcorea.jikgong.network.model.common.*

/**
 * 프로젝트 데이터
 */
data class ProjectData(
  // ===== 기본 정보 =====
  val id: String,
  val companyId: String,
  val name: String,
  val type: ProjectType,
  val category: ProjectCategory,
  val status: ProjectStatus,
  val description: String,

  // ===== 일정 =====
  val startDate: String,
  val endDate: String,
  val workDays: List<DayOfWeek>,
  val workStartTime: String,
  val workEndTime: String,
  val breakTime: BreakTime,

  // ===== 위치 =====
  val location: ProjectLocation,

  // ===== 계약/금액 =====
  val contractAmount: Long,
  val paymentTerms: PaymentTerms,
  val wageType: WageType,
  val wageAmount: Long,

  // ===== 인력 =====
  val requiredWorkers: WorkerRequirement,
  val currentApplicants: Int = 0,
  val confirmedWorkers: List<String> = emptyList(),

  // ===== 요구사항 =====
  val requirements: ProjectRequirements,

  // ===== 수당 =====
  val allowances: Allowances,

  // ===== 기타 =====
  val images: List<String> = emptyList(),
  val attachments: List<Attachment> = emptyList(),
  val tags: List<String> = emptyList(),
  val isUrgent: Boolean = false,
  val viewCount: Int = 0,
  val createdAt: String,
  val updatedAt: String
)

/**
 * 휴게 시간
 */
data class BreakTime(
  val startTime: String,
  val endTime: String,
  val duration: Int
)

/**
 * 프로젝트 위치
 */
data class ProjectLocation(
  val address: String,
  val detailAddress: String,
  val latitude: Double,
  val longitude: Double,
  val nearbyStation: String? = null,
  val parkingAvailable: Boolean = false
)

/**
 * 지급 조건
 */
data class PaymentTerms(
  val method: PaymentMethod,
  val schedule: PaymentSchedule,
  val dayOfPayment: Int? = null
)

/**
 * 필요 인력
 */
data class WorkerRequirement(
  val estimatedTotal: Int,
  val dailyRequired: Int,
  val maxWorkers: Int,
  val byJobType: Map<JobType, Int>
)

/**
 * 프로젝트 요구사항
 */
data class ProjectRequirements(
  val requiredCertificates: List<String>,
  val safetyRequirements: List<String>,
  val experienceYears: Int,
  val ageRange: AgeRange? = null,
  val preferredGender: Gender? = null
)

/**
 * 연령 범위
 */
data class AgeRange(
  val min: Int,
  val max: Int
)

/**
 * 수당
 */
data class Allowances(
  val overtimeRate: Double,
  val weekendRate: Double,
  val nightRate: Double,
  val hasPerformanceBonus: Boolean,
  val bonusDetails: String? = null
)

/**
 * 첨부파일
 */
data class Attachment(
  val name: String,
  val url: String,
  val type: String,
  val size: Long
)
