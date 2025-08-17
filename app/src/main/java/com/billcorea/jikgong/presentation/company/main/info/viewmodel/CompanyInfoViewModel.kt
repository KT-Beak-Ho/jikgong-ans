package com.billcorea.jikgong.presentation.company.main.info.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.common.CompanySharedViewModel
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