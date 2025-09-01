package com.billcorea.jikgong.presentation.company.main.scout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Worker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Proposal
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProposalStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class CompanyScoutViewModel : ViewModel() {

  // CompanyMockDataFactory를 직접 사용

  private val _uiState = MutableStateFlow(ScoutUiState())
  val uiState: StateFlow<ScoutUiState> = _uiState.asStateFlow()

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    viewModelScope.launch {
      // 로딩 상태 제거 - 즉시 데이터 표시
      val workers = CompanyMockDataFactory.getScoutWorkers()
      val proposals = CompanyMockDataFactory.getScoutProposals()

      _uiState.value = _uiState.value.copy(
        workers = workers,
        proposals = proposals,
        isLoading = false
      )
    }
  }

  fun refreshWorkers() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true)
      delay(1000)

      val workers = CompanyMockDataFactory.getScoutWorkers()
      _uiState.value = _uiState.value.copy(
        workers = workers,
        isLoading = false
      )
    }
  }

  fun refreshProposals() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isRefreshing = true)
      delay(1000)

      val proposals = CompanyMockDataFactory.getScoutProposals()
      _uiState.value = _uiState.value.copy(
        proposals = proposals,
        isRefreshing = false
      )
    }
  }

  fun showWorkerDetail(worker: Worker) {
    _uiState.value = _uiState.value.copy(selectedWorker = worker)
  }

  fun dismissWorkerDetail() {
    _uiState.value = _uiState.value.copy(selectedWorker = null)
  }

  fun sendScoutProposal(worker: Worker) {
    _uiState.value = _uiState.value.copy(selectedWorker = worker)
  }

  fun confirmScoutProposal(worker: Worker, wage: String, message: String) {
    viewModelScope.launch {
      // 제안 전송
      val proposal = Proposal(
        id = System.currentTimeMillis().toString(),
        workerId = worker.id,
        workerName = worker.name,
        proposedWage = wage,
        message = message,
        status = ProposalStatus.PENDING,
        createdAt = LocalDateTime.now(),
        jobTypes = worker.jobTypes,
        distance = "${worker.distance}km",
        workerPhone = null,
        rejectReason = null
      )

      // 제안 전송 (실제로는 API 호출)
      // CompanyMockDataFactory는 샘플 데이터만 제공하므로 여기서는 주석 처리

      // UI 업데이트
      val updatedProposals = _uiState.value.proposals + proposal
      _uiState.value = _uiState.value.copy(
        proposals = updatedProposals,
        selectedWorker = null
      )

      // 성공 메시지 표시 (토스트 등)
    }
  }

  fun showProposalDetail(proposal: Proposal) {
    // 제안 상세 보기 구현
    // TODO: 상세 화면 네비게이션 또는 바텀시트 표시
  }

  fun updateLocation(location: String) {
    _uiState.value = _uiState.value.copy(currentLocation = location)
    // 위치 변경 시 주변 인력 재검색
    refreshWorkers()
  }

  fun updateSearchRadius(radius: Int) {
    _uiState.value = _uiState.value.copy(searchRadius = radius)
    // 반경 변경 시 주변 인력 재검색
    refreshWorkers()
  }

  fun getCurrentLocation() {
    viewModelScope.launch {
      // TODO: 실제 위치 권한 확인 및 현재 위치 가져오기
      // 일단 샘플 위치로 설정
      _uiState.value = _uiState.value.copy(currentLocation = "서울특별시 서초구")
      refreshWorkers()
    }
  }
  
  fun toggleFilterDialog() {
    _uiState.value = _uiState.value.copy(
      showFilterDialog = !_uiState.value.showFilterDialog
    )
  }
  
  fun applyFilters(filters: WorkerFilters) {
    val isActive = filters.jobTypes.isNotEmpty() ||
      filters.minExperience > 0 ||
      filters.maxDistance < Double.MAX_VALUE ||
      filters.minRating > 0f ||
      filters.availableOnly
    
    _uiState.value = _uiState.value.copy(
      currentFilters = filters,
      isFilterActive = isActive
    )
  }
}

data class ScoutUiState(
  val workers: List<Worker> = emptyList(),
  val proposals: List<Proposal> = emptyList(),
  val selectedWorker: Worker? = null,
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val errorMessage: String? = null,
  val currentLocation: String = "서울특별시 강남구",
  val searchRadius: Int = 10,
  val currentFilters: WorkerFilters = WorkerFilters(),
  val showFilterDialog: Boolean = false,
  val isFilterActive: Boolean = false
) {
  val filteredWorkers: List<Worker>
    get() = if (isFilterActive) {
      workers.filter { worker ->
        // 직종 필터
        val jobTypeMatch = currentFilters.jobTypes.isEmpty() || 
          worker.jobTypes.any { it in currentFilters.jobTypes }
        
        // 경력 필터
        val experienceMatch = worker.experience >= currentFilters.minExperience
        
        // 거리 필터
        val distanceMatch = worker.distance <= currentFilters.maxDistance
        
        // 평점 필터
        val ratingMatch = worker.rating >= currentFilters.minRating
        
        // 이용 가능여부 필터
        val availabilityMatch = !currentFilters.availableOnly || worker.isAvailable
        
        jobTypeMatch && experienceMatch && distanceMatch && ratingMatch && availabilityMatch
      }
    } else {
      workers
    }
}

data class WorkerFilters(
  val jobTypes: Set<String> = emptySet(),
  val minExperience: Int = 0,
  val maxDistance: Double = Double.MAX_VALUE,
  val minRating: Float = 0f,
  val availableOnly: Boolean = false
)