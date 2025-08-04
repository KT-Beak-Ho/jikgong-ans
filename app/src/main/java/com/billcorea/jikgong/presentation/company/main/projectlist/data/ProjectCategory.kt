package com.billcorea.jikgong.presentation.company.main.projectlist.data

enum class ProjectCategory(val displayName: String, val code: String) {
  REBAR_WORKER("철근공", "REBAR_WORKER"),
  ELECTRICIAN("전기공", "ELECTRICIAN"),
  PLUMBER("배관공", "PLUMBER"),
  CARPENTER("목공", "CARPENTER"),
  CIVIL_ENGINEER("토목공", "CIVIL_ENGINEER"),
  PAINTER("도장공", "PAINTER"),
  WELDER("용접공", "WELDER"),
  GENERAL_LABORER("일반작업자", "GENERAL_LABORER");

  companion object {
    fun fromCode(code: String): ProjectCategory? {
      return values().find { it.code == code }
    }

    fun getDisplayName(code: String): String {
      return fromCode(code)?.displayName ?: code
    }
  }
}
