package com.billcorea.jikgong.presentation.company.main.money.shared

import com.billcorea.jikgong.presentation.company.main.money.data.PaymentData
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentSummary
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentType
import java.time.LocalDate

data class CompanyMoneySharedUiState(
    // 지급 데이터
    val payments: List<PaymentData> = emptyList(),
    val filteredPayments: List<PaymentData> = emptyList(),
    val selectedPayment: PaymentData? = null,
    val summary: PaymentSummary? = null,

    // 필터링 상태
    val selectedStatus: PaymentStatus? = null,
    val selectedPaymentType: PaymentType? = null,
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val searchQuery: String = "",

    // UI 상태
    val selectedTabIndex: Int = 0, // 0: 목록, 1: 캘린더
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,

    // 정렬 및 표시 옵션
    val sortBy: PaymentSortOption = PaymentSortOption.DUE_DATE,
    val sortAscending: Boolean = true,
    val showCompletedPayments: Boolean = false,

    // 캘린더 관련
    val selectedCalendarDate: LocalDate = LocalDate.now(),
    val calendarPayments: Map<LocalDate, List<PaymentData>> = emptyMap(),

    // 액션 상태
    val selectedPaymentIds: Set<String> = emptySet(),
    val isMultiSelectMode: Boolean = false,
    val isProcessingPayment: Boolean = false,

    // 다이얼로그 상태
    val showPaymentDialog: Boolean = false,
    val showFilterDialog: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val confirmDialogMessage: String = "",

    // 네비게이션 상태
    val shouldNavigateToDetail: String? = null,
    val shouldNavigateToWorkerDetail: String? = null
) {
    // 긴급 지급 건수
    val urgentPaymentsCount: Int
        get() = filteredPayments.count { it.isUrgent }

    // 총 지급 대기 금액
    val totalPendingAmount: Long
        get() = filteredPayments
            .filter { it.status == PaymentStatus.PENDING || it.status == PaymentStatus.URGENT }
            .sumOf { it.totalAmount.toLong() }

    // 선택된 지급 건수와 총액
    val selectedPaymentsInfo: Pair<Int, Long>
        get() {
            val selectedPayments = filteredPayments.filter { it.id in selectedPaymentIds }
            return Pair(
                selectedPayments.size,
                selectedPayments.sumOf { it.totalAmount.toLong() }
            )
        }

    // 필터가 적용되었는지 확인
    val hasActiveFilters: Boolean
        get() = selectedStatus != null ||
                selectedPaymentType != null ||
                selectedDateRange.first != null ||
                selectedDateRange.second != null ||
                searchQuery.isNotEmpty()

    // 캘린더에서 선택된 날짜의 지급 건수
    val selectedDatePaymentsCount: Int
        get() = calendarPayments[selectedCalendarDate]?.size ?: 0

    // 캘린더에서 선택된 날짜의 총 지급액
    val selectedDateTotalAmount: Long
        get() = calendarPayments[selectedCalendarDate]?.sumOf { it.totalAmount.toLong() } ?: 0L
}