package com.billcorea.jikgong.presentation.company.main.projectlist.data

enum class ProjectStatus(val displayName: String, val code: String) {
  RECRUITING("모집중", "RECRUITING"),
  IN_PROGRESS("진행중", "IN_PROGRESS"),
  COMPLETED("완료", "COMPLETED"),
  SUSPENDED("일시중단", "SUSPENDED"),
  CANCELLED("취소", "CANCELLED");

  companion object {
    fun fromCode(code: String): ProjectStatus? {
      return values().find { it.code == code }
    }

    fun getDisplayName(code: String): String {
      return fromCode(code)?.displayName ?: code
    }
  }
}
