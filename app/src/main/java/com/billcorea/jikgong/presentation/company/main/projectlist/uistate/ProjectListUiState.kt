// ========================================
// 📄 uistate/ProjectListUiState.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSortBy

/**
 * 🎯 프로젝트 목록 화면 UI 상태
 *
 * 모든 화면 상태를 하나의 데이터 클래스로 관리하여
 * 상태 변경을 예측 가능하게 만드는 단일 진실 공급원
 */
data class ProjectListUiState(
  // 📊 데이터 상태
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val summary: ProjectSummary = ProjectSummary(),

  // 🔄 로딩 상태
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val isLoadingMore: Boolean = false,
  val canLoadMore: Boolean = true,

  // 🔍 검색 상태
  val isSearchVisible: Boolean = false,
  val isSearching: Boolean = false,
  val searchQuery: String = "",
  val searchSuggestions: List<String> = emptyList(),

  // 🏷️ 필터 상태
  val selectedFilter: ProjectStatus? = null,
  val isFiltered: Boolean = false,
  val isFilterDialogVisible: Boolean = false,

  // 📊 정렬 상태
  val currentSort: ProjectSortBy = ProjectSortBy.DATE_LATEST,
  val isSortDialogVisible: Boolean = false,

  // 🎛️ UI 상태
  val isFabVisible: Boolean = true,
  val errorMessage: String? = null,
  val scrollPosition: Int = 0
) {
  /**
   * 빈 상태 여부 확인
   */
  val isEmpty: Boolean
    get() = !isLoading && filteredProjects.isEmpty()

  /**
   * 검색 결과 없음 상태
   */
  val isSearchEmpty: Boolean
    get() = isSearching && filteredProjects.isEmpty()

  /**
   * 필터 결과 없음 상태
   */
  val isFilterEmpty: Boolean
    get() = isFiltered && !isSearching && filteredProjects.isEmpty()
}

// ========================================
// 📄 uistate/ProjectListEvent.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSortBy

/**
 * 🎮 프로젝트 목록 화면 이벤트
 *
 * 사용자의 모든 상호작용을 sealed class로 정의하여
 * 타입 안전성과 이벤트 처리의 일관성을 보장
 */
sealed class ProjectListEvent {

  // ========================================
  // 📋 프로젝트 관련 이벤트
  // ========================================

  /**
   * 프로젝트 선택 (상세 화면으로 이동)
   */
  data class SelectProject(val projectId: String) : ProjectListEvent()

  /**
   * 북마크 토글
   */
  data class ToggleBookmark(val projectId: String) : ProjectListEvent()

  /**
   * 빠른 지원
   */
  data class QuickApply(val projectId: String) : ProjectListEvent()

  /**
   * 프로젝트 상세 정보 표시
   */
  data class ShowProjectDetails(val projectId: String) : ProjectListEvent()

  /**
   * 프로젝트 공유
   */
  data class ShareProject(val projectId: String) : ProjectListEvent()

  // ========================================
  // 🔍 필터링 및 검색 이벤트
  // ========================================

  /**
   * 상태별 필터링
   */
  data class FilterByStatus(val status: ProjectStatus?) : ProjectListEvent()

  /**
   * 프로젝트 검색
   */
  data class SearchProjects(val query: String) : ProjectListEvent()

  /**
   * 검색 제안 선택
   */
  data class SelectSearchSuggestion(val suggestion: String) : ProjectListEvent()

  /**
   * 검색 토글
   */
  object ToggleSearch : ProjectListEvent()

  /**
   * 검색 초기화
   */
  object ClearSearch : ProjectListEvent()

  /**
   * 필터 초기화
   */
  object ClearFilters : ProjectListEvent()

  // ========================================
  // 📊 정렬 이벤트
  // ========================================

  /**
   * 정렬 방식 변경
   */
  data class SortBy(val sortBy: ProjectSortBy) : ProjectListEvent()

  /**
   * 정렬 다이얼로그 토글
   */
  object ToggleSortDialog : ProjectListEvent()

  // ========================================
  // 🔄 새로고침 및 로딩 이벤트
  // ========================================

  /**
   * 프로젝트 목록 새로고침
   */
  object RefreshProjects : ProjectListEvent()

  /**
   * 더 많은 프로젝트 로딩 (페이징)
   */
  object LoadMoreProjects : ProjectListEvent()

  /**
   * 로딩 재시도
   */
  object RetryLoading : ProjectListEvent()

  // ========================================
  // ➕ 프로젝트 생성 및 관리 이벤트
  // ========================================

  /**
   * 새 프로젝트 생성
   */
  object CreateNewProject : ProjectListEvent()

  /**
   * 프로젝트 복제
   */
  data class DuplicateProject(val projectId: String) : ProjectListEvent()

  /**
   * 프로젝트 삭제
   */
  data class DeleteProject(val projectId: String) : ProjectListEvent()

  /**
   * 프로젝트 편집
   */
  data class EditProject(val projectId: String) : ProjectListEvent()

  // ========================================
  // 🎛️ UI 상태 이벤트
  // ========================================

  /**
   * 필터 다이얼로그 표시
   */
  object ShowFilterDialog : ProjectListEvent()

  /**
   * 필터 다이얼로그 숨기기
   */
  object HideFilterDialog : ProjectListEvent()

  /**
   * 에러 메시지 해제
   */
  object DismissError : ProjectListEvent()

  /**
   * FAB 가시성 업데이트
   */
  data class UpdateFabVisibility(val visible: Boolean) : ProjectListEvent()

  /**
   * 스크롤 위치 변경
   */
  data class OnScrollPositionChanged(val position: Int) : ProjectListEvent()
}

// ========================================
// 📄 data/ProjectSortBy.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

/**
 * 📊 프로젝트 정렬 방식
 */
enum class ProjectSortBy(val displayName: String) {
  DATE_LATEST("최신순"),
  DATE_OLDEST("오래된순"),
  SALARY_HIGH("임금 높은순"),
  SALARY_LOW("임금 낮은순"),
  DEADLINE("마감일순"),
  LOCATION("지역순")
}

// ========================================
