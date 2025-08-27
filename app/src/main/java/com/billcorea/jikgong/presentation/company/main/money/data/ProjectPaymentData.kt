package com.billcorea.jikgong.presentation.company.main.money.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

// 프로젝트별 결제 상태
enum class ProjectPaymentStatus {
    PENDING,     // 지급 대기
    PROCESSING,  // 처리중
    COMPLETED,   // 지급 완료
    FAILED,      // 지급 실패
    OVERDUE      // 연체
}


// 프로젝트별 결제 정보
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

// 프로젝트별 결제 통계
data class ProjectPaymentSummary(
    val totalProjects: Int = 0,
    val completedPayments: Int = 0,
    val pendingPayments: Int = 0,
    val totalAmount: Long = 0L,
    val paidAmount: Long = 0L,
    val pendingAmount: Long = 0L,
    val overdueCount: Int = 0
)