package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model

import java.time.LocalDate

data class ProjectCreateUiState(
  val projectName: String = "",
  val category: String = "일반",
  val startDate: LocalDate? = null,
  val endDate: LocalDate? = null,
  val workLocation: String = "",
  val wage: String = "",
  val maxApplicants: String = "",
  val description: String = "",
  val isUrgent: Boolean = false,
  val isLoading: Boolean = false,
  val isFormValid: Boolean = false,
  val showSuccessDialog: Boolean = false,
  val errorMessage: String? = null
)