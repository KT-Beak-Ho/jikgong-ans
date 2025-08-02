// ========================================
// 📄 uistate/ProjectListEvent.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus

/**
 * 프로젝트 목록 화면 이벤트
 */
sealed class ProjectListEvent {
  // 프로젝트 관련 이벤트
  data class SelectProject(val projectId: String) : ProjectListEvent()
  data class ToggleBookmark(val projectId: String) : ProjectListEvent()
  data class QuickApply(val projectId: String) : ProjectListEvent()
  data class ShowProjectDetails(val projectId: String) : ProjectListEvent()
  data class ShareProject(val projectId: String) : ProjectListEvent()

  // 필터링 및 검색 이벤트
  data class FilterByStatus(val status: ProjectStatus?) : ProjectListEvent()
  data class SearchProjects(val query: String) : ProjectListEvent()
  data class SelectSearchSuggestion(val suggestion: String) : ProjectListEvent()
  object ToggleSearch : ProjectListEvent()
  object ClearSearch : ProjectListEvent()
  object ClearFilters : ProjectListEvent()

  // 정렬 이벤트
  data class SortBy(val sortBy: ProjectSortBy) : ProjectListEvent()
  object ToggleSortDialog : ProjectListEvent()

  // 새로고침 및 로딩 이벤트
  object RefreshProjects : ProjectListEvent()
  object LoadMoreProjects : ProjectListEvent()
  object RetryLoading : ProjectListEvent()

  // 프로젝트 생성 및 관리 이벤트
  object CreateNewProject : ProjectListEvent()
  data class DuplicateProject(val projectId: String) : ProjectListEvent()
  data class DeleteProject(val projectId: String) : ProjectListEvent()
  data class EditProject(val projectId: String) : ProjectListEvent()

  // UI 상태 이벤트
  object ShowFilterDialog : ProjectListEvent()
  object HideFilterDialog : ProjectListEvent()
  object DismissError : ProjectListEvent()
  data class UpdateFabVisibility(val visible: Boolean) : ProjectListEvent()

  // 스크롤 이벤트
  data class OnScrollPositionChanged(val position: Int) : ProjectListEvent()

  // 네트워크 이벤트
  object OnNetworkAvailable : ProjectListEvent()
  object OnNetworkLost : ProjectListEvent()
}

/**
 * 프로젝트 정렬 기준
 */
enum class ProjectSortBy(
  val displayName: String,
  val description: String
) {
  CREATED_DATE_DESC("최신 등록순", "가장 최근에 등록된 프로젝트부터"),
  CREATED_DATE_ASC("오래된 등록순", "가장 오래전에 등록된 프로젝트부터"),
  START_DATE_ASC("시작일 빠른순", "시작일이 가장 빠른 프로젝트부터"),
  START_DATE_DESC("시작일 늦은순", "시작일이 가장 늦은 프로젝트부터"),
  DAILY_WAGE_DESC("높은 일당순", "일당이 높은 프로젝트부터"),
  DAILY_WAGE_ASC("낮은 일당순", "일당이 낮은 프로젝트부터"),
  LOCATION("지역순", "지역별로 정렬"),
  RECRUIT_RATE("모집률순", "모집률이 낮은 프로젝트부터"),
  URGENT_FIRST("긴급 우선", "긴급 프로젝트를 먼저 표시"),
  COMPANY_RATING("기업 평점순", "평점이 높은 기업부터"),
  DURATION("기간순", "프로젝트 기간이 짧은 순서부터");

  companion object {
    /**
     * 기본 정렬 옵션들
     */
    val defaultOptions = listOf(
      CREATED_DATE_DESC,
      START_DATE_ASC,
      DAILY_WAGE_DESC,
      URGENT_FIRST
    )

    /**
     * 고급 정렬 옵션들
     */
    val advancedOptions = listOf(
      LOCATION,
      RECRUIT_RATE,
      COMPANY_RATING,
      DURATION
    )
  }
}