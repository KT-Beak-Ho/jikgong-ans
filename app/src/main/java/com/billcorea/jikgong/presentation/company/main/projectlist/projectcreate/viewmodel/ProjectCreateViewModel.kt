package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectCategory
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ProjectCreateEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ProjectCreateUiState
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ValidationErrors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ProjectCreateViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProjectCreateUiState())
    val uiState: StateFlow<ProjectCreateUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: ProjectCreateEvent) {
        when (event) {
            is ProjectCreateEvent.UpdateTitle -> updateTitle(event.title)
            is ProjectCreateEvent.UpdateDescription -> updateDescription(event.description)
            is ProjectCreateEvent.UpdateLocation -> updateLocation(event.location)
            is ProjectCreateEvent.UpdateCategory -> updateCategory(event.category)
            is ProjectCreateEvent.UpdateWage -> updateWage(event.wage)
            is ProjectCreateEvent.UpdateStartDate -> updateStartDate(event.date)
            is ProjectCreateEvent.UpdateEndDate -> updateEndDate(event.date)
            is ProjectCreateEvent.UpdateMaxApplicants -> updateMaxApplicants(event.maxApplicants)
            is ProjectCreateEvent.UpdateIsUrgent -> updateIsUrgent(event.isUrgent)
            ProjectCreateEvent.ShowStartDatePicker -> showStartDatePicker()
            ProjectCreateEvent.ShowEndDatePicker -> showEndDatePicker()
            ProjectCreateEvent.HideDatePicker -> hideDatePicker()
            ProjectCreateEvent.ShowCategorySelector -> showCategorySelector()
            ProjectCreateEvent.HideCategorySelector -> hideCategorySelector()
            ProjectCreateEvent.CreateProject -> createProject()
            ProjectCreateEvent.ClearError -> clearError()
            ProjectCreateEvent.ResetForm -> resetForm()
        }
    }
    
    private fun updateTitle(title: String) {
        _uiState.update { 
            it.copy(
                title = title,
                validationErrors = it.validationErrors.copy(title = null)
            )
        }
    }
    
    private fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }
    
    private fun updateLocation(location: String) {
        _uiState.update { 
            it.copy(
                location = location,
                validationErrors = it.validationErrors.copy(location = null)
            )
        }
    }
    
    private fun updateCategory(category: ProjectCategory) {
        _uiState.update { it.copy(category = category) }
    }
    
    private fun updateWage(wage: String) {
        // 숫자만 입력 허용
        val filteredWage = wage.filter { it.isDigit() }
        _uiState.update { 
            it.copy(
                wage = filteredWage,
                validationErrors = it.validationErrors.copy(wage = null)
            )
        }
    }
    
    private fun updateStartDate(date: String) {
        _uiState.update { 
            it.copy(
                startDate = date,
                validationErrors = it.validationErrors.copy(startDate = null, endDate = null)
            )
        }
        validateDateRange()
    }
    
    private fun updateEndDate(date: String) {
        _uiState.update { 
            it.copy(
                endDate = date,
                validationErrors = it.validationErrors.copy(endDate = null)
            )
        }
        validateDateRange()
    }
    
    private fun updateMaxApplicants(maxApplicants: String) {
        // 숫자만 입력 허용
        val filteredMaxApplicants = maxApplicants.filter { it.isDigit() }
        _uiState.update { 
            it.copy(
                maxApplicants = filteredMaxApplicants,
                validationErrors = it.validationErrors.copy(maxApplicants = null)
            )
        }
    }
    
    private fun updateIsUrgent(isUrgent: Boolean) {
        _uiState.update { it.copy(isUrgent = isUrgent) }
    }
    
    private fun showStartDatePicker() {
        _uiState.update { 
            it.copy(
                showDatePicker = true,
                datePickerType = com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.DatePickerType.START_DATE
            )
        }
    }
    
    private fun showEndDatePicker() {
        _uiState.update { 
            it.copy(
                showDatePicker = true,
                datePickerType = com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.DatePickerType.END_DATE
            )
        }
    }
    
    private fun hideDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }
    
    private fun showCategorySelector() {
        _uiState.update { it.copy(showCategorySelector = true) }
    }
    
    private fun hideCategorySelector() {
        _uiState.update { it.copy(showCategorySelector = false) }
    }
    
    private fun validateDateRange() {
        val currentState = _uiState.value
        if (currentState.startDate.isNotEmpty() && currentState.endDate.isNotEmpty()) {
            try {
                val startDate = LocalDate.parse(currentState.startDate)
                val endDate = LocalDate.parse(currentState.endDate)
                
                if (endDate.isBefore(startDate)) {
                    _uiState.update { 
                        it.copy(
                            validationErrors = it.validationErrors.copy(
                                endDate = "종료일은 시작일 이후여야 합니다"
                            )
                        )
                    }
                }
            } catch (e: DateTimeParseException) {
                // 날짜 파싱 실패 시 무시
            }
        }
    }
    
    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    private fun resetForm() {
        _uiState.value = ProjectCreateUiState()
    }
    
    private fun createProject() {
        val validationErrors = validateForm()
        if (validationErrors != null) {
            _uiState.update { it.copy(validationErrors = validationErrors) }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // TODO: Repository를 통해 프로젝트 생성 API 호출
                
                // 성공적으로 생성된 경우
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = null
                    )
                }
                
                // 폼 초기화
                resetForm()
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "프로젝트 생성에 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }
    
    private fun validateForm(): ValidationErrors? {
        val currentState = _uiState.value
        var hasErrors = false
        val errors = ValidationErrors()
        
        // 제목 검증
        val titleError = if (currentState.title.isBlank()) {
            hasErrors = true
            "프로젝트 제목을 입력해주세요"
        } else if (currentState.title.length < 3) {
            hasErrors = true
            "제목은 최소 3자 이상 입력해주세요"
        } else null
        
        // 위치 검증
        val locationError = if (currentState.location.isBlank()) {
            hasErrors = true
            "위치를 입력해주세요"
        } else null
        
        // 일당 검증
        val wageError = if (currentState.wage.isBlank()) {
            hasErrors = true
            "일당을 입력해주세요"
        } else {
            val wage = currentState.wage.toIntOrNull()
            when {
                wage == null -> {
                    hasErrors = true
                    "올바른 금액을 입력해주세요"
                }
                wage < 50000 -> {
                    hasErrors = true
                    "일당은 최소 50,000원 이상이어야 합니다"
                }
                wage > 1000000 -> {
                    hasErrors = true
                    "일당은 1,000,000원 이하여야 합니다"
                }
                else -> null
            }
        }
        
        // 시작일 검증
        val startDateError = if (currentState.startDate.isBlank()) {
            hasErrors = true
            "시작일을 선택해주세요"
        } else {
            try {
                val startDate = LocalDate.parse(currentState.startDate)
                if (startDate.isBefore(LocalDate.now())) {
                    hasErrors = true
                    "시작일은 오늘 이후여야 합니다"
                } else null
            } catch (e: DateTimeParseException) {
                hasErrors = true
                "올바른 날짜 형식이 아닙니다"
            }
        }
        
        // 종료일 검증
        val endDateError = if (currentState.endDate.isBlank()) {
            hasErrors = true
            "종료일을 선택해주세요"
        } else {
            try {
                val endDate = LocalDate.parse(currentState.endDate)
                val startDate = if (currentState.startDate.isNotBlank()) {
                    LocalDate.parse(currentState.startDate)
                } else null
                
                when {
                    endDate.isBefore(LocalDate.now()) -> {
                        hasErrors = true
                        "종료일은 오늘 이후여야 합니다"
                    }
                    startDate != null && endDate.isBefore(startDate) -> {
                        hasErrors = true
                        "종료일은 시작일 이후여야 합니다"
                    }
                    else -> null
                }
            } catch (e: DateTimeParseException) {
                hasErrors = true
                "올바른 날짜 형식이 아닙니다"
            }
        }
        
        // 최대 지원자 수 검증
        val maxApplicantsError = if (currentState.maxApplicants.isBlank()) {
            hasErrors = true
            "최대 지원자 수를 입력해주세요"
        } else {
            val maxApplicants = currentState.maxApplicants.toIntOrNull()
            when {
                maxApplicants == null -> {
                    hasErrors = true
                    "올바른 숫자를 입력해주세요"
                }
                maxApplicants < 1 -> {
                    hasErrors = true
                    "최소 1명 이상이어야 합니다"
                }
                maxApplicants > 100 -> {
                    hasErrors = true
                    "최대 100명까지 가능합니다"
                }
                else -> null
            }
        }
        
        return if (hasErrors) {
            ValidationErrors(
                title = titleError,
                location = locationError,
                wage = wageError,
                startDate = startDateError,
                endDate = endDateError,
                maxApplicants = maxApplicantsError
            )
        } else null
    }
    
    // Utility functions
    fun formatWageDisplay(wage: String): String {
        return if (wage.isNotEmpty()) {
            val number = wage.toLongOrNull() ?: 0
            String.format("%,d원", number)
        } else ""
    }
    
    fun isFormValid(): Boolean {
        val currentState = _uiState.value
        return currentState.title.isNotBlank() &&
                currentState.location.isNotBlank() &&
                currentState.wage.isNotBlank() &&
                currentState.startDate.isNotBlank() &&
                currentState.endDate.isNotBlank() &&
                currentState.maxApplicants.isNotBlank() &&
                currentState.validationErrors.let { errors ->
                    errors.title == null &&
                    errors.location == null &&
                    errors.wage == null &&
                    errors.startDate == null &&
                    errors.endDate == null &&
                    errors.maxApplicants == null
                }
    }
}