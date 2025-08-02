// ========================================
// 📄 data/Project.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 프로젝트 데이터 모델 - 건설업 인력 매칭에 특화
 */
data class Project(
  val id: String,
  val title: String,
  val description: String,
  val location: ProjectLocation,
  val dailyWage: Int, // 일당 (원)
  val requiredWorkers: Int, // 필요 인력
  val appliedWorkers: Int, // 지원한 인력
  val workType: WorkType, // 작업 유형
  val status: ProjectStatus,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val workHours: WorkHours,
  val requirements: List<String>, // 필요 자격/경력
  val benefits: List<String>, // 복리후생
  val isUrgent: Boolean = false, // 긴급 모집 여부
  val isBookmarked: Boolean = false, // 북마크 여부
  val companyName: String,
  val companyRating: Float, // 기업 평점 (0.0 ~ 5.0)
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val tags: List<String> = emptyList(), // 검색용 태그
  val paymentMethod: PaymentMethod = PaymentMethod.DIRECT, // 지급 방식
  val hasAccommodation: Boolean = false, // 숙소 제공 여부
  val hasMeals: Boolean = false, // 식사 제공 여부
  val contactInfo: ContactInfo? = null
) {
  /**
   * 모집률 계산 (0.0 ~ 1.0)
   */
  val recruitmentRate: Float
    get() = if (requiredWorkers > 0) {
      (appliedWorkers.toFloat() / requiredWorkers.toFloat()).coerceIn(0f, 1f)
    } else 0f

  /**
   * 남은 모집 인원
   */
  val remainingWorkers: Int
    get() = (requiredWorkers - appliedWorkers).coerceAtLeast(0)

  /**
   * 프로젝트 기간 (일수)
   */
  val durationDays: Long
    get() = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1

  /**
   * 총 예상 급여
   */
  val totalWage: Int
    get() = dailyWage * durationDays.toInt()

  /**
   * 마감일까지 남은 일수
   */
  val daysUntilStart: Long
    get() = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), startDate)

  /**
   * 지원 가능 여부
   */
  val canApply: Boolean
    get() = status == ProjectStatus.RECRUITING &&
      remainingWorkers > 0 &&
      daysUntilStart >= 0
}

/**
 * 프로젝트 위치 정보
 */
data class ProjectLocation(
  val city: String, // 도시 (부산)
  val district: String, // 구 (사하구)
  val detail: String, // 상세 주소
  val latitude: Double? = null,
  val longitude: Double? = null
) {
  val fullAddress: String
    get() = "$city $district $detail"

  val shortAddress: String
    get() = "$city $district"
}

/**
 * 작업 시간 정보
 */
data class WorkHours(
  val startTime: String, // "08:00"
  val endTime: String,   // "18:00"
  val breakTime: Int = 60, // 점심시간 (분)
  val overtime: Boolean = false // 야근 가능 여부
) {
  val totalMinutes: Int
    get() {
      val start = startTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
      val end = endTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
      return end - start - breakTime
    }

  val displayText: String
    get() = "$startTime ~ $endTime"
}

/**
 * 연락처 정보
 */
data class ContactInfo(
  val managerName: String,
  val phoneNumber: String,
  val email: String? = null
)