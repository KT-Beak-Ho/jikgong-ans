package com.billcorea.jikgong.presentation.company.main.projectlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.model.*
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectListViewModel : ViewModel() {

  private val repository: ProjectRepository = ProjectRepositoryImpl()

  private val _uiState = MutableStateFlow(ProjectListUiState())
  val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

  private val _selectedFilter = MutableStateFlow(ProjectFilter.ALL)
  val selectedFilter: StateFlow<ProjectFilter> = _selectedFilter.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  init {
    loadProjects()
  }

  fun loadProjects() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        val projects = repository.getProjects()
        _uiState.update { state ->
          state.copy(
            projects = projects,
            filteredProjects = filterProjects(projects, _selectedFilter.value, _searchQuery.value),
            isLoading = false,
            error = null
          )
        }
        updateStats(projects)
      } catch (e: Exception) {
        _uiState.update { state ->
          state.copy(
            isLoading = false,
            error = "프로젝트를 불러오는데 실패했습니다."
          )
        }
      }
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    applyFilters()
  }

  fun selectFilter(filter: ProjectFilter) {
    _selectedFilter.value = filter
    applyFilters()
  }

  fun toggleBookmark(projectId: String) {
    viewModelScope.launch {
      try {
        val updated = repository.toggleBookmark(projectId)
        if (updated) {
          _uiState.update { state ->
            state.copy(
              projects = state.projects.map { project ->
                if (project.id == projectId) {
                  project.copy(isBookmarked = !project.isBookmarked)
                } else project
              }
            )
          }
          applyFilters()
        }
      } catch (e: Exception) {
        // Handle error
      }
    }
  }

  fun refreshProjects() {
    loadProjects()
  }

  private fun applyFilters() {
    _uiState.update { state ->
      state.copy(
        filteredProjects = filterProjects(
          state.projects,
          _selectedFilter.value,
          _searchQuery.value
        )
      )
    }
  }

  private fun filterProjects(
    projects: List<Project>,
    filter: ProjectFilter,
    searchQuery: String
  ): List<Project> {
    var filtered = when (filter) {
      ProjectFilter.ALL -> projects
      ProjectFilter.RECRUITING -> projects.filter { it.status == "RECRUITING" }
      ProjectFilter.IN_PROGRESS -> projects.filter { it.status == "IN_PROGRESS" }
      ProjectFilter.COMPLETED -> projects.filter { it.status == "COMPLETED" }
      ProjectFilter.URGENT -> projects.filter { it.isUrgent }
      ProjectFilter.BOOKMARKED -> projects.filter { it.isBookmarked }
    }

    if (searchQuery.isNotBlank()) {
      filtered = filtered.filter { project ->
        project.title.contains(searchQuery, ignoreCase = true) ||
          project.company.contains(searchQuery, ignoreCase = true) ||
          project.location.contains(searchQuery, ignoreCase = true)
      }
    }

    return filtered
  }

  private fun updateStats(projects: List<Project>) {
    val stats = ProjectStats(
      totalProjects = projects.size,
      activeProjects = projects.count { it.status == "IN_PROGRESS" },
      recruitingProjects = projects.count { it.status == "RECRUITING" },
      completedProjects = projects.count { it.status == "COMPLETED" },
      totalApplicants = projects.sumOf { it.currentApplicants },
      averageWage = if (projects.isNotEmpty()) {
        projects.map { it.wage }.average().toInt()
      } else 0
    )
    _uiState.update { it.copy(stats = stats) }
  }
}