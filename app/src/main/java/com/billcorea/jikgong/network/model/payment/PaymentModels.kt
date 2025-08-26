// ============================================
// PaymentModels.kt - 결제/급여 관련 모델 (완전판)
// ============================================
package com.billcorea.jikgong.network.model.payment

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime

// ===== 기본 Payment 데이터 =====
data class PaymentData(
  @SerializedName("id")
  val id: String? = null,

  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("paymentType")
  val paymentType: PaymentType,

  @SerializedName("amount")
  val amount: Long,

  @SerializedName("baseAmount")
  val baseAmount: Long,

  @SerializedName("workHours")
  val workHours: Double? = null,

  @SerializedName("overtimeHours")
  val overtimeHours: Double? = null,

  @SerializedName("deductions")
  val deductions: PaymentDeductions? = null,

  @SerializedName("bonuses")
  val bonuses: PaymentBonuses? = null,

  @SerializedName("finalAmount")
  val finalAmount: Long,

  @SerializedName("paymentDate")
  val paymentDate: String,

  @SerializedName("scheduledDate")
  val scheduledDate: String,

  @SerializedName("actualPaymentDate")
  val actualPaymentDate: String? = null,

  @SerializedName("paymentMethod")
  val paymentMethod: PaymentMethod,

  @SerializedName("status")
  val status: PaymentStatus,

  @SerializedName("paymentStatus")
  val paymentStatus: PaymentStatus? = null,

  @SerializedName("bankTransferInfo")
  val bankTransferInfo: BankTransferInfo? = null,

  @SerializedName("invoice")
  val invoice: String? = null,

  @SerializedName("notes")
  val notes: String? = null,

  @SerializedName("description")
  val description: String? = null,

  @SerializedName("createdAt")
  val createdAt: String? = null,

  @SerializedName("updatedAt")
  val updatedAt: String? = null
)

// ===== Payment Enums =====
enum class PaymentType {
  DAILY,
  WEEKLY,
  MONTHLY,
  PROJECT,
  BONUS,
  OVERTIME,
  ADJUSTMENT
}

enum class PaymentMethod {
  BANK_TRANSFER,
  CASH,
  CHECK,
  DIGITAL_PAYMENT,
  OTHER
}

enum class PaymentStatus {
  PENDING,
  SCHEDULED,
  PROCESSING,
  COMPLETED,
  FAILED,
  CANCELLED,
  REFUNDED
}

// ===== 공제 및 보너스 =====
data class PaymentDeductions(
  @SerializedName("tax")
  val tax: Long = 0,

  @SerializedName("insurance")
  val insurance: Long = 0,

  @SerializedName("pension")
  val pension: Long = 0,

  @SerializedName("other")
  val other: Long = 0,

  @SerializedName("total")
  val total: Long = 0
)

data class PaymentBonuses(
  @SerializedName("overtime")
  val overtime: Long = 0,

  @SerializedName("performance")
  val performance: Long = 0,

  @SerializedName("meal")
  val meal: Long = 0,

  @SerializedName("transport")
  val transport: Long = 0,

  @SerializedName("other")
  val other: Long = 0,

  @SerializedName("total")
  val total: Long = 0
)

// ===== 은행 이체 정보 =====
data class BankTransferInfo(
  @SerializedName("bankName")
  val bankName: String,

  @SerializedName("accountNumber")
  val accountNumber: String,

  @SerializedName("accountHolder")
  val accountHolder: String,

  @SerializedName("transferDate")
  val transferDate: String? = null,

  @SerializedName("transferReference")
  val transferReference: String? = null
)

// ===== Payment 상태 업데이트 =====
data class PaymentStatusUpdate(
  @SerializedName("status")
  val status: PaymentStatus,

  @SerializedName("reason")
  val reason: String? = null,

  @SerializedName("updatedBy")
  val updatedBy: String? = null
)

// ===== Payment 요청/생성 =====
data class PaymentRequest(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("paymentType")
  val paymentType: PaymentType,

  @SerializedName("amount")
  val amount: Long,

  @SerializedName("paymentMethod")
  val paymentMethod: PaymentMethod,

  @SerializedName("scheduledDate")
  val scheduledDate: String,

  @SerializedName("description")
  val description: String? = null
)

// ===== Payment 일괄 처리 =====
data class BatchPaymentRequest(
  @SerializedName("projectId")
  val projectId: String,

  @SerializedName("paymentDate")
  val paymentDate: String,

  @SerializedName("payments")
  val payments: List<PaymentItem>
)

data class PaymentItem(
  @SerializedName("workerId")
  val workerId: String,

  @SerializedName("amount")
  val amount: Long,

  @SerializedName("workHours")
  val workHours: Double,

  @SerializedName("overtimeHours")
  val overtimeHours: Double = 0.0
)

// ===== Payment 통계 =====
data class PaymentSummary(
  @SerializedName("totalPaid")
  val totalPaid: Long,

  @SerializedName("totalPending")
  val totalPending: Long,

  @SerializedName("totalScheduled")
  val totalScheduled: Long,

  @SerializedName("averagePayment")
  val averagePayment: Long,

  @SerializedName("paymentCount")
  val paymentCount: Int,

  @SerializedName("period")
  val period: String
)

// ===== Payment 이력 =====
data class PaymentHistory(
  @SerializedName("paymentId")
  val paymentId: String,

  @SerializedName("status")
  val status: PaymentStatus,

  @SerializedName("changedAt")
  val changedAt: String,

  @SerializedName("changedBy")
  val changedBy: String,

  @SerializedName("reason")
  val reason: String? = null
)

// ===== 급여 명세서 =====
data class PaySlip(
  @SerializedName("paymentId")
  val paymentId: String,

  @SerializedName("workerName")
  val workerName: String,

  @SerializedName("projectName")
  val projectName: String,

  @SerializedName("period")
  val period: String,

  @SerializedName("workDays")
  val workDays: Int,

  @SerializedName("totalHours")
  val totalHours: Double,

  @SerializedName("baseAmount")
  val baseAmount: Long,

  @SerializedName("overtimeAmount")
  val overtimeAmount: Long,

  @SerializedName("bonuses")
  val bonuses: PaymentBonuses,

  @SerializedName("deductions")
  val deductions: PaymentDeductions,

  @SerializedName("netAmount")
  val netAmount: Long,

  @SerializedName("issuedDate")
  val issuedDate: String
)