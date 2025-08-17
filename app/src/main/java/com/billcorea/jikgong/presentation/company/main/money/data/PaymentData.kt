package com.billcorea.jikgong.presentation.company.main.money.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

// 결제 상태
enum class PaymentStatus {
    PENDING,     // 대기중
    PROCESSING,  // 처리중
    COMPLETED,   // 완료
    FAILED,      // 실패
    CANCELLED    // 취소
}

// 임금 타입
enum class WageType {
    DAILY,       // 일당
    HOURLY,      // 시급
    PROJECT      // 프로젝트
}

// 작업자 정보
data class WorkerInfo(
    val id: String,
    val name: String,
    val phone: String,
    val jobType: String,
    val experienceLevel: String,
    val profileImageUrl: String? = null
)

// 임금 지급 데이터
data class PaymentData(
    val id: String,
    val projectId: String,
    val projectTitle: String,
    val worker: WorkerInfo,
    val workDate: LocalDate,
    val startTime: String,
    val endTime: String,
    val totalHours: Double,
    val wageType: WageType,
    val wagePerHour: Int,
    val totalWage: Long,
    val deductions: Long = 0, // 공제액
    val finalAmount: Long,    // 실지급액
    val status: PaymentStatus,
    val paymentDate: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val notes: String = ""
) {
    // 상태별 색상
    val statusColor: Color
        get() = when (status) {
            PaymentStatus.PENDING -> Color(0xFFFFA726)
            PaymentStatus.PROCESSING -> Color(0xFF42A5F5)
            PaymentStatus.COMPLETED -> Color(0xFF66BB6A)
            PaymentStatus.FAILED -> Color(0xFFEF5350)
            PaymentStatus.CANCELLED -> Color(0xFF9E9E9E)
        }

    // 상태별 한글명
    val statusDisplayName: String
        get() = when (status) {
            PaymentStatus.PENDING -> "지급 대기"
            PaymentStatus.PROCESSING -> "처리중"
            PaymentStatus.COMPLETED -> "지급 완료"
            PaymentStatus.FAILED -> "지급 실패"
            PaymentStatus.CANCELLED -> "취소"
        }

    // 작업 시간 표시
    val workTimeDisplay: String
        get() = "$startTime - $endTime (${totalHours}시간)"
}

// 임금 통계 데이터
data class PaymentSummary(
    val totalPayments: Int = 0,
    val totalAmount: Long = 0L,
    val pendingCount: Int = 0,
    val pendingAmount: Long = 0L,
    val completedCount: Int = 0,
    val completedAmount: Long = 0L,
    val monthlyTotal: Long = 0L,
    val weeklyTotal: Long = 0L
)

// 필터 옵션
data class PaymentFilter(
    val status: PaymentStatus? = null,
    val dateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val projectId: String? = null,
    val workerId: String? = null
)