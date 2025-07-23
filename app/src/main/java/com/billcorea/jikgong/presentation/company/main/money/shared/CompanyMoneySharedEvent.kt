package com.billcorea.jikgong.presentation.company.main.money.shared

import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentType
import java.time.LocalDate

sealed class CompanyMoneySharedEvent {
    // 데이터 로딩
    object LoadPayments : CompanyMoneySharedEvent()
    object RefreshPayments : CompanyMoneySharedEvent()
    object LoadPaymentSummary : CompanyMoneySharedEvent()

    // 탭 변경
    data class ChangeTab(val tabIndex: Int) : CompanyMoneySharedEvent()

    // 필터링
    data class FilterByStatus(val status: PaymentStatus?) : CompanyMoneySharedEvent()
    data class FilterByPaymentType(val type: PaymentType?) : CompanyMoneySharedEvent()
    data class FilterByDateRange(val from: LocalDate?, val to: LocalDate?) : CompanyMoneySharedEvent()
    data class SearchPayments(val query: String) : CompanyMoneySharedEvent()
    object ClearFilters : CompanyMoneySharedEvent()
    data class ApplyFilters(val options: PaymentFilterOptions) : CompanyMoneySharedEvent()

    // 정렬
    data class ChangeSortOption(val sortBy: PaymentSortOption, val ascending: Boolean = true) : CompanyMoneySharedEvent()
    object ToggleSortDirection : CompanyMoneySharedEvent()

    // 지급 처리
    data class SelectPayment(val paymentId: String) : CompanyMoneySharedEvent()
    data class DeselectPayment(val paymentId: String) : CompanyMoneySharedEvent()
    data class TogglePaymentSelection(val paymentId: String) : CompanyMoneySharedEvent()
    object SelectAllVisible : CompanyMoneySharedEvent()
    object ClearSelection : CompanyMoneySharedEvent()
    object ToggleMultiSelectMode : CompanyMoneySharedEvent()

    // 지급 액션
    object ProcessSelectedPayments : CompanyMoneySharedEvent()
    data class ProcessSinglePayment(val paymentId: String) : CompanyMoneySharedEvent()
    data class MarkAsUrgent(val paymentId: String) : CompanyMoneySharedEvent()
    data class AddPaymentNote(val paymentId: String, val note: String) : CompanyMoneySharedEvent()
    data class RetryFailedPayment(val paymentId: String) : CompanyMoneySharedEvent()

    // 캘린더
    data class SelectCalendarDate(val date: LocalDate) : CompanyMoneySharedEvent()
    data class LoadCalendarPayments(val month: Int, val year: Int) : CompanyMoneySharedEvent()

    // 상세 정보
    data class ShowPaymentDetail(val paymentId: String) : CompanyMoneySharedEvent()
    data class ShowWorkerDetail(val workerId: String) : CompanyMoneySharedEvent()
    data class ShowProjectDetail(val projectId: String) : CompanyMoneySharedEvent()

    // 다이얼로그
    object ShowPaymentDialog : CompanyMoneySharedEvent()
    object ShowFilterDialog : CompanyMoneySharedEvent()
    data class ShowConfirmDialog(val message: String, val action: () -> Unit) : CompanyMoneySharedEvent()
    object DismissDialog : CompanyMoneySharedEvent()

    // 설정
    data class ToggleShowCompleted(val show: Boolean) : CompanyMoneySharedEvent()
    data class UpdateDisplaySettings(val showCompletedPayments: Boolean) : CompanyMoneySharedEvent()

    // 에러 처리
    object ClearError : CompanyMoneySharedEvent()
    data class ShowError(val message: String) : CompanyMoneySharedEvent()

    // 네비게이션 클리어
    object ClearNavigation : CompanyMoneySharedEvent()

    // 통계 및 리포트
    object GeneratePaymentReport : CompanyMoneySharedEvent()
    data class ExportPayments(val format: ExportFormat) : CompanyMoneySharedEvent()
    object ShowPaymentStatistics : CompanyMoneySharedEvent()
}

enum class ExportFormat {
    CSV,
    EXCEL,
    PDF
}

data class PaymentFilterOptions(
    val status: PaymentStatus? = null,
    val paymentType: PaymentType? = null,
    val dateFrom: LocalDate? = null,
    val dateTo: LocalDate? = null,
    val minAmount: Int? = null,
    val maxAmount: Int? = null,
    val projectId: String? = null,
    val workerId: String? = null
)

enum class PaymentSortOption {
    DUE_DATE,        // 지급 예정일
    CREATED_DATE,    // 생성일
    AMOUNT,          // 금액
    WORKER_NAME,     // 근로자명
    PROJECT_NAME,    // 프로젝트명
    STATUS           // 상태
}