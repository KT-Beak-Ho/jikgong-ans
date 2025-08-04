package com.billcorea.jikgong.presentation.company.main.projectlist.model

import java.time.LocalDateTime

data class Project(
  val id: String,
  val title: String,
  val company: String,
  val location: String,
  val category: String,
  val status: String, // RECRUITING, IN_PROGRESS, COMPLETED
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val wage: Int, // 일당
  val description: String,
  val requirements: List<String>,
  val benefits: List<String>,
  val currentApplicants: Int,
  val maxApplicants: Int,
  val isBookmarked: Boolean = false,
  val isUrgent: Boolean = false,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)

data class ProjectListUiState(
  val projects: List<Project> = emptyList(),
  val filteredProjects: List<Project> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null,
  val stats: ProjectStats = ProjectStats()
)

data class ProjectStats(
  val totalProjects: Int = 0,
  val activeProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val completedProjects: Int = 0,
  val totalApplicants: Int = 0,
  val averageWage: Int = 0
)

enum class ProjectFilter(val displayName: String) {
  ALL("전체"),
  RECRUITING("모집중"),
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  URGENT("긴급"),
  BOOKMARKED("관심")
}

enum class ProjectCategory(val displayName: String, val code: String) {
  REBAR_WORKER("철근공", "REBAR_WORKER"),
  ELECTRICIAN("전기공", "ELECTRICIAN"),
  CIVIL_ENGINEER("토목공", "CIVIL_ENGINEER"),
  CARPENTER("목공", "CARPENTER"),
  WELDER("용접공", "WELDER"),
  PAINTER("도장공", "PAINTER"),
  PLUMBER("배관공", "PLUMBER"),
  GENERAL_LABORER("일반인부", "GENERAL_LABORER")
}