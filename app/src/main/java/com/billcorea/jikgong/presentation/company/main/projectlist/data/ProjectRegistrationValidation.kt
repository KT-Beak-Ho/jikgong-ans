package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalTime

object ProjectRegistrationValidation {

    // 상수 정의
    object Constants {
        const val MIN_PROJECT_TITLE_LENGTH = 5
        const val MAX_PROJECT_TITLE_LENGTH = 50
        const val MIN_LOCATION_LENGTH = 2
        const val MAX_LOCATION_LENGTH = 30
        const val MIN_WORKERS = 1
        const val MAX_WORKERS = 100
        const val MIN_DAILY_WAGE = 50000L
        const val MAX_DAILY_WAGE = 1000000L
        const val MIN_HOURLY_WAGE = 10000L
        const val MAX_HOURLY_WAGE = 50000L
        const val MIN_PROJECT_WAGE = 100000L
        const val MAX_PROJECT_WAGE = 10000000L
        const val MAX_TRANSPORTATION_FEE = 100000L
        const val MAX_NOTES_LENGTH = 500
        const val MAX_REQUIREMENT_LENGTH = 50
        const val MAX_PROVIDED_ITEM_LENGTH = 50
        const val MAX_REQUIREMENTS_COUNT = 10
        const val MAX_PROVIDED_ITEMS_COUNT = 10
    }

    /**
     * 프로젝트 제목 유효성 검증
     */
    fun validateProjectTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult(
                isValid = false,
                errorMessage = "프로젝트 제목을 입력해주세요"
            )
            title.length < Constants.MIN_PROJECT_TITLE_LENGTH -> ValidationResult(
                isValid = false,
                errorMessage = "프로젝트 제목은 ${Constants.MIN_PROJECT_TITLE_LENGTH}자 이상 입력해주세요"
            )
            title.length > Constants.MAX_PROJECT_TITLE_LENGTH -> ValidationResult(
                isValid = false,
                errorMessage = "프로젝트 제목은 ${Constants.MAX_PROJECT_TITLE_LENGTH}자 이하로 입력해주세요"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 작업 유형 유효성 검증
     */
    fun validateWorkType(workType: JobType?): ValidationResult {
        return if (workType == null) {
            ValidationResult(
                isValid = false,
                errorMessage = "작업 유형을 선택해주세요"
            )
        } else {
            ValidationResult(isValid = true)
        }
    }

    /**
     * 근무지 유효성 검증
     */
    fun validateLocation(location: String): ValidationResult {
        return when {
            location.isBlank() -> ValidationResult(
                isValid = false,
                errorMessage = "근무지를 입력해주세요"
            )
            location.length < Constants.MIN_LOCATION_LENGTH -> ValidationResult(
                isValid = false,
                errorMessage = "근무지는 ${Constants.MIN_LOCATION_LENGTH}자 이상 입력해주세요"
            )
            location.length > Constants.MAX_LOCATION_LENGTH -> ValidationResult(
                isValid = false,
                errorMessage = "근무지는 ${Constants.MAX_LOCATION_LENGTH}자 이하로 입력해주세요"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 날짜 유효성 검증
     */
    fun validateDateRange(startDate: LocalDate?, endDate: LocalDate?): ValidationResult {
        return when {
            startDate == null -> ValidationResult(
                isValid = false,
                errorMessage = "시작일을 선택해주세요"
            )
            endDate == null -> ValidationResult(
                isValid = false,
                errorMessage = "종료일을 선택해주세요"
            )
            startDate.isBefore(LocalDate.now()) -> ValidationResult(
                isValid = false,
                errorMessage = "시작일은 오늘 이후로 선택해주세요"
            )
            endDate.isBefore(startDate) -> ValidationResult(
                isValid = false,
                errorMessage = "종료일은 시작일 이후로 선택해주세요"
            )
            startDate.plusYears(1).isBefore(endDate) -> ValidationResult(
                isValid = false,
                errorMessage = "프로젝트 기간은 1년을 초과할 수 없습니다"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 시간 유효성 검증
     */
    fun validateTimeRange(startTime: LocalTime?, endTime: LocalTime?): ValidationResult {
        return when {
            startTime == null -> ValidationResult(
                isValid = false,
                errorMessage = "시작 시간을 선택해주세요"
            )
            endTime == null -> ValidationResult(
                isValid = false,
                errorMessage = "종료 시간을 선택해주세요"
            )
            endTime.isBefore(startTime) -> ValidationResult(
                isValid = false,
                errorMessage = "종료 시간은 시작 시간 이후로 선택해주세요"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 인원수 유효성 검증
     */
    fun validateWorkerCount(totalWorkers: Int, requiredWorkers: Int): ValidationResult {
        return when {
            totalWorkers < Constants.MIN_WORKERS -> ValidationResult(
                isValid = false,
                errorMessage = "총 인원수는 ${Constants.MIN_WORKERS}명 이상이어야 합니다"
            )
            totalWorkers > Constants.MAX_WORKERS -> ValidationResult(
                isValid = false,
                errorMessage = "총 인원수는 ${Constants.MAX_WORKERS}명 이하여야 합니다"
            )
            requiredWorkers < Constants.MIN_WORKERS -> ValidationResult(
                isValid = false,
                errorMessage = "모집 인원수는 ${Constants.MIN_WORKERS}명 이상이어야 합니다"
            )
            requiredWorkers > totalWorkers -> ValidationResult(
                isValid = false,
                errorMessage = "모집 인원수는 총 인원수를 초과할 수 없습니다"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 급여 유효성 검증
     */
    fun validateWage(paymentType: PaymentType?, dailyWage: Long, hourlyWage: Long, projectWage: Long): ValidationResult {
        return when (paymentType) {
            null -> ValidationResult(
                isValid = false,
                errorMessage = "급여 유형을 선택해주세요"
            )
            PaymentType.DAILY -> {
                when {
                    dailyWage < Constants.MIN_DAILY_WAGE -> ValidationResult(
                        isValid = false,
                        errorMessage = "일당은 ${Constants.MIN_DAILY_WAGE}원 이상이어야 합니다"
                    )
                    dailyWage > Constants.MAX_DAILY_WAGE -> ValidationResult(
                        isValid = false,
                        errorMessage = "일당은 ${Constants.MAX_DAILY_WAGE}원 이하여야 합니다"
                    )
                    else -> ValidationResult(isValid = true)
                }
            }
            PaymentType.HOURLY -> {
                when {
                    hourlyWage < Constants.MIN_HOURLY_WAGE -> ValidationResult(
                        isValid = false,
                        errorMessage = "시급은 ${Constants.MIN_HOURLY_WAGE}원 이상이어야 합니다"
                    )
                    hourlyWage > Constants.MAX_HOURLY_WAGE -> ValidationResult(
                        isValid = false,
                        errorMessage = "시급은 ${Constants.MAX_HOURLY_WAGE}원 이하여야 합니다"
                    )
                    else -> ValidationResult(isValid = true)
                }
            }
            PaymentType.PROJECT -> {
                when {
                    projectWage < Constants.MIN_PROJECT_WAGE -> ValidationResult(
                        isValid = false,
                        errorMessage = "프로젝트 금액은 ${Constants.MIN_PROJECT_WAGE}원 이상이어야 합니다"
                    )
                    projectWage > Constants.MAX_PROJECT_WAGE -> ValidationResult(
                        isValid = false,
                        errorMessage = "프로젝트 금액은 ${Constants.MAX_PROJECT_WAGE}원 이하여야 합니다"
                    )
                    else -> ValidationResult(isValid = true)
                }
            }
        }
    }

    /**
     * 교통비 유효성 검증
     */
    fun validateTransportationFee(fee: Long): ValidationResult {
        return when {
            fee < 0 -> ValidationResult(
                isValid = false,
                errorMessage = "교통비는 0원 이상이어야 합니다"
            )
            fee > Constants.MAX_TRANSPORTATION_FEE -> ValidationResult(
                isValid = false,
                errorMessage = "교통비는 ${Constants.MAX_TRANSPORTATION_FEE}원 이하여야 합니다"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 특이사항 유효성 검증
     */
    fun validateNotes(notes: String): ValidationResult {
        return when {
            notes.length > Constants.MAX_NOTES_LENGTH -> ValidationResult(
                isValid = false,
                errorMessage = "특이사항은 ${Constants.MAX_NOTES_LENGTH}자 이하로 입력해주세요"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 요구사항 리스트 유효성 검증
     */
    fun validateRequirements(requirements: List<String>): ValidationResult {
        return when {
            requirements.size > Constants.MAX_REQUIREMENTS_COUNT -> ValidationResult(
                isValid = false,
                errorMessage = "요구사항은 최대 ${Constants.MAX_REQUIREMENTS_COUNT}개까지 등록할 수 있습니다"
            )
            requirements.any { it.length > Constants.MAX_REQUIREMENT_LENGTH } -> ValidationResult(
                isValid = false,
                errorMessage = "각 요구사항은 ${Constants.MAX_REQUIREMENT_LENGTH}자 이하여야 합니다"
            )
            requirements.any { it.isBlank() } -> ValidationResult(
                isValid = false,
                errorMessage = "빈 요구사항은 등록할 수 없습니다"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 제공사항 리스트 유효성 검증
     */
    fun validateProvidedItems(providedItems: List<String>): ValidationResult {
        return when {
            providedItems.size > Constants.MAX_PROVIDED_ITEMS_COUNT -> ValidationResult(
                isValid = false,
                errorMessage = "제공사항은 최대 ${Constants.MAX_PROVIDED_ITEMS_COUNT}개까지 등록할 수 있습니다"
            )
            providedItems.any { it.length > Constants.MAX_PROVIDED_ITEM_LENGTH } -> ValidationResult(
                isValid = false,
                errorMessage = "각 제공사항은 ${Constants.MAX_PROVIDED_ITEM_LENGTH}자 이하여야 합니다"
            )
            providedItems.any { it.isBlank() } -> ValidationResult(
                isValid = false,
                errorMessage = "빈 제공사항은 등록할 수 없습니다"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * 전체 프로젝트 데이터 유효성 검증
     */
    fun validateProjectData(projectData: ProjectRegistrationData): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        // 필수 정보 검증
        validateProjectTitle(projectData.requiredInfo.projectTitle).let { result ->
            if (!result.isValid) errors["projectTitle"] = result.errorMessage ?: ""
        }

        validateWorkType(projectData.requiredInfo.workType).let { result ->
            if (!result.isValid) errors["workType"] = result.errorMessage ?: ""
        }

        validateLocation(projectData.requiredInfo.location).let { result ->
            if (!result.isValid) errors["location"] = result.errorMessage ?: ""
        }

        validateDateRange(projectData.requiredInfo.startDate, projectData.requiredInfo.endDate).let { result ->
            if (!result.isValid) errors["dateRange"] = result.errorMessage ?: ""
        }

        validateTimeRange(projectData.requiredInfo.startTime, projectData.requiredInfo.endTime).let { result ->
            if (!result.isValid) errors["timeRange"] = result.errorMessage ?: ""
        }

        // 팀 정보 검증
        validateWorkerCount(projectData.teamInfo.totalWorkers, projectData.teamInfo.requiredWorkers).let { result ->
            if (!result.isValid) errors["workerCount"] = result.errorMessage ?: ""
        }

        validateRequirements(projectData.teamInfo.requirements).let { result ->
            if (!result.isValid) errors["requirements"] = result.errorMessage ?: ""
        }

        validateProvidedItems(projectData.teamInfo.providedItems).let { result ->
            if (!result.isValid) errors["providedItems"] = result.errorMessage ?: ""
        }

        // 급여 정보 검증
        validateWage(
            projectData.moneyInfo.paymentType,
            projectData.moneyInfo.dailyWage,
            projectData.moneyInfo.hourlyWage,
            projectData.moneyInfo.projectWage
        ).let { result ->
            if (!result.isValid) errors["wage"] = result.errorMessage ?: ""
        }

        validateTransportationFee(projectData.moneyInfo.transportationFee).let { result ->
            if (!result.isValid) errors["transportationFee"] = result.errorMessage ?: ""
        }

        validateNotes(projectData.moneyInfo.notes).let { result ->
            if (!result.isValid) errors["notes"] = result.errorMessage ?: ""
        }

        return errors
    }
}