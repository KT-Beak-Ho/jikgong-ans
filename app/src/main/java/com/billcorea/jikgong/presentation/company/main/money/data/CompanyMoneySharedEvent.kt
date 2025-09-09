package com.billcorea.jikgong.presentation.company.main.money.data

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentStatus
import java.time.LocalDate

sealed class CompanyMoneySharedEvent {
    // 데이터 로딩
    object LoadPayments : CompanyMoneySharedEvent()
    object RefreshData : CompanyMoneySharedEvent()

    // 필터링
    data class FilterByStatus(val status: PaymentStatus?) : CompanyMoneySharedEvent()
    data class FilterByDateRange(val startDate: LocalDate?, val endDate: LocalDate?) : CompanyMoneySharedEvent()
    data class FilterByProject(val projectId: String?) : CompanyMoneySharedEvent()
    object ClearFilters : CompanyMoneySharedEvent()

    // 임금 지급 액션
    data class ApprovePayment(val paymentId: String) : CompanyMoneySharedEvent()
    data class RejectPayment(val paymentId: String, val reason: String) : CompanyMoneySharedEvent()
    data class BulkApprovePayments(val paymentIds: List<String>) : CompanyMoneySharedEvent()

    // 상세 보기
    data class ViewPaymentDetail(val paymentId: String) : CompanyMoneySharedEvent()
    object ClosePaymentDetail : CompanyMoneySharedEvent()

    // 검색
    data class SearchPayments(val query: String) : CompanyMoneySharedEvent()
    object ClearSearch : CompanyMoneySharedEvent()

    // 정렬
    data class SortPayments(val sortBy: PaymentSortBy, val ascending: Boolean = true) : CompanyMoneySharedEvent()

    // 에러 처리
    object ClearError : CompanyMoneySharedEvent()

    // 로딩 상태
    data class UpdateLoading(val isLoading: Boolean) : CompanyMoneySharedEvent()
}

enum class PaymentSortBy {
    DATE,           // 날짜순
    AMOUNT,         // 금액순
    WORKER_NAME,    // 작업자명순
    STATUS,         // 상태순
    PROJECT_TITLE   // 프로젝트명순
}