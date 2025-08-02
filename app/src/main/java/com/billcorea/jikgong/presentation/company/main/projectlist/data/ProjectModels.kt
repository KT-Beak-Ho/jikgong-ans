// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/data/ProjectModels.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 프로젝트 상태
 */
enum class ProjectStatus(val displayName: String) {
  RECRUITING("모집중"),
  IN_PROGRESS("진행중"),
  COMPLETED("완료")
}

/**
 * 프로젝트 데이터 모델
 */
data class ProjectItem(
  val id: String,
  val title: String,
  val location: String,
  val workType: String,
  val dailyWage: Int,
  val status: ProjectStatus,
  val recruitCount: Int,
  val currentCount: Int,
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val description: String,
  val requiredSkills: List<String>,
  val contactPhone: String
) {
  val formattedWage: String
    get() = "${String.format("%,d", dailyWage)}원"

  val formattedDateRange: String
    get() {
      val formatter = DateTimeFormatter.ofPattern("MM/dd")
      return "${startDate.format(formatter)} ~ ${endDate.format(formatter)}"
    }

  val recruitmentStatus: String
    get() = "$currentCount/$recruitCount"
}

/**
 * 프로젝트 목록 UI 상태
 */
data class ProjectListUiState(
  val isLoading: Boolean = false,
  val projects: List<ProjectItem> = emptyList(),
  val filteredProjects: List<ProjectItem> = emptyList(),
  val selectedProjectId: String? = null,
  val selectedFilter: ProjectStatus? = null,
  val searchQuery: String = "",
  val error: String? = null
)

/**
 * 프로젝트 목록 이벤트
 */
sealed class ProjectListEvent {
  object RefreshProjects : ProjectListEvent()
  data class FilterByStatus(val status: ProjectStatus?) : ProjectListEvent()
  data class SelectProject(val projectId: String) : ProjectListEvent()
  object CreateNewProject : ProjectListEvent()
  data class SearchProjects(val query: String) : ProjectListEvent()
}