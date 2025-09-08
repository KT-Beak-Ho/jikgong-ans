package com.billcorea.jikgong.presentation.company.main.money.feature

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.presentation.model.PaymentSortBy

sealed class CompanyMoneySharedEvent {
    object LoadPayments : CompanyMoneySharedEvent()
    object RefreshData : CompanyMoneySharedEvent()
    data class FilterByStatus(val status: PaymentStatus?) : CompanyMoneySharedEvent()
    data class SearchPayments(val query: String) : CompanyMoneySharedEvent()
    object ClearSearch : CompanyMoneySharedEvent()
    data class SortPayments(val sortBy: PaymentSortBy, val ascending: Boolean) : CompanyMoneySharedEvent()
    data class ApprovePayment(val paymentId: String) : CompanyMoneySharedEvent()
    data class RejectPayment(val paymentId: String, val reason: String) : CompanyMoneySharedEvent()
    data class ViewPaymentDetail(val paymentId: String) : CompanyMoneySharedEvent()
    object ClosePaymentDetail : CompanyMoneySharedEvent()
    object ClearFilters : CompanyMoneySharedEvent()
    object ClearError : CompanyMoneySharedEvent()
    data class UpdateLoading(val isLoading: Boolean) : CompanyMoneySharedEvent()
}