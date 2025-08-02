// ========================================
// 📄 data/ProjectStatus.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 프로젝트 상태
 */
enum class ProjectStatus(
  val displayName: String,
  val description: String,
  val color: Color,
  val icon: ImageVector
) {
  RECRUITING(
    displayName = "모집중",
    description = "인력을 모집하고 있는 프로젝트",
    color = Color(0xFF2196F3), // 파란색
    icon = Icons.Default.PersonAdd
  ),
  IN_PROGRESS(
    displayName = "진행중",
    description = "현재 진행 중인 프로젝트",
    color = Color(0xFF4CAF50), // 초록색
    icon = Icons.Default.Build
  ),
  COMPLETED(
    displayName = "완료",
    description = "완료된 프로젝트",
    color = Color(0xFF9E9E9E), // 회색
    icon = Icons.Default.CheckCircle
  ),
  CANCELLED(
    displayName = "취소",
    description = "취소된 프로젝트",
    color = Color(0xFFF44336), // 빨간색
    icon = Icons.Default.Cancel
  ),
  PAUSED(
    displayName = "일시중단",
    description = "일시적으로 중단된 프로젝트",
    color = Color(0xFFFF9800), // 주황색
    icon = Icons.Default.Pause
  );

  companion object {
    /**
     * 활성 상태 목록 (지원 가능한 상태들)
     */
    val activeStatuses = listOf(RECRUITING, IN_PROGRESS)

    /**
     * 완료 상태 목록
     */
    val completedStatuses = listOf(COMPLETED, CANCELLED)

    /**
     * 표시명으로 상태 찾기
     */
    fun fromDisplayName(displayName: String): ProjectStatus? {
      return values().find { it.displayName == displayName }
    }
  }
}

/**
 * 건설 작업 유형
 */
enum class WorkType(
  val displayName: String,
  val description: String,
  val icon: ImageVector,
  val requiredSkill: SkillLevel = SkillLevel.BEGINNER
) {
  GENERAL_CONSTRUCTION(
    displayName = "일반건설",
    description = "기본적인 건설 작업",
    icon = Icons.Default.Construction
  ),
  INTERIOR(
    displayName = "인테리어",
    description = "실내 인테리어 작업",
    icon = Icons.Default.Home,
    requiredSkill = SkillLevel.INTERMEDIATE
  ),
  DEMOLITION(
    displayName = "철거",
    description = "건물 철거 작업",
    icon = Icons.Default.Delete
  ),
  LANDSCAPING(
    displayName = "조경",
    description = "조경 및 정원 작업",
    icon = Icons.Default.Grass
  ),
  ROAD_CONSTRUCTION(
    displayName = "도로공사",
    description = "도로 건설 및 보수",
    icon = Icons.Default.Route
  ),
  PLUMBING(
    displayName = "배관",
    description = "배관 설치 및 수리",
    icon = Icons.Default.Plumbing,
    requiredSkill = SkillLevel.EXPERT
  ),
  ELECTRICAL(
    displayName = "전기",
    description = "전기 설비 작업",
    icon = Icons.Default.ElectricalServices,
    requiredSkill = SkillLevel.EXPERT
  ),
  PAINTING(
    displayName = "도색",
    description = "건물 도색 작업",
    icon = Icons.Default.FormatPaint
  ),
  CLEANING(
    displayName = "청소",
    description = "건설 현장 청소",
    icon = Icons.Default.CleaningServices
  );

  companion object {
    fun getByDisplayName(name: String): WorkType? {
      return values().find { it.displayName == name }
    }
  }
}

/**
 * 기술 수준
 */
enum class SkillLevel(
  val displayName: String,
  val description: String
) {
  BEGINNER("초급", "경험이 없어도 가능"),
  INTERMEDIATE("중급", "어느 정도 경험 필요"),
  EXPERT("고급", "전문 기술 필요")
}

/**
 * 지급 방식
 */
enum class PaymentMethod(
  val displayName: String,
  val description: String
) {
  DIRECT("직접지급", "회사에서 직접 지급"),
  WEEKLY("주급", "매주 지급"),
  MONTHLY("월급", "매월 지급"),
  PROJECT_END("완료 후", "프로젝트 완료 후 일괄 지급")
}