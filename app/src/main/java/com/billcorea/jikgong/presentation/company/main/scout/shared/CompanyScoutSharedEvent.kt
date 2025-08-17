// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/shared/CompanyScoutSharedEvent.kt
package com.billcorea.jikgong.presentation.company.main.scout.shared

import com.billcorea.jikgong.presentation.company.main.scout.data.SearchFilters
import com.billcorea.jikgong.presentation.company.main.scout.data.SortOption
import com.billcorea.jikgong.presentation.company.main.scout.data.SortOrder

/**
 * 스카웃 화면 이벤트
 */
sealed class CompanyScoutSharedEvent {

    // 데이터 로딩 이벤트
    object LoadData : CompanyScoutSharedEvent()
    object RefreshData : CompanyScoutSharedEvent()
    object StartSearch : CompanyScoutSharedEvent()

    // 검색 및 필터 이벤트
    data class UpdateSearchFilters(val filters: SearchFilters) : CompanyScoutSharedEvent()
    data class SearchWorkers(val query: String) : CompanyScoutSharedEvent()
    object ClearSearch : CompanyScoutSharedEvent()
    object ResetFilters : CompanyScoutSharedEvent()
    data class ApplyPresetFilter(val preset: SearchFilters) : CompanyScoutSharedEvent()

    // 정렬 이벤트
    data class UpdateSortOption(val sortBy: SortOption, val order: SortOrder) : CompanyScoutSharedEvent()

    // 인력 관련 이벤트
    data class SendProposal(val workerId: String) : CompanyScoutSharedEvent()
    data class ConfirmSendProposal(val message: String) : CompanyScoutSharedEvent()
    data class ToggleFavorite(val workerId: String) : CompanyScoutSharedEvent()
    data class ViewWorkerProfile(val workerId: String) : CompanyScoutSharedEvent()
    data class CallWorker(val workerId: String, val phoneNumber: String) : CompanyScoutSharedEvent()

    // 제안서 관련 이벤트
    object CreateProposal : CompanyScoutSharedEvent()
    object CreateBulkProposal : CompanyScoutSharedEvent()
    data class SelectWorkersForBulkProposal(val workerIds: List<String>) : CompanyScoutSharedEvent()
    data class ViewProposalHistory(val workerId: String) : CompanyScoutSharedEvent()

    // UI 상태 이벤트
    object ToggleSearchMode : CompanyScoutSharedEvent()
    object ShowProposalDialog : CompanyScoutSharedEvent()
    object DismissProposalDialog : CompanyScoutSharedEvent()
    object ShowFilterSheet : CompanyScoutSharedEvent()
    object DismissFilterSheet : CompanyScoutSharedEvent()

    // 네비게이션 이벤트
    object NavigateToProposalList : CompanyScoutSharedEvent()
    object NavigateToFavoriteList : CompanyScoutSharedEvent()
    object NavigateToWorkerDetail : CompanyScoutSharedEvent()

    // 에러 및 로딩 이벤트
    object ClearError : CompanyScoutSharedEvent()
    data class ShowError(val message: String) : CompanyScoutSharedEvent()
    data class UpdateLoading(val isLoading: Boolean) : CompanyScoutSharedEvent()
}
