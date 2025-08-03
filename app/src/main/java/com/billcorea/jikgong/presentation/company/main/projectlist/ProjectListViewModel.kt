// ========================================
// 📄 ProjectListViewModel.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.data.*
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 프로젝트 목록 뷰모델
 */
class ProjectListViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(ProjectListUiState())
  val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

  init {
    loadProjects()
  }

  /**
   * 이벤트 처리
   */
  fun onEvent(event: ProjectListEvent) {
    when (event) {
      // 프로젝트 관련
      is ProjectListEvent.SelectProject -> selectProject(event.projectId)
      is ProjectListEvent.ToggleBookmark -> toggleBookmark(event.projectId)
      is ProjectListEvent.QuickApply -> quickApply(event.projectId)
      is ProjectListEvent.ShareProject -> shareProject(event.projectId)

      // 필터링 및 검색
      is ProjectListEvent.FilterByStatus -> filterByStatus(event.status)
      is ProjectListEvent.SearchProjects -> searchProjects(event.query)
      is ProjectListEvent.SelectSearchSuggestion -> selectSearchSuggestion(event.suggestion)
      ProjectListEvent.ToggleSearch -> toggleSearch()
      ProjectListEvent.ClearSearch -> clearSearch()
      ProjectListEvent.ClearFilters -> clearFilters()

      // 정렬
      is ProjectListEvent.SortBy -> sortProjects(event.sortBy)
      ProjectListEvent.ToggleSortDialog -> toggleSortDialog()

      // 새로고침 및 로딩
      ProjectListEvent.RefreshProjects -> refreshProjects()
      ProjectListEvent.LoadMoreProjects -> loadMoreProjects()
      ProjectListEvent.RetryLoading -> retryLoading()

      // 프로젝트 생성 및 관리
      ProjectListEvent.CreateNewProject -> createNewProject()
      is ProjectListEvent.DuplicateProject -> duplicateProject(event.projectId)
      is ProjectListEvent.DeleteProject -> deleteProject(event.projectId)
      is ProjectListEvent.EditProject -> editProject(event.projectId)

      // UI 상태
      ProjectListEvent.ShowFilterDialog -> showFilterDialog()
      ProjectListEvent.HideFilterDialog -> hideFilterDialog()
      ProjectListEvent.DismissError -> dismissError()
      is ProjectListEvent.UpdateFabVisibility -> updateFabVisibility(event.visible)

      // 기타
      is ProjectListEvent.OnScrollPositionChanged -> onScrollPositionChanged(event.position)
      ProjectListEvent.OnNetworkAvailable -> onNetworkAvailable()
      ProjectListEvent.OnNetworkLost -> onNetworkLost()
    }
  }

  private fun loadProjects() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }

      try {
        // 샘플 데이터 로드 시뮬레이션
        delay(1000)

        val projects = ProjectSampleData.getSampleProjects()
        val summary = ProjectSampleData.getSampleProjectSummary()

        _uiState.update { currentState ->
          currentState.copy(
            isLoading = false,
            projects = projects,
            filteredProjects = filterAndSortProjects(
              projects,
              currentState.selectedFilter,
              currentState.sortBy,
              currentState.searchQuery
            ),
            summary = summary,
            searchSuggestions = ProjectSampleData.getSearchSuggestions()
          )
        }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoading = false,
            errorMessage = "프로젝트를 불러오는데 실패했습니다."
          )
        }
      }
    }
  }

  private fun filterAndSortProjects(
    projects: List<Project>,
    statusFilter: ProjectStatus?,
    sortBy: ProjectSortBy,
    searchQuery: String
  ): List<Project> {
    var filtered = projects

    // 상태 필터 적용
    if (statusFilter != null) {
      filtered = filtered.filter { it.status == statusFilter }
    }

    // 검색 필터 적용
    if (searchQuery.isNotBlank()) {
      filtered = filtered.filter { project ->
        project.title.contains(searchQuery, ignoreCase = true) ||
          project.location.shortAddress.contains(searchQuery, ignoreCase = true) ||
          project.workType.displayName.contains(searchQuery, ignoreCase = true) ||
          project.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) }
      }
    }

    // 정렬 적용
    return when (sortBy) {
      ProjectSortBy.CREATED_DATE_DESC -> filtered.sortedByDescending { it.createdAt }
      ProjectSortBy.CREATED_DATE_ASC -> filtered.sortedBy { it.createdAt }
      ProjectSortBy.START_DATE_ASC -> filtered.sortedBy { it.startDate }
      ProjectSortBy.START_DATE_DESC -> filtered.sortedByDescending { it.startDate }
      ProjectSortBy.DAILY_WAGE_DESC -> filtered.sortedByDescending { it.dailyWage }
      ProjectSortBy.DAILY_WAGE_ASC -> filtered.sortedBy { it.dailyWage }
      ProjectSortBy.LOCATION -> filtered.sortedBy { it.location.shortAddress }
      ProjectSortBy.RECRUIT_RATE -> filtered.sortedBy { it.recruitmentRate }
      ProjectSortBy.URGENT_FIRST -> filtered.sortedWith(
        compareByDescending<Project> { it.isUrgent }
          .thenByDescending { it.createdAt }
      )
      ProjectSortBy.COMPANY_RATING -> filtered.sortedByDescending { it.companyRating }
      ProjectSortBy.DURATION -> filtered.sortedBy { it.durationDays }
    }
  }

  private fun selectProject(projectId: String) {
    _uiState.update {
      it.copy(selectedProjectId = projectId)
    }
    // TODO: 프로젝트 상세 화면으로 네비게이션
  }

  private fun toggleBookmark(projectId: String) {
    _uiState.update { currentState ->
      val updatedProjects = currentState.projects.map { project ->
        if (project.id == projectId) {
          project.copy(isBookmarked = !project.isBookmarked)
        } else {
          project
        }
      }
      currentState.copy(
        projects = updatedProjects,
        filteredProjects = filterAndSortProjects(
          updatedProjects,
          currentState.selectedFilter,
          currentState.sortBy,
          currentState.searchQuery
        )
      )
    }
  }

  private fun quickApply(projectId: String) {
    viewModelScope.launch {
      try {
        // TODO: 실제 지원 API 호출
        delay(500)

        _uiState.update { currentState ->
          val updatedProjects = currentState.projects.map { project ->
            if (project.id == projectId && project.canApply) {
              project.copy(appliedWorkers = project.appliedWorkers + 1)
            } else {
              project
            }
          }
          currentState.copy(
            projects = updatedProjects,
            filteredProjects = filterAndSortProjects(
              updatedProjects,
              currentState.selectedFilter,
              currentState.sortBy,
              currentState.searchQuery
            )
          )
        }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(errorMessage = "지원에 실패했습니다.")
        }
      }
    }
  }

  private fun shareProject(projectId: String) {
    // TODO: 프로젝트 공유 기능 구현
  }

  private fun filterByStatus(status: ProjectStatus?) {
    _uiState.update { currentState ->
      currentState.copy(
        selectedFilter = status,
        filteredProjects = filterAndSortProjects(
          currentState.projects,
          status,
          currentState.sortBy,
          currentState.searchQuery
        )
      )
    }
  }

  private fun searchProjects(query: String) {
    _uiState.update { currentState ->
      currentState.copy(
        searchQuery = query,
        filteredProjects = filterAndSortProjects(
          currentState.projects,
          currentState.selectedFilter,
          currentState.sortBy,
          query
        )
      )
    }
  }

  private fun selectSearchSuggestion(suggestion: String) {
    searchProjects(suggestion)
    _uiState.update { it.copy(isSearchVisible = false) }
  }

  private fun toggleSearch() {
    _uiState.update {
      it.copy(isSearchVisible = !it.isSearchVisible)
    }
  }

  private fun clearSearch() {
    _uiState.update { currentState ->
      currentState.copy(
        searchQuery = "",
        isSearchVisible = false,
        filteredProjects = filterAndSortProjects(
          currentState.projects,
          currentState.selectedFilter,
          currentState.sortBy,
          ""
        )
      )
    }
  }

  private fun clearFilters() {
    _uiState.update { currentState ->
      currentState.copy(
        selectedFilter = null,
        searchQuery = "",
        isSearchVisible = false,
        filteredProjects = filterAndSortProjects(
          currentState.projects,
          null,
          currentState.sortBy,
          ""
        )
      )
    }
  }

  private fun sortProjects(sortBy: ProjectSortBy) {
    _uiState.update { currentState ->
      currentState.copy(
        sortBy = sortBy,
        showSortDialog = false,
        filteredProjects = filterAndSortProjects(
          currentState.projects,
          currentState.selectedFilter,
          sortBy,
          currentState.searchQuery
        )
      )
    }
  }

  private fun toggleSortDialog() {
    _uiState.update {
      it.copy(showSortDialog = !it.showSortDialog)
    }
  }

  private fun refreshProjects() {
    viewModelScope.launch {
      _uiState.update { it.copy(isRefreshing = true) }

      try {
        delay(1000) // 새로고침 시뮬레이션
        loadProjects()
      } finally {
        _uiState.update { it.copy(isRefreshing = false) }
      }
    }
  }

  private fun loadMoreProjects() {
    if (_uiState.value.isLoadingMore || !_uiState.value.hasNextPage) return

    viewModelScope.launch {
      _uiState.update { it.copy(isLoadingMore = true) }

      try {
        delay(1000) // 로딩 시뮬레이션
        // TODO: 추가 프로젝트 로드

        _uiState.update {
          it.copy(
            isLoadingMore = false,
            hasNextPage = false // 더 이상 로드할 데이터 없음
          )
        }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoadingMore = false,
            errorMessage = "추가 데이터를 불러오는데 실패했습니다."
          )
        }
      }
    }
  }

  private fun retryLoading() {
    loadProjects()
  }

  private fun createNewProject() {
    // TODO: 프로젝트 생성 화면으로 네비게이션
  }

  private fun duplicateProject(projectId: String) {
    // TODO: 프로젝트 복제 기능 구현
  }

  private fun deleteProject(projectId: String) {
    viewModelScope.launch {
      try {
        // TODO: 실제 삭제 API 호출

        _uiState.update { currentState ->
          val updatedProjects = currentState.projects.filterNot { it.id == projectId }
          currentState.copy(
            projects = updatedProjects,
            filteredProjects = filterAndSortProjects(
              updatedProjects,
              currentState.selectedFilter,
              currentState.sortBy,
              currentState.searchQuery
            )
          )
        }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(errorMessage = "프로젝트 삭제에 실패했습니다.")
        }
      }
    }
  }

  private fun editProject(projectId: String) {
    // TODO: 프로젝트 편집 화면으로 네비게이션
  }

  private fun showFilterDialog() {
    _uiState.update { it.copy(showFilterDialog = true) }
  }

  private fun hideFilterDialog() {
    _uiState.update { it.copy(showFilterDialog = false) }
  }

  private fun dismissError() {
    _uiState.update { it.copy(errorMessage = null) }
  }

  private fun updateFabVisibility(visible: Boolean) {
    _uiState.update { it.copy(fabVisible = visible) }
  }

  private fun onScrollPositionChanged(position: Int) {
    // 스크롤 위치에 따른 FAB 가시성 조절
    val shouldShowFab = position < 100 // 상단 근처에서만 FAB 표시
    if (_uiState.value.fabVisible != shouldShowFab) {
      updateFabVisibility(shouldShowFab)
    }
  }

  private fun onNetworkAvailable() {
    _uiState.update { it.copy(hasNetworkError = false) }
    if (_uiState.value.projects.isEmpty()) {
      loadProjects()
    }
  }

  private fun onNetworkLost() {
    _uiState.update { it.copy(hasNetworkError = true) }
  }
}