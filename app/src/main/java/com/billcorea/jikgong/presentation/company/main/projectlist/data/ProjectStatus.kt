// 📄 data/ProjectStatus.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 프로젝트 상태 enum
 */
enum class ProjectStatus(
  val displayName: String,
  val priority: Int
) {
  RECRUITING("모집중", 1),
  IN_PROGRESS("진행중", 2),
  COMPLETED("완료", 3),
  CANCELLED("취소", 4),
  PAUSED("일시정지", 5);

  /**
   * 상태별 색상
   */
  val color: Color
    @Composable
    get() = when (this) {
      RECRUITING -> appColorScheme.primary
      IN_PROGRESS -> appColorScheme.secondary
      COMPLETED -> appColorScheme.tertiary
      CANCELLED -> appColorScheme.error
      PAUSED -> appColorScheme.outline
    }

  /**
   * 상태별 설명
   */
  val description: String
    get() = when (this) {
      RECRUITING -> "인력을 모집하고 있는 프로젝트"
      IN_PROGRESS -> "현재 진행 중인 프로젝트"
      COMPLETED -> "완료된 프로젝트"
      CANCELLED -> "취소된 프로젝트"
      PAUSED -> "일시 정지된 프로젝트"
    }

  companion object {
    /**
     * 활성 상태 목록 (취소/완료 제외)
     */
    val activeStatuses = listOf(RECRUITING, IN_PROGRESS, PAUSED)

    /**
     * 완료 상태 목록
     */
    val finishedStatuses = listOf(COMPLETED, CANCELLED)
  }
}

// ========================================
