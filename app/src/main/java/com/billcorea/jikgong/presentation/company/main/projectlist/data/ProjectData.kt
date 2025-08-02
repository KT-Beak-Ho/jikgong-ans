package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 프로젝트 데이터 모델 - 직직직 건설업 플랫폼용
 */
data class Project(
  val id: String,
  val title: String,
  val description: String,
  val location: String,
  val detailAddress: String? = null,
  val workType: WorkType,
  val status: ProjectStatus,
  val startDate: String, // "MM/dd" 형식
  val endDate: String,   // "MM/dd" 형식
  val fullStartDate: LocalDate,
  val fullEndDate: LocalDate,
  val dailyWage: Long,
  val requiredWorkers: Int,
  val currentWorkers: Int,
  val workingHours: WorkingHours,
  val companyName: String,
  val companyId: String,
  val contactPerson: String,
  val contactNumber: String,
  val requirements: List<String> = emptyList(),
  val benefits: List<String> = emptyList(),
  val isUrgent: Boolean = false,
  val isBookmarked: Boolean = false,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val views: Int = 0,
  val applicationCount: Int = 0,
  val thumbnailImageUrl: String? = null,
  val safetyLevel: SafetyLevel = SafetyLevel.STANDARD,
  val settlementType: SettlementType = SettlementType.DAILY
) {
  /**
   * 모집률 계산
   */
  val recruitmentRate: Float
    get() = if (requiredWorkers > 0) {
      (currentWorkers.toFloat() / requiredWorkers.toFloat()).coerceAtMost(1.0f)
    } else 0f

  /**
   * 지원 가능 여부
   */
  val canApply: Boolean
    get() = status == ProjectStatus.RECRUITING && currentWorkers < requiredWorkers

  /**
   * 남은 모집 인원
   */
  val remainingWorkers: Int
    get() = (requiredWorkers - currentWorkers).coerceAtLeast(0)

  /**
   * 마감 임박 여부 (3일 이내)
   */
  val isDeadlineSoon: Boolean
    get() = fullStartDate.minusDays(3) <= LocalDate.now()

  /**
   * 프로젝트 기간 (일 단위)
   */
  val durationDays: Long
    get() = java.time.temporal.ChronoUnit.DAYS.between(fullStartDate, fullEndDate) + 1

  /**
   * 총 급여 계산
   */
  val totalWage: Long
    get() = dailyWage * durationDays
}

/**
 * 업무 타입
 */
enum class WorkType(
  val displayName: String,
  val category: String,
  val description: String
) {
  // 건축
  APARTMENT_CONSTRUCTION("아파트 건설", "건축", "아파트 및 주거용 건물 건설"),
  BUILDING_CONSTRUCTION("건물 건설", "건축", "상업용 및 공공 건물 건설"),
  HOUSE_RENOVATION("주택 리모델링", "건축", "기존 주택의 개보수 및 리모델링"),

  // 토목
  ROAD_CONSTRUCTION("도로 공사", "토목", "도로 건설 및 포장 공사"),
  BRIDGE_CONSTRUCTION("교량 공사", "토목", "교량 건설 및 보수"),
  UNDERGROUND_WORK("지하 공사", "토목", "지하철, 터널 등 지하 구조물"),

  // 전문 공사
  ELECTRICAL_WORK("전기 공사", "전문", "전기 설비 설치 및 배선"),
  PLUMBING_WORK("배관 공사", "전문", "상하수도 및 가스 배관"),
  INTERIOR_WORK("인테리어", "전문", "실내 장식 및 마감 공사"),
  PAINTING_WORK("도장 공사", "전문", "건물 외벽 및 내벽 도장"),

  // 설비
  HVAC_WORK("공조 설비", "설비", "냉난방 및 환기 시설"),
  ELEVATOR_WORK("승강기 공사", "설비", "엘리베이터 설치 및 보수"),

  // 기타
  DEMOLITION_WORK("철거 공사", "기타", "건물 및 구조물 철거"),
  LANDSCAPING("조경 공사", "기타", "정원 및 조경 시설"),
  CLEANING_WORK("청소 작업", "기타", "건설 현장 정리 및 청소")
}

/**
 * 근무 시간 정보
 */
data class WorkingHours(
  val startTime: String, // "09:00" 형식
  val endTime: String,   // "18:00" 형식
  val breakTime: Int = 60, // 휴게시간 (분)
  val isFlexible: Boolean = false
) {
  /**
   * 총 근무 시간 (시간 단위)
   */
  val totalHours: Float
    get() {
      val start = startTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
      val end = endTime.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
      return ((end - start - breakTime) / 60f).coerceAtLeast(0f)
    }

  /**
   * 시간당 임금 계산
   */
  fun getHourlyWage(dailyWage: Long): Long {
    return if (totalHours > 0) (dailyWage / totalHours).toLong() else 0L
  }
}

/**
 * 안전 등급
 */
enum class SafetyLevel(
  val displayName: String,
  val description: String,
  val colorHex: String
) {
  HIGH("높음", "AI 안전관리 시스템 적용", "#4CAF50"),
  STANDARD("보통", "기본 안전관리 적용", "#FF9800"),
  LOW("주의", "추가 안전교육 필요", "#F44336")
}

/**
 * 정산 방식
 */
enum class SettlementType(
  val displayName: String,
  val description: String
) {
  DAILY("일급제", "매일 정산"),
  WEEKLY("주급제", "매주 정산"),
  MONTHLY("월급제", "매월 정산"),
  PROJECT_END("완료 후", "프로젝트 완료 후 일괄 정산")
}

/**
 * 프로젝트 요약 정보
 */
data class ProjectSummary(
  val totalProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val inProgressProjects: Int = 0,
  val completedProjects: Int = 0,
  val urgentProjects: Int = 0,
  val totalWorkers: Int = 0,
  val averageDailyWage: Long = 0L,
  val totalApplications: Int = 0
) {
  /**
   * 활성 프로젝트 수 (모집중 + 진행중)
   */
  val activeProjects: Int
    get() = recruitingProjects + inProgressProjects

  /**
   * 완료율
   */
  val completionRate: Float
    get() = if (totalProjects > 0) {
      (completedProjects.toFloat() / totalProjects.toFloat())
    } else 0f
}

