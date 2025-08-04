// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/model/ProjectListUiState.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.model

data class ProjectListUiState(
  val isLoading: Boolean = false,
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val stats: ProjectStats = ProjectStats(),
  val error: String? = null
)