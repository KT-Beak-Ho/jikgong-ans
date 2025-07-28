package com.billcorea.jikgong.presentation.company.main.projectlist.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationData
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ProjectRegistrationSharedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectRegistrationSharedUiState())
    val uiState: StateFlow<ProjectRegistrationSharedUiState> = _uiState.asStateFlow()

    /**
     * 네비게이션 이벤트 클리어
     */
    fun clearNavigationEvents() {
        _uiState.value = _uiState.value.copy(
            shouldNavigateBack = false,
            shouldNavigateToList = false
        )
    }

    /**
     * 페이지 이동 가능 여부 확인
     */
    private fun canNavigateToNextPage(): Boolean {
        return _uiState.value.isCurrentPageComplete
    }

    /**
     * 다음 페이지로 이동
     */
    private fun navigateToNextPage() {
        val currentState = _uiState.value
        if (canNavigateToNextPage() && currentState.currentPage < currentState.totalPages) {
            _uiState.value = currentState.copy(
                currentPage = currentState.currentPage + 1,
                canNavigateNext = currentState.currentPage + 1 < currentState.totalPages,
                canNavigatePrevious = true
            )
        }
    }

    /**
     * 이전 페이지로 이동
     */
    private fun navigateToPreviousPage() {
        val currentState = _uiState.value
        if (currentState.currentPage > 1) {
            _uiState.value = currentState.copy(
                currentPage = currentState.currentPage - 1,
                canNavigateNext = true,
                canNavigatePrevious = currentState.currentPage - 1 > 1
            )
        }
    }

    /**
     * 특정 페이지로 이동
     */
    private fun navigateToPage(page: Int) {
        val currentState = _uiState.value
        if (page in 1..currentState.totalPages) {
            _uiState.value = currentState.copy(
                currentPage = page,
                canNavigateNext = page < currentState.totalPages,
                canNavigatePrevious = page > 1
            )
        }
    }

    /**
     * 임시저장
     */
    private fun saveDraft() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)

            try {
                // 실제로는 API 호출 또는 로컬 저장소에 저장
                val updatedData = _uiState.value.projectData.copy(
                    status = ProjectRegistrationStatus.DRAFT,
                    updatedAt = LocalDateTime.now(),
                    id = if (_uiState.value.projectData.id.isEmpty()) UUID.randomUUID().toString()
                    else _uiState.value.projectData.id
                )

                _uiState.value = _uiState.value.copy(
                    projectData = updatedData,
                    isSaving = false,
                    hasDraft = true,
                    lastSavedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                    isFormDirty = false,
                    showSaveDialog = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "임시저장에 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 프로젝트 제출
     */
    private fun submitProject() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)

            try {
                // 유효성 검증
                if (!_uiState.value.projectData.isAllComplete) {
                    throw Exception("모든 필수 정보를 입력해주세요")
                }

                // 실제로는 API 호출
                val submittedData = _uiState.value.projectData.copy(
                    status = ProjectRegistrationStatus.SUBMITTED,
                    updatedAt = LocalDateTime.now(),
                    id = if (_uiState.value.projectData.id.isEmpty()) UUID.randomUUID().toString()
                    else _uiState.value.projectData.id
                )

                _uiState.value = _uiState.value.copy(
                    projectData = submittedData,
                    isSubmitting = false,
                    shouldNavigateToList = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = "프로젝트 등록에 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 필드 유효성 검증
     */
    private fun validateField(field: String, value: Any?): String? {
        return when (field) {
            "projectTitle" -> {
                val title = value as? String
                when {
                    title.isNullOrBlank() -> "프로젝트 제목을 입력해주세요"
                    title.length < 5 -> "프로젝트 제목은 5자 이상 입력해주세요"
                    title.length > 50 -> "프로젝트 제목은 50자 이하로 입력해주세요"
                    else -> null
                }
            }
            "location" -> {
                val location = value as? String
                when {
                    location.isNullOrBlank() -> "근무지를 입력해주세요"
                    location.length < 2 -> "근무지는 2자 이상 입력해주세요"
                    else -> null
                }
            }
            "totalWorkers" -> {
                val count = value as? Int
                when {
                    count == null || count <= 0 -> "총 인원수를 입력해주세요"
                    count > 100 -> "총 인원수는 100명 이하로 입력해주세요"
                    else -> null
                }
            }
            "requiredWorkers" -> {
                val required = value as? Int
                val total = _uiState.value.projectData.teamInfo.totalWorkers
                when {
                    required == null || required <= 0 -> "모집 인원수를 입력해주세요"
                    required > total -> "모집 인원수는 총 인원수를 초과할 수 없습니다"
                    else -> null
                }
            }
            "dailyWage" -> {
                val wage = value as? Long
                when {
                    wage == null || wage <= 0 -> "일당을 입력해주세요"
                    wage < 50000 -> "일당은 50,000원 이상 입력해주세요"
                    wage > 1000000 -> "일당은 1,000,000원 이하로 입력해주세요"
                    else -> null
                }
            }
            else -> null
        }
    }

    /**
     * 폼 변경사항 체크
     */
    private fun checkFormDirty() {
        _uiState.value = _uiState.value.copy(isFormDirty = true)
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: ProjectRegistrationSharedEvent) {
        when (event) {
            // 네비게이션 이벤트
            is ProjectRegistrationSharedEvent.NextPage -> {
                if (_uiState.value.currentPage == _uiState.value.totalPages) {
                    submitProject()
                } else {
                    navigateToNextPage()
                }
            }

            is ProjectRegistrationSharedEvent.PreviousPage -> {
                navigateToPreviousPage()
            }

            is ProjectRegistrationSharedEvent.GoToPage -> {
                navigateToPage(event.page)
            }

            is ProjectRegistrationSharedEvent.BackPressed -> {
                if (_uiState.value.isFormDirty) {
                    _uiState.value = _uiState.value.copy(showExitDialog = true)
                } else {
                    _uiState.value = _uiState.value.copy(shouldNavigateBack = true)
                }
            }

            // 저장 관련 이벤트
            is ProjectRegistrationSharedEvent.SaveDraft -> {
                saveDraft()
            }

            is ProjectRegistrationSharedEvent.SubmitProject -> {
                submitProject()
            }

            // 필수 정보 업데이트 이벤트
            is ProjectRegistrationSharedEvent.UpdateProjectTitle -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(projectTitle = event.title)
                val validationError = validateField("projectTitle", event.title)

                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo),
                    validationErrors = if (validationError != null) {
                        _uiState.value.validationErrors + ("projectTitle" to validationError)
                    } else {
                        _uiState.value.validationErrors - "projectTitle"
                    }
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateWorkType -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(workType = event.workType)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateLocation -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(location = event.location)
                val validationError = validateField("location", event.location)

                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo),
                    validationErrors = if (validationError != null) {
                        _uiState.value.validationErrors + ("location" to validationError)
                    } else {
                        _uiState.value.validationErrors - "location"
                    }
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateDetailAddress -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(detailAddress = event.address)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateStartDate -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(startDate = event.date)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateEndDate -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(endDate = event.date)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateStartTime -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(startTime = event.time)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateEndTime -> {
                val currentData = _uiState.value.projectData
                val updatedRequiredInfo = currentData.requiredInfo.copy(endTime = event.time)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(requiredInfo = updatedRequiredInfo)
                )
                checkFormDirty()
            }

            // 팀 정보 업데이트 이벤트
            is ProjectRegistrationSharedEvent.UpdateTotalWorkers -> {
                val currentData = _uiState.value.projectData
                val updatedTeamInfo = currentData.teamInfo.copy(totalWorkers = event.count)
                val validationError = validateField("totalWorkers", event.count)

                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(teamInfo = updatedTeamInfo),
                    validationErrors = if (validationError != null) {
                        _uiState.value.validationErrors + ("totalWorkers" to validationError)
                    } else {
                        _uiState.value.validationErrors - "totalWorkers"
                    }
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateRequiredWorkers -> {
                val currentData = _uiState.value.projectData
                val updatedTeamInfo = currentData.teamInfo.copy(requiredWorkers = event.count)
                val validationError = validateField("requiredWorkers", event.count)

                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(teamInfo = updatedTeamInfo),
                    validationErrors = if (validationError != null) {
                        _uiState.value.validationErrors + ("requiredWorkers" to validationError)
                    } else {
                        _uiState.value.validationErrors - "requiredWorkers"
                    }
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdatePreferredAge -> {
                val currentData = _uiState.value.projectData
                val updatedTeamInfo = currentData.teamInfo.copy(preferredAge = event.age)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(teamInfo = updatedTeamInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateExperienceRequired -> {
                val currentData = _uiState.value.projectData
                val updatedTeamInfo = currentData.teamInfo.copy(experienceRequired = event.required)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(teamInfo = updatedTeamInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.AddRequirement -> {
                if (event.requirement.isNotBlank()) {
                    val currentData = _uiState.value.projectData
                    val updatedRequirements = currentData.teamInfo.requirements + event.requirement
                    val updatedTeamInfo = currentData.teamInfo.copy(requirements = updatedRequirements)
                    _uiState.value = _uiState.value.copy(
                        projectData = currentData.copy(teamInfo = updatedTeamInfo)
                    )
                    checkFormDirty()
                }
            }

            is ProjectRegistrationSharedEvent.RemoveRequirement -> {
                val currentData = _uiState.value.projectData
                val updatedRequirements = currentData.teamInfo.requirements.toMutableList()
                if (event.index in updatedRequirements.indices) {
                    updatedRequirements.removeAt(event.index)
                    val updatedTeamInfo = currentData.teamInfo.copy(requirements = updatedRequirements)
                    _uiState.value = _uiState.value.copy(
                        projectData = currentData.copy(teamInfo = updatedTeamInfo)
                    )
                    checkFormDirty()
                }
            }

            is ProjectRegistrationSharedEvent.AddProvidedItem -> {
                if (event.item.isNotBlank()) {
                    val currentData = _uiState.value.projectData
                    val updatedItems = currentData.teamInfo.providedItems + event.item
                    val updatedTeamInfo = currentData.teamInfo.copy(providedItems = updatedItems)
                    _uiState.value = _uiState.value.copy(
                        projectData = currentData.copy(teamInfo = updatedTeamInfo)
                    )
                    checkFormDirty()
                }
            }

            is ProjectRegistrationSharedEvent.RemoveProvidedItem -> {
                val currentData = _uiState.value.projectData
                val updatedItems = currentData.teamInfo.providedItems.toMutableList()
                if (event.index in updatedItems.indices) {
                    updatedItems.removeAt(event.index)
                    val updatedTeamInfo = currentData.teamInfo.copy(providedItems = updatedItems)
                    _uiState.value = _uiState.value.copy(
                        projectData = currentData.copy(teamInfo = updatedTeamInfo)
                    )
                    checkFormDirty()
                }
            }

            // 금액 정보 업데이트 이벤트
            is ProjectRegistrationSharedEvent.UpdatePaymentType -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(paymentType = event.paymentType)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateDailyWage -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(dailyWage = event.wage)
                val validationError = validateField("dailyWage", event.wage)

                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo),
                    validationErrors = if (validationError != null) {
                        _uiState.value.validationErrors + ("dailyWage" to validationError)
                    } else {
                        _uiState.value.validationErrors - "dailyWage"
                    }
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateHourlyWage -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(hourlyWage = event.wage)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateProjectWage -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(projectWage = event.wage)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateTransportationFee -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(transportationFee = event.fee)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateMealProvided -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(mealProvided = event.provided)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateAccommodationProvided -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(accommodationProvided = event.provided)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.UpdateNotes -> {
                val currentData = _uiState.value.projectData
                val updatedMoneyInfo = currentData.moneyInfo.copy(notes = event.notes)
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(moneyInfo = updatedMoneyInfo)
                )
                checkFormDirty()
            }

            // 기타 이벤트
            is ProjectRegistrationSharedEvent.UpdateUrgent -> {
                val currentData = _uiState.value.projectData
                _uiState.value = _uiState.value.copy(
                    projectData = currentData.copy(isUrgent = event.urgent)
                )
                checkFormDirty()
            }

            is ProjectRegistrationSharedEvent.ClearForm -> {
                _uiState.value = _uiState.value.copy(
                    projectData = ProjectRegistrationData(),
                    currentPage = 1,
                    isFormDirty = false,
                    validationErrors = emptyMap()
                )
            }

            is ProjectRegistrationSharedEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }

            is ProjectRegistrationSharedEvent.ShowError -> {
                _uiState.value = _uiState.value.copy(errorMessage = event.message)
            }

            // 다이얼로그 이벤트
            is ProjectRegistrationSharedEvent.ShowExitDialog -> {
                _uiState.value = _uiState.value.copy(showExitDialog = true)
            }

            is ProjectRegistrationSharedEvent.DismissExitDialog -> {
                _uiState.value = _uiState.value.copy(showExitDialog = false)
            }

            is ProjectRegistrationSharedEvent.ConfirmExit -> {
                _uiState.value = _uiState.value.copy(
                    showExitDialog = false,
                    shouldNavigateBack = true
                )
            }

            is ProjectRegistrationSharedEvent.ShowSaveDialog -> {
                _uiState.value = _uiState.value.copy(showSaveDialog = true)
            }

            is ProjectRegistrationSharedEvent.DismissSaveDialog -> {
                _uiState.value = _uiState.value.copy(showSaveDialog = false)
            }

            else -> {
                // 처리되지 않은 이벤트
            }
        }
    }
}