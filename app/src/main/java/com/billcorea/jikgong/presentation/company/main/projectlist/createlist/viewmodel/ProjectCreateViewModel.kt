// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/createlist/viewmodel/ProjectCreateViewModel.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.createlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.createlist.state.ProjectCreateState
import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ProjectCreateViewModel(
  private val projectRepository: ProjectRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(ProjectCreateState())
  val uiState: StateFlow<ProjectCreateState> = _uiState.asStateFlow()

  fun updateProjectName(name: String) {
    _uiState.update {
      it.copy(
        projectName = name,
        projectNameError = null,
        errorMessage = null
      )
    }
  }

  fun updateStartDate(date: LocalDate) {
    _uiState.update { state ->
      state.copy(
        startDate = date,
        startDateError = null,
        errorMessage = null,
        // 시작일이 종료일보다 늦으면 종료일 초기화
        endDate = if (state.endDate != null && date >= state.endDate) null else state.endDate,
        endDateError = if (state.endDate != null && date >= state.endDate) "준공일을 다시 선택해주세요" else null
      )
    }
  }

  fun updateEndDate(date: LocalDate) {
    _uiState.update {
      it.copy(
        endDate = date,
        endDateError = null,
        errorMessage = null
      )
    }
  }

  fun updateLocation(location: String) {
    _uiState.update {
      it.copy(
        location = location,
        locationError = null,
        errorMessage = null
      )
    }
  }

  fun updateLocationDetail(detail: String) {
    _uiState.update {
      it.copy(
        locationDetail = detail
      )
    }
  }

  fun onCreateProject(onSuccess: () -> Unit) {
    viewModelScope.launch {
      // 유효성 검사
      if (!validateForm()) {
        return@launch
      }

      _uiState.update { it.copy(isLoading = true, errorMessage = null) }

      try {
        // 프로젝트 생성
        val newProject = Project(
          id = generateProjectId(), // 실제로는 서버에서 생성
          title = _uiState.value.projectName,
          company = "대한건설(주)", // 실제로는 로그인된 회사 정보
          location = _uiState.value.location,
          category = "CONSTRUCTION",
          status = "RECRUITING",
          startDate = LocalDateTime.from(_uiState.value.startDate!!.atStartOfDay()),
          endDate = LocalDateTime.from(_uiState.value.endDate!!.atStartOfDay()),
          wage = 200000, // 기본값, 실제로는 입력받아야 함
          description = "",
          requirements = emptyList(),
          benefits = emptyList(),
          currentApplicants = 0,
          maxApplicants = 10,
          isUrgent = false,
          isBookmarked = false
        )

        val success = projectRepository.createProject(newProject)

        if (success) {
          _uiState.update { it.copy(isLoading = false) }
          onSuccess()
        } else {
          _uiState.update {
            it.copy(
              isLoading = false,
              errorMessage = "프로젝트 등록에 실패했습니다."
            )
          }
        }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoading = false,
            errorMessage = e.message ?: "알 수 없는 오류가 발생했습니다."
          )
        }
      }
    }
  }

  private fun validateForm(): Boolean {
    var isValid = true

    _uiState.update { state ->
      state.copy(
        projectNameError = if (state.projectName.isBlank()) {
          isValid = false
          "프로젝트 이름을 입력해주세요"
        } else null,
        startDateError = if (state.startDate == null) {
          isValid = false
          "착공일을 선택해주세요"
        } else null,
        endDateError = when {
          state.endDate == null -> {
            isValid = false
            "준공일을 선택해주세요"
          }
          state.startDate != null && state.endDate <= state.startDate -> {
            isValid = false
            "준공일은 착공일보다 늦어야 합니다"
          }
          else -> null
        },
        locationError = if (state.location.isBlank()) {
          isValid = false
          "작업장소를 검색해주세요"
        } else null
      )
    }

    return isValid
  }

  private fun generateProjectId(): String {
    return "PRJ${System.currentTimeMillis()}"
  }
}