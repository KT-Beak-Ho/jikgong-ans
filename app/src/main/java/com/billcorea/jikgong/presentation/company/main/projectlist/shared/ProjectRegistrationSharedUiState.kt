package com.billcorea.jikgong.presentation.company.main.projectlist.shared

import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationData

data class ProjectRegistrationSharedUiState(
    // 프로젝트 데이터
    val projectData: ProjectRegistrationData = ProjectRegistrationData(),

    // 페이지 상태
    val currentPage: Int = 1,
    val totalPages: Int = 3,

    // 로딩 상태
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSubmitting: Boolean = false,

    // 에러 상태
    val errorMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap(),

    // 다이얼로그 상태
    val showExitDialog: Boolean = false,
    val showSaveDialog: Boolean = false,

    // 네비게이션 상태
    val shouldNavigateBack: Boolean = false,
    val shouldNavigateToList: Boolean = false,

    // 임시저장 상태
    val hasDraft: Boolean = false,
    val lastSavedTime: String? = null,

    // 폼 상태
    val isFormDirty: Boolean = false, // 변경사항이 있는지
    val canNavigateNext: Boolean = false,
    val canNavigatePrevious: Boolean = false
) {
    // 현재 페이지 완료 상태
    val isCurrentPageComplete: Boolean
        get() = when (currentPage) {
            1 -> projectData.isPage1Complete
            2 -> projectData.isPage2Complete
            3 -> projectData.isPage3Complete
            else -> false
        }

    // 전체 진행률
    val overallProgress: Float
        get() = projectData.progressPercentage

    // 페이지별 진행률
    val currentPageProgress: Float
        get() = when (currentPage) {
            1 -> if (projectData.isPage1Complete) 1f else 0.3f
            2 -> if (projectData.isPage2Complete) 1f else 0.5f
            3 -> if (projectData.isPage3Complete) 1f else 0.7f
            else -> 0f
        }

    // 다음 버튼 텍스트
    val nextButtonText: String
        get() = when (currentPage) {
            1 -> "다음"
            2 -> "다음"
            3 -> "등록하기"
            else -> "다음"
        }

    // 현재 페이지 제목
    val currentPageTitle: String
        get() = when (currentPage) {
            1 -> "기본 정보"
            2 -> "팀 정보"
            3 -> "급여 정보"
            else -> ""
        }

    // 현재 페이지 부제목
    val currentPageSubtitle: String
        get() = when (currentPage) {
            1 -> "프로젝트의 기본 정보를 입력해주세요"
            2 -> "필요한 인력 정보를 입력해주세요"
            3 -> "급여 및 혜택 정보를 입력해주세요"
            else -> ""
        }

    // 필수 입력 필드 완료 개수
    val completedRequiredFields: Int
        get() {
            var count = 0
            // Page 1 필수 필드들
            if (projectData.requiredInfo.projectTitle.isNotEmpty()) count++
            if (projectData.requiredInfo.workType != null) count++
            if (projectData.requiredInfo.location.isNotEmpty()) count++
            if (projectData.requiredInfo.startDate != null) count++
            if (projectData.requiredInfo.endDate != null) count++
            if (projectData.requiredInfo.startTime != null) count++
            if (projectData.requiredInfo.endTime != null) count++

            // Page 2 필수 필드들
            if (projectData.teamInfo.totalWorkers > 0) count++
            if (projectData.teamInfo.requiredWorkers > 0) count++

            // Page 3 필수 필드들
            if (projectData.moneyInfo.paymentType != null) count++
            when (projectData.moneyInfo.paymentType) {
                com.billcorea.jikgong.presentation.company.main.projectlist.data.PaymentType.DAILY ->
                    if (projectData.moneyInfo.dailyWage > 0) count++
                com.billcorea.jikgong.presentation.company.main.projectlist.data.PaymentType.HOURLY ->
                    if (projectData.moneyInfo.hourlyWage > 0) count++
                com.billcorea.jikgong.presentation.company.main.projectlist.data.PaymentType.PROJECT ->
                    if (projectData.moneyInfo.projectWage > 0) count++
                null -> {}
            }

            return count
        }

    // 총 필수 입력 필드 개수
    val totalRequiredFields: Int = 11

    // 필수 필드 완료율
    val requiredFieldsCompletionRate: Float
        get() = completedRequiredFields.toFloat() / totalRequiredFields.toFloat()
}