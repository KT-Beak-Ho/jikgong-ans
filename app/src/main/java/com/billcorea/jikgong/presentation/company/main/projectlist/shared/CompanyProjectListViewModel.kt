package com.billcorea.jikgong.presentation.company.main.projectlist.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectData
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class CompanyProjectListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyProjectListUiState())
    val uiState: StateFlow<CompanyProjectListUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 샘플 데이터 생성
                val today = LocalDate.now()
                val projects = listOf(
                    ProjectData(
                        id = "project1",
                        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
                        description = "친환경 온도 측정 센터 건립을 위한 신축 공사입니다.",
                        location = "부산 사하구",
                        detailAddress = "부산광역시 사하구 낙동대로 123",
                        distance = 2.5,
                        jobTypes = listOf(),
                        totalWorkers = 15,
                        completedWorkers = 2,
                        dailyWage = 130000,
                        startDate = today.plusDays(3),
                        endDate = today.plusDays(25),
                        startTime = "08:00",
                        endTime = "17:00",
                        status = ProjectStatus.RECRUITING,
                        isUrgent = true,
                        requirements = listOf("안전화 필수", "작업복 착용"),
                        providedItems = listOf("중식 제공", "교통비 지급"),
                        notes = "신축 공사로 깔끔한 작업 환경입니다.",
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                )

                _uiState.value = _uiState.value.copy(
                    projects = projects,
                    filteredProjects = projects,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "프로젝트를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    fun onEvent(event: CompanyProjectListEvent) {
        when (event) {
            is CompanyProjectListEvent.FilterByStatus -> {
                val filtered = if (event.status == null) {
                    _uiState.value.projects
                } else {
                    _uiState.value.projects.filter { it.status == event.status }
                }
                _uiState.value = _uiState.value.copy(
                    selectedStatus = event.status,
                    filteredProjects = filtered
                )
            }
            is CompanyProjectListEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }
            else -> {
                // 다른 이벤트들은 추후 구현
            }
        }
    }
}

// 이벤트 정의
sealed class CompanyProjectListEvent {
    data class FilterByStatus(val status: ProjectStatus?) : CompanyProjectListEvent()
    object ClearError : CompanyProjectListEvent()
}

// UI 상태 정의
data class CompanyProjectListUiState(
    val projects: List<ProjectData> = emptyList(),
    val filteredProjects: List<ProjectData> = emptyList(),
    val selectedStatus: ProjectStatus? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)