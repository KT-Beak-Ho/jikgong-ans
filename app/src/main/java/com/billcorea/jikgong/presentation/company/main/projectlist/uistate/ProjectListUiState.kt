package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary

data class ProjectListUiState(
  // 데이터 상태
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val summary: ProjectSummary = ProjectSummary(),

  // 로딩 상태
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val isLoadingMore: Boolean = false,
  val canLoadMore: Boolean = true,

  // 검색 상태
  val isSearching: Boolean = false,
  val searchQuery: String = "",
  val searchSuggestions: List<String> = emptyList(),

  // 필터 상태
  val selectedFilter: ProjectStatus? = null,
  val isFiltered: Boolean = false,
  val showFilterDialog: Boolean = false,

  // 정렬 상태
  val sortBy: ProjectSortBy = ProjectSortBy.CREATED_DATE_DESC,
  val showSortDialog: Boolean = false,

  // UI 상태
  val errorMessage: String? = null,
  val fabVisible: Boolean = true,
  val scrollPosition: Int = 0
) {
  val isEmpty: Boolean
    get() = filteredProjects.isEmpty() && !isLoading
}