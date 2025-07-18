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

// 개선된 샘플 데이터 생성 함수
object PaymentSampleData {

    private val workers = listOf(
        WorkerInfo("w1", "김철수", "보통인부", "010-1234-5678", "123-456-789012", "국민은행", "김철수"),
        WorkerInfo("w2", "이영희", "작업반장", "010-2345-6789", "987-654-321098", "신한은행", "이영희"),
        WorkerInfo("w3", "박민수", "철근공", "010-3456-7890", "456-789-123456", "우리은행", "박민수"),
        WorkerInfo("w4", "최수진", "타일공", "010-4567-8901", "789-123-456789", "하나은행", "최수진"),
        WorkerInfo("w5", "정호준", "용접공", "010-5678-9012", "321-654-987321", "KB국민은행", "정호준"),
        WorkerInfo("w6", "김미영", "미장공", "010-6789-0123", "654-987-321654", "농협은행", "김미영"),
        WorkerInfo("w7", "송대현", "전기공", "010-7890-1234", "147-258-369147", "기업은행", "송대현"),
        WorkerInfo("w8", "이지훈", "배관공", "010-8901-2345", "258-369-147258", "카카오뱅크", "이지훈")
    )

    private val projects = listOf(
        ProjectInfo("p1", "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사", "부산시 사하구 하단동"),
        ProjectInfo("p2", "인천 물류센터 건설", "인천광역시 연수구"),
        ProjectInfo("p3", "부산 교량 보수", "부산광역시 해운대구"),
        ProjectInfo("p4", "서울 아파트 리모델링", "서울시 강남구 역삼동"),
        ProjectInfo("p5", "대구 상업시설 건축", "대구광역시 중구")
    )

    fun getSamplePayments(): List<PaymentData> {
        return listOf(
            // 긴급 지급 건들 (48시간 이내)
            PaymentData(
                id = "pay1",
                worker = workers[0],
                project = projects[0],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(1),
                workHours = 8.0,
                hourlyWage = 18750,
                overtimeHours = 2.0,
                overtimeWage = 28125,
                allowances = 10000,
                deductions = 5000,
                totalAmount = 216250,
                status = PaymentStatus.URGENT,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusHours(24),
                notes = "야근 2시간 포함, 식대 지급"
            ),
            PaymentData(
                id = "pay2",
                worker = workers[1],
                project = projects[0],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(2),
                workHours = 8.0,
                hourlyWage = 25000,
                overtimeHours = 1.0,
                overtimeWage = 37500,
                allowances = 15000,
                deductions = 0,
                totalAmount = 252500,
                status = PaymentStatus.URGENT,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusHours(48),
                notes = "작업반장 수당 포함"
            ),

            // 일반 지급 대기 건들
            PaymentData(
                id = "pay3",
                worker = workers[2],
                project = projects[1],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(3),
                workHours = 8.0,
                hourlyWage = 22500,
                overtimeHours = 0.0,
                overtimeWage = 0,
                allowances = 5000,
                deductions = 3000,
                totalAmount = 182000,
                status = PaymentStatus.PENDING,
                dueDate = LocalDate.now().plusDays(3),
                createdAt = LocalDateTime.now().minusDays(3)
            ),
            PaymentData(
                id = "pay4",
                worker = workers[3],
                project = projects[1],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(4),
                workHours = 8.0,
                hourlyWage = 20000,
                overtimeHours = 3.0,
                overtimeWage = 30000,
                allowances = 8000,
                deductions = 2000,
                totalAmount = 256000,
                status = PaymentStatus.PENDING,
                dueDate = LocalDate.now().plusDays(4),
                createdAt = LocalDateTime.now().minusDays(4),
                notes = "야간 작업 수당 포함"
            ),
            PaymentData(
                id = "pay5",
                worker = workers[4],
                project = projects[2],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(5),
                workHours = 8.0,
                hourlyWage = 24000,
                overtimeHours = 0.5,
                overtimeWage = 36000,
                allowances = 12000,
                deductions = 1000,
                totalAmount = 221000,
                status = PaymentStatus.PENDING,
                dueDate = LocalDate.now().plusDays(5),
                createdAt = LocalDateTime.now().minusDays(5)
            ),

            // 처리 중인 건들
            PaymentData(
                id = "pay6",
                worker = workers[5],
                project = projects[2],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(6),
                workHours = 8.0,
                hourlyWage = 19000,
                overtimeHours = 1.5,
                overtimeWage = 28500,
                allowances = 6000,
                deductions = 0,
                totalAmount = 200750,
                status = PaymentStatus.PROCESSING,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusDays(6),
                notes = "은행 송금 처리 중"
            ),

            // 완료된 건들 (이번 달)
            PaymentData(
                id = "pay7",
                worker = workers[6],
                project = projects[3],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(10),
                workHours = 8.0,
                hourlyWage = 26000,
                overtimeHours = 0.0,
                overtimeWage = 0,
                allowances = 20000,
                deductions = 5000,
                totalAmount = 223000,
                status = PaymentStatus.COMPLETED,
                dueDate = LocalDate.now().minusDays(5),
                createdAt = LocalDateTime.now().minusDays(10),
                processedAt = LocalDateTime.now().minusDays(5),
                notes = "전기 설비 작업 완료"
            ),
            PaymentData(
                id = "pay8",
                worker = workers[7],
                project = projects[3],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(12),
                workHours = 8.0,
                hourlyWage = 21000,
                overtimeHours = 2.0,
                overtimeWage = 31500,
                allowances = 7000,
                deductions = 0,
                totalAmount = 232000,
                status = PaymentStatus.COMPLETED,
                dueDate = LocalDate.now().minusDays(7),
                createdAt = LocalDateTime.now().minusDays(12),
                processedAt = LocalDateTime.now().minusDays(7)
            ),

            // 보너스 지급
            PaymentData(
                id = "pay9",
                worker = workers[1],
                project = projects[4],
                paymentType = PaymentType.BONUS,
                workDate = LocalDate.now().minusDays(7),
                workHours = 0.0,
                hourlyWage = 0,
                overtimeHours = 0.0,
                overtimeWage = 0,
                allowances = 100000,
                deductions = 0,
                totalAmount = 100000,
                status = PaymentStatus.PENDING,
                dueDate = LocalDate.now().plusDays(2),
                createdAt = LocalDateTime.now().minusDays(7),
                notes = "프로젝트 완료 보너스"
            ),

            // 실패한 지급
            PaymentData(
                id = "pay10",
                worker = workers[3],
                project = projects[4],
                paymentType = PaymentType.DAILY_WAGE,
                workDate = LocalDate.now().minusDays(8),
                workHours = 8.0,
                hourlyWage = 20000,
                overtimeHours = 0.0,
                overtimeWage = 0,
                allowances = 5000,
                deductions = 0,
                totalAmount = 165000,
                status = PaymentStatus.FAILED,
                dueDate = LocalDate.now().minusDays(3),
                createdAt = LocalDateTime.now().minusDays(8),
                notes = "계좌번호 오류로 지급 실패"
            )
        )
    }

    fun getSampleSummary(): PaymentSummary {
        val payments = getSamplePayments()

        return PaymentSummary(
            totalPendingAmount = payments
                .filter { it.status == PaymentStatus.PENDING || it.status == PaymentStatus.URGENT }
                .sumOf { it.totalAmount.toLong() },
            totalPendingCount = payments
                .count { it.status == PaymentStatus.PENDING || it.status == PaymentStatus.URGENT },
            urgentPaymentsCount = payments
                .count { it.status == PaymentStatus.URGENT },
            completedThisMonthAmount = payments
                .filter {
                    it.status == PaymentStatus.COMPLETED &&
                            it.processedAt?.month == LocalDateTime.now().month
                }
                .sumOf { it.totalAmount.toLong() },
            completedThisMonthCount = payments
                .count {
                    it.status == PaymentStatus.COMPLETED &&
                            it.processedAt?.month == LocalDateTime.now().month
                },
            averageProcessingTime = 6.5 // 평균 6.5시간
        )
    }

    // 빈 상태를 위한 함수
    fun getEmptyPayments(): List<PaymentData> {
        return emptyList()
    }

    // 빈 요약 정보
    fun getEmptySummary(): PaymentSummary {
        return PaymentSummary(
            totalPendingAmount = 0L,
            totalPendingCount = 0,
            urgentPaymentsCount = 0,
            completedThisMonthAmount = 0L,
            completedThisMonthCount = 0,
            averageProcessingTime = 0.0
        )
    }
}