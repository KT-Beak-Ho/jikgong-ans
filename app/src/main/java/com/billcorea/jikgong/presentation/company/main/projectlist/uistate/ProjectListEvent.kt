package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

sealed class ProjectListEvent {
  object LoadProjects : ProjectListEvent()
  object LoadMoreProjects : ProjectListEvent()
  object RefreshProjects : ProjectListEvent()

  data class SearchProjects(val query: String) : ProjectListEvent()
  object ToggleSearch : ProjectListEvent()

  data class FilterByCategory(val category: String?) : ProjectListEvent()
  data class FilterByStatus(val status: String?) : ProjectListEvent()
  data class SortBy(val sortBy: String) : ProjectListEvent()

  data class ToggleBookmark(val projectId: String) : ProjectListEvent()
  data class ApplyToProject(val projectId: String) : ProjectListEvent()

  object ToggleBookmarkFilter : ProjectListEvent()
  object ClearFilters : ProjectListEvent()
}
