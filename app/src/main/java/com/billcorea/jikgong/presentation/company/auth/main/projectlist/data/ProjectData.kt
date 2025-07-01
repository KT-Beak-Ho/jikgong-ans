package com.billcorea.jikgong.presentation.company.auth.main.projectlist.data


import java.time.LocalDate
import java.time.LocalDateTime

// 프로젝트 상태
enum class ProjectStatus {
    PLANNING,     // 계획중
    RECRUITING,   // 모집중
    IN_PROGRESS,  // 진행중
    COMPLETED,    // 완료
    CANCELLED     // 취소
}

// 프로젝트 우선순위
enum class ProjectPriority {
    LOW,      // 낮음
    MEDIUM,   // 보통
    HIGH,     // 높음
    URGENT    // 긴급
}

// 필요 직종 정보
data class RequiredJob(
    val jobType: String,        // 직종 (예: "NORMAL", "FOREMAN" 등)
    val jobName: String,        // 직종명 (예: "보통인부", "작업반장")
    val requiredCount: Int,     // 필요 인원수
    val currentCount: Int = 0,  // 현재 지원 인원수
    val dailyWage: Int         // 일당
)

// 프로젝트 데이터 모델
data class ProjectData(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: ProjectStatus,
    val priority: ProjectPriority,
    val requiredJobs: List<RequiredJob>,
    val totalBudget: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val companyId: String,
    val companyName: String
) {
    // 총 필요 인원
    val totalRequiredWorkers: Int
        get() = requiredJobs.sumOf { it.requiredCount }

    // 현재 지원 인원
    val totalCurrentWorkers: Int
        get() = requiredJobs.sumOf { it.currentCount }

    // 모집 완료율
    val recruitmentProgress: Float
        get() = if (totalRequiredWorkers > 0) {
            totalCurrentWorkers.toFloat() / totalRequiredWorkers.toFloat()
        } else 0f

    // 프로젝트 기간 (일)
    val durationDays: Long
        get() = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1

    // 상태별 색상 (UI에서 사용)
    val statusColor: androidx.compose.ui.graphics.Color
        get() = when (status) {
            ProjectStatus.PLANNING -> androidx.compose.ui.graphics.Color.Gray
            ProjectStatus.RECRUITING -> androidx.compose.ui.graphics.Color.Blue
            ProjectStatus.IN_PROGRESS -> androidx.compose.ui.graphics.Color.Green
            ProjectStatus.COMPLETED -> androidx.compose.ui.graphics.Color.DarkGray
            ProjectStatus.CANCELLED -> androidx.compose.ui.graphics.Color.Red
        }

    // 상태별 한글명
    val statusDisplayName: String
        get() = when (status) {
            ProjectStatus.PLANNING -> "계획중"
            ProjectStatus.RECRUITING -> "모집중"
            ProjectStatus.IN_PROGRESS -> "진행중"
            ProjectStatus.COMPLETED -> "완료"
            ProjectStatus.CANCELLED -> "취소"
        }
}

// 샘플 데이터 생성 함수 (개발/테스트용)
object ProjectSampleData {
    fun getSampleProjects(): List<ProjectData> {
        return listOf(
            ProjectData(
                id = "1",
                title = "강남구 아파트 신축 공사",
                description = "강남구 역삼동 아파트 신축 공사입니다. 총 15층 규모의 대형 프로젝트입니다.",
                location = "서울시 강남구 역삼동",
                address = "서울시 강남구 역삼동 123-45",
                latitude = 37.5665,
                longitude = 127.0780,
                startDate = LocalDate.now().plusDays(7),
                endDate = LocalDate.now().plusDays(90),
                status = ProjectStatus.RECRUITING,
                priority = ProjectPriority.HIGH,
                requiredJobs = listOf(
                    RequiredJob("NORMAL", "보통인부", 10, 3, 150000),
                    RequiredJob("FOREMAN", "작업반장", 2, 1, 200000),
                    RequiredJob("REBAR_WORKER", "철근공", 5, 2, 180000)
                ),
                totalBudget = 500000000L,
                createdAt = LocalDateTime.now().minusDays(3),
                updatedAt = LocalDateTime.now().minusHours(2),
                companyId = "company1",
                companyName = "대한건설"
            ),
            ProjectData(
                id = "2",
                title = "인천 물류센터 건설",
                description = "인천광역시 물류센터 건설 프로젝트입니다.",
                location = "인천광역시 연수구",
                address = "인천광역시 연수구 송도동 100-1",
                latitude = 37.3914,
                longitude = 126.6406,
                startDate = LocalDate.now().plusDays(14),
                endDate = LocalDate.now().plusDays(120),
                status = ProjectStatus.PLANNING,
                priority = ProjectPriority.MEDIUM,
                requiredJobs = listOf(
                    RequiredJob("NORMAL", "보통인부", 15, 0, 140000),
                    RequiredJob("CONCRETE_WORKER", "콘크리트공", 8, 0, 170000)
                ),
                totalBudget = 800000000L,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now().minusHours(5),
                companyId = "company1",
                companyName = "대한건설"
            ),
            ProjectData(
                id = "3",
                title = "부산 교량 보수 공사",
                description = "부산광역시 해운대구 교량 보수 및 보강 공사입니다.",
                location = "부산광역시 해운대구",
                address = "부산광역시 해운대구 우동 200-3",
                latitude = 35.1595,
                longitude = 129.1603,
                startDate = LocalDate.now().minusDays(10),
                endDate = LocalDate.now().plusDays(45),
                status = ProjectStatus.IN_PROGRESS,
                priority = ProjectPriority.URGENT,
                requiredJobs = listOf(
                    RequiredJob("STEEL_STRUCTURE", "철골공", 6, 6, 190000),
                    RequiredJob("WELDER", "용접공", 4, 4, 210000)
                ),
                totalBudget = 300000000L,
                createdAt = LocalDateTime.now().minusDays(25),
                updatedAt = LocalDateTime.now().minusHours(1),
                companyId = "company1",
                companyName = "대한건설"
            )
        )
    }
}