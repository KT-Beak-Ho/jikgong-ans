// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/model/ProjectStats.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.model

data class ProjectStats(
  val totalProjects: Int = 0,
  val activeProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val completedProjects: Int = 0,
  val totalApplicants: Int = 0,
  val averageWage: Int = 0
)

