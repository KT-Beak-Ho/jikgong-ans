package com.billcorea.jikgong.presentation.company.main.money.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentData
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentSampleData
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class CompanyMoneySharedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyMoneySharedUiState())
    val uiState: StateFlow<CompanyMoneySharedUiState> = _uiState.asStateFlow()

    // 네비게이션 이벤트
    private val _shouldNavigateToDetail = MutableStateFlow<String?>(null)
    val shouldNavigateToDetail: StateFlow<String?> = _shouldNavigateToDetail.asStateFlow()

    private val _shouldNavigateToWorkerDetail = MutableStateFlow<String?>(null)
    val shouldNavigateToWorkerDetail: StateFlow<String?> = _shouldNavigateToWorkerDetail.asStateFlow()

    // 데이터 모드 (테스트용)
    private var dataMode: Boolean = false

    init {
        loadEmptyState() // 기본적으로 빈 상태로 시작
    }

    fun onEvent(event: CompanyMoneySharedEvent) {
        when (event) {
            is CompanyMoneySharedEvent.LoadPayments -> loadPayments()
            is CompanyMoneySharedEvent.RefreshPayments -> refreshPayments()
            is CompanyMoneySharedEvent.LoadPaymentSummary -> loadPaymentSummary()
            is CompanyMoneySharedEvent.ChangeTab -> handleChangeTab(event.tabIndex)
            is CompanyMoneySharedEvent.FilterByStatus -> filterByStatus(event.status)
            is CompanyMoneySharedEvent.FilterByPaymentType -> filterByPaymentType(event.type)
            is CompanyMoneySharedEvent.FilterByDateRange -> filterByDateRange(event.from, event.to)
            is CompanyMoneySharedEvent.SearchPayments -> searchPayments(event.query)
            is CompanyMoneySharedEvent.ClearFilters -> clearFilters()
            is CompanyMoneySharedEvent.ApplyFilters -> applyFilters(event.options)
            is CompanyMoneySharedEvent.ChangeSortOption -> changeSortOption(event.sortBy, event.ascending)
            is CompanyMoneySharedEvent.ToggleSortDirection -> toggleSortDirection()
            is CompanyMoneySharedEvent.SelectPayment -> selectPayment(event.paymentId)
            is CompanyMoneySharedEvent.DeselectPayment -> deselectPayment(event.paymentId)
            is CompanyMoneySharedEvent.TogglePaymentSelection -> togglePaymentSelection(event.paymentId)
            is CompanyMoneySharedEvent.SelectAllVisible -> selectAllVisible()
            is CompanyMoneySharedEvent.ClearSelection -> clearSelection()
            is CompanyMoneySharedEvent.ToggleMultiSelectMode -> toggleMultiSelectMode()
            is CompanyMoneySharedEvent.ProcessSelectedPayments -> processSelectedPayments()
            is CompanyMoneySharedEvent.ProcessSinglePayment -> processSinglePayment(event.paymentId)
            is CompanyMoneySharedEvent.MarkAsUrgent -> markAsUrgent(event.paymentId)
            is CompanyMoneySharedEvent.AddPaymentNote -> addPaymentNote(event.paymentId, event.note)
            is CompanyMoneySharedEvent.RetryFailedPayment -> retryFailedPayment(event.paymentId)
            is CompanyMoneySharedEvent.SelectCalendarDate -> selectCalendarDate(event.date)
            is CompanyMoneySharedEvent.LoadCalendarPayments -> loadCalendarPayments(event.month, event.year)
            is CompanyMoneySharedEvent.ShowPaymentDetail -> showPaymentDetail(event.paymentId)
            is CompanyMoneySharedEvent.ShowWorkerDetail -> showWorkerDetail(event.workerId)
            is CompanyMoneySharedEvent.ShowProjectDetail -> showProjectDetail(event.projectId)
            is CompanyMoneySharedEvent.ShowPaymentDialog -> {
                _uiState.value = _uiState.value.copy(showPaymentDialog = true)
            }
            is CompanyMoneySharedEvent.ShowFilterDialog -> {
                _uiState.value = _uiState.value.copy(showFilterDialog = true)
            }
            is CompanyMoneySharedEvent.ShowConfirmDialog -> {
                _uiState.value = _uiState.value.copy(
                    showConfirmDialog = true,
                    confirmDialogMessage = event.message
                )
            }
            is CompanyMoneySharedEvent.DismissDialog -> dismissDialogs()
            is CompanyMoneySharedEvent.ShowBottomSheet -> showBottomSheet(event.type)
            is CompanyMoneySharedEvent.HideBottomSheet -> hideBottomSheet()
            is CompanyMoneySharedEvent.ToggleBottomSheet -> toggleBottomSheet()
            is CompanyMoneySharedEvent.ToggleShowCompleted -> toggleShowCompleted(event.show)
            is CompanyMoneySharedEvent.UpdateDisplaySettings -> updateDisplaySettings(event.showCompletedPayments)
            is CompanyMoneySharedEvent.ClearError -> clearError()
            is CompanyMoneySharedEvent.ShowError -> showError(event.message)
            is CompanyMoneySharedEvent.ClearNavigation -> clearNavigation()
            is CompanyMoneySharedEvent.GeneratePaymentReport -> generateReport()
            is CompanyMoneySharedEvent.ExportPayments -> exportPayments(event.format)
            is CompanyMoneySharedEvent.ShowPaymentStatistics -> showPaymentStatistics()
            is CompanyMoneySharedEvent.ToggleDataMode -> toggleDataMode()
        }
    }

    private fun handleChangeTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = tabIndex)
        if (tabIndex == 1) { // 캘린더 탭
            loadCalendarPayments()
        }
    }

    private fun toggleDataMode() {
        dataMode = !dataMode
        if (dataMode) {
            loadPayments()
            loadPaymentSummary()
        } else {
            loadEmptyState()
        }
    }

    private fun loadEmptyState() {
        _uiState.value = _uiState.value.copy(
            payments = emptyList(),
            filteredPayments = emptyList(),
            summary = getEmptySummary(),
            isLoading = false,
            errorMessage = null
        )
    }

    private fun loadPayments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val payments = PaymentSampleData.getSamplePayments()
                val filteredPayments = applyCurrentFilters(payments)

                _uiState.value = _uiState.value.copy(
                    payments = payments,
                    filteredPayments = filteredPayments,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "지급 정보를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    private fun refreshPayments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            if (dataMode) {
                loadPayments()
            } else {
                loadEmptyState()
            }
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    private fun loadPaymentSummary() {
        viewModelScope.launch {
            try {
                val summary = if (dataMode) {
                    PaymentSampleData.getSampleSummary()
                } else {
                    getEmptySummary()
                }
                _uiState.value = _uiState.value.copy(summary = summary)
            } catch (e: Exception) {
                showError("요약 정보를 불러오는데 실패했습니다")
            }
        }
    }

    private fun getEmptySummary() = com.billcorea.jikgong.presentation.company.main.money.data.PaymentSummary(
        totalPendingAmount = 0L,
        totalPendingCount = 0,
        urgentPaymentsCount = 0,
        completedThisMonthAmount = 0L,
        completedThisMonthCount = 0,
        averageProcessingTime = 0.0
    )

    // 하단 시트 관련 메소드들
    private fun showBottomSheet(type: BottomSheetType) {
        _uiState.value = _uiState.value.copy(
            showBottomSheet = true,
            bottomSheetType = type
        )
    }

    private fun hideBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = false)
    }

    private fun toggleBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showBottomSheet = !_uiState.value.showBottomSheet
        )
    }

    // 기존 메소드들
    private fun filterByStatus(status: PaymentStatus?) {
        _uiState.value = _uiState.value.copy(selectedStatus = status)
        applyFilters()
    }

    private fun filterByPaymentType(type: PaymentType?) {
        _uiState.value = _uiState.value.copy(selectedPaymentType = type)
        applyFilters()
    }

    private fun filterByDateRange(from: LocalDate?, to: LocalDate?) {
        _uiState.value = _uiState.value.copy(selectedDateRange = Pair(from, to))
        applyFilters()
    }

    private fun searchPayments(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    private fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            selectedStatus = null,
            selectedPaymentType = null,
            selectedDateRange = Pair(null, null),
            searchQuery = ""
        )
        applyFilters()
    }

    private fun applyFilters(options: PaymentFilterOptions? = null) {
        val currentState = _uiState.value
        val filteredPayments = applyCurrentFilters(currentState.payments)
        val sortedPayments = sortPayments(filteredPayments, currentState.sortBy, currentState.sortAscending)

        _uiState.value = currentState.copy(filteredPayments = sortedPayments)
    }

    private fun applyCurrentFilters(payments: List<PaymentData>): List<PaymentData> {
        val currentState = _uiState.value

        return payments.filter { payment ->
            (currentState.selectedStatus == null || payment.status == currentState.selectedStatus) &&
                    (currentState.selectedPaymentType == null || payment.paymentType == currentState.selectedPaymentType) &&
                    (currentState.selectedDateRange.first == null || payment.workDate >= currentState.selectedDateRange.first!!) &&
                    (currentState.selectedDateRange.second == null || payment.workDate <= currentState.selectedDateRange.second!!) &&
                    (currentState.searchQuery.isEmpty() ||
                            payment.worker.name.contains(currentState.searchQuery, true) ||
                            payment.project.name.contains(currentState.searchQuery, true)) &&
                    (currentState.showCompletedPayments || payment.status != PaymentStatus.COMPLETED)
        }
    }

    private fun sortPayments(payments: List<PaymentData>, sortBy: PaymentSortOption, ascending: Boolean): List<PaymentData> {
        val sorted = when (sortBy) {
            PaymentSortOption.DUE_DATE -> payments.sortedBy { it.dueDate }
            PaymentSortOption.CREATED_DATE -> payments.sortedBy { it.createdAt }
            PaymentSortOption.AMOUNT -> payments.sortedBy { it.totalAmount }
            PaymentSortOption.WORKER_NAME -> payments.sortedBy { it.worker.name }
            PaymentSortOption.PROJECT_NAME -> payments.sortedBy { it.project.name }
            PaymentSortOption.STATUS -> payments.sortedBy { it.status.ordinal }
        }

        return if (ascending) sorted else sorted.reversed()
    }

    private fun changeSortOption(sortBy: PaymentSortOption, ascending: Boolean) {
        _uiState.value = _uiState.value.copy(
            sortBy = sortBy,
            sortAscending = ascending
        )
        applyFilters()
    }

    private fun toggleSortDirection() {
        _uiState.value = _uiState.value.copy(sortAscending = !_uiState.value.sortAscending)
        applyFilters()
    }

    private fun selectPayment(paymentId: String) {
        val currentSelected = _uiState.value.selectedPaymentIds
        _uiState.value = _uiState.value.copy(
            selectedPaymentIds = currentSelected + paymentId
        )
    }

    private fun deselectPayment(paymentId: String) {
        val currentSelected = _uiState.value.selectedPaymentIds
        _uiState.value = _uiState.value.copy(
            selectedPaymentIds = currentSelected - paymentId
        )
    }

    private fun togglePaymentSelection(paymentId: String) {
        val currentSelected = _uiState.value.selectedPaymentIds
        if (paymentId in currentSelected) {
            deselectPayment(paymentId)
        } else {
            selectPayment(paymentId)
        }
    }

    private fun selectAllVisible() {
        val visiblePaymentIds = _uiState.value.filteredPayments.map { it.id }.toSet()
        _uiState.value = _uiState.value.copy(selectedPaymentIds = visiblePaymentIds)
    }

    private fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedPaymentIds = emptySet(),
            isMultiSelectMode = false
        )
    }

    private fun toggleMultiSelectMode() {
        val newMode = !_uiState.value.isMultiSelectMode
        _uiState.value = _uiState.value.copy(
            isMultiSelectMode = newMode,
            selectedPaymentIds = if (!newMode) emptySet() else _uiState.value.selectedPaymentIds
        )
    }

    private fun processSelectedPayments() {
        viewModelScope.launch {
            val selectedIds = _uiState.value.selectedPaymentIds
            if (selectedIds.isEmpty()) return@launch

            _uiState.value = _uiState.value.copy(isProcessingPayment = true)

            try {
                selectedIds.forEach { paymentId ->
                    processSinglePaymentInternal(paymentId)
                }

                if (dataMode) {
                    loadPayments()
                }
                clearSelection()

            } catch (e: Exception) {
                showError("지급 처리 중 오류가 발생했습니다: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isProcessingPayment = false)
            }
        }
    }

    private fun processSinglePayment(paymentId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessingPayment = true)

            try {
                processSinglePaymentInternal(paymentId)
                if (dataMode) {
                    loadPayments()
                }
            } catch (e: Exception) {
                showError("지급 처리에 실패했습니다: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isProcessingPayment = false)
            }
        }
    }

    private suspend fun processSinglePaymentInternal(paymentId: String) {
        val currentPayments = _uiState.value.payments.toMutableList()
        val paymentIndex = currentPayments.indexOfFirst { it.id == paymentId }
        if (paymentIndex != -1) {
            currentPayments[paymentIndex] = currentPayments[paymentIndex].copy(
                status = PaymentStatus.PROCESSING
            )
            _uiState.value = _uiState.value.copy(payments = currentPayments)
        }
    }

    private fun markAsUrgent(paymentId: String) {
        viewModelScope.launch {
            try {
                val currentPayments = _uiState.value.payments.toMutableList()
                val paymentIndex = currentPayments.indexOfFirst { it.id == paymentId }
                if (paymentIndex != -1) {
                    currentPayments[paymentIndex] = currentPayments[paymentIndex].copy(
                        status = PaymentStatus.URGENT
                    )
                    _uiState.value = _uiState.value.copy(payments = currentPayments)
                    applyFilters()
                }
            } catch (e: Exception) {
                showError("긴급 표시에 실패했습니다")
            }
        }
    }

    private fun addPaymentNote(paymentId: String, note: String) {
        viewModelScope.launch {
            try {
                val currentPayments = _uiState.value.payments.toMutableList()
                val paymentIndex = currentPayments.indexOfFirst { it.id == paymentId }
                if (paymentIndex != -1) {
                    currentPayments[paymentIndex] = currentPayments[paymentIndex].copy(
                        notes = note
                    )
                    _uiState.value = _uiState.value.copy(payments = currentPayments)
                }
            } catch (e: Exception) {
                showError("메모 추가에 실패했습니다")
            }
        }
    }

    private fun retryFailedPayment(paymentId: String) {
        viewModelScope.launch {
            try {
                processSinglePaymentInternal(paymentId)
                if (dataMode) {
                    loadPayments()
                }
            } catch (e: Exception) {
                showError("지급 재시도에 실패했습니다")
            }
        }
    }

    private fun selectCalendarDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedCalendarDate = date)
    }

    private fun loadCalendarPayments(month: Int? = null, year: Int? = null) {
        viewModelScope.launch {
            try {
                val targetDate = if (month != null && year != null) {
                    LocalDate.of(year, month, 1)
                } else {
                    _uiState.value.selectedCalendarDate
                }

                val monthlyPayments = _uiState.value.payments
                    .filter {
                        it.workDate.month == targetDate.month &&
                                it.workDate.year == targetDate.year
                    }
                    .groupBy { it.workDate }

                _uiState.value = _uiState.value.copy(calendarPayments = monthlyPayments)
            } catch (e: Exception) {
                showError("캘린더 데이터 로딩에 실패했습니다")
            }
        }
    }

    private fun showPaymentDetail(paymentId: String) {
        _shouldNavigateToDetail.value = paymentId
    }

    private fun showWorkerDetail(workerId: String) {
        _shouldNavigateToWorkerDetail.value = workerId
    }

    private fun showProjectDetail(projectId: String) {
        // 프로젝트 상세 네비게이션 로직 추가
    }

    private fun dismissDialogs() {
        _uiState.value = _uiState.value.copy(
            showPaymentDialog = false,
            showFilterDialog = false,
            showConfirmDialog = false,
            confirmDialogMessage = ""
        )
    }

    private fun toggleShowCompleted(show: Boolean) {
        _uiState.value = _uiState.value.copy(showCompletedPayments = show)
        applyFilters()
    }

    private fun updateDisplaySettings(showCompletedPayments: Boolean) {
        _uiState.value = _uiState.value.copy(showCompletedPayments = showCompletedPayments)
        applyFilters()
    }

    private fun showPaymentStatistics() {
        // 통계 화면 표시 로직
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    private fun clearNavigation() {
        _shouldNavigateToDetail.value = null
        _shouldNavigateToWorkerDetail.value = null
    }

    private fun generateReport() {
        viewModelScope.launch {
            try {
                showError("리포트 생성 기능은 준비 중입니다")
            } catch (e: Exception) {
                showError("리포트 생성에 실패했습니다")
            }
        }
    }

    private fun exportPayments(format: ExportFormat) {
        viewModelScope.launch {
            try {
                showError("내보내기 기능은 준비 중입니다")
            } catch (e: Exception) {
                showError("파일 내보내기에 실패했습니다")
            }
        }
    }

    /**
     * 네비게이션 이벤트 클리어
     */
    fun clearNavigationEvents() {
        clearNavigation()
    }
}