// ========================================
// 📄 ProjectListViewModel.kt - MVVM 패턴 완전 구현
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.data.*
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.*
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * 🏗️ 직직직 프로젝트 목록 뷰모델
 *
 * MVVM 패턴에 따른 프로젝트 목록 화면의 비즈니스 로직 관리
 * - 상태 관리: StateFlow를 통한 반응형 상태 관리
 * - 이벤트 처리: 사용자 액션에 대한 일관된 처리
 * - 데이터 로딩: Repository 패턴을 통한 데이터 계층 분리
 */
class ProjectListViewModel(
  private val repository: ProjectRepository = ProjectRepositoryImpl()
) : ViewModel() {

  // 🎯 UI 상태 관리
  private val _uiState = MutableStateFlow(ProjectListUiState())
  val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

  init {
    loadProjects()
  }

  /**
   * 🎮 이벤트 처리 중앙 허브
   */
  fun onEvent(event: ProjectListEvent) {
    when (event) {
      // 프로젝트 관련 이벤트
      is ProjectListEvent.SelectProject -> selectProject(event.projectId)
      is ProjectListEvent.ToggleBookmark -> toggleBookmark(event.projectId)
      is ProjectListEvent.QuickApply -> quickApply(event.projectId)
      is ProjectListEvent.ShareProject -> shareProject(event.projectId)

      // 필터링 및 검색 이벤트
      is ProjectListEvent.FilterByStatus -> filterByStatus(event.status)
      is ProjectListEvent.SearchProjects -> searchProjects(event.query)
      is ProjectListEvent.SelectSearchSuggestion -> selectSearchSuggestion(event.suggestion)
      ProjectListEvent.ToggleSearch -> toggleSearch()
      ProjectListEvent.ClearSearch -> clearSearch()
      ProjectListEvent.ClearFilters -> clearFilters()

      // 정렬 이벤트
      is ProjectListEvent.SortBy -> sortProjects(event.sortBy)
      ProjectListEvent.ToggleSortDialog -> toggleSortDialog()

      // 새로고침 및 로딩 이벤트
      ProjectListEvent.RefreshProjects -> refreshProjects()
      ProjectListEvent.LoadMoreProjects -> loadMoreProjects()
      ProjectListEvent.RetryLoading -> retryLoading()

      // 프로젝트 생성 및 관리 이벤트
      ProjectListEvent.CreateNewProject -> createNewProject()
      is ProjectListEvent.DuplicateProject -> duplicateProject(event.projectId)
      is ProjectListEvent.DeleteProject -> deleteProject(event.projectId)
      is ProjectListEvent.EditProject -> editProject(event.projectId)

      // UI 상태 이벤트
      ProjectListEvent.ShowFilterDialog -> showFilterDialog()
      ProjectListEvent.HideFilterDialog -> hideFilterDialog()
      ProjectListEvent.DismissError -> dismissError()
      is ProjectListEvent.UpdateFabVisibility -> updateFabVisibility(event.visible)

      // 기타 이벤트
      is ProjectListEvent.OnScrollPositionChanged -> onScrollPositionChanged(event.position)
    }
  }

  // ========================================
  // 🚀 핵심 기능 구현
  // ========================================

  /**
   * 📂 프로젝트 목록 로딩
   */
  private fun loadProjects() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, errorMessage = null) }

      try {
        delay(500) // 로딩 시뮬레이션

        val projects = repository.getProjects()
        val summary = calculateSummary(projects)

        _uiState.update { currentState ->
          currentState.copy(
            isLoading = false,
            projects = projects,
            summary = summary,
            filteredProjects = applyFiltersAndSearch(projects, currentState)
          )
        }

      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoading = false,
            errorMessage = "프로젝트를 불러오는 중 오류가 발생했습니다: ${e.message}"
          )
        }
      }
    }
  }

  /**
   * 🔄 프로젝트 새로고침
   */
  private fun refreshProjects() {
    viewModelScope.launch {
      _uiState.update { it.copy(isRefreshing = true) }

      try {
        delay(1000) // 새로고침 시뮬레이션

        val projects = repository.getProjects()
        val summary = calculateSummary(projects)

        _uiState.update { currentState ->
          currentState.copy(
            isRefreshing = false,
            projects = projects,
            summary = summary,
            filteredProjects = applyFiltersAndSearch(projects, currentState)
          )
        }

      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isRefreshing = false,
            errorMessage = "새로고침 중 오류가 발생했습니다."
          )
        }
      }
    }
  }

  /**
   * 📄 더 많은 프로젝트 로딩 (페이징)
   */
  private fun loadMoreProjects() {
    if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) return

    viewModelScope.launch {
      _uiState.update { it.copy(isLoadingMore = true) }

      try {
        delay(1000) // 로딩 시뮬레이션

        val moreProjects = repository.getMoreProjects(_uiState.value.projects.size)
        val allProjects = _uiState.value.projects + moreProjects
        val summary = calculateSummary(allProjects)

        _uiState.update { currentState ->
          currentState.copy(
            isLoadingMore = false,
            projects = allProjects,
            summary = summary,
            canLoadMore = moreProjects.size >= 10, // 10개 미만이면 더 이상 로드할 것이 없음
            filteredProjects = applyFiltersAndSearch(allProjects, currentState)
          )
        }

      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoadingMore = false,
            errorMessage = "추가 프로젝트를 불러오는 중 오류가 발생했습니다."
          )
        }
      }
    }
  }

  /**
   * 🔍 프로젝트 검색
   */
  private fun searchProjects(query: String) {
    _uiState.update { currentState ->
      currentState.copy(
        searchQuery = query,
        isSearching = query.isNotBlank(),
        filteredProjects = applyFiltersAndSearch(currentState.projects, currentState.copy(searchQuery = query)),
        searchSuggestions = generateSearchSuggestions(query, currentState.projects)
      )
    }
  }

  /**
   * 🏷️ 상태별 필터링
   */
  private fun filterByStatus(status: ProjectStatus?) {
    _uiState.update { currentState ->
      currentState.copy(
        selectedFilter = status,
        isFiltered = status != null,
        filteredProjects = applyFiltersAndSearch(currentState.projects, currentState.copy(selectedFilter = status))
      )
    }
  }

  /**
   * 📋 프로젝트 선택
   */
  private fun selectProject(projectId: String) {
    // TODO: 프로젝트 상세 화면으로 이동
    viewModelScope.launch {
      // 네비게이션 로직 구현
    }
  }

  /**
   * ⭐ 북마크 토글
   */
  private fun toggleBookmark(projectId: String) {
    viewModelScope.launch {
      try {
        val updatedProjects = _uiState.value.projects.map { project ->
          if (project.id == projectId) {
            project.copy(isBookmarked = !project.isBookmarked)
          } else {
            project
          }
        }

        _uiState.update { currentState ->
          currentState.copy(
            projects = updatedProjects,
            filteredProjects = applyFiltersAndSearch(updatedProjects, currentState)
          )
        }

        // 서버에 북마크 상태 업데이트
        repository.updateBookmark(projectId, updatedProjects.find { it.id == projectId }?.isBookmarked ?: false)

      } catch (e: Exception) {
        _uiState.update {
          it.copy(errorMessage = "북마크 업데이트 중 오류가 발생했습니다.")
        }
      }
    }
  }

  /**
   * ⚡ 빠른 지원
   */
  private fun quickApply(projectId: String) {
    viewModelScope.launch {
      try {
        // TODO: 빠른 지원 로직 구현
        repository.applyToProject(projectId)

        _uiState.update {
          it.copy(errorMessage = null) // 성공 메시지는 별도 처리
        }

      } catch (e: Exception) {
        _uiState.update {
          it.copy(errorMessage = "프로젝트 지원 중 오류가 발생했습니다.")
        }
      }
    }
  }

  /**
   * 📤 프로젝트 공유
   */
  private fun shareProject(projectId: String) {
    // TODO: 공유 기능 구현
  }

  /**
   * ➕ 새 프로젝트 생성
   */
  private fun createNewProject() {
    // TODO: 프로젝트 생성 화면으로 이동
  }

  /**
   * 📋 프로젝트 복제
   */
  private fun duplicateProject(projectId: String) {
    // TODO: 프로젝트 복제 로직
  }

  /**
   * 🗑️ 프로젝트 삭제
   */
  private fun deleteProject(projectId: String) {
    viewModelScope.launch {
      try {
        repository.deleteProject(projectId)

        val updatedProjects = _uiState.value.projects.filter { it.id != projectId }
        val summary = calculateSummary(updatedProjects)

        _uiState.update { currentState ->
          currentState.copy(
            projects = updatedProjects,
            summary = summary,
            filteredProjects = applyFiltersAndSearch(updatedProjects, currentState)
          )
        }

      } catch (e: Exception) {
        _uiState.update {
          it.copy(errorMessage = "프로젝트 삭제 중 오류가 발생했습니다.")
        }
      }
    }
  }

  /**
   * ✏️ 프로젝트 편집
   */
  private fun editProject(projectId: String) {
    // TODO: 프로젝트 편집 화면으로 이동
  }

  // ========================================
  // 🔧 유틸리티 기능들
  // ========================================

  /**
   * 🔍 검색 토글
   */
  private fun toggleSearch() {
    _uiState.update { currentState ->
      val newSearchVisible = !currentState.isSearchVisible
      currentState.copy(
        isSearchVisible = newSearchVisible,
        searchQuery = if (newSearchVisible) currentState.searchQuery else "",
        isSearching = if (newSearchVisible) currentState.isSearching else false,
        filteredProjects = if (newSearchVisible) {
          currentState.filteredProjects
        } else {
          applyFiltersAndSearch(currentState.projects, currentState.copy(searchQuery = "", isSearching = false))
        }
      )
    }
  }

  /**
   * 🧹 검색 초기화
   */
  private fun clearSearch() {
    _uiState.update { currentState ->
      currentState.copy(
        searchQuery = "",
        isSearching = false,
        isSearchVisible = false,
        searchSuggestions = emptyList(),
        filteredProjects = applyFiltersAndSearch(currentState.projects, currentState.copy(searchQuery = "", isSearching = false))
      )
    }
  }

  /**
   * 🧹 필터 초기화
   */
  private fun clearFilters() {
    _uiState.update { currentState ->
      currentState.copy(
        selectedFilter = null,
        isFiltered = false,
        filteredProjects = applyFiltersAndSearch(currentState.projects, currentState.copy(selectedFilter = null, isFiltered = false))
      )
    }
  }

  /**
   * 📊 정렬 기능
   */
  private fun sortProjects(sortBy: ProjectSortBy) {
    _uiState.update { currentState ->
      val sortedProjects = when (sortBy) {
        ProjectSortBy.DATE_LATEST -> currentState.projects.sortedByDescending { it.createdAt }
        ProjectSortBy.DATE_OLDEST -> currentState.projects.sortedBy { it.createdAt }
        ProjectSortBy.SALARY_HIGH -> currentState.projects.sortedByDescending { it.dailyWage }
        ProjectSortBy.SALARY_LOW -> currentState.projects.sortedBy { it.dailyWage }
        ProjectSortBy.DEADLINE -> currentState.projects.sortedBy { it.endDate }
        ProjectSortBy.LOCATION -> currentState.projects.sortedBy { it.location.shortAddress }
      }

      currentState.copy(
        projects = sortedProjects,
        currentSort = sortBy,
        filteredProjects = applyFiltersAndSearch(sortedProjects, currentState),
        isSortDialogVisible = false
      )
    }
  }

  /**
   * 🔄 재시도
   */
  private fun retryLoading() {
    loadProjects()
  }

  /**
   * 📊 통계 계산
   */
  private fun calculateSummary(projects: List<Project>): ProjectSummary {
    return ProjectSummary(
      totalProjects = projects.size,
      recruitingProjects = projects.count { it.status == ProjectStatus.RECRUITING },
      inProgressProjects = projects.count { it.status == ProjectStatus.IN_PROGRESS },
      completedProjects = projects.count { it.status == ProjectStatus.COMPLETED },
      totalBudget = projects.sumOf { it.dailyWage * it.duration },
      averageWage = if (projects.isNotEmpty()) projects.map { it.dailyWage }.average().toInt() else 0
    )
  }

  /**
   * 🔍 필터 및 검색 적용
   */
  private fun applyFiltersAndSearch(
    projects: List<Project>,
    state: ProjectListUiState
  ): List<Project> {
    var filtered = projects

    // 상태 필터 적용
    state.selectedFilter?.let { status ->
      filtered = filtered.filter { it.status == status }
    }

    // 검색어 적용
    if (state.searchQuery.isNotBlank()) {
      filtered = filtered.filter { project ->
        project.title.contains(state.searchQuery, ignoreCase = true) ||
          project.location.shortAddress.contains(state.searchQuery, ignoreCase = true) ||
          project.description.contains(state.searchQuery, ignoreCase = true)
      }
    }

    return filtered
  }

  /**
   * 🔍 검색 제안 생성
   */
  private fun generateSearchSuggestions(query: String, projects: List<Project>): List<String> {
    if (query.isBlank()) return emptyList()

    val suggestions = mutableSetOf<String>()

    // 프로젝트 제목에서 제안
    projects.forEach { project ->
      if (project.title.contains(query, ignoreCase = true)) {
        suggestions.add(project.title)
      }
      if (project.location.shortAddress.contains(query, ignoreCase = true)) {
        suggestions.add(project.location.shortAddress)
      }
    }

    return suggestions.take(5) // 최대 5개만
  }

  /**
   * 💡 검색 제안 선택
   */
  private fun selectSearchSuggestion(suggestion: String) {
    searchProjects(suggestion)
  }

  /**
   * 📊 정렬 다이얼로그 토글
   */
  private fun toggleSortDialog() {
    _uiState.update {
      it.copy(isSortDialogVisible = !it.isSortDialogVisible)
    }
  }

  /**
   * 🎛️ 필터 다이얼로그 표시
   */
  private fun showFilterDialog() {
    _uiState.update {
      it.copy(isFilterDialogVisible = true)
    }
  }

  /**
   * 🎛️ 필터 다이얼로그 숨기기
   */
  private fun hideFilterDialog() {
    _uiState.update {
      it.copy(isFilterDialogVisible = false)
    }
  }

  /**
   * ❌ 에러 메시지 해제
   */
  private fun dismissError() {
    _uiState.update {
      it.copy(errorMessage = null)
    }
  }

  /**
   * 🎈 FAB 가시성 업데이트
   */
  private fun updateFabVisibility(visible: Boolean) {
    _uiState.update {
      it.copy(isFabVisible = visible)
    }
  }

  /**
   * 📜 스크롤 위치 변경
   */
  private fun onScrollPositionChanged(position: Int) {
    // 스크롤 위치에 따른 UI 업데이트 로직
    val shouldShowFab = position < 3 // 상단 3개 아이템 내에서만 FAB 표시
    updateFabVisibility(shouldShowFab)
  }
}