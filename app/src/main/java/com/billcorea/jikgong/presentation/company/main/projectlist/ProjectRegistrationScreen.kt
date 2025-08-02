// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/ProjectListViewModel.kt
package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * 프로젝트 목록 화면의 ViewModel
 */
class ProjectListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectListUiState())
    val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    fun onEvent(event: ProjectListEvent) {
        when (event) {
            is ProjectListEvent.RefreshProjects -> {
                refreshProjects()
            }
            is ProjectListEvent.FilterByStatus -> {
                filterProjectsByStatus(event.status)
            }
            is ProjectListEvent.SelectProject -> {
                selectProject(event.projectId)
            }
            is ProjectListEvent.CreateNewProject -> {
                createNewProject()
            }
            is ProjectListEvent.SearchProjects -> {
                searchProjects(event.query)
            }
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 실제로는 Repository에서 데이터를 가져옴
                val projects = getSampleProjects()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    projects = projects,
                    filteredProjects = projects
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun refreshProjects() {
        loadProjects()
    }

    private fun filterProjectsByStatus(status: ProjectStatus?) {
        val filteredProjects = if (status == null) {
            _uiState.value.projects
        } else {
            _uiState.value.projects.filter { it.status == status }
        }

        _uiState.value = _uiState.value.copy(
            filteredProjects = filteredProjects,
            selectedFilter = status
        )
    }

    private fun selectProject(projectId: String) {
        _uiState.value = _uiState.value.copy(selectedProjectId = projectId)
    }

    private fun createNewProject() {
        // 새 프로젝트 생성 로직
    }

    private fun searchProjects(query: String) {
        val filteredProjects = if (query.isEmpty()) {
            _uiState.value.projects
        } else {
            _uiState.value.projects.filter { project ->
                project.title.contains(query, ignoreCase = true) ||
                  project.location.contains(query, ignoreCase = true) ||
                  project.workType.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(
            filteredProjects = filteredProjects,
            searchQuery = query
        )
    }

    private fun getSampleProjects(): List<Project> {
        return listOf(
            Project(
                id = "1",
                title = "강남 아파트 신축공사 A동",
                location = "서울특별시 강남구",
                workType = "철근공",
                dailyWage = 250000,
                status = ProjectStatus.RECRUITING,
                recruitCount = 5,
                currentCount = 2,
                startDate = LocalDateTime.now().plusDays(3),
                endDate = LocalDateTime.now().plusDays(30),
                description = "아파트 신축공사 철근 작업",
                requiredSkills = listOf("철근공 경력 3년 이상", "안전교육 이수자"),
                contactPhone = "010-1234-5678"
            ),
            Project(
                id = "2",
                title = "서초 오피스텔 리모델링",
                location = "서울특별시 서초구",
                workType = "타일공",
                dailyWage = 200000,
                status = ProjectStatus.IN_PROGRESS,
                recruitCount = 3,
                currentCount = 3,
                startDate = LocalDateTime.now().minusDays(5),
                endDate = LocalDateTime.now().plusDays(20),
                description = "오피스텔 화장실 타일 작업",
                requiredSkills = listOf("타일공 경력 2년 이상"),
                contactPhone = "010-2345-6789"
            ),
            Project(
                id = "3",
                title = "성남 단독주택 인테리어",
                location = "경기도 성남시",
                workType = "도배공",
                dailyWage = 180000,
                status = ProjectStatus.RECRUITING,
                recruitCount = 2,
                currentCount = 0,
                startDate = LocalDateTime.now().plusDays(7),
                endDate = LocalDateTime.now().plusDays(14),
                description = "단독주택 도배 작업",
                requiredSkills = listOf("도배공 경력 1년 이상"),
                contactPhone = "010-3456-7890"
            ),
            Project(
                id = "4",
                title = "판교 상가 전기공사",
                location = "경기도 성남시 분당구",
                workType = "전기공",
                dailyWage = 280000,
                status = ProjectStatus.COMPLETED,
                recruitCount = 4,
                currentCount = 4,
                startDate = LocalDateTime.now().minusDays(20),
                endDate = LocalDateTime.now().minusDays(5),
                description = "상가 전기 배선 작업",
                requiredSkills = listOf("전기기능사", "고압전기 경험"),
                contactPhone = "010-4567-8901"
            ),
            Project(
                id = "5",
                title = "잠실 아파트 배관공사",
                location = "서울특별시 송파구",
                workType = "배관공",
                dailyWage = 220000,
                status = ProjectStatus.IN_PROGRESS,
                recruitCount = 3,
                currentCount = 2,
                startDate = LocalDateTime.now().minusDays(2),
                endDate = LocalDateTime.now().plusDays(18),
                description = "아파트 급수관 교체 작업",
                requiredSkills = listOf("배관공 경력 3년 이상"),
                contactPhone = "010-5678-9012"
            )
        )
    }
}

/**
 * 프로젝트 목록 UI 상태
 */
data class ProjectListUiState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
    val filteredProjects: List<Project> = emptyList(),
    val selectedProjectId: String? = null,
    val selectedFilter: ProjectStatus? = null,
    val searchQuery: String = "",
    val error: String? = null
)

/**
 * 프로젝트 목록 이벤트
 */
sealed class ProjectListEvent {
    object RefreshProjects : ProjectListEvent()
    data class FilterByStatus(val status: ProjectStatus?) : ProjectListEvent()
    data class SelectProject(val projectId: String) : ProjectListEvent()
    object CreateNewProject : ProjectListEvent()
    data class SearchProjects(val query: String) : ProjectListEvent()
}