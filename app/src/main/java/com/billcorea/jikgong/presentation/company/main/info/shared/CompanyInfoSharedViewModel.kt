// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/shared/CompanyInfoSharedViewModel.kt
package com.billcorea.jikgong.presentation.company.main.info.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.info.data.CompanyInfoMenuItems
import com.billcorea.jikgong.presentation.company.main.info.data.CompanyInfoSampleData
import com.billcorea.jikgong.presentation.company.main.info.data.InfoMenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyInfoSharedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyInfoSharedUiState())
    val uiState: StateFlow<CompanyInfoSharedUiState> = _uiState.asStateFlow()

    /**
     * 네비게이션 이벤트
     */
    private val _shouldNavigateToLogin = MutableStateFlow(false)
    val shouldNavigateToLogin: StateFlow<Boolean> = _shouldNavigateToLogin.asStateFlow()

    private val _selectedMenuId = MutableStateFlow<String?>(null)
    val selectedMenuId: StateFlow<String?> = _selectedMenuId.asStateFlow()

    /**
     * 네비게이션 이벤트 클리어
     */
    fun clearNavigationEvents() {
        _shouldNavigateToLogin.value = false
        _selectedMenuId.value = null
    }

    /**
     * 회사 정보 로드
     */
    private fun loadCompanyInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 실제로는 API 호출 또는 로컬 데이터베이스에서 데이터를 가져옴
                // 여기서는 샘플 데이터 사용
                val companyInfo = CompanyInfoSampleData.getSampleCompanyInfo()
                val menuItems = CompanyInfoMenuItems.getDefaultMenuItems().map { menuItem ->
                    menuItem.copy(
                        onClick = { handleMenuItemClick(menuItem.id) }
                    )
                }

                _uiState.value = _uiState.value.copy(
                    companyInfo = companyInfo,
                    menuItems = menuItems,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "회사 정보를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 메뉴 아이템 클릭 처리
     */
    private fun handleMenuItemClick(menuId: String) {
        _selectedMenuId.value = menuId

        // 각 메뉴별 처리 로직
        when (menuId) {
            "saved_workers" -> {
                // 저장한 인부 화면으로 이동
            }
            "notifications" -> {
                // 알림 설정 화면으로 이동
            }
            "events" -> {
                // 이벤트 화면으로 이동
            }
            "announcements" -> {
                // 공지사항 화면으로 이동
            }
            "customer_service" -> {
                // 고객센터 화면으로 이동
            }
            "policies" -> {
                // 약관 및 정책 화면으로 이동
            }
        }
    }

    /**
     * 로그아웃 처리
     */
    private fun handleLogout() {
        viewModelScope.launch {
            try {
                // 실제로는 로그아웃 API 호출
                // 로컬 저장소 정리 등

                _shouldNavigateToLogin.value = true
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "로그아웃에 실패했습니다: ${e.message}"
                )
            }
        }
    }

    /**
     * 이벤트 처리
     */
    fun onEvent(event: CompanyInfoSharedEvent) {
        when (event) {
            is CompanyInfoSharedEvent.LoadCompanyInfo -> {
                loadCompanyInfo()
            }

            is CompanyInfoSharedEvent.OnMenuItemClick -> {
                handleMenuItemClick(event.menuId)
            }

            is CompanyInfoSharedEvent.Logout -> {
                handleLogout()
            }

            is CompanyInfoSharedEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }

            is CompanyInfoSharedEvent.UpdateLoading -> {
                _uiState.value = _uiState.value.copy(isLoading = event.isLoading)
            }
        }
    }

    init {
        // ViewModel 초기화 시 회사 정보 로드
        loadCompanyInfo()
    }
}