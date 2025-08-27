package com.billcorea.jikgong.network.models

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

// ==================== 결제 상태 ====================

enum class PaymentStatus {
    PENDING,     // 대기중
    PROCESSING,  // 처리중
    COMPLETED,   // 완료
    FAILED,      // 실패
    CANCELLED    // 취소
}

enum class ProjectPaymentStatus {
    PENDING,     // 지급 대기
    PROCESSING,  // 처리중
    COMPLETED,   // 지급 완료
    FAILED,      // 지급 실패
    OVERDUE      // 연체
}

enum class WageType {
    DAILY,       // 일당
    HOURLY,      // 시급
    PROJECT      // 프로젝트
}

// ==================== 작업자 정보 ====================

data class WorkerInfo(
    val id: String,
    val name: String,
    val phone: String,
    val jobType: String,
    val experienceLevel: String,
    val profileImageUrl: String? = null
)

// ==================== 임금 지급 데이터 ====================

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

// ==================== 프로젝트별 결제 정보 ====================

data class ProjectPaymentData(
    val id: String,
    val projectTitle: String,
    val projectId: String,
    val company: String,
    val location: String,
    val workDate: LocalDate,
    val status: ProjectPaymentStatus,
    val workers: List<WorkerPaymentInfo>,
    val totalAmount: Long,
    val paidAmount: Long,
    val pendingAmount: Long,
    val createdAt: LocalDateTime,
    val completedAt: LocalDateTime? = null
) {
    // Nested data class for worker payment info
    data class WorkerPaymentInfo(
        val workerId: String,
        val workerName: String,
        val jobType: String,
        val hoursWorked: Double,
        val hourlyRate: Int,
        val totalAmount: Long,
        val isPaid: Boolean,
        val paidAt: LocalDateTime? = null
    )

    // 총 작업자 수
    val totalWorkers: Int
        get() = workers.size

    // 지급 완료된 작업자 수
    val completedWorkers: Int
        get() = workers.count { it.isPaid }

    // 상태별 색상
    val statusColor: Color
        get() = when (status) {
            ProjectPaymentStatus.PENDING -> Color(0xFFFFA726)
            ProjectPaymentStatus.PROCESSING -> Color(0xFF42A5F5)
            ProjectPaymentStatus.COMPLETED -> Color(0xFF66BB6A)
            ProjectPaymentStatus.FAILED -> Color(0xFFEF5350)
            ProjectPaymentStatus.OVERDUE -> Color(0xFFD32F2F)
        }
}

// ==================== 통계 데이터 ====================

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

data class ProjectPaymentSummary(
    val totalProjects: Int = 0,
    val completedPayments: Int = 0,
    val pendingPayments: Int = 0,
    val totalAmount: Long = 0L,
    val paidAmount: Long = 0L,
    val pendingAmount: Long = 0L,
    val overdueCount: Int = 0
)

// ==================== 필터 옵션 ====================

data class PaymentFilter(
    val status: PaymentStatus? = null,
    val dateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val projectId: String? = null,
    val workerId: String? = null
)