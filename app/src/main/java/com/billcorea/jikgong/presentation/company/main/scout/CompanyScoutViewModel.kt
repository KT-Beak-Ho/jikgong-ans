// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/CompanyScoutViewModel.kt
package com.billcorea.jikgong.presentation.company.main.scout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 인력 스카웃 화면의 ViewModel
 */
class CompanyScoutViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyScoutUiState())
  val uiState: StateFlow<CompanyScoutUiState> = _uiState.asStateFlow()

  init {
    loadWorkers()
  }

  fun onEvent(event: CompanyScoutEvent) {
    when (event) {
      is CompanyScoutEvent.RefreshWorkers -> {
        refreshWorkers()
      }
      is CompanyScoutEvent.FilterByWorkType -> {
        filterWorkersByType(event.workType)
      }
      is CompanyScoutEvent.SearchWorkers -> {
        searchWorkers(event.query)
      }
      is CompanyScoutEvent.SaveWorker -> {
        saveWorker(event.workerId)
      }
      is CompanyScoutEvent.SendScoutMessage -> {
        sendScoutMessage(event.workerId, event.message)
      }
      is CompanyScoutEvent.ShowWorkerDetail -> {
        showWorkerDetail(event.workerId)
      }
      is CompanyScoutEvent.SortWorkers -> {
        sortWorkers(event.sortType)
      }
    }
  }

  private fun loadWorkers() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)

      try {
        // 실제로는 Repository에서 데이터를 가져옴
        val workers = getSampleWorkers()

        _uiState.value = _uiState.value.copy(
          isLoading = false,
          workers = workers,
          filteredWorkers = workers
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(
          isLoading = false,
          error = e.message
        )
      }
    }
  }

  private fun refreshWorkers() {
    loadWorkers()
  }

  private fun filterWorkersByType(workType: WorkType?) {
    val filteredWorkers = if (workType == null) {
      _uiState.value.workers
    } else {
      _uiState.value.workers.filter { worker ->
        worker.workTypes.contains(workType)
      }
    }

    _uiState.value = _uiState.value.copy(
      filteredWorkers = filteredWorkers,
      selectedWorkType = workType
    )
  }

  private fun searchWorkers(query: String) {
    val filteredWorkers = if (query.isEmpty()) {
      _uiState.value.workers
    } else {
      _uiState.value.workers.filter { worker ->
        worker.name.contains(query, ignoreCase = true) ||
          worker.location.contains(query, ignoreCase = true) ||
          worker.workTypes.any { it.displayName.contains(query, ignoreCase = true) }
      }
    }

    _uiState.value = _uiState.value.copy(
      filteredWorkers = filteredWorkers,
      searchQuery = query
    )
  }

  private fun saveWorker(workerId: String) {
    viewModelScope.launch {
      try {
        // 실제로는 Repository를 통해 저장
        val updatedWorkers = _uiState.value.workers.map { worker ->
          if (worker.id == workerId) {
            worker.copy(isSaved = !worker.isSaved)
          } else {
            worker
          }
        }

        _uiState.value = _uiState.value.copy(
          workers = updatedWorkers,
          filteredWorkers = updatedWorkers.filter { worker ->
            _uiState.value.selectedWorkType?.let { type ->
              worker.workTypes.contains(type)
            } ?: true
          }
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(error = e.message)
      }
    }
  }

  private fun sendScoutMessage(workerId: String, message: String) {
    viewModelScope.launch {
      try {
        // 실제로는 Repository를 통해 메시지 전송
        _uiState.value = _uiState.value.copy(
          scoutMessageSent = true,
          selectedWorkerId = null
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(error = e.message)
      }
    }
  }

  private fun showWorkerDetail(workerId: String) {
    _uiState.value = _uiState.value.copy(selectedWorkerId = workerId)
  }

  private fun sortWorkers(sortType: WorkerSortType) {
    val sortedWorkers = when (sortType) {
      WorkerSortType.RATING_HIGH -> _uiState.value.filteredWorkers.sortedByDescending { it.rating }
      WorkerSortType.RATING_LOW -> _uiState.value.filteredWorkers.sortedBy { it.rating }
      WorkerSortType.EXPERIENCE_HIGH -> _uiState.value.filteredWorkers.sortedByDescending { it.experienceYears }
      WorkerSortType.EXPERIENCE_LOW -> _uiState.value.filteredWorkers.sortedBy { it.experienceYears }
      WorkerSortType.DISTANCE -> _uiState.value.filteredWorkers.sortedBy { it.distance }
    }

    _uiState.value = _uiState.value.copy(
      filteredWorkers = sortedWorkers,
      selectedSortType = sortType
    )
  }

  private fun getSampleWorkers(): List<Worker> {
    return listOf(
      Worker(
        id = "1",
        name = "김철수",
        age = 35,
        location = "서울특별시 강남구",
        workTypes = listOf(WorkType.REINFORCEMENT, WorkType.FORMWORK),
        experienceYears = 8,
        rating = 4.8f,
        completedProjects = 142,
        profileImageUrl = null,
        introduction = "8년차 철근공입니다. 성실하고 안전하게 작업합니다. 대형 건설현장에서의 경험이 풍부하며, 안전 수칙을 철저히 준수합니다.",
        skills = listOf("철근배근", "용접", "안전관리", "도면해석"),
        distance = 2.5f,
        isAvailable = true,
        isSaved = false,
        lastActiveDate = "2일 전"
      ),
      Worker(
        id = "2",
        name = "박영희",
        age = 42,
        location = "서울특별시 서초구",
        workTypes = listOf(WorkType.TILE, WorkType.INTERIOR),
        experienceYears = 15,
        rating = 4.9f,
        completedProjects = 267,
        profileImageUrl = null,
        introduction = "15년 경력의 타일공입니다. 꼼꼼한 작업으로 고객 만족도가 높습니다. 특히 고급 타일 시공에 능숙합니다.",
        skills = listOf("타일시공", "방수작업", "마감재시공", "측량"),
        distance = 3.2f,
        isAvailable = true,
        isSaved = true,
        lastActiveDate = "1일 전"
      ),
      Worker(
        id = "3",
        name = "이민수",
        age = 28,
        location = "경기도 성남시",
        workTypes = listOf(WorkType.PAINTING, WorkType.WALLPAPER),
        experienceYears = 5,
        rating = 4.6f,
        completedProjects = 89,
        profileImageUrl = null,
        introduction = "성실한 도배공입니다. 깔끔한 마무리가 장점입니다. 인테리어 도배 전문으로 활동하고 있습니다.",
        skills = listOf("도배", "페인팅", "벽지시공", "퍼티작업"),
        distance = 5.8f,
        isAvailable = false,
        isSaved = false,
        lastActiveDate = "5일 전"
      ),
      Worker(
        id = "4",
        name = "정수민",
        age = 39,
        location = "서울특별시 영등포구",
        workTypes = listOf(WorkType.ELECTRICAL),
        experienceYears = 12,
        rating = 4.7f,
        completedProjects = 198,
        profileImageUrl = null,
        introduction = "전기공사 전문가입니다. 아파트, 상가, 공장 전기공사 경험이 풍부합니다.",
        skills = listOf("전기배선", "분전반설치", "조명설치", "통신배선"),
        distance = 4.1f,
        isAvailable = true,
        isSaved = false,
        lastActiveDate = "3시간 전"
      ),
      Worker(
        id = "5",
        name = "강지영",
        age = 33,
        location = "서울특별시 송파구",
        workTypes = listOf(WorkType.PLUMBING),
        experienceYears = 9,
        rating = 4.5f,
        completedProjects = 156,
        profileImageUrl = null,
        introduction = "배관공사 전문 기술자입니다. 급수, 배수, 난방배관 모두 가능합니다.",
        skills = listOf("급수배관", "배수배관", "난방배관", "배관용접"),
        distance = 3.7f,
        isAvailable = true,
        isSaved = true,
        lastActiveDate = "1일 전"
      )
    )
  }
}

/**
 * 인력 스카웃 UI 상태
 */
data class CompanyScoutUiState(
  val isLoading: Boolean = false,
  val workers: List<Worker> = emptyList(),
  val filteredWorkers: List<Worker> = emptyList(),
  val selectedWorkType: WorkType? = null,
  val selectedSortType: WorkerSortType = WorkerSortType.RATING_HIGH,
  val searchQuery: String = "",
  val selectedWorkerId: String? = null,
  val scoutMessageSent: Boolean = false,
  val error: String? = null
)

/**
 * 인력 스카웃 이벤트
 */
sealed class CompanyScoutEvent {
  object RefreshWorkers : CompanyScoutEvent()
  data class FilterByWorkType(val workType: WorkType?) : CompanyScoutEvent()
  data class SearchWorkers(val query: String) : CompanyScoutEvent()
  data class SaveWorker(val workerId: String) : CompanyScoutEvent()
  data class SendScoutMessage(val workerId: String, val message: String) : CompanyScoutEvent()
  data class ShowWorkerDetail(val workerId: String) : CompanyScoutEvent()
  data class SortWorkers(val sortType: WorkerSortType) : CompanyScoutEvent()
}