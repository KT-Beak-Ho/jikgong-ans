package com.billcorea.jikgong.presentation.company.main.projectlist.model

enum class ProjectFilter(val displayName: String) {
  ALL("전체"),
  RECRUITING("모집중"),
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  URGENT("긴급"),
  BOOKMARKED("북마크")
}
