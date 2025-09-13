package com.billcorea.jikgong.presentation.company.main.scout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Worker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Proposal
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProposalStatus
import com.billcorea.jikgong.utils.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CompanyScoutViewModel : ViewModel(), KoinComponent {

  // MainViewModel 주입하여 위치 정보 연동
  private val mainViewModel: MainViewModel by inject()

  private val _uiState = MutableStateFlow(ScoutUiState())
  val uiState: StateFlow<ScoutUiState> = _uiState.asStateFlow()
  
  // 네비게이션 이벤트 추가
  private val _navigationEvent = MutableStateFlow<ScoutNavigationEvent?>(null)
  val navigationEvent: StateFlow<ScoutNavigationEvent?> = _navigationEvent.asStateFlow()

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    viewModelScope.launch {
      // 로딩 상태 제거 - 즉시 데이터 표시
      val workers = CompanyMockDataFactory.getScoutWorkers()
      val proposals = CompanyMockDataFactory.getScoutProposals()

      // MainViewModel에서 현재 위치 정보 가져오기
      val currentAddress = mainViewModel.respAddress.value
      val initialLocation = if (currentAddress?.isNotEmpty() == true) {
        currentAddress
      } else {
        "서울특별시 강남구" // 기본값
      }

      _uiState.value = _uiState.value.copy(
        workers = workers,
        proposals = proposals,
        currentLocation = initialLocation,
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
    // 제안 상세 보기 - 바텀시트로 표시
    _uiState.value = _uiState.value.copy(
      selectedProposal = proposal,
      showProposalDetailSheet = true
    )
  }
  
  fun dismissProposalDetail() {
    // 제안 상세 바텀시트 닫기
    _uiState.value = _uiState.value.copy(
      selectedProposal = null,
      showProposalDetailSheet = false
    )
  }

  fun updateLocation(location: String) {
    // CompanyScoutScreen과 LocationSettingPage 간 위치 동기화
    _uiState.value = _uiState.value.copy(currentLocation = location)
    
    // MainViewModel의 respAddress도 업데이트하여 전역적으로 동기화
    mainViewModel._respAddress.value = location
    
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
      try {
        // MainViewModel의 실제 주소 정보 사용
        val currentAddress = mainViewModel.respAddress.value
        if (currentAddress?.isNotEmpty() == true) {
          _uiState.value = _uiState.value.copy(currentLocation = currentAddress)
        } else {
          // 주소가 없으면 좌표로부터 주소 검색
          val lat = mainViewModel.lat.value ?: 37.5665
          val lon = mainViewModel.lon.value ?: 126.9780
          
          if (lat != 0.0 && lon != 0.0) {
            // 좌표를 주소로 변환
            mainViewModel.doFindAddress(lat, lon)
            
            // 주소 변환 결과 대기 (간단한 방법)
            delay(1000)
            
            val roadAddressList = mainViewModel.roadAddress1.value
            if (roadAddressList?.isNotEmpty() == true) {
              val address = roadAddressList[0].addressName
              _uiState.value = _uiState.value.copy(currentLocation = address)
            }
          }
        }
        
        refreshWorkers()
      } catch (e: Exception) {
        // 오류 발생 시 기본값 사용
        _uiState.value = _uiState.value.copy(currentLocation = "위치 확인 중...")
      }
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
  
  fun toggleAIFilterDialog() {
    _uiState.value = _uiState.value.copy(
      showAIFilterDialog = !_uiState.value.showAIFilterDialog
    )
  }
  
  fun applyAIFiltering() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAIFiltering = true)
      
      // 2초간 로딩 표시
      delay(2000)
      
      // 평점 순으로 정렬된 인력 목록 생성
      val sortedWorkers = _uiState.value.workers.sortedWith(
        compareByDescending<Worker> { it.rating }
          .thenByDescending { it.completedProjects }
          .thenByDescending { it.experience }
      )
      
      _uiState.value = _uiState.value.copy(
        workers = sortedWorkers,
        isAIFiltering = false,
        isAIFilterApplied = true
      )
    }
  }
  
  // 네비게이션 메서드들
  fun navigateToWorkerDetail(workerId: String) {
    _navigationEvent.value = ScoutNavigationEvent.NavigateToWorkerDetail(workerId)
  }
  
  fun makePhoneCall(phoneNumber: String) {
    _navigationEvent.value = ScoutNavigationEvent.MakePhoneCall(phoneNumber)
  }
  
  fun clearNavigationEvent() {
    _navigationEvent.value = null
  }
}

// 네비게이션 이벤트
sealed class ScoutNavigationEvent {
  data class NavigateToWorkerDetail(val workerId: String) : ScoutNavigationEvent()
  data class MakePhoneCall(val phoneNumber: String) : ScoutNavigationEvent()
  object NavigateBack : ScoutNavigationEvent()
}

data class ScoutUiState(
  val workers: List<Worker> = emptyList(),
  val proposals: List<Proposal> = emptyList(),
  val selectedWorker: Worker? = null,
  val selectedProposal: Proposal? = null,
  val showProposalDetailSheet: Boolean = false,
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val errorMessage: String? = null,
  val currentLocation: String = "서울특별시 강남구",
  val searchRadius: Int = 10,
  val currentFilters: WorkerFilters = WorkerFilters(),
  val showFilterDialog: Boolean = false,
  val isFilterActive: Boolean = false,
  val showAIFilterDialog: Boolean = false,
  val isAIFiltering: Boolean = false,
  val isAIFilterApplied: Boolean = false
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