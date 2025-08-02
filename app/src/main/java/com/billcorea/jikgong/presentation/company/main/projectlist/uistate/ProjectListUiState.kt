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
  val isLoadingMore: Boolean = false,

  // 정렬 상태
  val sortBy: ProjectSortBy = ProjectSortBy.CREATED_DATE_DESC,

  // UI 상태
  val showFilterDialog: Boolean = false,
  val showSortDialog: Boolean = false,
  val fabVisible: Boolean = true
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
}

/**
 * 프로젝트 정렬 기준
 */
enum class ProjectSortBy(
  val displayName: String,
  val description: String
) {
  CREATED_DATE_DESC("최신 등록순", "가장 최근에 등록된 프로젝트부터"),
  CREATED_DATE_ASC("오래된 등록순", "가장 오래전에 등록된 프로젝트부터"),
  START_DATE_ASC("시작일 빠른순", "시작일이 가장 빠른 프로젝트부터"),
  START_DATE_DESC("시작일 늦은순", "시작일이 가장 늦은 프로젝트부터"),
  DAILY_WAGE_DESC("높은 일당순", "일당이 높은 프로젝트부터"),
  DAILY_WAGE_ASC("낮은 일당순", "일당이 낮은 프로젝트부터"),
  LOCATION("지역순", "지역별로 정렬"),
  RECRUIT_RATE("모집률순", "모집률이 낮은 프로젝트부터"),
  URGENT_FIRST("긴급 우선", "긴급 프로젝트를 먼저 표시")
}

// ========================================
