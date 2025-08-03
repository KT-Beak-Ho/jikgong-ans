package com.billcorea.jikgong.presentation.company.main.projectlist.data

enum class ProjectStatus(val displayName: String) {
  RECRUITING("모집중"),
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  PAUSED("일시중단"),
  CANCELLED("취소됨")
}