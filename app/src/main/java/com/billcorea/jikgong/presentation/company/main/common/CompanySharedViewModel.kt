package com.billcorea.jikgong.presentation.company.main.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.network.models.CompanyData
import com.billcorea.jikgong.presentation.company.main.common.repository.CompanyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CompanySharedViewModel(
  private val repository: CompanyRepository
) : ViewModel() {

  val companyData: StateFlow<CompanyData> = repository.getCompanyData()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = CompanyData()
    )

  fun refreshData() {
    viewModelScope.launch {
      repository.refreshFromServer()
    }
  }

  fun clearNotifications() {
    viewModelScope.launch {
      repository.clearNotifications()
    }
  }
}