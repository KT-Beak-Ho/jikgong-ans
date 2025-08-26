package com.billcorea.jikgong.network.model.payment

import com.billcorea.jikgong.network.model.common.*

/**
 * 결제/정산 데이터
 */
data class PaymentData(
  val id: String,
  val projectId: String,
  val workerId: String,
  val companyId: String,

  // ===== 금액 정보 =====
  val baseAmount: Long,
  val workHours: Double,
  val overtimeHours: Double = 0.0,
  val deductions: PaymentDeductions,
  val bonuses: PaymentBonuses,
  val finalAmount: Long,

  // ===== 지급 정보 =====
  val paymentMethod: PaymentMethod,
  val paymentStatus: PaymentStatus,
  val scheduledDate: String,
  val actualPaymentDate: String? = null,
  val bankTransferInfo: BankTransferInfo? = null,

  // ===== 기타 =====
  val invoice: Invoice? = null,
  val notes: String? = null,
  val createdAt: String,
  val updatedAt: String
)

/**
 * 공제 내역
 */
data class PaymentDeductions(
  val incomeTax: Long,
  val nationalPension: Long,
  val healthInsurance: Long,
  val employmentInsurance: Long,
  val other: Long = 0L,
  val total: Long
)

/**
 * 보너스 내역
 */
data class PaymentBonuses(
  val performance: Long = 0L,
  val overtime: Long = 0L,
  val weekend: Long = 0L,
  val night: Long = 0L,
  val special: Long = 0L,
  val total: Long
)

/**
 * 계좌 이체 정보
 */
data class BankTransferInfo(
  val bankName: String,
  val accountNumber: String,
  val accountHolder: String,
  val transactionId: String? = null
)

/**
 * 송장
 */
data class Invoice(
  val number: String,
  val issueDate: String,
  val dueDate: String,
  val items: List<InvoiceItem>,
  val totalAmount: Long,
  val taxAmount: Long,
  val pdfUrl: String? = null
)

/**
 * 송장 항목
 */
data class InvoiceItem(
  val description: String,
  val quantity: Double,
  val unitPrice: Long,
  val amount: Long
)

/**
 * 정산
 */
data class Settlement(
  val id: String,
  val projectId: String,
  val companyId: String,
  val totalAmount: Long,
  val paidAmount: Long,
  val remainingAmount: Long,
  val settlementDate: String,
  val status: PaymentStatus
)