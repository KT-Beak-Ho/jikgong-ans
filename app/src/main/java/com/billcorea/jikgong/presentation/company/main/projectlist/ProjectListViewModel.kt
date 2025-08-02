// ========================================
// 📄 수정된 ProjectListViewModel.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSampleData
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.*

class ProjectListViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(ProjectListUiState())
  val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

  init {
    loadProjects()
  }

  fun onEvent(event: ProjectListEvent) {
    when (event) {
      is ProjectListEvent.RefreshProjects -> loadProjects()
      is ProjectListEvent.FilterByStatus -> filterByStatus(event.status)
      is ProjectListEvent.SelectProject -> selectProject(event.projectId)
      is ProjectListEvent.CreateNewProject -> navigateToCreate()
      is ProjectListEvent.SearchProjects -> searchProjects(event.query)
      is ProjectListEvent.ToggleSearch -> toggleSearch()
      is ProjectListEvent.ClearSearch -> clearSearch()
      is ProjectListEvent.QuickApply -> quickApply(event.projectId)
      is ProjectListEvent.LoadMoreProjects -> loadMoreProjects()
      is ProjectListEvent.SortProjects -> sortProjects(event.sortBy)
      is ProjectListEvent.ClearFilters -> clearFilters()
      is ProjectListEvent.SelectSearchSuggestion -> selectSearchSuggestion(event.suggestion)
      is ProjectListEvent.NavigateToProjectDetail -> navigateToProjectDetail(event.projectId)
      is ProjectListEvent.NavigateToProjectEdit -> navigateToProjectEdit(event.projectId)
      is ProjectListEvent.ToggleBookmark -> toggleBookmark(event.projectId)
      is ProjectListEvent.ShareProject -> shareProject(event.projectId)
      is ProjectListEvent.DeleteProject -> deleteProject(event.projectId)
      is ProjectListEvent.DuplicateProject -> duplicateProject(event.projectId)
      is ProjectListEvent.ShowFilterDialog -> showFilterDialog()
      is ProjectListEvent.HideFilterDialog -> hideFilterDialog()
      is ProjectListEvent.ShowSortDialog -> showSortDialog()
      is ProjectListEvent.HideSortDialog -> hideSortDialog()
      is ProjectListEvent.UpdateFabVisibility -> updateFabVisibility(event.visible)
      is ProjectListEvent.DismissError -> dismissError()
      is ProjectListEvent.RetryLastAction -> retryLastAction()
      is ProjectListEvent.UpdateNotificationSettings -> updateNotificationSettings(event.enabled)
      is ProjectListEvent.UpdateAutoRefresh -> updateAutoRefresh(event.enabled)
    }
  }

  private fun loadProjects() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }

      try {
        // 실제로는 Repository에서 데이터를 가져옴
        val projects = ProjectSampleData.getSampleProjects()
        val summary = ProjectSampleData.calculateSummary(projects)

        _uiState.update {
          it.copy(
            isLoading = false,
            projects = projects,
            filteredProjects = projects,
            summary = summary,
            errorMessage = null
          )
        }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoading = false,
            errorMessage = e.message
          )
        }
      }
    }
  }

  private fun filterByStatus(status: ProjectStatus?) {
    val filteredProjects = if (status == null) {
      uiState.value.projects
    } else {
      uiState.value.projects.filter { it.status == status }
    }

    _uiState.update {
      it.copy(
        selectedFilter = status,
        filteredProjects = filteredProjects
      )
    }
  }

  private fun searchProjects(query: String) {
    val filteredProjects = if (query.isEmpty()) {
      if (uiState.value.selectedFilter == null) {
        uiState.value.projects
      } else {
        uiState.value.projects.filter { it.status == uiState.value.selectedFilter }
      }
    } else {
      uiState.value.projects.filter { project ->
        project.title.contains(query, ignoreCase = true) ||
          project.location.contains(query, ignoreCase = true) ||
          project.workType.contains(query, ignoreCase = true)
      }
    }

    _uiState.update {
      it.copy(
        searchQuery = query,
        filteredProjects = filteredProjects
      )
    }
  }

  private fun toggleSearch() {
    _uiState.update {
      it.copy(isSearchVisible = !it.isSearchVisible)
    }
  }

  private fun clearSearch() {
    _uiState.update {
      it.copy(
        searchQuery = "",
        isSearchVisible = false,
        filteredProjects = if (it.selectedFilter == null) it.projects
        else it.projects.filter { project -> project.status == it.selectedFilter }
      )
    }
  }

  private fun selectProject(projectId: String) {
    _uiState.update { it.copy(selectedProjectId = projectId) }
    // TODO: 프로젝트 상세 화면으로 이동
  }

  private fun navigateToCreate() {
    // TODO: 프로젝트 생성 화면으로 이동
  }

  private fun quickApply(projectId: String) {
    // TODO: 빠른 지원 처리
  }

  private fun loadMoreProjects() {
    // TODO: 페이징 처리
  }

  private fun sortProjects(sortBy: ProjectSortBy) {
    val sortedProjects = when (sortBy) {
      ProjectSortBy.CREATED_DATE_DESC -> uiState.value.filteredProjects.sortedByDescending { it.createdAt }
      ProjectSortBy.CREATED_DATE_ASC -> uiState.value.filteredProjects.sortedBy { it.createdAt }
      ProjectSortBy.START_DATE_ASC -> uiState.value.filteredProjects.sortedBy { it.startDate }
      ProjectSortBy.START_DATE_DESC -> uiState.value.filteredProjects.sortedByDescending { it.startDate }
      ProjectSortBy.DAILY_WAGE_DESC -> uiState.value.filteredProjects.sortedByDescending { it.dailyWage }
      ProjectSortBy.DAILY_WAGE_ASC -> uiState.value.filteredProjects.sortedBy { it.dailyWage }
      ProjectSortBy.LOCATION -> uiState.value.filteredProjects.sortedBy { it.location }
      ProjectSortBy.RECRUIT_RATE -> uiState.value.filteredProjects.sortedBy { it.progressPercentage }
      ProjectSortBy.URGENT_FIRST -> uiState.value.filteredProjects.sortedWith(
        compareByDescending<com.billcorea.jikgong.presentation.company.main.projectlist.data.Project> { it.isUrgent }
          .thenByDescending { it.createdAt }
      )
    }

    _uiState.update {
      it.copy(
        sortBy = sortBy,
        filteredProjects = sortedProjects
      )
    }
  }

  private fun clearFilters() {
    _uiState.update {
      it.copy(
        selectedFilter = null,
        searchQuery = "",
        filteredProjects = it.projects,
        sortBy = ProjectSortBy.CREATED_DATE_DESC
      )
    }
  }

  private fun selectSearchSuggestion(suggestion: String) {
    searchProjects(suggestion)
  }

  private fun navigateToProjectDetail(projectId: String) {
    // TODO: 프로젝트 상세 화면으로 이동
  }

  private fun navigateToProjectEdit(projectId: String) {
    // TODO: 프로젝트 편집 화면으로 이동
  }

  private fun toggleBookmark(projectId: String) {
    // TODO: 북마크 토글 처리
  }

  private fun shareProject(projectId: String) {
    // TODO: 프로젝트 공유 처리
  }

  private fun deleteProject(projectId: String) {
    // TODO: 프로젝트 삭제 처리
  }

  private fun duplicateProject(projectId: String) {
    // TODO: 프로젝트 복제 처리
  }

  private fun showFilterDialog() {
    _uiState.update { it.copy(showFilterDialog = true) }
  }

  private fun hideFilterDialog() {
    _uiState.update { it.copy(showFilterDialog = false) }
  }

  private fun showSortDialog() {
    _uiState.update { it.copy(showSortDialog = true) }
  }

  private fun hideSortDialog() {
    _uiState.update { it.copy(showSortDialog = false) }
  }

  private fun updateFabVisibility(visible: Boolean) {
    _uiState.update { it.copy(fabVisible = visible) }
  }

  private fun dismissError() {
    _uiState.update { it.copy(errorMessage = null) }
  }

  private fun retryLastAction() {
    // TODO: 마지막 액션 재시도
    loadProjects()
  }

  private fun updateNotificationSettings(enabled: Boolean) {
    // TODO: 알림 설정 업데이트
  }

  private fun updateAutoRefresh(enabled: Boolean) {
    // TODO: 자동 새로고침 설정 업데이트
  }
}