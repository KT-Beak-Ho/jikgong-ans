package com.billcorea.jikgong.presentation.company.main.info.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CompanyInfoUiState(
  val companyName: String = "(주)직직직",
  val ceoName: String = "김대표",
  val businessNumber: String = "123-45-67890",
  val phoneNumber: String = "055-1234-5678",
  val email: String = "info@jikjikjik.com",
  val address: String = "경상남도 밀양시 삼문동 123-45",
  val establishDate: String = "2024.01.15",
  val employeeCount: Int = 45,
  val description: String = "건설업 디지털 전환을 선도하는 AI 기반 인력 매칭 플랫폼",
  val rating: Float = 4.8f,
  val reviewCount: Int = 234,
  val totalProjects: Int = 127,
  val activeProjects: Int = 15,
  val completedProjects: Int = 112,
  val totalWorkers: Int = 2450,
  val monthlyRevenue: String = "3.5억원"
)

class CompanyInfoViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyInfoUiState())
  val uiState: StateFlow<CompanyInfoUiState> = _uiState.asStateFlow()

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  init {
    loadCompanyInfo()
  }

  private fun loadCompanyInfo() {
    viewModelScope.launch {
      _isLoading.value = true
      // API 호출 시뮬레이션
      kotlinx.coroutines.delay(500) // 로딩 시뮬레이션
      _isLoading.value = false
    }
  }

  fun navigateToEdit() {
    // 편집 화면으로 이동
  }

  fun callCompany() {
    // 전화 걸기
  }

  fun sendEmail() {
    // 이메일 보내기
  }

  fun openMap() {
    // 지도 열기
  }
}