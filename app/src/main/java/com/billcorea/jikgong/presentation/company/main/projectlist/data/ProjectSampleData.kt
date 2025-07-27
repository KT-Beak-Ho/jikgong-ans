package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime

object ProjectSampleData {

    fun getSampleProjects(): List<ProjectData> {
        val today = LocalDate.now()

        return listOf(
            ProjectData(
                id = "project1",
                title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
                description = "친환경 온도 측정 센터 건립을 위한 신축 공사입니다. 최신 기술을 적용한 스마트 빌딩으로 건설될 예정입니다.",
                location = "부산 사하구",
                detailAddress = "부산광역시 사하구 낙동대로 123",
                distance = 2.5,
                jobTypes = listOf(JobType.GENERAL_WORKER, JobType.SKILLED_WORKER, JobType.SUPERVISOR),
                totalWorkers = 15,
                completedWorkers = 2,
                dailyWage = 130000,
                startDate = today.plusDays(3),
                endDate = today.plusDays(25),
                startTime = "08:00",
                endTime = "17:00",
                status = ProjectStatus.RECRUITING,
                isUrgent = true,
                requirements = listOf("안전화 필수", "작업복 착용", "안전교육 이수"),
                providedItems = listOf("중식 제공", "교통비 지급", "안전장비 제공"),
                notes = "신축 공사로 깔끔한 작업 환경입니다."
            ),

            ProjectData(
                id = "project2",
                title = "직공센터 공사",
                description = "현대적인 업무 시설 구축을 위한 인테리어 공사",
                location = "인천 연수구",
                detailAddress = "인천광역시 연수구 컨벤시아대로 456",
                distance = 12.8,
                jobTypes = listOf(JobType.GENERAL_WORKER, JobType.SKILLED_WORKER),
                totalWorkers = 8,
                completedWorkers = 8,
                dailyWage = 140000,
                startDate = today.minusDays(10),
                endDate = today.minusDays(1),
                startTime = "09:00",
                endTime = "18:00",
                status = ProjectStatus.COMPLETED,
                isUrgent = false,
                requirements = listOf("인테리어 경험 우대", "섬세한 작업 가능자"),
                providedItems = listOf("중식 제공", "간식 제공"),
                notes = "완료된 프로젝트입니다."
            ),

            ProjectData(
                id = "project3",
                title = "아파트 외벽 도색 작업",
                description = "15층 아파트 외벽 전체 도색 및 방수 작업",
                location = "서울 강남구",
                detailAddress = "서울특별시 강남구 테헤란로 789",
                distance = 45.2,
                jobTypes = listOf(JobType.SKILLED_WORKER, JobType.SPECIALIST),
                totalWorkers = 12,
                completedWorkers = 7,
                dailyWage = 160000,
                startDate = today.plusDays(1),
                endDate = today.plusDays(14),
                startTime = "07:30",
                endTime = "16:30",
                status = ProjectStatus.IN_PROGRESS,
                isUrgent = false,
                requirements = listOf("고소작업 가능자", "도색 경험 필수", "안전교육 이수"),
                providedItems = listOf("중식 제공", "교통비 지급", "안전장비 제공", "작업복 제공"),
                notes = "고소작업이므로 안전에 각별히 유의해주세요."
            ),

            ProjectData(
                id = "project4",
                title = "물류센터 건설",
                description = "대형 물류센터 신축을 위한 토목 및 건축 공사",
                location = "경기 화성시",
                detailAddress = "경기도 화성시 동탄대로 321",
                distance = 18.7,
                jobTypes = listOf(JobType.GENERAL_WORKER, JobType.CRANE_OPERATOR, JobType.SUPERVISOR),
                totalWorkers = 25,
                completedWorkers = 12,
                dailyWage = 145000,
                startDate = today.minusDays(5),
                endDate = today.plusDays(30),
                startTime = "08:00",
                endTime = "17:30",
                status = ProjectStatus.IN_PROGRESS,
                isUrgent = true,
                priority = 1,
                requirements = listOf("대형 건설 경험", "크레인 조종 자격증", "팀워크 중시"),
                providedItems = listOf("중식 제공", "교통비 지급", "숙박 시설 제공"),
                notes = "대규모 프로젝트로 장기간 안정적인 근무가 가능합니다."
            ),

            ProjectData(
                id = "project5",
                title = "교량 보수 공사",
                description = "노후화된 교량의 안전성 강화를 위한 보수 작업",
                location = "부산 해운대구",
                detailAddress = "부산광역시 해운대구 해운대해변로 567",
                distance = 5.3,
                jobTypes = listOf(JobType.SKILLED_WORKER, JobType.SPECIALIST, JobType.SUPERVISOR),
                totalWorkers = 10,
                completedWorkers = 0,
                dailyWage = 175000,
                startDate = today.plusDays(7),
                endDate = today.plusDays(21),
                startTime = "08:30",
                endTime = "17:00",
                status = ProjectStatus.RECRUITING,
                isUrgent = false,
                requirements = listOf("교량 공사 경험", "용접 자격증", "고소작업 가능"),
                providedItems = listOf("중식 제공", "교통비 지급", "안전장비 제공"),
                notes = "전문성이 요구되는 고급 기술 작업입니다."
            )
        )
    }

    fun getEmptyProjects(): List<ProjectData> {
        return emptyList()
    }

    fun getProjectsByStatus(status: ProjectStatus): List<ProjectData> {
        return getSampleProjects().filter { it.status == status }
    }

    fun getUrgentProjects(): List<ProjectData> {
        return getSampleProjects().filter { it.isUrgent || it.isUrgentRecruitment }
    }

    fun getProjectsByLocation(location: String): List<ProjectData> {
        return getSampleProjects().filter {
            it.location.contains(location, ignoreCase = true)
        }
    }

    fun getProjectsByJobType(jobType: JobType): List<ProjectData> {
        return getSampleProjects().filter {
            it.jobTypes.contains(jobType)
        }
    }

    fun getProjectsNearby(maxDistance: Double): List<ProjectData> {
        return getSampleProjects().filter {
            it.distance <= maxDistance
        }.sortedBy { it.distance }
    }

    fun getHighWageProjects(minWage: Int): List<ProjectData> {
        return getSampleProjects().filter {
            it.dailyWage >= minWage
        }.sortedByDescending { it.dailyWage }
    }
}