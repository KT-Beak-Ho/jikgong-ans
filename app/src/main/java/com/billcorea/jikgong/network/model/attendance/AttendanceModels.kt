package com.billcorea.jikgong.network.model.attendance

import com.billcorea.jikgong.network.model.common.*

/**
 * 출퇴근/근태 데이터
 */
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
  val status: AttendanceStatus,

  // ===== 인증 =====
  val verificationMethod: VerificationMethod,
  val workPhotos: List<String> = emptyList(),
  val remarks: String? = null,

  val createdAt: String,
  val updatedAt: String
)

/**
 * 체크인/아웃 정보
 */
data class CheckInOut(
  val time: String,
  val location: LocationData,
  val photo: String? = null,
  val verified: Boolean = false
)

/**
 * 휴게 기록
 */
data class BreakRecord(
  val startTime: String,
  val endTime: String,
  val duration: Int
)

/**
 * 위치 데이터
 */
data class LocationData(
  val latitude: Double,
  val longitude: Double,
  val address: String? = null,
  val timestamp: String
)

/**
 * 근무 기록
 */
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