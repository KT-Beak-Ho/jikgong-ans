package com.billcorea.jikgong.presentation.company.main.projectlist.presentation.model

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStats

data class ProjectListUiState(
  val isLoading: Boolean = false,
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val error: String? = null,
  val stats: ProjectStats = ProjectStats()
)
