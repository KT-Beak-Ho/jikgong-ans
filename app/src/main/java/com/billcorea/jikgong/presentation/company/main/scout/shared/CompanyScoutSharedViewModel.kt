package com.billcorea.jikgong.presentation.company.main.scout.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.scout.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 스카웃 화면 ViewModel
 */
class CompanyScoutSharedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyScoutSharedUiState())
    val uiState: StateFlow<CompanyScoutSharedUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    /**
     * 초기 데이터 로드
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 샘플 데이터 로드
                val workers = ScoutSampleData.getSampleWorkers()
                val proposals = ScoutSampleData.getSampleProposals()
                val presets = ScoutSampleData.getSearchFiltersPresets()

                _uiState.value = _uiState.value.copy(
                    workers = workers,
                    filteredWorkers = workers,
                    proposals = proposals,
                    favoriteWorkers = workers.filter { it.isFavorite },
                    availablePresets = presets,
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
     * 필터 적용
     */
    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = currentState.workers

        // 검색어 필터
        if (currentState.searchQuery.isNotEmpty()) {
            filtered = filtered.filter { worker ->
                worker.name.contains(currentState.searchQuery, ignoreCase = true) ||
                        worker.skills.any { it.contains(currentState.searchQuery, ignoreCase = true) } ||
                        worker.location.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        // 반경 필터
        filtered = filtered.filter { it.distance <= currentState.searchFilters.radius }

        // 직종 필터
        if (currentState.searchFilters.jobType != "전체") {
            filtered = filtered.filter { worker ->
                worker.skills.any { it.contains(currentState.searchFilters.jobType) }
            }
        }

        // 경력 필터
        currentState.searchFilters.experienceYears?.let { minExperience ->
            filtered = when (currentState.searchFilters.experience) {
                "신입" -> filtered.filter { it.experience == 0 }
                else -> filtered.filter { it.experience >= minExperience }
            }
        }

        // 평점 필터
        if (currentState.searchFilters.minRating > 0f) {
            filtered = filtered.filter { it.rating >= currentState.searchFilters.minRating }
        }

        // 온라인 필터
        if (currentState.searchFilters.isOnlineOnly) {
            filtered = filtered.filter { it.isOnline }
        }

        // 신규 인력 필터
        if (currentState.searchFilters.isNewWorkerOnly) {
            filtered = filtered.filter { it.isNewWorker }
        }

        // 인증 필터
        if (currentState.searchFilters.isVerifiedOnly) {
            filtered = filtered.filter { it.isVerified }
        }

        // 응답률 필터
        if (currentState.searchFilters.minResponseRate > 0f) {
            filtered = filtered.filter { it.responseRate >= currentState.searchFilters.minResponseRate }
        }

        // 정렬 적용
        filtered = applySorting(filtered, currentState.searchFilters)

        _uiState.value = currentState.copy(filteredWorkers = filtered)
    }

    /**
     * 정렬 적용
     */
    private fun applySorting(workers: List<WorkerData>, filters: SearchFilters): List<WorkerData> {
        return when (filters.sortBy) {
            SortOption.RECOMMENDATION -> {
                if (filters.sortOrder == SortOrder.DESCENDING) {
                    workers.sortedByDescending { it.calculateRecommendationScore(filters) }
                } else {
                    workers.sortedBy { it.calculateRecommendationScore(filters) }
                }
            }
            SortOption.DISTANCE -> {
                if (filters.sortOrder == SortOrder.ASCENDING) {
                    workers.sortedBy { it.distance }
                } else {
                    workers.sortedByDescending { it.distance }
                }
            }
            SortOption.RATING -> {
                if (filters.sortOrder == SortOrder.DESCENDING) {
                    workers.sortedByDescending { it.rating }
                } else {
                    workers.sortedBy { it.rating }
                }
            }
            SortOption.EXPERIENCE -> {
                if (filters.sortOrder == SortOrder.DESCENDING) {
                    workers.sortedByDescending { it.experience }
                } else {
                    workers.sortedBy { it.experience }
                }
            }
            SortOption.RESPONSE_RATE -> {
                if (filters.sortOrder == SortOrder.DESCENDING) {
                    workers.sortedByDescending { it.responseRate }
                } else {
                    workers.sortedBy { it.responseRate }
                }
            }
            SortOption.LAST_ACTIVE -> {
                if (filters.sortOrder == SortOrder.DESCENDING) {
                    workers.sortedByDescending { it.lastActiveAt ?: it.joinedDate ?: java.time.LocalDateTime.MIN }
                } else {
                    workers.sortedBy { it.lastActiveAt ?: it.joinedDate ?: java.time.LocalDateTime.MAX }
                }
            }
            SortOption.JOINED_DATE -> {
                if (filters.sortOrder == SortOrder.DESCENDING) {
                    workers.sortedByDescending { it.joinedDate ?: java.time.LocalDateTime.MIN }
                } else {
                    workers.sortedBy { it.joinedDate ?: java.time.LocalDateTime.MAX }
                }
            }
        }
    }

    /**
     * 제안서 전송
     */
    private fun sendProposal(workerId: String, message: String) {
        viewModelScope.launch {
            try {
                // 실제로는 API 호출
                // proposalRepository.sendProposal(workerId, message)

                // 시뮬레이션용 딜레이
                delay(1000)

                // 성공 시 제안서 목록에 추가
                val worker = _uiState.value.workers.find { it.id == workerId }
                if (worker != null) {
                    val newProposal = ProposalData(
                        id = "proposal_${System.currentTimeMillis()}",
                        workerId = workerId,
                        workerName = worker.name,
                        projectId = null,
                        projectTitle = null,
                        message = message,
                        status = ProposalStatus.PENDING,
                        sentAt = java.time.LocalDateTime.now(),
                        expiresAt = java.time.LocalDateTime.now().plusDays(7),
                        proposalType = ProposalType.INDIVIDUAL
                    )

                    _uiState.value = _uiState.value.copy(
                        proposals = _uiState.value.proposals + newProposal,
                        showProposalDialog = false,
                        selectedWorkerId = null,
                        selectedWorkerName = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "제안서 전송에 실패했습니다: ${e.message}",
                    showProposalDialog = false
                )
            }
        }
    }

    /**
     * 즐겨찾기 토글
     */
    private fun toggleFavorite(workerId: String) {
        val updatedWorkers = _uiState.value.workers.map { worker ->
            if (worker.id == workerId) {
                worker.copy(isFavorite = !worker.isFavorite)
            } else {
                worker
            }
        }

        _uiState.value = _uiState.value.copy(
            workers = updatedWorkers,
            favoriteWorkers = updatedWorkers.filter { it.isFavorite }
        )

        applyFilters()
    }

    /**
     * 네비게이션 이벤트 클리어
     */
    fun clearNavigationEvents() {
        _uiState.value = _uiState.value.copy(
            shouldNavigateToProposalList = false,
            shouldNavigateToWorkerDetail = false,
            shouldNavigateToFavorites = false
        )
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: CompanyScoutSharedEvent) {
        when (event) {
            // 데이터 로딩 이벤트
            is CompanyScoutSharedEvent.LoadData -> {
                loadInitialData()
            }

            is CompanyScoutSharedEvent.RefreshData -> {
                _uiState.value = _uiState.value.copy(isRefreshing = true)
                loadInitialData()
            }

            is CompanyScoutSharedEvent.StartSearch -> {
                _uiState.value = _uiState.value.copy(
                    isSearching = true,
                    isSearchMode = true
                )
                applyFilters()
                _uiState.value = _uiState.value.copy(isSearching = false)
            }

            // 검색 및 필터 이벤트
            is CompanyScoutSharedEvent.UpdateSearchFilters -> {
                _uiState.value = _uiState.value.copy(searchFilters = event.filters)
                applyFilters()
            }

            is CompanyScoutSharedEvent.SearchWorkers -> {
                _uiState.value = _uiState.value.copy(searchQuery = event.query)
                applyFilters()
            }

            is CompanyScoutSharedEvent.ClearSearch -> {
                _uiState.value = _uiState.value.copy(
                    searchQuery = "",
                    isSearchMode = false
                )
                applyFilters()
            }

            is CompanyScoutSharedEvent.ResetFilters -> {
                _uiState.value = _uiState.value.copy(
                    searchFilters = SearchFilters(),
                    searchQuery = ""
                )
                applyFilters()
            }

            is CompanyScoutSharedEvent.ApplyPresetFilter -> {
                _uiState.value = _uiState.value.copy(searchFilters = event.preset)
                applyFilters()
            }

            // 정렬 이벤트
            is CompanyScoutSharedEvent.UpdateSortOption -> {
                val updatedFilters = _uiState.value.searchFilters.copy(
                    sortBy = event.sortBy,
                    sortOrder = event.order
                )
                _uiState.value = _uiState.value.copy(searchFilters = updatedFilters)
                applyFilters()
            }

            // 인력 관련 이벤트
            is CompanyScoutSharedEvent.SendProposal -> {
                val worker = _uiState.value.workers.find { it.id == event.workerId }
                _uiState.value = _uiState.value.copy(
                    selectedWorkerId = event.workerId,
                    selectedWorkerName = worker?.name,
                    showProposalDialog = true
                )
            }

            is CompanyScoutSharedEvent.ConfirmSendProposal -> {
                _uiState.value.selectedWorkerId?.let { workerId ->
                    sendProposal(workerId, event.message)
                }
            }

            is CompanyScoutSharedEvent.ToggleFavorite -> {
                toggleFavorite(event.workerId)
            }

            is CompanyScoutSharedEvent.ViewWorkerProfile -> {
                _uiState.value = _uiState.value.copy(
                    selectedWorkerId = event.workerId,
                    shouldNavigateToWorkerDetail = true
                )
            }

            // UI 상태 이벤트
            is CompanyScoutSharedEvent.ToggleSearchMode -> {
                _uiState.value = _uiState.value.copy(
                    isSearchMode = !_uiState.value.isSearchMode
                )
            }

            is CompanyScoutSharedEvent.ShowProposalDialog -> {
                _uiState.value = _uiState.value.copy(showProposalDialog = true)
            }

            is CompanyScoutSharedEvent.DismissProposalDialog -> {
                _uiState.value = _uiState.value.copy(
                    showProposalDialog = false,
                    selectedWorkerId = null,
                    selectedWorkerName = null
                )
            }

            is CompanyScoutSharedEvent.ShowFilterSheet -> {
                _uiState.value = _uiState.value.copy(showFilterSheet = true)
            }

            is CompanyScoutSharedEvent.DismissFilterSheet -> {
                _uiState.value = _uiState.value.copy(showFilterSheet = false)
            }

            // 네비게이션 이벤트
            is CompanyScoutSharedEvent.NavigateToProposalList -> {
                _uiState.value = _uiState.value.copy(shouldNavigateToProposalList = true)
            }

            is CompanyScoutSharedEvent.NavigateToFavoriteList -> {
                _uiState.value = _uiState.value.copy(shouldNavigateToFavorites = true)
            }

            // 에러 처리
            is CompanyScoutSharedEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }

            is CompanyScoutSharedEvent.ShowError -> {
                _uiState.value = _uiState.value.copy(errorMessage = event.message)
            }

            is CompanyScoutSharedEvent.UpdateLoading -> {
                _uiState.value = _uiState.value.copy(isLoading = event.isLoading)
            }

            // 기타 이벤트들
            else -> {
                // 처리되지 않은 이벤트들은 필요에 따라 구현
            }
        }
    }
}