package com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels

import java.time.LocalDate

// ==================== 프로젝트 기본 정보 ====================

data class SimpleProject(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val category: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val currentApplicants: Int,
    val maxApplicants: Int = 20,
    val wage: Int = 150000,
    val isUrgent: Boolean = false,
    val isBookmarked: Boolean = false
)

// ==================== 프로젝트 상태 ====================

enum class ProjectStatus {
    PLANNING,     // 계획중
    RECRUITING,   // 모집중
    IN_PROGRESS,  // 진행중
    COMPLETED,    // 완료
    CANCELLED     // 취소
}

// ==================== 프로젝트 카테고리 ====================

enum class ProjectCategory(val displayName: String) {
    APARTMENT("아파트 신축"),
    WAREHOUSE("물류센터"),
    BRIDGE("교량 보수"),
    SPECIAL_FACILITY("특수시설"),
    FACTORY("공장 건설"),
    SUBWAY("지하철 공사")
}

// ==================== 일자별 작업 데이터 ====================

data class WorkDay(
  val id: String,
  val title: String, // 모집 제목
  val date: LocalDate,
  val startTime: String,
  val endTime: String,
  val recruitPeriod: String,
  val applicants: Int,
  val confirmed: Int,
  val maxWorkers: Int,
  val status: String, // "IN_PROGRESS", "UPCOMING", "COMPLETED"
  val projectId: String = "1" // 프로젝트 ID 추가
)

// ==================== 프로젝트별 노동자 관리 ====================

data class ProjectWorker(
  val workerId: String,
  val workerName: String,
  val projectId: String,
  val workDayId: String,
  val jobType: String,
  val registrationDate: LocalDate,
  val attendanceRecords: List<AttendanceRecord> = emptyList()
)

data class AttendanceRecord(
  val date: LocalDate,
  val status: AttendanceStatus,
  val checkInTime: String? = null,
  val checkOutTime: String? = null,
  val workHours: Double = 0.0,
  val notes: String = ""
)

enum class AttendanceStatus {
  SCHEDULED,      // 예정됨
  CHECKED_IN,     // 출근함
  CHECKED_OUT,    // 퇴근함
  ABSENT,         // 결근
  LATE,          // 지각
  EARLY_LEAVE     // 조퇴
}