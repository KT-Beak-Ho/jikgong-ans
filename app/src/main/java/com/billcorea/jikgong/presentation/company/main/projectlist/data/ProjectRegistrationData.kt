package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// 프로젝트 등록 상태
enum class ProjectRegistrationStatus {
    DRAFT,      // 임시저장
    SUBMITTED,  // 제출완료
    PUBLISHED,  // 게시중
    CLOSED      // 마감
}

// 작업 유형
enum class JobType(val displayName: String) {
    GENERAL_WORKER("보통인부"),
    SKILLED_WORKER("숙련인부"),
    REBAR_WORKER("철근공"),
    CONCRETE_WORKER("콘크리트공"),
    WELDING_WORKER("용접공"),
    ELECTRICAL_WORKER("전기공"),
    PLUMBING_WORKER("배관공"),
    TILE_WORKER("타일공"),
    PAINTING_WORKER("도장공"),
    CARPENTER("목수"),
    FOREMAN("작업반장")
}

// 급여 유형
enum class PaymentType(val displayName: String) {
    DAILY("일당"),
    HOURLY("시급"),
    PROJECT("프로젝트")
}

// 필수 정보 데이터
data class RequiredInfo(
    val projectTitle: String = "",
    val workType: JobType? = null,
    val location: String = "",
    val detailAddress: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
)

// 팀 정보 데이터
data class TeamInfo(
    val totalWorkers: Int = 0,
    val requiredWorkers: Int = 0,
    val preferredAge: String = "",
    val experienceRequired: Boolean = false,
    val requirements: List<String> = emptyList(),
    val providedItems: List<String> = emptyList()
)

// 금액 정보 데이터
data class MoneyInfo(
    val paymentType: PaymentType? = null,
    val dailyWage: Long = 0L,
    val hourlyWage: Long = 0L,
    val projectWage: Long = 0L,
    val transportationFee: Long = 0L,
    val mealProvided: Boolean = false,
    val accommodationProvided: Boolean = false,
    val notes: String = ""
)

// 프로젝트 등록 전체 데이터
data class ProjectRegistrationData(
    val id: String = "",
    val requiredInfo: RequiredInfo = RequiredInfo(),
    val teamInfo: TeamInfo = TeamInfo(),
    val moneyInfo: MoneyInfo = MoneyInfo(),
    val status: ProjectRegistrationStatus = ProjectRegistrationStatus.DRAFT,
    val currentPage: Int = 1,
    val isUrgent: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // 각 페이지별 완료 상태 확인
    val isPage1Complete: Boolean
        get() = requiredInfo.projectTitle.isNotEmpty() &&
                requiredInfo.workType != null &&
                requiredInfo.location.isNotEmpty() &&
                requiredInfo.startDate != null &&
                requiredInfo.endDate != null &&
                requiredInfo.startTime != null &&
                requiredInfo.endTime != null

    val isPage2Complete: Boolean
        get() = teamInfo.totalWorkers > 0 &&
                teamInfo.requiredWorkers > 0 &&
                teamInfo.requiredWorkers <= teamInfo.totalWorkers

    val isPage3Complete: Boolean
        get() = moneyInfo.paymentType != null &&
                when (moneyInfo.paymentType) {
                    PaymentType.DAILY -> moneyInfo.dailyWage > 0
                    PaymentType.HOURLY -> moneyInfo.hourlyWage > 0
                    PaymentType.PROJECT -> moneyInfo.projectWage > 0
                    null -> false
                }

    // 전체 완료 상태
    val isAllComplete: Boolean
        get() = isPage1Complete && isPage2Complete && isPage3Complete

    // 페이지별 진행률
    val progressPercentage: Float
        get() {
            var completed = 0
            if (isPage1Complete) completed++
            if (isPage2Complete) completed++
            if (isPage3Complete) completed++
            return completed / 3f
        }
}

// 유효성 검증 결과
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

// 폼 섹션 데이터
data class FormSectionData(
    val title: String,
    val isRequired: Boolean = false,
    val isCompleted: Boolean = false,
    val validationError: String? = null
)