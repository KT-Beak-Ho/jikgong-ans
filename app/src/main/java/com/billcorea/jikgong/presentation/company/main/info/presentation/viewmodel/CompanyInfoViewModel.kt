package com.billcorea.jikgong.presentation.company.main.info.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.common.CompanySharedViewModel
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.StatItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class CompanyInfoUiState(
  val isRefreshing: Boolean = false,
  val error: String? = null
)

class CompanyInfoViewModel(
  private val sharedViewModel: CompanySharedViewModel
) : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyInfoUiState())
  val uiState: StateFlow<CompanyInfoUiState> = _uiState.asStateFlow()

  val companyData = sharedViewModel.companyData
  
  // 총 절약 금액 데이터 가져오기
  val totalSavingsAmount: StateFlow<Long> = flow {
    emit(CompanyMockDataFactory.getProjectPaymentSummary().totalSavingsAmount)
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = 5420000L
  )
  
  // 프로젝트 통계 데이터를 실시간으로 계산
  val projectStats: StateFlow<List<StatItem>> = flow {
    val projects = CompanyMockDataFactory.getSimpleProjects()
    val recruitingCount = projects.count { it.status == "RECRUITING" }
    val inProgressCount = projects.count { it.status == "IN_PROGRESS" }
    val completedCount = projects.count { it.status == "COMPLETED" }
    
    emit(listOf(
      StatItem(
        icon = "👥",
        label = "매칭 인력",
        value = recruitingCount,
        unit = "명",
        trendText = "활성"
      ),
      StatItem(
        icon = "✅",
        label = "완료 프로젝트",
        value = completedCount,
        unit = "개",
        trendText = "100%"
      ),
      StatItem(
        icon = "🏗️",
        label = "시공 현장",
        value = inProgressCount,
        unit = "곳",
        isActive = true,
        trendText = "활성"
      )
    ))
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )
  
  // 스카우트 제안 건수 데이터
  val scoutProposalCount: StateFlow<Int> = flow {
    val proposals = CompanyMockDataFactory.getScoutProposals()
    emit(proposals.size)
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = 0
  )

  // NumberFormat 인스턴스를 미리 생성하여 성능 향상
  private val koreanNumberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

  fun refresh() {
    viewModelScope.launch {
      _uiState.update { it.copy(isRefreshing = true) }
      sharedViewModel.refreshData()
      _uiState.update { it.copy(isRefreshing = false) }
    }
  }

  fun clearNotifications() {
    sharedViewModel.clearNotifications()
  }

  /**
   * 금액을 한국 원화 형식으로 포맷팅
   * Locale을 명시적으로 지정하여 일관된 포맷팅 보장
   */
  fun formatCurrency(amount: Long): String {
    return "₩${String.format(Locale.KOREA, "%,d", amount)}"
  }

  /**
   * NumberFormat을 사용한 대안 메서드 (필요시 사용)
   * 더 나은 성능과 국제화 지원
   */
  fun formatCurrencyOptimized(amount: Long): String {
    return "₩${koreanNumberFormat.format(amount)}"
  }
}