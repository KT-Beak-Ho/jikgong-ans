package com.billcorea.jikgong.network.model.attendance

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.location.LocationData

/**
 * 출퇴근/근태 관련 모델 (완전판)
 */

// ============================================
// 출퇴근 데이터
// ============================================
data class AttendanceData(
  val id: String,
  val workerId: String,
  val projectId: String,
  val workDate: String,

  // ===== 체크인/아웃 =====
  val checkIn: CheckInOut?,
  val checkOut: CheckInOut?,

  // ===== 근무 정보 =====
  val breakRecords: List<BreakRecord>,
  val actualWorkHours: Double,
  val overtimeHours: Double = 0.0,
  val nightWorkHours: Double = 0.0,
  val weekendWorkHours: Double = 0.0,
  val status: AttendanceStatus,

  // ===== 인증 =====
  val verificationMethod: VerificationMethod,
  val workPhotos: List<String> = emptyList(),
  val remarks: String? = null,

  val createdAt: String,
  val updatedAt: String
)

// ============================================
// 체크인/아웃 정보
// ============================================
data class CheckInOut(
  val time: String,
  val location: LocationData,
  val photo: String? = null,
  val verified: Boolean = false,
  val verificationMethod: VerificationMethod? = null
)

// ============================================
// 요청 모델 (AttendanceAPI에서 이동)
// ============================================
data class CheckInRequest(
  val workerId: String,
  val projectId: String,
  val location: LocationData,
  val photo: String? = null,
  val verificationMethod: VerificationMethod
)

data class CheckOutRequest(
  val attendanceId: String,
  val location: LocationData,
  val photo: String? = null
)

data class AttendanceVerification(
  val attendanceId: String,
  val verificationMethod: VerificationMethod,
  val verificationData: String
)

data class LocationVerificationRequest(
  val attendanceId: String,
  val location: LocationData
)

data class LocationVerificationResult(
  val isValid: Boolean,
  val distance: Double,  // 미터 단위
  val message: String
)

// ============================================
// 휴게 기록
// ============================================
data class BreakRecord(
  val id: String? = null,
  val startTime: String,
  val endTime: String? = null,
  val duration: Int = 0,  // 분 단위
  val type: BreakType = BreakType.NORMAL
)

enum class BreakType {
  NORMAL,    // 일반 휴게
  LUNCH,     // 점심시간
  DINNER,    // 저녁시간
  OTHER
}

// ============================================
// 근무 기록
// ============================================
data class WorkRecord(
  val workerId: String,
  val projectId: String,
  val date: String,
  val startTime: String,
  val endTime: String,
  val totalHours: Double,
  val overtimeHours: Double,
  val status: AttendanceStatus
)

// ============================================
// 근태 통계
// ============================================
data class AttendanceStats(
  val totalDays: Int,
  val presentDays: Int,
  val absentDays: Int,
  val lateDays: Int,
  val earlyLeaveDays: Int,
  val totalWorkHours: Double,
  val totalOvertimeHours: Double,
  val averageWorkHours: Double,
  val attendanceRate: Double,  // 백분율
  val period: String
)

// ============================================
// 월간 근태 요약
// ============================================
data class MonthlyAttendanceSummary(
  val year: Int,
  val month: Int,
  val workerId: String,
  val workerName: String,
  val totalWorkDays: Int,
  val actualWorkDays: Int,
  val totalWorkHours: Double,
  val totalOvertimeHours: Double,
  val totalNightWorkHours: Double,
  val totalWeekendWorkHours: Double,
  val totalBreakHours: Double,
  val attendanceRate: Double,
  val totalWage: Long,
  val details: List<AttendanceData>
)