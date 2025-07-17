package com.billcorea.jikgong.presentation.company.main.money.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

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
        get() = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate)

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

// 샘플 데이터 생성 함수
object PaymentSampleData {
    fun getSamplePayments(): List<PaymentData> {
        val workers = listOf(
            WorkerInfo("w1", "김철수", "보통인부", "010-1234-5678", "123-456-789", "국민은행", "김철수"),
            WorkerInfo("w2", "이영희", "작업반장", "010-2345-6789", "987-654-321", "신한은행", "이영희"),
            WorkerInfo("w3", "박민수", "철근공", "010-3456-7890", "456-789-123", "우리은행", "박민수")
        )

        val projects = listOf(
            ProjectInfo("p1", "강남구 아파트 신축", "서울시 강남구"),
            ProjectInfo("p2", "인천 물류센터 건설", "인천광역시 연수구"),
            ProjectInfo("p3", "부산 교량 보수", "부산광역시 해운대구")
        )

        return listOf(
            PaymentData(
                id = "pay1",
                worker = workers[0],
                project = projects[0],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(1),
                workHours = 8.0,
                hourlyWage = 18750, // 150,000원 / 8시간
                overtimeHours = 2.0,
                overtimeWage = 28125, // 1.5배
                allowances = 10000,
                deductions = 5000,
                totalAmount = 216250,
                status = PaymentStatus.URGENT,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusHours(24),
                notes = "야근 2시간 포함"
            ),
            PaymentData(
                id = "pay2",
                worker = workers[1],
                project = projects[1],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(2),
                workHours = 8.0,
                hourlyWage = 25000, // 작업반장 200,000원 / 8시간
                overtimeHours = 0.0,
                overtimeWage = 0,
                allowances = 15000,
                deductions = 0,
                totalAmount = 215000,
                status = PaymentStatus.PENDING,
                dueDate = LocalDate.now().plusDays(3),
                createdAt = LocalDateTime.now().minusHours(48)
            ),
            PaymentData(
                id = "pay3",
                worker = workers[2],
                project = projects[2],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(7),
                workHours = 8.0,
                hourlyWage = 22500, // 철근공 180,000원 / 8시간
                overtimeHours = 1.0,
                overtimeWage = 33750,
                allowances = 5000,
                deductions = 3000,
                totalAmount = 215750,
                status = PaymentStatus.COMPLETED,
                dueDate = LocalDate.now().minusDays(5),
                createdAt = LocalDateTime.now().minusDays(7),
                processedAt = LocalDateTime.now().minusDays(5)
            )
        )
    }

    fun getSampleSummary(): PaymentSummary {
        return PaymentSummary(
            totalPendingAmount = 431250L,
            totalPendingCount = 2,
            urgentPaymentsCount = 1,
            completedThisMonthAmount = 2158750L,
            completedThisMonthCount = 10,
            averageProcessingTime = 6.5
        )
    }
}