// ========================================
// 📄 uistate/ProjectListUiState.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary

/**
 * 프로젝트 목록 UI 상태
 */
data class ProjectListUiState(
  // 데이터 상태
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val summary: ProjectSummary = ProjectSummary(),

  // 로딩 상태
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val isLoadingMore: Boolean = false,

  // 선택 상태
  val selectedProjectId: String? = null,
  val selectedFilter: ProjectStatus? = null,

  // 검색 상태
  val searchQuery: String = "",
  val isSearchVisible: Boolean = false,
  val searchSuggestions: List<String> = emptyList(),

  // 에러 상태
  val errorMessage: String? = null,
  val hasNetworkError: Boolean = false,

  // 페이징 상태
  val currentPage: Int = 1,
  val hasNextPage: Boolean = false,

  // 정렬 상태
  val sortBy: ProjectSortBy = ProjectSortBy.CREATED_DATE_DESC,

  // UI 상태
  val showFilterDialog: Boolean = false,
  val showSortDialog: Boolean = false,
  val fabVisible: Boolean = true,

  // 권한 상태
  val canCreateProject: Boolean = true,
  val canEditProject: Boolean = true,
  val canDeleteProject: Boolean = true
) {
  /**
   * 빈 상태 확인
   */
  val isEmpty: Boolean
    get() = !isLoading && filteredProjects.isEmpty()

  /**
   * 검색 중인지 확인
   */
  val isSearching: Boolean
    get() = searchQuery.isNotEmpty()

  /**
   * 필터링된 상태인지 확인
   */
  val isFiltered: Boolean
    get() = selectedFilter != null

  /**
   * 활성 프로젝트 개수
   */
  val activeProjectsCount: Int
    get() = projects.count { it.status in ProjectStatus.activeStatuses }

  /**
   * 긴급 프로젝트 개수
   */
  val urgentProjectsCount: Int
    get() = projects.count { it.isUrgent && it.status == ProjectStatus.RECRUITING }

  /**
   * 현재 필터의 프로젝트 개수
   */
  val currentFilterCount: Int
    get() = if (selectedFilter == null) {
      projects.size
    } else {
      projects.count { it.status == selectedFilter }
    }

  /**
   * 로딩 상태가 아니고 데이터가 있는지 확인
   */
  val hasData: Boolean
    get() = !isLoading && projects.isNotEmpty()

  /**
   * 검색 결과가 있는지 확인
   */
  val hasSearchResults: Boolean
    get() = isSearching && filteredProjects.isNotEmpty()

  /**
   * 새로고침 가능 여부
   */
  val canRefresh: Boolean
    get() = !isLoading && !isRefreshing

  /**
   * 더 많은 데이터 로드 가능 여부
   */
  val canLoadMore: Boolean
    get() = hasNextPage && !isLoadingMore && !isLoading
}