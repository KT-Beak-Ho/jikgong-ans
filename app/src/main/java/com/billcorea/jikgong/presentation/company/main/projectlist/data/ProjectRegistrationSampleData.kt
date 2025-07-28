package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object ProjectRegistrationSampleData {

    fun getSampleProjectRegistrationData(): ProjectRegistrationData {
        return ProjectRegistrationData(
            id = "sample_project_1",
            requiredInfo = RequiredInfo(
                projectTitle = "강남구 아파트 신축 공사",
                workType = JobType.GENERAL_WORKER,
                location = "서울시 강남구 테헤란로",
                detailAddress = "테헤란로 123-45 신축현장",
                startDate = LocalDate.now().plusDays(7),
                endDate = LocalDate.now().plusDays(37),
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(17, 0)
            ),
            teamInfo = TeamInfo(
                totalWorkers = 20,
                requiredWorkers = 15,
                preferredAge = "25-45세",
                experienceRequired = true,
                requirements = listOf(
                    "안전화 필수 착용",
                    "작업복 착용",
                    "안전모 착용",
                    "건설 경력 1년 이상"
                ),
                providedItems = listOf(
                    "중식 제공",
                    "교통비 지급",
                    "안전장비 제공",
                    "작업복 제공"
                )
            ),
            moneyInfo = MoneyInfo(
                paymentType = PaymentType.DAILY,
                dailyWage = 150000L,
                hourlyWage = 0L,
                projectWage = 0L,
                transportationFee = 10000L,
                mealProvided = true,
                accommodationProvided = false,
                notes = "성실하고 책임감 있는 분들을 찾습니다. 장기 근무 가능한 분 우대합니다."
            ),
            status = ProjectRegistrationStatus.DRAFT,
            currentPage = 1,
            isUrgent = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    fun getEmptyProjectRegistrationData(): ProjectRegistrationData {
        return ProjectRegistrationData()
    }

    fun getSampleDraftData(): List<ProjectRegistrationData> {
        return listOf(
            ProjectRegistrationData(
                id = "draft_1",
                requiredInfo = RequiredInfo(
                    projectTitle = "부산 교량 보수 공사",
                    workType = JobType.SKILLED_WORKER,
                    location = "부산시 해운대구"
                ),
                status = ProjectRegistrationStatus.DRAFT,
                createdAt = LocalDateTime.now().minusHours(2),
                updatedAt = LocalDateTime.now().minusHours(1)
            ),
            ProjectRegistrationData(
                id = "draft_2",
                requiredInfo = RequiredInfo(
                    projectTitle = "인천 물류센터 건설",
                    workType = JobType.CONCRETE_WORKER,
                    location = "인천시 연수구",
                    startDate = LocalDate.now().plusDays(14)
                ),
                teamInfo = TeamInfo(
                    totalWorkers = 30,
                    requiredWorkers = 25
                ),
                status = ProjectRegistrationStatus.DRAFT,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now().minusHours(3)
            )
        )
    }

    fun getSampleCompletedProjects(): List<ProjectRegistrationData> {
        return listOf(
            getSampleProjectRegistrationData().copy(
                id = "completed_1",
                status = ProjectRegistrationStatus.PUBLISHED,
                requiredInfo = getSampleProjectRegistrationData().requiredInfo.copy(
                    projectTitle = "서울역 인근 오피스텔 건설"
                )
            ),
            getSampleProjectRegistrationData().copy(
                id = "completed_2",
                status = ProjectRegistrationStatus.PUBLISHED,
                requiredInfo = getSampleProjectRegistrationData().requiredInfo.copy(
                    projectTitle = "대전 공장 신축 공사",
                    workType = JobType.WELDING_WORKER
                ),
                moneyInfo = getSampleProjectRegistrationData().moneyInfo.copy(
                    dailyWage = 180000L
                )
            )
        )
    }

    // 검증용 샘플 데이터
    fun getValidationTestData(): Map<String, ProjectRegistrationData> {
        return mapOf(
            "empty_title" to ProjectRegistrationData(
                requiredInfo = RequiredInfo(projectTitle = "")
            ),
            "short_title" to ProjectRegistrationData(
                requiredInfo = RequiredInfo(projectTitle = "짧음")
            ),
            "long_title" to ProjectRegistrationData(
                requiredInfo = RequiredInfo(
                    projectTitle = "매우 긴 프로젝트 제목입니다. 이 제목은 50자를 초과하여 유효성 검증에서 실패해야 합니다."
                )
            ),
            "invalid_workers" to ProjectRegistrationData(
                teamInfo = TeamInfo(
                    totalWorkers = 5,
                    requiredWorkers = 10 // 총 인원수보다 많음
                )
            ),
            "low_wage" to ProjectRegistrationData(
                moneyInfo = MoneyInfo(
                    paymentType = PaymentType.DAILY,
                    dailyWage = 30000L // 최저임금 미만
                )
            )
        )
    }
}

// 샘플 데이터 확장 함수들
fun ProjectRegistrationData.withSampleRequiredInfo(): ProjectRegistrationData {
    return this.copy(
        requiredInfo = RequiredInfo(
            projectTitle = "샘플 프로젝트",
            workType = JobType.GENERAL_WORKER,
            location = "서울시 강남구",
            detailAddress = "테헤란로 123",
            startDate = LocalDate.now().plusDays(1),
            endDate = LocalDate.now().plusDays(30),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(18, 0)
        )
    )
}

fun ProjectRegistrationData.withSampleTeamInfo(): ProjectRegistrationData {
    return this.copy(
        teamInfo = TeamInfo(
            totalWorkers = 10,
            requiredWorkers = 8,
            preferredAge = "20-50세",
            experienceRequired = false,
            requirements = listOf("안전화 착용", "작업복 착용"),
            providedItems = listOf("중식 제공", "교통비 일부 지급")
        )
    )
}

fun ProjectRegistrationData.withSampleMoneyInfo(): ProjectRegistrationData {
    return this.copy(
        moneyInfo = MoneyInfo(
            paymentType = PaymentType.DAILY,
            dailyWage = 120000L,
            transportationFee = 5000L,
            mealProvided = true,
            accommodationProvided = false,
            notes = "성실한 분들을 찾습니다."
        )
    )
}