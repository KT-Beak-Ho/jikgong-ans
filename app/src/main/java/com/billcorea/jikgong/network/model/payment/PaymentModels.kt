package com.billcorea.jikgong.network.model.payment

import com.billcorea.jikgong.network.model.common.*

/**
 * 결제/정산 관련 모델
 */

// ============================================
// 결제 데이터
// ============================================
data class PaymentData(
  val id: String,
  val paymentType: PaymentType,
  val amount: Long,
  val status: PaymentStatus,

  // ===== 관계 정보 =====
  val workerId: String? = null,
  val companyId: String? = null,
  val projectId: String? = null,
  val attendanceIds: List<String> = emptyList(),

  // ===== 결제 정보 =====
  val paymentMethod: PaymentMethod,
  val paymentDate: String? = null,
  val dueDate: String? = null,
  val completedAt: String? = null,

  // ===== 계좌 정보 =====
  val accountHolder: String? = null,
  val accountNumber: String? = null,
  val bankName: String? = null,

  // ===== 상세 정보 =====
  val description: String? = null,
  val memo: String? = null,
  val breakdown: PaymentBreakdown? = null,

  // ===== 영수증 =====
  val receiptUrl: String? = null,
  val invoiceNumber: String? = null,

  val createdAt: String,
  val updatedAt: String
)

// ============================================
// 결제 상세 내역
// ============================================
data class PaymentBreakdown(
  val basicWage: Long,
  val overtimeWage: Long = 0,
  val nightWage: Long = 0,
  val weekendWage: Long = 0,
  val bonuses: Long = 0,
  val deductions: Long = 0,
  val tax: Long = 0,
  val totalAmount: Long
)

// ============================================
// 요청/응답 모델
// ============================================
data class PaymentStatusUpdate(
  val status: PaymentStatus,
  val reason: String? = null
)

data class CancelReason(
  val reason: String,
  val refundAmount: Long? = null
)

data class PaymentProcessRequest(
  val confirmationNumber: String? = null,
  val processedBy: String? = null
)

data class BatchPaymentRequest(
  val paymentIds: List<String>,
  val processDate: String? = null
)

data class BatchPaymentResult(
  val totalCount: Int,
  val successCount: Int,
  val failedCount: Int,
  val results: List<PaymentProcessResult>
)

data class PaymentProcessResult(
  val paymentId: String,
  val success: Boolean,
  val message: String? = null
)

// ============================================
// 정산 관련
// ============================================
data class SettlementRequest(
  val projectId: String,
  val startDate: String,
  val endDate: String,
  val workerIds: List<String>,
  val settlementType: SettlementType
)

data class SettlementData(
  val id: String,
  val projectId: String,
  val period: SettlementPeriod,
  val totalAmount: Long,
  val status: SettlementStatus,
  val details: List<SettlementDetail>,
  val approvedBy: String? = null,
  val approvedAt: String? = null,
  val createdAt: String,
  val updatedAt: String
)

data class SettlementPeriod(
  val startDate: String,
  val endDate: String
)

data class SettlementDetail(
  val workerId: String,
  val workerName: String,
  val totalWorkDays: Int,
  val totalWorkHours: Double,
  val totalAmount: Long,
  val paymentStatus: PaymentStatus
)

enum class SettlementType {
  WEEKLY,
  BIWEEKLY,
  MONTHLY,
  PROJECT_END
}

enum class SettlementStatus {
  PENDING,
  REVIEWING,
  APPROVED,
  PROCESSING,
  COMPLETED,
  CANCELLED
}

// ============================================
// 송금 관련
// ============================================
data class TransferRequest(
  val fromAccountId: String,
  val toAccountId: String,
  val amount: Long,
  val description: String? = null,
  val transferType: TransferType
)

data class TransferResult(
  val transferId: String,
  val status: TransferStatus,
  val transactionNumber: String,
  val processedAt: String,
  val fee: Long? = null
)

data class TransferData(
  val id: String,
  val fromAccount: String,
  val toAccount: String,
  val amount: Long,
  val fee: Long,
  val status: TransferStatus,
  val type: TransferType,
  val description: String? = null,
  val createdAt: String,
  val completedAt: String? = null
)

enum class TransferType {
  WAGE_PAYMENT,
  REFUND,
  ADJUSTMENT,
  OTHER
}

enum class TransferStatus {
  PENDING,
  PROCESSING,
  COMPLETED,
  FAILED,
  CANCELLED
}

// ============================================
// 통계
// ============================================
data class PaymentStatistics(
  val totalAmount: Long,
  val paidAmount: Long,
  val pendingAmount: Long,
  val averageAmount: Long,
  val paymentCount: Int,
  val completedCount: Int,
  val pendingCount: Int,
  val failedCount: Int
)

data class MonthlySummary(
  val year: Int,
  val month: Int,
  val totalIncome: Long,
  val totalExpense: Long,
  val netAmount: Long,
  val paymentsByCategory: Map<PaymentType, Long>,
  val dailyBreakdown: List<DailyPayment>
)

data class DailyPayment(
  val date: String,
  val income: Long,
  val expense: Long
)