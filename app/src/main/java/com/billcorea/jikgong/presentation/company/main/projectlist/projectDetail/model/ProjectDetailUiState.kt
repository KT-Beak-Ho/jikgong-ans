package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.model

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectWorker

data class ProjectDetailUiState(
    val project: SimpleProject? = null,
    val workDays: List<WorkDay> = emptyList(),
    val projectWorkers: List<ProjectWorker> = emptyList(),
    val selectedTab: Int = 0,
    val selectedMonth: String? = null,
    val showBottomSheet: Boolean = false,
    val selectedWorkDay: WorkDay? = null,
    val showJobRegistrationBottomSheet: Boolean = false,
    val paymentStatus: Map<String, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ProjectDetailEvent {
    data class LoadProject(val projectId: String) : ProjectDetailEvent()
    data class SelectTab(val tabIndex: Int) : ProjectDetailEvent()
    data class SelectMonth(val month: String?) : ProjectDetailEvent()
    data class ShowWorkDayMenu(val workDay: WorkDay) : ProjectDetailEvent()
    object HideWorkDayMenu : ProjectDetailEvent()
    object ShowJobRegistrationOptions : ProjectDetailEvent()
    object HideJobRegistrationOptions : ProjectDetailEvent()
    data class UpdatePaymentStatus(val workDayId: String, val isCompleted: Boolean) : ProjectDetailEvent()
    data class NavigateToWorkerManagement(val workDayId: String) : ProjectDetailEvent()
    data class NavigateToPaymentSummary(val workDayId: String, val date: String) : ProjectDetailEvent()
    data class NavigateToJobRegistration(val startDate: String, val endDate: String) : ProjectDetailEvent()
    data class NavigateToPreviousJobPosts(val projectId: String) : ProjectDetailEvent()
    object NavigateToTempSave : ProjectDetailEvent()
}

enum class ProjectTab(val index: Int, val label: String) {
    IN_PROGRESS(0, "진행중"),
    UPCOMING(1, "예정"),
    PAYMENT(2, "임금입금")
}

data class ProjectDetailFilter(
    val tab: ProjectTab = ProjectTab.IN_PROGRESS,
    val selectedMonth: String? = null
)