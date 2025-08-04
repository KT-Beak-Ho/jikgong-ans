package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

enum class ProjectSortBy(val displayName: String, val code: String) {
  LATEST("최신순", "LATEST"),
  DEADLINE("마감임박순", "DEADLINE"),
  WAGE_HIGH("임금높은순", "WAGE_HIGH"),
  WAGE_LOW("임금낮은순", "WAGE_LOW"),
  DISTANCE("거리순", "DISTANCE"),
  POPULAR("인기순", "POPULAR"),
  APPLICANTS_ASC("지원자적은순", "APPLICANTS_ASC");

  companion object {
    fun fromCode(code: String): ProjectSortBy {
      return values().find { it.code == code } ?: LATEST
    }
  }
}