// app/src/main/java/com/billcorea/jikgong/presentation/company/main/money/CompanyMoneyViewModel.kt
package com.billcorea.jikgong.presentation.company.main.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 지급 관리 화면의 ViewModel
 */
class CompanyMoneyViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyMoneyUiState())
  val uiState: StateFlow<CompanyMoneyUiState> = _uiState.asStateFlow()

  init {
    loadPaymentData()
  }

  fun onEvent(event: CompanyMoneyEvent) {
    when (event) {
      is CompanyMoneyEvent.RefreshData -> {
        refreshPaymentData()
      }
      is CompanyMoneyEvent.FilterByStatus -> {
        filterPaymentsByStatus(event.status)
      }
      is CompanyMoneyEvent.FilterByPeriod -> {
        filterPaymentsByPeriod(event.period)
      }
      is CompanyMoneyEvent.SearchPayments -> {
        searchPayments(event.query)
      }
      is CompanyMoneyEvent.ProcessPayment -> {
        processPayment(event.paymentId)
      }
      is CompanyMoneyEvent.ShowPaymentDetail -> {
        showPaymentDetail(event.paymentId)
      }
      is CompanyMoneyEvent.ExportPayments -> {
        exportPayments(event.period)
      }
    }
  }

  private fun loadPaymentData() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)

      try {
        val payments = getSamplePayments()
        val summary = calculateSummary(payments)

        _uiState.value = _uiState.value.copy(
          isLoading = false,
          payments = payments,
          filteredPayments = payments,
          summary = summary
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(
          isLoading = false,
          error = e.message
        )
      }
    }
  }

  private fun refreshPaymentData() {
    loadPaymentData()
  }

  private fun filterPaymentsByStatus(status: PaymentStatus?) {
    val filteredPayments = if (status == null) {
      _uiState.value.payments
    } else {
      _uiState.value.payments.filter { it.status == status }
    }

    _uiState.value = _uiState.value.copy(
      filteredPayments = filteredPayments,
      selectedStatus = status
    )
  }

  private fun filterPaymentsByPeriod(period: PaymentPeriod) {
    val currentDate = LocalDate.now()
    val filteredPayments = _uiState.value.payments.filter { payment ->
      when (period) {
        PaymentPeriod.THIS_WEEK -> {
          payment.workDate.toLocalDate().isAfter(currentDate.minusWeeks(1))
        }
        PaymentPeriod.THIS_MONTH -> {
          payment.workDate.toLocalDate().isAfter(currentDate.minusMonths(1))
        }
        PaymentPeriod.LAST_MONTH -> {
          val lastMonth = currentDate.minusMonths(1)
          payment.workDate.toLocalDate().month == lastMonth.month &&
            payment.workDate.toLocalDate().year == lastMonth.year
        }
        PaymentPeriod.ALL -> true
      }
    }

    _uiState.value = _uiState.value.copy(
      filteredPayments = filteredPayments,
      selectedPeriod = period
    )
  }

  private fun searchPayments(query: String) {
    val filteredPayments = if (query.isEmpty()) {
      _uiState.value.payments
    } else {
      _uiState.value.payments.filter { payment ->
        payment.workerName.contains(query, ignoreCase = true) ||
          payment.projectName.contains(query, ignoreCase = true) ||
          payment.workType.contains(query, ignoreCase = true)
      }
    }

    _uiState.value = _uiState.value.copy(
      filteredPayments = filteredPayments,
      searchQuery = query
    )
  }

  private fun processPayment(paymentId: String) {
    viewModelScope.launch {
      try {
        val updatedPayments = _uiState.value.payments.map { payment ->
          if (payment.id == paymentId) {
            payment.copy(
              status = PaymentStatus.COMPLETED,
              paidDate = LocalDateTime.now()
            )
          } else {
            payment
          }
        }

        val updatedSummary = calculateSummary(updatedPayments)

        _uiState.value = _uiState.value.copy(
          payments = updatedPayments,
          filteredPayments = updatedPayments.filter { payment ->
            _uiState.value.selectedStatus?.let { status ->
              payment.status == status
            } ?: true
          },
          summary = updatedSummary
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(error = e.message)
      }
    }
  }

  private fun showPaymentDetail(paymentId: String) {
    _uiState.value = _uiState.value.copy(selectedPaymentId = paymentId)
  }

  private fun exportPayments(period: PaymentPeriod) {
    viewModelScope.launch {
      try {
        // 실제로는 파일 내보내기 로직 구현
        _uiState.value = _uiState.value.copy(
          exportCompleted = true
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(error = e.message)
      }
    }
  }

  private fun calculateSummary(payments: List<Payment>): PaymentSummary {
    val thisMonthPayments = payments.filter {
      it.workDate.toLocalDate().month == LocalDate.now().month
    }

    val pendingAmount = payments.filter { it.status == PaymentStatus.PENDING }
      .sumOf { it.amount }

    val completedAmount = payments.filter { it.status == PaymentStatus.COMPLETED }
      .sumOf { it.amount }

    val thisMonthAmount = thisMonthPayments.sumOf { it.amount }

    return PaymentSummary(
      totalPendingAmount = pendingAmount,
      totalCompletedAmount = completedAmount,
      thisMonthAmount = thisMonthAmount,
      pendingCount = payments.count { it.status == PaymentStatus.PENDING },
      completedCount = payments.count { it.status == PaymentStatus.COMPLETED }
    )
  }

  private fun getSamplePayments(): List<Payment> {
    return listOf(
      Payment(
        id = "1",
        workerName = "김철수",
        workType = "철근공",
        projectName = "아파트 신축공사 A동",
        workDate = LocalDateTime.now().minusDays(1),
        workHours = 8.0,
        hourlyWage = 25000,
        amount = 200000,
        status = PaymentStatus.PENDING,
        paidDate = null,
        description = "철근 배근 작업"
      ),
      Payment(
        id = "2",
        workerName = "박영희",
        workType = "타일공",
        projectName = "오피스텔 리모델링",
        workDate = LocalDateTime.now().minusDays(3),
        workHours = 8.0,
        hourlyWage = 22000,
        amount = 176000,
        status = PaymentStatus.COMPLETED,
        paidDate = LocalDateTime.now().minusDays(1),
        description = "화장실 타일 시공"
      ),
      Payment(
        id = "3",
        workerName = "이민수",
        workType = "도배공",
        projectName = "단독주택 인테리어",
        workDate = LocalDateTime.now().minusDays(5),
        workHours = 6.0,
        hourlyWage = 20000,
        amount = 120000,
        status = PaymentStatus.COMPLETED,
        paidDate = LocalDateTime.now().minusDays(3),
        description = "도배 작업"
      ),
      Payment(
        id = "4",
        workerName = "정수민",
        workType = "전기공",
        projectName = "상가 전기 공사",
        workDate = LocalDateTime.now().minusDays(2),
        workHours = 8.0,
        hourlyWage = 28000,
        amount = 224000,
        status = PaymentStatus.PENDING,
        paidDate = null,
        description = "전기 배선 작업"
      ),
      Payment(
        id = "5",
        workerName = "강지영",
        workType = "배관공",
        projectName = "아파트 배관 교체",
        workDate = LocalDateTime.now().minusDays(7),
        workHours = 8.0,
        hourlyWage = 26000,
        amount = 208000,
        status = PaymentStatus.OVERDUE,
        paidDate = null,
        description = "급수관 교체 작업"
      )
    )
  }
}

/**
 * 지급 관리 UI 상태
 */
data class CompanyMoneyUiState(
  val isLoading: Boolean = false,
  val payments: List<Payment> = emptyList(),
  val filteredPayments: List<Payment> = emptyList(),
  val summary: PaymentSummary = PaymentSummary(),
  val selectedStatus: PaymentStatus? = null,
  val selectedPeriod: PaymentPeriod = PaymentPeriod.THIS_MONTH,
  val searchQuery: String = "",
  val selectedPaymentId: String? = null,
  val exportCompleted: Boolean = false,
  val error: String? = null
)

/**
 * 지급 관리 이벤트
 */
sealed class CompanyMoneyEvent {
  object RefreshData : CompanyMoneyEvent()
  data class FilterByStatus(val status: PaymentStatus?) : CompanyMoneyEvent()
  data class FilterByPeriod(val period: PaymentPeriod) : CompanyMoneyEvent()
  data class SearchPayments(val query: String) : CompanyMoneyEvent()
  data class ProcessPayment(val paymentId: String) : CompanyMoneyEvent()
  data class ShowPaymentDetail(val paymentId: String) : CompanyMoneyEvent()
  data class ExportPayments(val period: PaymentPeriod) : CompanyMoneyEvent()
}

/**
 * 지급 상태
 */
enum class PaymentStatus(val displayName: String, val color: androidx.compose.ui.graphics.Color) {
  PENDING("지급 대기", androidx.compose.ui.graphics.Color(0xFFFF9800)),
  COMPLETED("지급 완료", androidx.compose.ui.graphics.Color(0xFF4CAF50)),
  OVERDUE("지급 지연", androidx.compose.ui.graphics.Color(0xFFF44336))
}

/**
 * 지급 기간
 */
enum class PaymentPeriod(val displayName: String) {
  ALL("전체"),
  THIS_WEEK("이번 주"),
  THIS_MONTH("이번 달"),
  LAST_MONTH("지난 달")
}

/**
 * 지급 데이터 모델
 */
data class Payment(
  val id: String,
  val workerName: String,
  val workType: String,
  val projectName: String,
  val workDate: LocalDateTime,
  val workHours: Double,
  val hourlyWage: Int,
  val amount: Int,
  val status: PaymentStatus,
  val paidDate: LocalDateTime?,
  val description: String
) {
  val formattedAmount: String
    get() = "${String.format("%,d", amount)}원"

  val formattedWorkDate: String
    get() = workDate.format(DateTimeFormatter.ofPattern("MM/dd (E)"))

  val formattedPaidDate: String?
    get() = paidDate?.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"))

  val workHoursText: String
    get() = "${workHours}시간"
}

/**
 * 지급 요약 정보
 */
data class PaymentSummary(
  val totalPendingAmount: Int = 0,
  val totalCompletedAmount: Int = 0,
  val thisMonthAmount: Int = 0,
  val pendingCount: Int = 0,
  val completedCount: Int = 0
) {
  val formattedPendingAmount: String
    get() = "${String.format("%,d", totalPendingAmount)}원"

  val formattedCompletedAmount: String
    get() = "${String.format("%,d", totalCompletedAmount)}원"

  val formattedThisMonthAmount: String
    get() = "${String.format("%,d", thisMonthAmount)}원"
}