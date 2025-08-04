package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary

data class ProjectListUiState(
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val summary: ProjectSummary = ProjectSummary(),
  val isLoading: Boolean = false,
  val isLoadingMore: Boolean = false,
  val canLoadMore: Boolean = true,
  val errorMessage: String? = null,
  val searchQuery: String = "",
  val isSearching: Boolean = false,
  val selectedCategory: String? = null,
  val selectedStatus: String? = null,
  val selectedSortBy: String = "LATEST",
  val showBookmarkedOnly: Boolean = false
)
