// ========================================
// 📄 data/ProjectSummary.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

/**
 * 프로젝트 목록 요약 통계
 */
data class ProjectSummary(
  val totalProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val inProgressProjects: Int = 0,
  val completedProjects: Int = 0,
  val cancelledProjects: Int = 0,
  val urgentProjects: Int = 0,
  val totalRequiredWorkers: Int = 0,
  val totalAppliedWorkers: Int = 0,
  val averageDailyWage: Int = 0,
  val thisMonthProjects: Int = 0
) {
  /**
   * 전체 모집률
   */
  val overallRecruitmentRate: Float
    get() = if (totalRequiredWorkers > 0) {
      (totalAppliedWorkers.toFloat() / totalRequiredWorkers.toFloat()).coerceIn(0f, 1f)
    } else 0f

  /**
   * 활성 프로젝트 수 (모집중 + 진행중)
   */
  val activeProjects: Int
    get() = recruitingProjects + inProgressProjects

  /**
   * 완료율
   */
  val completionRate: Float
    get() = if (totalProjects > 0) {
      (completedProjects.toFloat() / totalProjects.toFloat()).coerceIn(0f, 1f)
    } else 0f
}