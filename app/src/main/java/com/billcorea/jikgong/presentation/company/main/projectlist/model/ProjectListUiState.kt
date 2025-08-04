package com.billcorea.jikgong.presentation.company.main.projectlist.model

data class ProjectListUiState(
  val isLoading: Boolean = false,
  val error: String? = null,
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val stats: ProjectStats = ProjectStats()
)