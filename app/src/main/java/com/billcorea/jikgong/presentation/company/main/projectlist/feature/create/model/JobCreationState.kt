package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * 구인 공고 생성 화면의 전체 UI 상태
 */
data class JobCreationUiState(
    // 기본 정보
    val basicInfo: BasicJobInfo = BasicJobInfo(),
    
    // 작업 조건
    val workConditions: WorkConditions = WorkConditions(),
    
    // 작업 시간
    val workSchedule: WorkSchedule = WorkSchedule(),
    
    // 연락처 정보
    val contactInfo: ContactInfo = ContactInfo(),
    
    // 추가 정보
    val additionalInfo: AdditionalInfo = AdditionalInfo(),
    
    // UI 상태
    val dialogState: DialogState = DialogState(),
    
    // 로딩 및 에러 상태
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFormValid: Boolean = false
)

/**
 * 기본 구인 정보
 */
data class BasicJobInfo(
    val projectTitle: String = "",
    val selectedJobTypes: Set<String> = emptySet(),
    val recruitmentCount: String = "",
    val dailyWage: String = "",
    val workLocation: String = "",
    val workLocationDetail: String = ""
)

/**
 * 작업 조건 정보
 */
data class WorkConditions(
    val workEnvironment: String = "",
    val pickupProvided: Boolean = false,
    val pickupLocation: String = "",
    val pickupTime: String = "",
    val mealProvided: Boolean = false,
    val mealDescription: String = "",
    val parkingOption: ParkingOption = ParkingOption.NONE,
    val parkingDescription: String = ""
)

/**
 * 작업 일정 정보
 */
data class WorkSchedule(
    val selectedDates: Set<LocalDate> = emptySet(),
    val startTime: LocalTime = LocalTime.of(8, 0),
    val endTime: LocalTime = LocalTime.of(18, 0),
    val breakTime: String = "1시간",
    val workDays: String = ""
)

/**
 * 연락처 정보
 */
data class ContactInfo(
    val managerName: String = "",
    val contactNumber: String = "",
    val emergencyContact: String = ""
)

/**
 * 추가 정보
 */
data class AdditionalInfo(
    val requirements: String = "",
    val isUrgent: Boolean = false,
    val urgentReason: String = "",
    val additionalNotes: String = "",
    val photoUrls: List<String> = emptyList()
)

/**
 * 다이얼로그 상태 관리
 */
data class DialogState(
    val showValidationDialog: Boolean = false,
    val showUrgentDialog: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showLocationDialog: Boolean = false,
    val showPhotoDialog: Boolean = false,
    val validationErrors: List<String> = emptyList()
)

/**
 * 주차 옵션
 */
enum class ParkingOption(val displayName: String) {
    NONE("주차 불가"),
    FREE("무료 주차"),
    PAID("유료 주차"),
    LIMITED("제한적 주차")
}

/**
 * 직종 카테고리
 */
data class JobCategory(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)

/**
 * 미리 정의된 직종 목록
 */
val predefinedJobCategories = listOf(
    "철근공", "목공", "전기공", "미장공", "타일공",
    "도배공", "조적공", "방수공", "비계공", "용접공",
    "배관공", "석공", "도장공", "샷시공", "잡부"
)