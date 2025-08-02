// 📄 uistate/ProjectListEvent.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus

/**
 * 프로젝트 목록 이벤트
 */
sealed class ProjectListEvent {

  // 데이터 관련 이벤트
  object RefreshProjects : ProjectListEvent()
  object LoadMoreProjects : ProjectListEvent()
  data class SelectProject(val projectId: String) : ProjectListEvent()

  // 필터링 관련 이벤트
  data class FilterByStatus(val status: ProjectStatus?) : ProjectListEvent()
  data class SortProjects(val sortBy: ProjectSortBy) : ProjectListEvent()
  object ClearFilters : ProjectListEvent()

  // 검색 관련 이벤트
  data class SearchProjects(val query: String) : ProjectListEvent()
  object ToggleSearch : ProjectListEvent()
  object ClearSearch : ProjectListEvent()
  data class SelectSearchSuggestion(val suggestion: String) : ProjectListEvent()

  // 네비게이션 관련 이벤트
  object CreateNewProject : ProjectListEvent()
  data class NavigateToProjectDetail(val projectId: String) : ProjectListEvent()
  data class NavigateToProjectEdit(val projectId: String) : ProjectListEvent()

  // 액션 관련 이벤트
  data class QuickApply(val projectId: String) : ProjectListEvent()
  data class ToggleBookmark(val projectId: String) : ProjectListEvent()
  data class ShareProject(val projectId: String) : ProjectListEvent()
  data class DeleteProject(val projectId: String) : ProjectListEvent()
  data class DuplicateProject(val projectId: String) : ProjectListEvent()

  // UI 상태 관련 이벤트
  object ShowFilterDialog : ProjectListEvent()
  object HideFilterDialog : ProjectListEvent()
  object ShowSortDialog : ProjectListEvent()
  object HideSortDialog : ProjectListEvent()
  data class UpdateFabVisibility(val visible: Boolean) : ProjectListEvent()

  // 에러 처리 관련 이벤트
  object DismissError : ProjectListEvent()
  object RetryLastAction : ProjectListEvent()

  // 설정 관련 이벤트
  data class UpdateNotificationSettings(val enabled: Boolean) : ProjectListEvent()
  data class UpdateAutoRefresh(val enabled: Boolean) : ProjectListEvent()
}