package com.billcorea.jikgong.presentation.company.main.money.shared

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentFilter
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentSummary

data class CompanyMoneySharedUiState(
    // 데이터
    val payments: List<PaymentData> = emptyList(),
    val filteredPayments: List<PaymentData> = emptyList(),
    val summary: PaymentSummary = PaymentSummary(),

    // 필터 및 검색
    val currentFilter: PaymentFilter = PaymentFilter(),
    val searchQuery: String = "",
    val selectedStatus: PaymentStatus? = null,

    // 정렬
    val sortBy: PaymentSortBy = PaymentSortBy.DATE,
    val sortAscending: Boolean = false,

    // 선택된 항목
    val selectedPayments: Set<String> = emptySet(),
    val selectedPaymentDetail: PaymentData? = null,

    // UI 상태
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,

    // 페이지 상태
    val currentPage: Int = 0,
    val hasMoreData: Boolean = false,

    // 액션 상태
    val isProcessingPayment: Boolean = false,
    val isBulkActionMode: Boolean = false
) {
    // 계산된 속성들
    val hasData: Boolean
        get() = payments.isNotEmpty()

    val hasActiveFilters: Boolean
        get() = currentFilter.status != null ||
                currentFilter.dateRange.first != null ||
                currentFilter.projectId != null ||
                searchQuery.isNotEmpty()

    val pendingPaymentsCount: Int
        get() = payments.count { it.status == PaymentStatus.PENDING }

    val canPerformBulkActions: Boolean
        get() = selectedPayments.isNotEmpty() && !isProcessingPayment

    val filteredPendingPayments: List<PaymentData>
        get() = filteredPayments.filter { it.status == PaymentStatus.PENDING }

    val totalSelectedAmount: Long
        get() = payments
            .filter { it.id in selectedPayments }
            .sumOf { it.finalAmount }
}