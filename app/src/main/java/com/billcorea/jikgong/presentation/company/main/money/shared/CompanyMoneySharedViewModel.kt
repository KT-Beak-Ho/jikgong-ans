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

    init {
        loadPayments()
        loadPaymentSummary()
    }

    fun onEvent(event: CompanyMoneySharedEvent) {
        when (event) {
            // 데이터 로딩
            is CompanyMoneySharedEvent.LoadPayments -> loadPayments()
            is CompanyMoneySharedEvent.RefreshPayments -> refreshPayments()
            is CompanyMoneySharedEvent.LoadPaymentSummary -> loadPaymentSummary()

            // 탭 변경
            is CompanyMoneySharedEvent.ChangeTab -> {
                _uiState.value = _uiState.value.copy(selectedTabIndex = event.tabIndex)
                if (event.tabIndex == 1) { // 캘린더 탭
                    loadCalendarPayments()
                }
            }

            // 필터링
            is CompanyMoneySharedEvent.FilterByStatus -> filterByStatus(event.status)
            is CompanyMoneySharedEvent.FilterByPaymentType -> filterByPaymentType(event.type)
            is CompanyMoneySharedEvent.FilterByDateRange -> filterByDateRange(event.from, event.to)
            is CompanyMoneySharedEvent.SearchPayments -> searchPayments(event.query)
            is CompanyMoneySharedEvent.ClearFilters -> clearFilters()
            is CompanyMoneySharedEvent.ApplyFilters -> applyFilters(event.options)

            // 정렬
            is CompanyMoneySharedEvent.ChangeSortOption -> changeSortOption(event.sortBy, event.ascending)
            is CompanyMoneySharedEvent.ToggleSortDirection -> toggleSortDirection()

            // 선택
            is CompanyMoneySharedEvent.SelectPayment -> selectPayment(event.paymentId)
            is CompanyMoneySharedEvent.DeselectPayment -> deselectPayment(event.paymentId)
            is CompanyMoneySharedEvent.TogglePaymentSelection -> togglePaymentSelection(event.paymentId)
            is CompanyMoneySharedEvent.SelectAllVisible -> selectAllVisible()
            is CompanyMoneySharedEvent.ClearSelection -> clearSelection()
            is CompanyMoneySharedEvent.ToggleMultiSelectMode -> toggleMultiSelectMode()

            // 지급 처리
            is CompanyMoneySharedEvent.ProcessSelectedPayments -> processSelectedPayments()
            is CompanyMoneySharedEvent.ProcessSinglePayment -> processSinglePayment(event.paymentId)
            is CompanyMoneySharedEvent.MarkAsUrgent -> markAsUrgent(event.paymentId)
            is CompanyMoneySharedEvent.AddPaymentNote -> addPaymentNote(event.paymentId, event.note)
            is CompanyMoneySharedEvent.RetryFailedPayment -> retryFailedPayment(event.paymentId)

            // 캘린더
            is CompanyMoneySharedEvent.SelectCalendarDate -> selectCalendarDate(event.date)
            is CompanyMoneySharedEvent.LoadCalendarPayments -> loadCalendarPayments(event.month, event.year)

            // 상세 정보
            is CompanyMoneySharedEvent.ShowPaymentDetail -> showPaymentDetail(event.paymentId)
            is CompanyMoneySharedEvent.ShowWorkerDetail -> showWorkerDetail(event.workerId)

            // 다이얼로그
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

            // 설정
            is CompanyMoneySharedEvent.ToggleShowCompleted -> toggleShowCompleted(event.show)

            // 에러 처리
            is CompanyMoneySharedEvent.ClearError -> clearError()
            is CompanyMoneySharedEvent.ShowError -> showError(event.message)

            // 네비게이션
            is CompanyMoneySharedEvent.ClearNavigation -> clearNavigation()

            // 기타
            is CompanyMoneySharedEvent.GeneratePaymentReport -> generateReport()
            is CompanyMoneySharedEvent.ExportPayments -> exportPayments(event.format)

            // 누락된 이벤트들 추가
            is CompanyMoneySharedEvent.UpdateDisplaySettings -> updateDisplaySettings(event.showCompletedPayments)
            is CompanyMoneySharedEvent.ShowPaymentStatistics -> showPaymentStatistics()
            is CompanyMoneySharedEvent.ShowProjectDetail -> showProjectDetail(event.projectId)
        }
    }

    private fun loadPayments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 실제로는 Repository에서 데이터를 가져옴
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

            // 새로고침 로직
            loadPayments()

            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    private fun loadPaymentSummary() {
        viewModelScope.launch {
            try {
                val summary = PaymentSampleData.getSampleSummary()
                _uiState.value = _uiState.value.copy(summary = summary)
            } catch (e: Exception) {
                showError("요약 정보를 불러오는데 실패했습니다")
            }
        }
    }

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
            // 상태 필터
            (currentState.selectedStatus == null || payment.status == currentState.selectedStatus) &&
                    // 지급 유형 필터
                    (currentState.selectedPaymentType == null || payment.paymentType == currentState.selectedPaymentType) &&
                    // 날짜 범위 필터
                    (currentState.selectedDateRange.first == null || payment.workDate >= currentState.selectedDateRange.first!!) &&
                    (currentState.selectedDateRange.second == null || payment.workDate <= currentState.selectedDateRange.second!!) &&
                    // 검색 쿼리 필터
                    (currentState.searchQuery.isEmpty() ||
                            payment.worker.name.contains(currentState.searchQuery, true) ||
                            payment.project.name.contains(currentState.searchQuery, true)) &&
                    // 완료된 지급 표시 옵션
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
                // 실제로는 Repository를 통해 지급 처리 API 호출
                selectedIds.forEach { paymentId ->
                    // API 호출 시뮬레이션
                    processSinglePaymentInternal(paymentId)
                }

                // 성공 후 데이터 새로고침
                loadPayments()
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
                loadPayments()
            } catch (e: Exception) {
                showError("지급 처리에 실패했습니다: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isProcessingPayment = false)
            }
        }
    }

    private suspend fun processSinglePaymentInternal(paymentId: String) {
        // 실제 API 호출 로직
        // 예: paymentRepository.processPayment(paymentId)

        // 시뮬레이션: 지급 상태를 PROCESSING으로 변경
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
                // 실제로는 API 호출
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
                // 실제로는 API 호출
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
                // 실패한 지급 재시도 로직
                processSinglePaymentInternal(paymentId)
                loadPayments()
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

                // 해당 월의 지급 데이터 그룹화
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
        // 예: _shouldNavigateToProjectDetail.value = projectId
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
        // 예: 통계 다이얼로그나 별도 화면으로 네비게이션
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
                // 리포트 생성 로직
                // 실제로는 PDF나 Excel 파일 생성
                showError("리포트 생성 기능은 준비 중입니다")
            } catch (e: Exception) {
                showError("리포트 생성에 실패했습니다")
            }
        }
    }

    private fun exportPayments(format: ExportFormat) {
        viewModelScope.launch {
            try {
                // 내보내기 로직
                when (format) {
                    ExportFormat.CSV -> {
                        // CSV 파일 생성
                    }
                    ExportFormat.EXCEL -> {
                        // Excel 파일 생성
                    }
                    ExportFormat.PDF -> {
                        // PDF 파일 생성
                    }
                }
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