// ============================================
// AttendanceModels.kt - 출퇴근/근태 관련 모델 (완전판)
// ============================================
package com.billcorea.jikgong.network.model.attendance

import com.google.gson.annotations.SerializedName
import com.billcorea.jikgong.network.model.location.LocationData

// ===== 출퇴근 데이터 =====
data class AttendanceData(
  @SerializedName("id")
  val id: String? = null,

  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("date")
  val date: String,

  @SerializedName("checkInTime")
  val checkInTime: String? = null,

  @SerializedName("checkOutTime")
  val checkOutTime: String? = null,

  @SerializedName("status")
  val status: AttendanceStatus = AttendanceStatus.PENDING,

  @SerializedName("workHours")
  val workHours: Double? = null,

  @SerializedName("overtimeHours")
  val overtimeHours: Double? = null,

  @SerializedName("checkInLocation")
  val checkInLocation: LocationData? = null,

  @SerializedName("checkOutLocation")
  val checkOutLocation: LocationData? = null,

  @SerializedName("verificationMethod")
  val verificationMethod: VerificationMethod? = null,

  @SerializedName("notes")
  val notes: String? = null,

  @SerializedName("approvedBy")
  val approvedBy: String? = null,

  @SerializedName("approvedAt")
  val approvedAt: String? = null,

  @SerializedName("createdAt")
  val createdAt: String? = null,

  @SerializedName("updatedAt")
  val updatedAt: String? = null
)

// ===== 출퇴근 상태 Enum =====
enum class AttendanceStatus {
  PRESENT,        // 출근
  LATE,          // 지각
  ABSENT,        // 결근
  LEAVE_EARLY,   // 조퇴
  VACATION,      // 휴가
  SICK_LEAVE,    // 병가
  PENDING,       // 대기
  HOLIDAY,       // 공휴일
  WEEKEND        // 주말
}

// ===== 인증 방법 Enum =====
enum class VerificationMethod {
  GPS,                // GPS 인증
  QR_CODE,           // QR 코드
  MANUAL,            // 수동 입력
  FACE_RECOGNITION,  // 얼굴 인식
  BEACON,            // 비콘
  NFC,               // NFC
  ADMIN_OVERRIDE     // 관리자 승인
}

// ===== 체크인 요청 =====
data class CheckInRequest(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("location")
  val location: LocationData,

  @SerializedName("verificationMethod")
  val verificationMethod: VerificationMethod,

  @SerializedName("deviceInfo")
  val deviceInfo: DeviceInfo? = null,

  @SerializedName("photo")
  val photo: String? = null // Base64 encoded
)

// ===== 체크아웃 요청 =====
data class CheckOutRequest(
  @SerializedName("attendanceId")
  val attendanceId: String,

  @SerializedName("location")
  val location: LocationData,

  @SerializedName("verificationMethod")
  val verificationMethod: VerificationMethod,

  @SerializedName("notes")
  val notes: String? = null
)

// ===== 디바이스 정보 =====
data class DeviceInfo(
  @SerializedName("deviceId")
  val deviceId: String,

  @SerializedName("deviceModel")
  val deviceModel: String,

  @SerializedName("osVersion")
  val osVersion: String,

  @SerializedName("appVersion")
  val appVersion: String
)

// ===== 근태 요약 =====
data class AttendanceSummary(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("period")
  val period: String,

  @SerializedName("totalDays")
  val totalDays: Int,

  @SerializedName("presentDays")
  val presentDays: Int,

  @SerializedName("absentDays")
  val absentDays: Int,

  @SerializedName("lateDays")
  val lateDays: Int,

  @SerializedName("earlyLeaveDays")
  val earlyLeaveDays: Int,

  @SerializedName("vacationDays")
  val vacationDays: Int,

  @SerializedName("totalWorkHours")
  val totalWorkHours: Double,

  @SerializedName("totalOvertimeHours")
  val totalOvertimeHours: Double,

  @SerializedName("attendanceRate")
  val attendanceRate: Double
)

// ===== 근태 수정 요청 =====
data class AttendanceUpdateRequest(
  @SerializedName("status")
  val status: AttendanceStatus,

  @SerializedName("checkInTime")
  val checkInTime: String? = null,

  @SerializedName("checkOutTime")
  val checkOutTime: String? = null,

  @SerializedName("reason")
  val reason: String,

  @SerializedName("updatedBy")
  val updatedBy: String
)

// ===== 근태 승인 요청 =====
data class AttendanceApprovalRequest(
  @SerializedName("attendanceIds")
  val attendanceIds: List<String>,

  @SerializedName("approved")
  val approved: Boolean,

  @SerializedName("reason")
  val reason: String? = null,

  @SerializedName("approvedBy")
  val approvedBy: String
)

// ===== 근태 통계 =====
data class AttendanceStatistics(
  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("date")
  val date: String,

  @SerializedName("totalWorkers")
  val totalWorkers: Int,

  @SerializedName("presentCount")
  val presentCount: Int,

  @SerializedName("absentCount")
  val absentCount: Int,

  @SerializedName("lateCount")
  val lateCount: Int,

  @SerializedName("attendanceRate")
  val attendanceRate: Double,

  @SerializedName("averageWorkHours")
  val averageWorkHours: Double
)

// ===== QR 코드 데이터 =====
data class QRCodeData(
  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("siteId")
  val siteId: String,

  @SerializedName("validUntil")
  val validUntil: String,

  @SerializedName("signature")
  val signature: String
)

// ===== 비콘 데이터 =====
data class BeaconData(
  @SerializedName("uuid")
  val uuid: String,

  @SerializedName("major")
  val major: Int,

  @SerializedName("minor")
  val minor: Int,

  @SerializedName("rssi")
  val rssi: Int,

  @SerializedName("distance")
  val distance: Double
)