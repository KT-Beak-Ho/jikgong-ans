package com.billcorea.jikgong.presentation.company.main.money.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyMoneySharedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyMoneySharedUiState())
    val uiState: StateFlow<CompanyMoneySharedUiState> = _uiState.asStateFlow()

    init {
        loadPayments()
    }

    /**
     * 임금 데이터 로드
     */
    private fun loadPayments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 실제로는 API 호출
                val payments = CompanyMockDataFactory.getSamplePayments()
                val summary = CompanyMockDataFactory.getSamplePaymentSummary()

                _uiState.value = _uiState.value.copy(
                    payments = payments,
                    filteredPayments = payments,
                    summary = summary,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "데이터를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 필터링 적용
     */
    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = currentState.payments

        // 상태 필터
        currentState.selectedStatus?.let { status ->
            filtered = filtered.filter { it.status == status }
        }

        // 검색 필터
        if (currentState.searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.worker.name.contains(currentState.searchQuery, ignoreCase = true) ||
                        it.projectTitle.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        // 정렬 적용
        filtered = when (currentState.sortBy) {
            PaymentSortBy.DATE -> {
                if (currentState.sortAscending) {
                    filtered.sortedBy { it.workDate }
                } else {
                    filtered.sortedByDescending { it.workDate }
                }
            }
            PaymentSortBy.AMOUNT -> {
                if (currentState.sortAscending) {
                    filtered.sortedBy { it.finalAmount }
                } else {
                    filtered.sortedByDescending { it.finalAmount }
                }
            }
            PaymentSortBy.WORKER_NAME -> {
                if (currentState.sortAscending) {
                    filtered.sortedBy { it.worker.name }
                } else {
                    filtered.sortedByDescending { it.worker.name }
                }
            }
            PaymentSortBy.STATUS -> {
                if (currentState.sortAscending) {
                    filtered.sortedBy { it.status.ordinal }
                } else {
                    filtered.sortedByDescending { it.status.ordinal }
                }
            }
            PaymentSortBy.PROJECT_TITLE -> {
                if (currentState.sortAscending) {
                    filtered.sortedBy { it.projectTitle }
                } else {
                    filtered.sortedByDescending { it.projectTitle }
                }
            }
        }

        _uiState.value = currentState.copy(filteredPayments = filtered)
    }

    /**
     * 임금 지급 승인
     */
    private fun approvePayment(paymentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessingPayment = true)

            try {
                // 실제로는 API 호출
                val updatedPayments = _uiState.value.payments.map { payment ->
                    if (payment.id == paymentId) {
                        payment.copy(
                            status = PaymentStatus.PROCESSING,
                            updatedAt = java.time.LocalDateTime.now()
                        )
                    } else {
                        payment
                    }
                }

                _uiState.value = _uiState.value.copy(
                    payments = updatedPayments,
                    isProcessingPayment = false
                )

                applyFilters()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessingPayment = false,
                    errorMessage = "지급 승인에 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 임금 지급 반려
     */
    private fun rejectPayment(paymentId: String, reason: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessingPayment = true)

            try {
                // 실제로는 API 호출
                val updatedPayments = _uiState.value.payments.map { payment ->
                    if (payment.id == paymentId) {
                        payment.copy(
                            status = PaymentStatus.FAILED,
                            notes = reason,
                            updatedAt = java.time.LocalDateTime.now()
                        )
                    } else {
                        payment
                    }
                }

                _uiState.value = _uiState.value.copy(
                    payments = updatedPayments,
                    isProcessingPayment = false
                )

                applyFilters()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessingPayment = false,
                    errorMessage = "지급 반려에 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: CompanyMoneySharedEvent) {
        when (event) {
            is CompanyMoneySharedEvent.LoadPayments -> {
                loadPayments()
            }

            is CompanyMoneySharedEvent.RefreshData -> {
                _uiState.value = _uiState.value.copy(isRefreshing = true)
                loadPayments()
            }

            is CompanyMoneySharedEvent.FilterByStatus -> {
                _uiState.value = _uiState.value.copy(selectedStatus = event.status)
                applyFilters()
            }

            is CompanyMoneySharedEvent.SearchPayments -> {
                _uiState.value = _uiState.value.copy(searchQuery = event.query)
                applyFilters()
            }

            is CompanyMoneySharedEvent.ClearSearch -> {
                _uiState.value = _uiState.value.copy(searchQuery = "")
                applyFilters()
            }

            is CompanyMoneySharedEvent.SortPayments -> {
                _uiState.value = _uiState.value.copy(
                    sortBy = event.sortBy,
                    sortAscending = event.ascending
                )
                applyFilters()
            }

            is CompanyMoneySharedEvent.ApprovePayment -> {
                approvePayment(event.paymentId)
            }

            is CompanyMoneySharedEvent.RejectPayment -> {
                rejectPayment(event.paymentId, event.reason)
            }

            is CompanyMoneySharedEvent.ViewPaymentDetail -> {
                val payment = _uiState.value.payments.find { it.id == event.paymentId }
                _uiState.value = _uiState.value.copy(selectedPaymentDetail = payment)
            }

            is CompanyMoneySharedEvent.ClosePaymentDetail -> {
                _uiState.value = _uiState.value.copy(selectedPaymentDetail = null)
            }

            is CompanyMoneySharedEvent.ClearFilters -> {
                _uiState.value = _uiState.value.copy(
                    selectedStatus = null,
                    searchQuery = ""
                )
                applyFilters()
            }

            is CompanyMoneySharedEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }

            is CompanyMoneySharedEvent.UpdateLoading -> {
                _uiState.value = _uiState.value.copy(isLoading = event.isLoading)
            }

            else -> {
                // 다른 이벤트들은 필요에 따라 구현
            }
        }
    }
}