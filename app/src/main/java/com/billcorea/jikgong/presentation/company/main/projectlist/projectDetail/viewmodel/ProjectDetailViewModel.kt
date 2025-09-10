package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.model.ProjectDetailEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.model.ProjectDetailUiState
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.model.ProjectTab
import com.billcorea.jikgong.api.repository.company.main.projectList.projectDetail.ProjectDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProjectDetailViewModel : ViewModel(), KoinComponent {
    
    private val repository: ProjectDetailRepository by inject()
    
    private val _uiState = MutableStateFlow(ProjectDetailUiState())
    val uiState: StateFlow<ProjectDetailUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: ProjectDetailEvent) {
        when (event) {
            is ProjectDetailEvent.LoadProject -> loadProjectData(event.projectId)
            is ProjectDetailEvent.SelectTab -> selectTab(event.tabIndex)
            is ProjectDetailEvent.SelectMonth -> selectMonth(event.month)
            is ProjectDetailEvent.ShowWorkDayMenu -> showWorkDayMenu(event.workDay)
            ProjectDetailEvent.HideWorkDayMenu -> hideWorkDayMenu()
            ProjectDetailEvent.ShowJobRegistrationOptions -> showJobRegistrationOptions()
            ProjectDetailEvent.HideJobRegistrationOptions -> hideJobRegistrationOptions()
            is ProjectDetailEvent.UpdatePaymentStatus -> updatePaymentStatus(event.workDayId, event.isCompleted)
            is ProjectDetailEvent.NavigateToWorkerManagement -> { /* Handle in Screen */ }
            is ProjectDetailEvent.NavigateToPaymentSummary -> { /* Handle in Screen */ }
            is ProjectDetailEvent.NavigateToJobRegistration -> { /* Handle in Screen */ }
            is ProjectDetailEvent.NavigateToPreviousJobPosts -> { /* Handle in Screen */ }
            ProjectDetailEvent.NavigateToTempSave -> { /* Handle in Screen */ }
        }
    }
    
    private fun loadProjectData(projectId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val project = repository.getProjectById(projectId)
                val workDays = repository.getWorkDaysForProject(projectId)
                val projectWorkers = repository.getProjectWorkers(projectId)
                val paymentStatus = repository.getPaymentStatus(projectId)
                
                _uiState.update {
                    it.copy(
                        project = project,
                        workDays = workDays,
                        projectWorkers = projectWorkers,
                        paymentStatus = paymentStatus,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "프로젝트 정보를 불러오는데 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }
    
    private fun selectTab(tabIndex: Int) {
        _uiState.update {
            it.copy(
                selectedTab = tabIndex,
                selectedMonth = null
            )
        }
    }
    
    private fun selectMonth(month: String?) {
        _uiState.update {
            it.copy(selectedMonth = month)
        }
    }
    
    private fun showWorkDayMenu(workDay: WorkDay) {
        _uiState.update {
            it.copy(
                selectedWorkDay = workDay,
                showBottomSheet = true
            )
        }
    }
    
    private fun hideWorkDayMenu() {
        _uiState.update {
            it.copy(
                selectedWorkDay = null,
                showBottomSheet = false
            )
        }
    }
    
    private fun showJobRegistrationOptions() {
        _uiState.update {
            it.copy(showJobRegistrationBottomSheet = true)
        }
    }
    
    private fun hideJobRegistrationOptions() {
        _uiState.update {
            it.copy(showJobRegistrationBottomSheet = false)
        }
    }
    
    private fun updatePaymentStatus(workDayId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            val success = repository.updatePaymentStatus(workDayId, isCompleted)
            if (success) {
                _uiState.update { state ->
                    state.copy(
                        paymentStatus = state.paymentStatus + (workDayId to isCompleted)
                    )
                }
            } else {
                _uiState.update {
                    it.copy(error = "임금 입금 상태 업데이트에 실패했습니다")
                }
            }
        }
    }
    
    fun getFilteredWorkDays(): List<WorkDay> {
        val workDays = _uiState.value.workDays
        val selectedTab = _uiState.value.selectedTab
        val selectedMonth = _uiState.value.selectedMonth
        
        return when (selectedTab) {
            0 -> workDays.filter { it.status == "IN_PROGRESS" }
            1 -> {
                val upcomingDays = workDays.filter { it.status == "UPCOMING" }
                if (selectedMonth != null) {
                    upcomingDays.filter {
                        it.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) == selectedMonth
                    }
                } else {
                    upcomingDays
                }
            }
            2 -> workDays.filter { it.status == "COMPLETED" }
            else -> workDays
        }
    }
    
    fun getAvailableMonths(): List<String> {
        return _uiState.value.workDays
            .filter { it.status == "UPCOMING" }
            .map { it.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) }
            .distinct()
            .sorted()
    }
    
    fun getApplicantsCountForDate(date: LocalDate): Int {
        val workDay = _uiState.value.workDays.find { it.date == date }
        return workDay?.applicants ?: 0
    }
    
    fun refreshData(projectId: String) {
        loadProjectData(projectId)
    }
    
    fun getWorkDayCountByStatus(status: String): Int {
        return _uiState.value.workDays.count { it.status == status }
    }
    
    fun deleteWorkDay(workDayId: String) {
        viewModelScope.launch {
            val success = repository.deleteWorkDay(workDayId)
            if (success) {
                _uiState.update { state ->
                    state.copy(
                        workDays = state.workDays.filter { it.id != workDayId },
                        showBottomSheet = false,
                        selectedWorkDay = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(error = "일자리 삭제에 실패했습니다")
                }
            }
        }
    }
}