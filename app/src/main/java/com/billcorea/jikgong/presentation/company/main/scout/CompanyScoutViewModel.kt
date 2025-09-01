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
      _uiState.value = _uiState.value.copy(isLoading = true)

      // 위치 권한 확인 후 주변 노동자 로드
      delay(1000) // 네트워크 요청 시뮬레이션

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
      _uiState.value = _uiState.value.copy(isRefreshing = true)
      delay(1000)

      val workers = CompanyMockDataFactory.getScoutWorkers()
      _uiState.value = _uiState.value.copy(
        workers = workers,
        isRefreshing = false
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
}

data class ScoutUiState(
  val workers: List<Worker> = emptyList(),
  val proposals: List<Proposal> = emptyList(),
  val selectedWorker: Worker? = null,
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val errorMessage: String? = null,
  val currentLocation: String = "서울특별시 강남구",
  val searchRadius: Int = 10
)