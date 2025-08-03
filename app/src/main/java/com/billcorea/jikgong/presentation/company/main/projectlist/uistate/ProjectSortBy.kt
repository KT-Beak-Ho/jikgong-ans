package com.billcorea.jikgong.presentation.company.main.projectlist.uistate

enum class ProjectSortBy(val displayName: String) {
  CREATED_DATE_DESC("최신순"),
  CREATED_DATE_ASC("오래된순"),
  DAILY_WAGE_DESC("높은 임금순"),
  DAILY_WAGE_ASC("낮은 임금순"),
  DEADLINE_ASC("마감 임박순"),
  APPLICANTS_DESC("지원자 많은순"),
  ALPHABETICAL("가나다순")
}