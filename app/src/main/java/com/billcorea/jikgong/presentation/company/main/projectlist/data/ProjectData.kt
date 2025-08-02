// ========================================
// 📄 data/ProjectData.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import androidx.compose.ui.graphics.Color
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDateTime

/**
 * 프로젝트 데이터 클래스
 */
data class Project(
  val id: String,
  val title: String,
  val location: String,
  val workType: String,
  val dailyWage: Long,
  val status: ProjectStatus,
  val recruitCount: Int,
  val currentCount: Int,
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val description: String,
  val requiredSkills: List<String> = emptyList(),
  val contactPhone: String,
  val isUrgent: Boolean = false,
  val transportationFee: Long = 0L,
  val mealProvided: Boolean = false,
  val accommodationProvided: Boolean = false,
  val companyName: String = "",
  val address: String = "",
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
) {
  // 모집 완료 여부
  val isRecruitmentComplete: Boolean
    get() = currentCount >= recruitCount

  // 마감일까지 남은 일수
  val daysUntilDeadline: Long
    get() = java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), startDate)

  // 프로젝트 진행률
  val progressPercentage: Float
    get() = if (recruitCount > 0) currentCount.toFloat() / recruitCount.toFloat() else 0f

  // 시급 계산 (8시간 기준)
  val hourlyWage: Long
    get() = dailyWage / 8
}

/**
 * 프로젝트 통계 요약
 */
data class ProjectSummary(
  val total: Int = 0,
  val recruiting: Int = 0,
  val inProgress: Int = 0,
  val completed: Int = 0,
  val thisMonthRecruits: Int = 0,
  val averageDailyWage: Long = 0L,
  val totalRecruitCount: Int = 0,
  val totalCurrentCount: Int = 0
) {
  val recruitmentRate: Float
    get() = if (totalRecruitCount > 0) totalCurrentCount.toFloat() / totalRecruitCount.toFloat() else 0f
}

// ========================================
