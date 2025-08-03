package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus

sealed class ProjectListEvent {
  // 프로젝트 관련 이벤트
  data class SelectProject(val projectId: String) : ProjectListEvent()
  data class ToggleBookmark(val projectId: String) : ProjectListEvent()
  data class QuickApply(val projectId: String) : ProjectListEvent()
  data class ShareProject(val projectId: String) : ProjectListEvent()
  data class EditProject(val projectId: String) : ProjectListEvent()
  data class DeleteProject(val projectId: String) : ProjectListEvent()
  data class DuplicateProject(val projectId: String) : ProjectListEvent()

  // 검색 관련 이벤트
  object ToggleSearch : ProjectListEvent()
  data class SearchProjects(val query: String) : ProjectListEvent()
  data class SelectSearchSuggestion(val suggestion: String) : ProjectListEvent()
  object ClearSearch : ProjectListEvent()

  // 필터 관련 이벤트
  data class FilterByStatus(val status: ProjectStatus?) : ProjectListEvent()
  object ShowFilterDialog : ProjectListEvent()
  object HideFilterDialog : ProjectListEvent()
  object ClearFilters : ProjectListEvent()

  // 정렬 관련 이벤트
  data class SortBy(val sortBy: ProjectSortBy) : ProjectListEvent()
  object ToggleSortDialog : ProjectListEvent()

  // 로딩 관련 이벤트
  object RefreshProjects : ProjectListEvent()
  object LoadMoreProjects : ProjectListEvent()
  object RetryLoading : ProjectListEvent()

  // 프로젝트 생성 관련 이벤트
  object CreateNewProject : ProjectListEvent()

  // UI 상태 이벤트
  object DismissError : ProjectListEvent()
  data class UpdateFabVisibility(val visible: Boolean) : ProjectListEvent()
  data class OnScrollPositionChanged(val position: Int) : ProjectListEvent()
}