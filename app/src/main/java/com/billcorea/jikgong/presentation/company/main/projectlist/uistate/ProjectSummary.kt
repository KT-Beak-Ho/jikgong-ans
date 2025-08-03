// File: uistate/ProjectSummary.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

data class ProjectSummary(
  val totalProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val inProgressProjects: Int = 0,
  val completedProjects: Int = 0,
  val totalApplicants: Int = 0,
  val averageDailyWage: Int = 0
)