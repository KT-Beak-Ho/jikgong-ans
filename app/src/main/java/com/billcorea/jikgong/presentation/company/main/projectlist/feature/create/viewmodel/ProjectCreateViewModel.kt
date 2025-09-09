package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model.*
import com.billcorea.jikgong.api.repository.company.main.projectList.projectCreate.ProjectCreateRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
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
      is ProjectCreateEvent.UpdateCategory -> updateCategory(event.category)
      is ProjectCreateEvent.UpdateStartDate -> updateStartDate(event.date)
      is ProjectCreateEvent.UpdateEndDate -> updateEndDate(event.date)
      is ProjectCreateEvent.UpdateWorkLocation -> updateWorkLocation(event.location)
      is ProjectCreateEvent.UpdateWage -> updateWage(event.wage)
      is ProjectCreateEvent.UpdateMaxApplicants -> updateMaxApplicants(event.count)
      is ProjectCreateEvent.UpdateDescription -> updateDescription(event.description)
      is ProjectCreateEvent.ToggleUrgent -> toggleUrgent(event.isUrgent)
      is ProjectCreateEvent.CreateProject -> createProject()
      is ProjectCreateEvent.DismissSuccessDialog -> dismissSuccessDialog()
      is ProjectCreateEvent.DismissError -> dismissError()
    }
  }

  private fun updateProjectName(name: String) {
    _uiState.update { state ->
      state.copy(
        projectName = name,
        isFormValid = validateForm(state.copy(projectName = name))
      )
    }
  }

  private fun updateCategory(category: String) {
    _uiState.update { it.copy(category = category) }
  }

  private fun updateStartDate(date: LocalDate) {
    _uiState.update { state ->
      state.copy(
        startDate = date,
        isFormValid = validateForm(state.copy(startDate = date))
      )
    }
  }

  private fun updateEndDate(date: LocalDate) {
    _uiState.update { state ->
      state.copy(
        endDate = date,
        isFormValid = validateForm(state.copy(endDate = date))
      )
    }
  }

  private fun updateWorkLocation(location: String) {
    _uiState.update { state ->
      state.copy(
        workLocation = location,
        isFormValid = validateForm(state.copy(workLocation = location))
      )
    }
  }

  private fun updateWage(wage: String) {
    val numericWage = wage.filter { it.isDigit() }
    _uiState.update { state ->
      state.copy(
        wage = numericWage,
        isFormValid = validateForm(state.copy(wage = numericWage))
      )
    }
  }

  private fun updateMaxApplicants(count: String) {
    val numericCount = count.filter { it.isDigit() }
    _uiState.update { state ->
      state.copy(
        maxApplicants = numericCount,
        isFormValid = validateForm(state.copy(maxApplicants = numericCount))
      )
    }
  }

  private fun updateDescription(description: String) {
    _uiState.update { it.copy(description = description) }
  }

  private fun toggleUrgent(isUrgent: Boolean) {
    _uiState.update { it.copy(isUrgent = isUrgent) }
  }

  private fun validateForm(state: ProjectCreateUiState): Boolean {
    return state.projectName.isNotBlank() &&
      state.startDate != null &&
      state.endDate != null &&
      state.workLocation.isNotBlank() &&
      state.wage.isNotBlank() &&
      state.maxApplicants.isNotBlank() &&
      (state.endDate?.isAfter(state.startDate) == true ||
        state.endDate?.isEqual(state.startDate) == true)
  }

  private fun createProject() {
    val state = _uiState.value
    if (!state.isFormValid) return

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, errorMessage = null) }

      val project = Project(
        id = "",
        title = state.projectName,
        company = "대림건설(주)", // TODO: SharedPreferences에서 가져오기
        location = state.workLocation,
        category = state.category,
        status = "RECRUITING",
        startDate = state.startDate?.atStartOfDay() ?: LocalDateTime.now(),
        endDate = state.endDate?.atStartOfDay() ?: LocalDateTime.now(),
        wage = state.wage.toIntOrNull() ?: 0,
        description = state.description,
        currentApplicants = 0,
        maxApplicants = state.maxApplicants.toIntOrNull() ?: 0,
        isUrgent = state.isUrgent
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
              errorMessage = exception.message ?: "프로젝트 등록에 실패했습니다"
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