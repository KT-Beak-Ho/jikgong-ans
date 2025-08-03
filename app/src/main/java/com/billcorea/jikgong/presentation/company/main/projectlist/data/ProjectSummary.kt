// 📄 data/ProjectSummary.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

/**
 * 📊 프로젝트 통계 정보
 */
data class ProjectSummary(
  val totalProjects: Int = 0,
  val recruitingProjects: Int = 0,
  val inProgressProjects: Int = 0,
  val completedProjects: Int = 0,
  val totalBudget: Int = 0,
  val averageWage: Int = 0
) {
  /**
   * 모집중 프로젝트 비율
   */
  val recruitingRate: Float
    get() = if (totalProjects > 0) recruitingProjects.toFloat() / totalProjects else 0f

  /**
   * 진행중 프로젝트 비율
   */
  val inProgressRate: Float
    get() = if (totalProjects > 0) inProgressProjects.toFloat() / totalProjects else 0f

  /**
   * 완료 프로젝트 비율
   */
  val completedRate: Float
    get() = if (totalProjects > 0) completedProjects.toFloat() / totalProjects else 0f

  /**
   * 평균 예산 계산
   */
  val averageBudget: Int
    get() = if (totalProjects > 0) totalBudget / totalProjects else 0
}

// ========================================
// 📄 data/ProjectStatus.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import androidx.compose.ui.graphics.Color
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 🏷️ 프로젝트 상태
 */
enum class ProjectStatus(
  val displayName: String,
  val description: String
) {
  RECRUITING("모집중", "인력을 모집하고 있는 프로젝트"),
  IN_PROGRESS("진행중", "현재 작업이 진행중인 프로젝트"),
  COMPLETED("완료", "작업이 완료된 프로젝트"),
  CANCELLED("취소됨", "취소된 프로젝트"),
  PAUSED("일시중단", "일시적으로 중단된 프로젝트");

  /**
   * 상태별 색상 반환
   */
  @androidx.compose.runtime.Composable
  fun getColor(): Color {
    return when (this) {
      RECRUITING -> appColorScheme.primary
      IN_PROGRESS -> appColorScheme.secondary
      COMPLETED -> Color(0xFF4CAF50) // 녹색
      CANCELLED -> Color(0xFFF44336) // 빨간색
      PAUSED -> Color(0xFFFF9800) // 주황색
    }
  }

  /**
   * 상태별 배경 색상 반환
   */
  @androidx.compose.runtime.Composable
  fun getBackgroundColor(): Color {
    return when (this) {
      RECRUITING -> appColorScheme.primaryContainer
      IN_PROGRESS -> appColorScheme.secondaryContainer
      COMPLETED -> Color(0xFFE8F5E8) // 연한 녹색
      CANCELLED -> Color(0xFFFFEBEE) // 연한 빨간색
      PAUSED -> Color(0xFFFFF3E0) // 연한 주황색
    }
  }
}