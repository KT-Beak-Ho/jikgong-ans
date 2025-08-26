package com.billcorea.jikgong.network.model.project

import com.billcorea.jikgong.network.model.common.*
import java.time.LocalDateTime

/**
 * 통합된 프로젝트 데이터 모델 앞으로 더 추가 될 예정
 * - 기존 presentation/company/main/projectlist/model/Project.kt 통합
 * - 기존 network/ProjectData.kt 통합
 */

data class ProjectData(
  // ===== 기본 정보 =====
  val id: String,
  val companyId: String,
  val name: String,                    // 프로젝트명 (주 필드)
  val title: String = "",               // 하위 호환성
  val company: String = "",             // 회사명
  val type: ProjectType = ProjectType.CONSTRUCTION,
  val category: ProjectCategory,
  val status: ProjectStatus,
  val description: String,

  // ===== 일정 =====
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val workDays: List<DayOfWeek> = emptyList(),
  val workStartTime: String = "08:00",
  val workEndTime: String = "18:00",
  val breakTime: BreakTime? = null,

  // ===== 위치 =====
  val location: ProjectLocation,

  // ===== 계약/금액 =====
  val contractAmount: Long = 0L,
  val paymentTerms: PaymentTerms? = null,
  val wageType: WageType = WageType.DAILY,
  val wageAmount: Long,                // 일당 (주 필드)
  val wage: Int = 0,                   // 하위 호환성

  // ===== 인력 =====
  val requiredWorkers: WorkerRequirement? = null,
  val currentApplicants: Int = 0,
  val maxApplicants: Int = 0,
  val confirmedWorkers: List<String> = emptyList(),

  // ===== 요구사항 =====
  val requirements: ProjectRequirements? = null,
  val requirementsList: List<String> = emptyList(),

  // ===== 수당/혜택 =====
  val allowances: Allowances? = null,
  val benefits: List<String> = emptyList(),

  // ===== 기타 =====
  val images: List<String> = emptyList(),
  val attachments: List<Attachment> = emptyList(),
  val tags: List<String> = emptyList(),
  val isUrgent: Boolean = false,
  val isBookmarked: Boolean = false,
  val viewCount: Int = 0,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * ✅ ProjectFilter enum 통합
 * - 기존 presentation/.../model/ProjectFilter.kt 통합
 */
enum class ProjectFilter(val displayName: String) {
  ALL("전체"),
  RECRUITING("모집중"),
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  URGENT("긴급"),
  BOOKMARKED("북마크")
}

/**
 * ✅ ProjectStats 통합
 * - 기존 presentation/.../model/ProjectStats.kt 통합
 */
data class ProjectStats(
  val totalProjects: Int = 0,
  val activeProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val completedProjects: Int = 0,
  val totalApplicants: Int = 0,
  val averageWage: Int = 0
)

/**
 * ✅ ProjectListUiState 통합
 * - 기존 presentation/.../model/ProjectListUiState.kt 통합
 */
data class ProjectListUiState(
  val isLoading: Boolean = false,
  val projects: List<ProjectData> = emptyList(),
  val filteredProjects: List<ProjectData> = emptyList(),
  val error: String? = null,
  val stats: ProjectStats = ProjectStats()
)

// ===== 기존 ProjectData의 하위 데이터 클래스들 =====

data class BreakTime(
  val startTime: String,
  val endTime: String,
  val duration: Int
)

data class ProjectLocation(
  val address: String,
  val detailAddress: String,
  val latitude: Double,
  val longitude: Double,
  val nearbyStation: String? = null,
  val parkingAvailable: Boolean = false
)

data class PaymentTerms(
  val method: PaymentMethod,
  val schedule: PaymentSchedule,
  val dayOfPayment: Int? = null
)

data class WorkerRequirement(
  val estimatedTotal: Int,
  val dailyRequired: Int,
  val maxWorkers: Int,
  val byJobType: Map<JobType, Int> = emptyMap()
)

data class ProjectRequirements(
  val requiredCertificates: List<String> = emptyList(),
  val safetyRequirements: List<String> = emptyList(),
  val experienceYears: Int = 0,
  val ageRange: AgeRange? = null,
  val preferredGender: Gender? = null
)

data class AgeRange(
  val min: Int,
  val max: Int
)

data class Allowances(
  val overtimeRate: Double = 1.5,
  val weekendRate: Double = 1.5,
  val nightRate: Double = 1.5,
  val hasPerformanceBonus: Boolean = false,
  val bonusDetails: String? = null
)

data class Attachment(
  val name: String,
  val url: String,
  val type: String,
  val size: Long
)