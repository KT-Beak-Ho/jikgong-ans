// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/projectcreate/viewmodel/ProjectCreateViewModel.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.*
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ProjectCreateViewModel(
  private val repository: ProjectCreateRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(ProjectCreateUiState())
  val uiState: StateFlow<ProjectCreateUiState> = _uiState.asStateFlow()

  private val _uiEvent = MutableSharedFlow<ProjectCreateUiEvent>()
  val uiEvent: SharedFlow<ProjectCreateUiEvent> = _uiEvent.asSharedFlow()

  fun handleEvent(event: ProjectCreateEvent) {
    when (event) {
      is ProjectCreateEvent.UpdateProjectName -> updateProjectName(event.name)
      is ProjectCreateEvent.UpdateStartDate -> updateStartDate(event.date)
      is ProjectCreateEvent.UpdateEndDate -> updateEndDate(event.date)
      is ProjectCreateEvent.UpdateWorkLocation -> updateWorkLocation(event.location)
      is ProjectCreateEvent.CreateProject -> createProject()
      is ProjectCreateEvent.DismissSuccessDialog -> dismissSuccessDialog()
      is ProjectCreateEvent.DismissError -> dismissError()
    }
  }

  private fun updateProjectName(name: String) {
    _uiState.update { state ->
      state.copy(
        projectName = name,
        isFormValid = validateForm(
          name = name,
          startDate = state.startDate,
          endDate = state.endDate,
          location = state.workLocation
        )
      )
    }
  }

  private fun updateStartDate(date: LocalDate) {
    _uiState.update { state ->
      state.copy(
        startDate = date,
        isFormValid = validateForm(
          name = state.projectName,
          startDate = date,
          endDate = state.endDate,
          location = state.workLocation
        )
      )
    }
  }

  private fun updateEndDate(date: LocalDate) {
    _uiState.update { state ->
      state.copy(
        endDate = date,
        isFormValid = validateForm(
          name = state.projectName,
          startDate = state.startDate,
          endDate = date,
          location = state.workLocation
        )
      )
    }
  }

  private fun updateWorkLocation(location: String) {
    _uiState.update { state ->
      state.copy(
        workLocation = location,
        isFormValid = validateForm(
          name = state.projectName,
          startDate = state.startDate,
          endDate = state.endDate,
          location = location
        )
      )
    }
  }

  private fun validateForm(
    name: String,
    startDate: LocalDate?,
    endDate: LocalDate?,
    location: String
  ): Boolean {
    return name.isNotBlank() &&
      startDate != null &&
      endDate != null &&
      location.isNotBlank() &&
      (endDate.isAfter(startDate) || endDate.isEqual(startDate))
  }

  private fun createProject() {
    val state = _uiState.value
    if (!state.isFormValid) return

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, errorMessage = null) }

      val project = Project(
        id = "", // 서버에서 생성
        title = state.projectName,
        company = "대림건설", // TODO: 실제 회사 정보 사용
        location = state.workLocation,
        category = "GENERAL", // TODO: 카테고리 선택 기능 추가
        status = "RECRUITING",
        startDate = state.startDate?.atStartOfDay() ?: LocalDateTime.now(),
        endDate = state.endDate?.atStartOfDay() ?: LocalDateTime.now(),
        wage = 200000, // TODO: 일당 입력 기능 추가
        description = "",
        requirements = emptyList(),
        benefits = emptyList(),
        currentApplicants = 0,
        maxApplicants = 10, // TODO: 필요 인원 입력 기능 추가
        isUrgent = false,
        isBookmarked = false
      )

      repository.createProject(project)
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              showSuccessDialog = true
            )
          }
        }
        .onFailure { exception ->
          _uiState.update {
            it.copy(
              isLoading = false,
              errorMessage = "프로젝트 등록에 실패했습니다: ${exception.message}"
            )
          }
        }
    }
  }

  private fun dismissSuccessDialog() {
    viewModelScope.launch {
      _uiState.update { it.copy(showSuccessDialog = false) }
      _uiEvent.emit(ProjectCreateUiEvent.NavigateBack)
    }
  }

  private fun dismissError() {
    _uiState.update { it.copy(errorMessage = null) }
  }
}