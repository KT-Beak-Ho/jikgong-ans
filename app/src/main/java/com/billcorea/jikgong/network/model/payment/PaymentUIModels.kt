package com.billcorea.jikgong.network.model.payment

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * UI용 결제 상태 (Presentation Layer용)
 * network의 PaymentStatus와 분리
 */
enum class PaymentUIStatus {
  PENDING,     // 대기중
  PROCESSING,  // 처리중
  COMPLETED,   // 완료
  FAILED,      // 실패
  CANCELLED,   // 취소
  OVERDUE      // 지연 (UI 전용)
}

/**
 * UI용 임금 타입
 */
enum class WageUIType {
  DAILY,       // 일당
  HOURLY,      // 시급
  PROJECT      // 프로젝트
}

/**
 * UI용 작업자 정보
 */
data class WorkerUIInfo(
  val id: String,
  val name: String,
  val phone: String,
  val jobType: String,
  val experienceLevel: String,
  val profileImageUrl: String? = null
)

/**
 * UI용 임금 지급 데이터
 * CompanyMoneyScreen에서 사용
 */
data class PaymentUIData(
  val id: String,
  val projectId: String,
  val projectTitle: String,
  val worker: WorkerUIInfo,
  val workDate: LocalDate,
  val startTime: String,
  val endTime: String,
  val totalHours: Double,
  val wageType: WageUIType,
  val wagePerHour: Int,
  val totalWage: Long,
  val deductions: Long = 0,
  val finalAmount: Long,
  val status: PaymentUIStatus,
  val paymentDate: LocalDateTime? = null,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now(),
  val notes: String = ""
) {
  // UI 표시용 속성들
  val statusColor: Color
    get() = when (status) {
      PaymentUIStatus.PENDING -> Color(0xFFFFA726)
      PaymentUIStatus.PROCESSING -> Color(0xFF42A5F5)
      PaymentUIStatus.COMPLETED -> Color(0xFF66BB6A)
      PaymentUIStatus.FAILED -> Color(0xFFEF5350)
      PaymentUIStatus.CANCELLED -> Color(0xFF9E9E9E)
      PaymentUIStatus.OVERDUE -> Color(0xFFFF5722)
    }

  val statusDisplayName: String
    get() = when (status) {
      PaymentUIStatus.PENDING -> "지급 대기"
      PaymentUIStatus.PROCESSING -> "처리중"
      PaymentUIStatus.COMPLETED -> "지급 완료"
      PaymentUIStatus.FAILED -> "지급 실패"
      PaymentUIStatus.CANCELLED -> "취소"
      PaymentUIStatus.OVERDUE -> "지급 지연"
    }

  val workTimeDisplay: String
    get() = "$startTime - $endTime (${totalHours}시간)"

  val formattedAmount: String
    get() = "${String.format(Locale.KOREA, "%,d", finalAmount)}원"

  val formattedWorkDate: String
    get() = workDate.format(DateTimeFormatter.ofPattern("MM/dd (E)", Locale.KOREAN))

  val formattedPaymentDate: String?
    get() = paymentDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}

/**
 * UI용 임금 통계 데이터
 */
data class PaymentUISummary(
  val totalPayments: Int = 0,
  val totalAmount: Long = 0L,
  val pendingCount: Int = 0,
  val pendingAmount: Long = 0L,
  val completedCount: Int = 0,
  val completedAmount: Long = 0L,
  val overdueCount: Int = 0,
  val overdueAmount: Long = 0L,
  val monthlyTotal: Long = 0L,
  val weeklyTotal: Long = 0L,
  val totalPendingAmount: Long = 0L,
  val totalCompletedAmount: Long = 0L,
  val thisMonthAmount: Long = 0L
) {
  val formattedTotalAmount: String
    get() = "₩${String.format(Locale.KOREA, "%,d", totalAmount)}"

  val formattedPendingAmount: String
    get() = "₩${String.format(Locale.KOREA, "%,d", pendingAmount)}"

  val formattedCompletedAmount: String
    get() = "₩${String.format(Locale.KOREA, "%,d", completedAmount)}"
}

/**
 * UI용 필터 옵션
 */
data class PaymentUIFilter(
  val status: PaymentUIStatus? = null,
  val dateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
  val projectId: String? = null,
  val workerId: String? = null
)

/**
 * UI용 지급 기간
 */
enum class PaymentUIPeriod(val displayName: String) {
  ALL("전체"),
  THIS_WEEK("이번 주"),
  THIS_MONTH("이번 달"),
  LAST_MONTH("지난 달")
}

/**
 * UI용 정렬 기준
 */
enum class PaymentUISortBy {
  DATE,           // 날짜순
  AMOUNT,         // 금액순
  WORKER_NAME,    // 작업자명순
  STATUS,         // 상태순
  PROJECT_TITLE   // 프로젝트명순
}

/**
 * PaymentData(Network) -> PaymentUIData(UI) 변환
 */
fun PaymentData.toUIModel(): PaymentUIData {
  return PaymentUIData(
    id = this.id,
    projectId = this.projectId,
    projectTitle = "프로젝트 #${this.projectId}", // 실제로는 조인해서 가져와야 함
    worker = WorkerUIInfo(
      id = this.workerId,
      name = "작업자", // 실제로는 조인해서 가져와야 함
      phone = "010-0000-0000",
      jobType = "일반공",
      experienceLevel = "중급"
    ),
    workDate = LocalDate.parse(this.scheduledDate.substring(0, 10)),
    startTime = "09:00",
    endTime = "18:00",
    totalHours = this.workHours,
    wageType = when (this.paymentMethod) {
      PaymentMethod.BANK_TRANSFER -> WageUIType.DAILY
      PaymentMethod.CASH -> WageUIType.HOURLY
      else -> WageUIType.PROJECT
    },
    wagePerHour = (this.baseAmount / this.workHours).toInt(),
    totalWage = this.baseAmount,
    deductions = this.deductions.total,
    finalAmount = this.finalAmount,
    status = when (this.paymentStatus) {
      PaymentStatus.PENDING -> PaymentUIStatus.PENDING
      PaymentStatus.PROCESSING -> PaymentUIStatus.PROCESSING
      PaymentStatus.COMPLETED -> PaymentUIStatus.COMPLETED
      PaymentStatus.FAILED -> PaymentUIStatus.FAILED
      PaymentStatus.CANCELLED -> PaymentUIStatus.CANCELLED
      else -> PaymentUIStatus.PENDING
    },
    paymentDate = this.actualPaymentDate?.let {
      LocalDateTime.parse(it)
    },
    createdAt = LocalDateTime.parse(this.createdAt),
    updatedAt = LocalDateTime.parse(this.updatedAt),
    notes = this.notes ?: ""
  )
}

/**
 * PaymentUIData(UI) -> PaymentData(Network) 변환
 */
fun PaymentUIData.toNetworkModel(): PaymentData {
  return PaymentData(
    id = this.id,
    projectId = this.projectId,
    workerId = this.worker.id,
    companyId = "company_001", // 실제로는 context에서 가져와야 함
    baseAmount = this.totalWage,
    workHours = this.totalHours,
    overtimeHours = 0.0,
    deductions = PaymentDeductions(
      incomeTax = (this.deductions * 0.4).toLong(),
      nationalPension = (this.deductions * 0.2).toLong(),
      healthInsurance = (this.deductions * 0.2).toLong(),
      employmentInsurance = (this.deductions * 0.1).toLong(),
      other = (this.deductions * 0.1).toLong(),
      total = this.deductions
    ),
    bonuses = PaymentBonuses(
      performance = 0L,
      overtime = 0L,
      weekend = 0L,
      night = 0L,
      special = 0L,
      total = 0L
    ),
    finalAmount = this.finalAmount,
    paymentMethod = PaymentMethod.BANK_TRANSFER,
    paymentStatus = when (this.status) {
      PaymentUIStatus.PENDING -> PaymentStatus.PENDING
      PaymentUIStatus.PROCESSING -> PaymentStatus.PROCESSING
      PaymentUIStatus.COMPLETED -> PaymentStatus.COMPLETED
      PaymentUIStatus.FAILED -> PaymentStatus.FAILED
      PaymentUIStatus.CANCELLED -> PaymentStatus.CANCELLED
      PaymentUIStatus.OVERDUE -> PaymentStatus.PENDING // OVERDUE는 UI 전용
    },
    scheduledDate = this.workDate.toString(),
    actualPaymentDate = this.paymentDate?.toString(),
    bankTransferInfo = null,
    invoice = null,
    notes = this.notes,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString()
  )
}