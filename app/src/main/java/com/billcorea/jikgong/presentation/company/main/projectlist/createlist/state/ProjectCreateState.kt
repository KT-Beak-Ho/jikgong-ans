// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/createlist/state/ProjectCreateState.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.createlist.state

import java.time.LocalDate

data class ProjectCreateState(
  // 입력 필드
  val projectName: String = "",
  val startDate: LocalDate? = null,
  val endDate: LocalDate? = null,
  val location: String = "",
  val locationDetail: String = "",

  // 에러 메시지
  val projectNameError: String? = null,
  val startDateError: String? = null,
  val endDateError: String? = null,
  val locationError: String? = null,

  // UI 상태
  val isLoading: Boolean = false,
  val errorMessage: String? = null
)