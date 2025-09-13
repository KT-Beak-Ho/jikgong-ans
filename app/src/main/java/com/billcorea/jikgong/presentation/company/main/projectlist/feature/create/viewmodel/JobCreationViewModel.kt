package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.util.Log

/**
 * 구인 공고 생성 화면의 ViewModel
 */
class JobCreationViewModel : ViewModel() {
    
    private val TAG = "JobCreationViewModel"
    
    // UI 상태
    private val _uiState = MutableStateFlow(JobCreationUiState())
    val uiState: StateFlow<JobCreationUiState> = _uiState.asStateFlow()
    
    // 이벤트 처리를 위한 Flow
    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()
    
    init {
        // 폼 유효성 검사 자동 실행
        validateForm()
    }
    
    // ===== 기본 정보 업데이트 =====
    
    fun updateProjectTitle(title: String) {
        _uiState.update { state ->
            state.copy(
                basicInfo = state.basicInfo.copy(projectTitle = title)
            )
        }
        validateForm()
    }
    
    fun toggleJobType(jobType: String) {
        _uiState.update { state ->
            val currentTypes = state.basicInfo.selectedJobTypes
            val newTypes = if (currentTypes.contains(jobType)) {
                currentTypes - jobType
            } else {
                currentTypes + jobType
            }
            state.copy(
                basicInfo = state.basicInfo.copy(selectedJobTypes = newTypes)
            )
        }
        validateForm()
    }
    
    fun updateRecruitmentCount(count: String) {
        // 숫자만 입력 가능
        if (count.isEmpty() || count.all { it.isDigit() }) {
            _uiState.update { state ->
                state.copy(
                    basicInfo = state.basicInfo.copy(recruitmentCount = count)
                )
            }
            validateForm()
        }
    }
    
    fun updateDailyWage(wage: String) {
        // 숫자만 입력 가능
        if (wage.isEmpty() || wage.all { it.isDigit() }) {
            _uiState.update { state ->
                state.copy(
                    basicInfo = state.basicInfo.copy(dailyWage = wage)
                )
            }
            validateForm()
        }
    }
    
    fun updateWorkLocation(location: String) {
        _uiState.update { state ->
            state.copy(
                basicInfo = state.basicInfo.copy(workLocation = location)
            )
        }
        validateForm()
    }
    
    fun updateWorkLocationDetail(detail: String) {
        _uiState.update { state ->
            state.copy(
                basicInfo = state.basicInfo.copy(workLocationDetail = detail)
            )
        }
    }
    
    // ===== 작업 조건 업데이트 =====
    
    fun updateWorkEnvironment(environment: String) {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(workEnvironment = environment)
            )
        }
    }
    
    fun togglePickupProvided() {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(
                    pickupProvided = !state.workConditions.pickupProvided
                )
            )
        }
    }
    
    fun updatePickupLocation(location: String) {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(pickupLocation = location)
            )
        }
    }
    
    fun toggleMealProvided() {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(
                    mealProvided = !state.workConditions.mealProvided
                )
            )
        }
    }
    
    fun updateMealDescription(description: String) {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(mealDescription = description)
            )
        }
    }
    
    fun updateParkingOption(option: ParkingOption) {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(parkingOption = option)
            )
        }
    }
    
    fun updateParkingDescription(description: String) {
        _uiState.update { state ->
            state.copy(
                workConditions = state.workConditions.copy(parkingDescription = description)
            )
        }
    }
    
    // ===== 작업 일정 업데이트 =====
    
    fun toggleDate(date: LocalDate) {
        _uiState.update { state ->
            val currentDates = state.workSchedule.selectedDates
            val newDates = if (currentDates.contains(date)) {
                currentDates - date
            } else {
                currentDates + date
            }
            state.copy(
                workSchedule = state.workSchedule.copy(selectedDates = newDates)
            )
        }
        validateForm()
    }
    
    fun updateStartTime(time: LocalTime) {
        _uiState.update { state ->
            state.copy(
                workSchedule = state.workSchedule.copy(startTime = time)
            )
        }
    }
    
    fun updateEndTime(time: LocalTime) {
        _uiState.update { state ->
            state.copy(
                workSchedule = state.workSchedule.copy(endTime = time)
            )
        }
    }
    
    fun updateBreakTime(breakTime: String) {
        _uiState.update { state ->
            state.copy(
                workSchedule = state.workSchedule.copy(breakTime = breakTime)
            )
        }
    }
    
    // ===== 연락처 정보 업데이트 =====
    
    fun updateManagerName(name: String) {
        _uiState.update { state ->
            state.copy(
                contactInfo = state.contactInfo.copy(managerName = name)
            )
        }
        validateForm()
    }
    
    fun updateContactNumber(number: String) {
        // 숫자와 하이픈만 허용
        if (number.all { it.isDigit() || it == '-' }) {
            _uiState.update { state ->
                state.copy(
                    contactInfo = state.contactInfo.copy(contactNumber = number)
                )
            }
            validateForm()
        }
    }
    
    // ===== 추가 정보 업데이트 =====
    
    fun updateRequirements(requirements: String) {
        _uiState.update { state ->
            state.copy(
                additionalInfo = state.additionalInfo.copy(requirements = requirements)
            )
        }
    }
    
    fun toggleUrgent() {
        _uiState.update { state ->
            state.copy(
                additionalInfo = state.additionalInfo.copy(
                    isUrgent = !state.additionalInfo.isUrgent
                )
            )
        }
    }
    
    fun updateUrgentReason(reason: String) {
        _uiState.update { state ->
            state.copy(
                additionalInfo = state.additionalInfo.copy(urgentReason = reason)
            )
        }
    }
    
    fun addPhotoUrl(url: String) {
        _uiState.update { state ->
            state.copy(
                additionalInfo = state.additionalInfo.copy(
                    photoUrls = state.additionalInfo.photoUrls + url
                )
            )
        }
    }
    
    fun removePhotoUrl(url: String) {
        _uiState.update { state ->
            state.copy(
                additionalInfo = state.additionalInfo.copy(
                    photoUrls = state.additionalInfo.photoUrls - url
                )
            )
        }
    }
    
    // ===== 다이얼로그 관리 =====
    
    fun showValidationDialog(show: Boolean) {
        _uiState.update { state ->
            state.copy(
                dialogState = state.dialogState.copy(showValidationDialog = show)
            )
        }
    }
    
    fun showUrgentDialog(show: Boolean) {
        _uiState.update { state ->
            state.copy(
                dialogState = state.dialogState.copy(showUrgentDialog = show)
            )
        }
    }
    
    fun showStartTimePicker(show: Boolean) {
        _uiState.update { state ->
            state.copy(
                dialogState = state.dialogState.copy(showStartTimePicker = show)
            )
        }
    }
    
    fun showEndTimePicker(show: Boolean) {
        _uiState.update { state ->
            state.copy(
                dialogState = state.dialogState.copy(showEndTimePicker = show)
            )
        }
    }
    
    fun showSuccessDialog(show: Boolean) {
        _uiState.update { state ->
            state.copy(
                dialogState = state.dialogState.copy(showSuccessDialog = show)
            )
        }
    }
    
    fun showLocationDialog(show: Boolean) {
        _uiState.update { state ->
            state.copy(
                dialogState = state.dialogState.copy(showLocationDialog = show)
            )
        }
    }
    
    // ===== 폼 검증 =====
    
    private fun validateForm() {
        val errors = mutableListOf<String>()
        val state = _uiState.value
        
        // 필수 필드 검증
        if (state.basicInfo.projectTitle.isBlank()) {
            errors.add("프로젝트 제목을 입력해주세요")
        }
        
        if (state.basicInfo.selectedJobTypes.isEmpty()) {
            errors.add("직종을 선택해주세요")
        }
        
        if (state.basicInfo.recruitmentCount.isBlank()) {
            errors.add("모집 인원을 입력해주세요")
        }
        
        if (state.basicInfo.dailyWage.isBlank()) {
            errors.add("일급을 입력해주세요")
        }
        
        if (state.workSchedule.selectedDates.isEmpty()) {
            errors.add("작업 날짜를 선택해주세요")
        }
        
        if (state.contactInfo.managerName.isBlank()) {
            errors.add("담당자 이름을 입력해주세요")
        }
        
        if (state.contactInfo.contactNumber.isBlank()) {
            errors.add("연락처를 입력해주세요")
        }
        
        _uiState.update { currentState ->
            currentState.copy(
                isFormValid = errors.isEmpty(),
                dialogState = currentState.dialogState.copy(
                    validationErrors = errors
                )
            )
        }
    }
    
    // ===== 구인 공고 생성 =====
    
    fun createJobPosting() {
        // 폼 검증
        validateForm()
        
        if (!_uiState.value.isFormValid) {
            showValidationDialog(true)
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                // API 호출 시뮬레이션 (실제 구현시 Repository 호출)
                delay(2000)
                
                // 성공 처리
                _uiState.update { it.copy(isLoading = false) }
                showSuccessDialog(true)
                
                // 네비게이션 이벤트 발생
                delay(1500)
                _navigationEvent.value = NavigationEvent.NavigateBack
                
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create job posting", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "구인 공고 등록에 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }
    
    // ===== 이전 공고 불러오기 =====
    
    fun loadPreviousJobPost(postId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // TODO: Repository에서 실제 데이터 로드
                // 임시 데이터 로드
                delay(500)
                
                _uiState.update { state ->
                    state.copy(
                        basicInfo = BasicJobInfo(
                            projectTitle = "아파트 신축공사 철근공 모집",
                            selectedJobTypes = setOf("철근공"),
                            recruitmentCount = "15",
                            dailyWage = "200000",
                            workLocation = "서울시 강남구 역삼동"
                        ),
                        workConditions = WorkConditions(
                            workEnvironment = "실내 작업 환경이며, 안전장비 착용 필수입니다.",
                            pickupProvided = true,
                            pickupLocation = "강남역 3번 출구",
                            mealProvided = false,
                            parkingOption = ParkingOption.FREE,
                            parkingDescription = "건물 지하 주차장 이용 가능"
                        ),
                        contactInfo = ContactInfo(
                            managerName = "김현수",
                            contactNumber = "010-1234-5678"
                        ),
                        additionalInfo = AdditionalInfo(
                            requirements = "신분증, 안전화, 작업복"
                        ),
                        isLoading = false
                    )
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load previous job post", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "이전 공고를 불러올 수 없습니다"
                    )
                }
            }
        }
    }
    
    // ===== 임시 저장 =====
    
    fun saveDraft() {
        viewModelScope.launch {
            try {
                // TODO: DataStore나 Room DB에 임시 저장
                Log.d(TAG, "Saving draft: ${_uiState.value}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save draft", e)
            }
        }
    }
    
    // ===== 초기화 =====
    
    fun resetForm() {
        _uiState.value = JobCreationUiState()
        validateForm()
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}

/**
 * 네비게이션 이벤트
 */
sealed class NavigationEvent {
    object NavigateBack : NavigationEvent()
    data class NavigateToPreview(val jobId: String) : NavigationEvent()
    object NavigateToSuccess : NavigationEvent()
}