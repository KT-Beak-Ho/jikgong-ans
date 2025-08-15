package com.billcorea.jikgong.presentation.company.main.info.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.common.CompanySharedViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

  fun formatCurrency(amount: Long): String {
    return "₩${String.format("%,d", amount)}"
  }
}