package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model

import java.time.LocalDate

data class ProjectCreateUiState(
  val projectName: String = "",
  val startDate: LocalDate? = null,
  val endDate: LocalDate? = null,
  val workLocation: String = "",
  val isLoading: Boolean = false,
  val isFormValid: Boolean = false,
  val showSuccessDialog: Boolean = false,
  val errorMessage: String? = null
)