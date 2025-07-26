package com.billcorea.jikgong.presentation.company.main.scout.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyScoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyScoutUiState())
    val uiState: StateFlow<CompanyScoutUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    /**
     * 데이터 로드
     */
    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 실제로는 API 호출
                // val workers = workerRepository.getAvailableWorkers()
                // val proposals = proposalRepository.getProposals()

                _uiState.value = _uiState.value.copy(
                    workers = emptyList(), // 임시로 빈 리스트
                    proposals = emptyList(), // 임시로 빈 리스트
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "데이터를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: CompanyScoutEvent) {
        when (event) {
            is CompanyScoutEvent.LoadData -> {
                loadData()
            }

            is CompanyScoutEvent.RefreshData -> {
                loadData()
            }

            is CompanyScoutEvent.SendProposal -> {
                sendProposal(event.workerId, event.message)
            }

            is CompanyScoutEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }
        }
    }

    /**
     * 제안서 전송
     */
    private fun sendProposal(workerId: String, message: String) {
        viewModelScope.launch {
            try {
                // 실제로는 API 호출
                // proposalRepository.sendProposal(workerId, message)

                // 성공 시 데이터 새로고침
                loadData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "제안서 전송에 실패했습니다: ${e.message}"
                )
            }
        }
    }
}

// 이벤트 정의
sealed class CompanyScoutEvent {
    object LoadData : CompanyScoutEvent()
    object RefreshData : CompanyScoutEvent()
    data class SendProposal(val workerId: String, val message: String) : CompanyScoutEvent()
    object ClearError : CompanyScoutEvent()
}

// UI 상태 정의
data class CompanyScoutUiState(
    val workers: List<Any> = emptyList(), // 실제로는 WorkerData 타입
    val proposals: List<Any> = emptyList(), // 실제로는 ProposalData 타입
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val hasWorkers: Boolean
        get() = workers.isNotEmpty()

    val hasProposals: Boolean
        get() = proposals.isNotEmpty()
}