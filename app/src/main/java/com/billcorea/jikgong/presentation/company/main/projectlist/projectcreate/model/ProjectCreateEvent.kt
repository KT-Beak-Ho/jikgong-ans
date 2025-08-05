package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model

import java.time.LocalDate

sealed class ProjectCreateEvent {
  data class UpdateProjectName(val name: String) : ProjectCreateEvent()
  data class UpdateStartDate(val date: LocalDate) : ProjectCreateEvent()
  data class UpdateEndDate(val date: LocalDate) : ProjectCreateEvent()
  data class UpdateWorkLocation(val location: String) : ProjectCreateEvent()
  object CreateProject : ProjectCreateEvent()
  object DismissSuccessDialog : ProjectCreateEvent()
  object DismissError : ProjectCreateEvent()
}
