package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectCategory

data class ProjectCreateUiState(
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val category: ProjectCategory = ProjectCategory.APARTMENT,
    val wage: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val maxApplicants: String = "",
    val isUrgent: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val validationErrors: ValidationErrors = ValidationErrors(),
    val showDatePicker: Boolean = false,
    val datePickerType: DatePickerType = DatePickerType.START_DATE,
    val showCategorySelector: Boolean = false
)

data class ValidationErrors(
    val title: String? = null,
    val location: String? = null,
    val wage: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val maxApplicants: String? = null
)

enum class DatePickerType {
    START_DATE,
    END_DATE
}

sealed class ProjectCreateEvent {
    data class UpdateTitle(val title: String) : ProjectCreateEvent()
    data class UpdateDescription(val description: String) : ProjectCreateEvent()
    data class UpdateLocation(val location: String) : ProjectCreateEvent()
    data class UpdateCategory(val category: ProjectCategory) : ProjectCreateEvent()
    data class UpdateWage(val wage: String) : ProjectCreateEvent()
    data class UpdateStartDate(val date: String) : ProjectCreateEvent()
    data class UpdateEndDate(val date: String) : ProjectCreateEvent()
    data class UpdateMaxApplicants(val maxApplicants: String) : ProjectCreateEvent()
    data class UpdateIsUrgent(val isUrgent: Boolean) : ProjectCreateEvent()
    object ShowStartDatePicker : ProjectCreateEvent()
    object ShowEndDatePicker : ProjectCreateEvent()
    object HideDatePicker : ProjectCreateEvent()
    object ShowCategorySelector : ProjectCreateEvent()
    object HideCategorySelector : ProjectCreateEvent()
    object CreateProject : ProjectCreateEvent()
    object ClearError : ProjectCreateEvent()
    object ResetForm : ProjectCreateEvent()
}