
package com.billcorea.jikgong.presentation.company.main.scout.shared

import com.billcorea.jikgong.presentation.company.main.scout.data.*

/**
 * 스카웃 화면 UI 상태
 */
data class CompanyScoutSharedUiState(
    // 데이터 상태
    val workers: List<WorkerData> = emptyList(),
    val filteredWorkers: List<WorkerData> = emptyList(),
    val proposals: List<ProposalData> = emptyList(),
    val favoriteWorkers: List<WorkerData> = emptyList(),

    // 검색 및 필터 상태
    val searchFilters: SearchFilters = SearchFilters(),
    val searchQuery: String = "",
    val isSearchMode: Boolean = false,
    val availablePresets: List<Pair<String, SearchFilters>> = emptyList(),

    // 선택 상태
    val selectedWorkerIds: Set<String> = emptySet(),
    val selectedWorkerId: String? = null,
    val selectedWorkerName: String? = null,

    // UI 표시 상태
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,

    // 다이얼로그 상태
    val showProposalDialog: Boolean = false,
    val showFilterSheet: Boolean = false,
    val showBulkProposalDialog: Boolean = false,

    // 네비게이션 상태
    val shouldNavigateToProposalList: Boolean = false,
    val shouldNavigateToWorkerDetail: Boolean = false,
    val shouldNavigateToFavorites: Boolean = false,

    // 페이지네이션 상태
    val currentPage: Int = 0,
    val hasMoreData: Boolean = true,
    val isLoadingMore: Boolean = false
) {

    /**
     * 인력 데이터 존재 여부
     */
    val hasWorkers: Boolean
        get() = workers.isNotEmpty()

    /**
     * 필터링된 인력 데이터 존재 여부
     */
    val hasFilteredWorkers: Boolean
        get() = filteredWorkers.isNotEmpty()

    /**
     * 제안서 데이터 존재 여부
     */
    val hasProposals: Boolean
        get() = proposals.isNotEmpty()

    /**
     * 즐겨찾기 인력 존재 여부
     */
    val hasFavoriteWorkers: Boolean
        get() = favoriteWorkers.isNotEmpty()

    /**
     * 대기중인 제안서 개수
     */
    val pendingProposalsCount: Int
        get() = proposals.count { it.status == ProposalStatus.PENDING }

    /**
     * 활성 필터 존재 여부
     */
    val hasActiveFilters: Boolean
        get() = searchFilters.hasActiveFilters || searchQuery.isNotEmpty()

    /**
     * 선택된 인력 수
     */
    val selectedWorkersCount: Int
        get() = selectedWorkerIds.size

    /**
     * 일괄 제안 가능 여부
     */
    val canSendBulkProposal: Boolean
        get() = selectedWorkerIds.size >= 2

    /**
     * 빈 상태 여부
     */
    val isEmpty: Boolean
        get() = !isLoading && !hasWorkers

    /**
     * 검색 결과 없음 상태
     */
    val isSearchEmpty: Boolean
        get() = !isLoading && hasWorkers && !hasFilteredWorkers && hasActiveFilters

    /**
     * 로딩 상태 통합
     */
    val isAnyLoading: Boolean
        get() = isLoading || isSearching || isRefreshing || isLoadingMore
}

