package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model

sealed class ProjectCreateUiEvent {
  object NavigateBack : ProjectCreateUiEvent()
  data class ShowError(val message: String) : ProjectCreateUiEvent()
}