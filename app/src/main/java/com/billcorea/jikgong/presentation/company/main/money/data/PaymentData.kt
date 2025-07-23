package com.billcorea.jikgong.presentation.company.main.money.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

// 지급 상태
enum class PaymentStatus {
    PENDING,     // 지급 대기
    URGENT,      // 긴급 (48시간 이내)
    PROCESSING,  // 처리 중
    COMPLETED,   // 지급 완료
    FAILED       // 지급 실패
}

// 지급 유형
enum class PaymentType {
    DAILY_WAGE,      // 일당
    OVERTIME,        // 야근비
    BONUS,           // 보너스
    ALLOWANCE        // 수당
}

// 근로자 정보
data class WorkerInfo(
    val id: String,
    val name: String,
    val jobType: String,
    val phoneNumber: String,
    val bankAccount: String,
    val bankName: String,
    val accountHolder: String
)

// 프로젝트 정보
data class ProjectInfo(
    val id: String,
    val name: String,
    val location: String
)

// 지급 데이터 모델
data class PaymentData(
    val id: String,
    val worker: WorkerInfo,
    val project: ProjectInfo,
    val paymentType: PaymentType,
    val workDate: LocalDate,
    val workHours: Double,
    val hourlyWage: Int,
    val overtimeHours: Double = 0.0,
    val overtimeWage: Int = 0,
    val allowances: Int = 0,
    val deductions: Int = 0,
    val totalAmount: Int,
    val status: PaymentStatus,
    val dueDate: LocalDate,
    val createdAt: LocalDateTime,
    val processedAt: LocalDateTime? = null,
    val notes: String = ""
) {
    // 기본 일당
    val basicWage: Int
        get() = (workHours * hourlyWage).toInt()

    // 야근비
    val overtimePay: Int
        get() = (overtimeHours * overtimeWage).toInt()

    // 순 지급액
    val netAmount: Int
        get() = totalAmount - deductions

    // 상태별 색상
    val statusColor: Color
        get() = when (status) {
            PaymentStatus.PENDING -> Color.Gray
            PaymentStatus.URGENT -> Color.Red
            PaymentStatus.PROCESSING -> Color.Blue
            PaymentStatus.COMPLETED -> Color.Green
            PaymentStatus.FAILED -> Color.Red
        }

    // 상태별 한글명
    val statusDisplayName: String
        get() = when (status) {
            PaymentStatus.PENDING -> "지급 대기"
            PaymentStatus.URGENT -> "긴급"
            PaymentStatus.PROCESSING -> "처리 중"
            PaymentStatus.COMPLETED -> "지급 완료"
            PaymentStatus.FAILED -> "지급 실패"
        }

    // 지급 마감까지 남은 일수
    val daysUntilDue: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), dueDate)

    // 긴급 여부 (48시간 이내)
    val isUrgent: Boolean
        get() = daysUntilDue <= 2 && status != PaymentStatus.COMPLETED
}

// 지급 요약 정보
data class PaymentSummary(
    val totalPendingAmount: Long,
    val totalPendingCount: Int,
    val urgentPaymentsCount: Int,
    val completedThisMonthAmount: Long,
    val completedThisMonthCount: Int,
    val averageProcessingTime: Double // 평균 처리 시간 (시간)
)

// 지급 통계
data class PaymentStatistics(
    val dailyPayments: Map<LocalDate, List<PaymentData>>,
    val monthlyTotal: Map<String, Long>, // "2025-01" -> 총액
    val workerPayments: Map<String, Long>, // 근로자별 총 지급액
    val projectPayments: Map<String, Long> // 프로젝트별 총 지급액
)