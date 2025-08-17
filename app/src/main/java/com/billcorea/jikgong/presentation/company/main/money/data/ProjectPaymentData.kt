package com.billcorea.jikgong.presentation.company.main.money.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

// 프로젝트별 결제 상태
enum class ProjectPaymentStatus {
    PENDING,     // 지급 대기
    PROCESSING,  // 처리중
    COMPLETED,   // 지급 완료
    FAILED       // 지급 실패
}

// 개별 작업자 결제 정보
data class WorkerPaymentInfo(
    val workerId: String,
    val workerName: String,
    val jobType: String,
    val workDate: LocalDate,
    val workHours: Double,
    val hourlyWage: Int,
    val totalAmount: Long,
    val deductionAmount: Long = 0, // 공제액
    val finalAmount: Long,         // 실지급액
    val status: ProjectPaymentStatus,
    val paymentDate: LocalDateTime? = null
)

// 프로젝트별 결제 정보
data class ProjectPaymentData(
    val id: String,
    val projectTitle: String,
    val projectLocation: String,
    val workers: List<WorkerPaymentInfo>,
    val totalAmount: Long,
    val serviceFeeReduction: Long, // 서비스 이용으로 인한 수수료 절감액
    val originalServiceFee: Long,  // 기존 수수료 (10%)
    val currentServiceFee: Long,   // 현재 수수료 (5%)
    val totalSavings: Long,        // 총 절감액
    val status: ProjectPaymentStatus,
    val workStartDate: LocalDate,
    val workEndDate: LocalDate,
    val notes: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    // 총 작업자 수
    val totalWorkers: Int
        get() = workers.size

    // 지급 완료된 작업자 수
    val completedWorkers: Int
        get() = workers.count { it.status == ProjectPaymentStatus.COMPLETED }

    // 상태별 색상
    val statusColor: Color
        get() = when (status) {
            ProjectPaymentStatus.PENDING -> Color(0xFFFFA726)
            ProjectPaymentStatus.PROCESSING -> Color(0xFF42A5F5)
            ProjectPaymentStatus.COMPLETED -> Color(0xFF66BB6A)
            ProjectPaymentStatus.FAILED -> Color(0xFFEF5350)
        }

    // 절감률 계산
    val savingsPercentage: Float
        get() = if (originalServiceFee > 0) {
            (totalSavings.toFloat() / originalServiceFee.toFloat()) * 100
        } else 0f
}

// 프로젝트별 결제 통계
data class ProjectPaymentSummary(
    val totalProjects: Int = 0,
    val totalAmount: Long = 0L,
    val pendingProjects: Int = 0,
    val pendingAmount: Long = 0L,
    val completedProjects: Int = 0,
    val completedAmount: Long = 0L,
    val monthlyTotal: Long = 0L,
    val totalServiceFeeSavings: Long = 0L, // 총 수수료 절감액
    val averageSavingsPerProject: Long = 0L // 프로젝트당 평균 절감액
)