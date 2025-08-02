// ========================================
// 📄 data/ProjectSampleData.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 프로젝트 샘플 데이터 - 이미지 UI와 유사한 데이터
 */
object ProjectSampleData {

  /**
   * 샘플 프로젝트 목록 생성
   */
  fun getSampleProjects(): List<Project> {
    return listOf(
      // 이미지의 "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사"
      Project(
        id = "project_001",
        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
        description = "낙동강 인근 온도 측정 센터 신축 프로젝트입니다. 콘크리트 타설 및 철근 작업이 주요 업무입니다.",
        location = ProjectLocation(
          city = "부산",
          district = "사하구",
          detail = "낙동대로 550번길"
        ),
        dailyWage = 510000,
        requiredWorkers = 15,
        appliedWorkers = 3,
        workType = WorkType.GENERAL_CONSTRUCTION,
        status = ProjectStatus.RECRUITING,
        startDate = LocalDate.of(2025, 1, 5),
        endDate = LocalDate.of(2025, 2, 4),
        workHours = WorkHours("08:00", "18:00"),
        requirements = listOf("건설업 경험 1년 이상", "기초 안전교육 이수"),
        benefits = listOf("중식 제공", "안전장비 지급", "교통비 지원"),
        isUrgent = true,
        companyName = "대한건설(주)",
        companyRating = 4.5f,
        createdAt = LocalDateTime.now().minusDays(2),
        updatedAt = LocalDateTime.now().minusHours(3),
        tags = listOf("부산", "사하구", "신축", "콘크리트", "철근"),
        paymentMethod = PaymentMethod.WEEKLY,
        hasAccommodation = false,
        hasMeals = true,
        contactInfo = ContactInfo(
          managerName = "김현장",
          phoneNumber = "010-1234-5678"
        )
      ),

      // 인천 물류센터 건설 (진행중 프로젝트)
      Project(
        id = "project_002",
        title = "인천 물류센터 건설",
        description = "대형 물류센터 건설 프로젝트입니다. 현재 내부 인테리어 작업 진행 중입니다.",
        location = ProjectLocation(
          city = "인천",
          district = "연수구",
          detail = "송도국제도시"
        ),
        dailyWage = 480000,
        requiredWorkers = 20,
        appliedWorkers = 18,
        workType = WorkType.INTERIOR,
        status = ProjectStatus.IN_PROGRESS,
        startDate = LocalDate.of(2024, 12, 30),
        endDate = LocalDate.of(2025, 2, 13),
        workHours = WorkHours("09:00", "18:00"),
        requirements = listOf("인테리어 경험 필수", "목공 작업 가능"),
        benefits = listOf("중식 제공", "주차 지원", "성과급"),
        isUrgent = false,
        companyName = "송도건설산업",
        companyRating = 4.2f,
        createdAt = LocalDateTime.now().minusDays(45),
        updatedAt = LocalDateTime.now().minusHours(1),
        tags = listOf("인천", "물류센터", "인테리어", "목공"),
        paymentMethod = PaymentMethod.DIRECT,
        hasAccommodation = true,
        hasMeals = true
      ),

      // 도로 공사 (모집중)
      Project(
        id = "project_003",
        title = "강남구 테헤란로 도로 보수공사",
        description = "테헤란로 일대 도로 보수 및 포장 작업입니다. 야간 작업이 포함됩니다.",
        location = ProjectLocation(
          city = "서울",
          district = "강남구",
          detail = "테헤란로 일대"
        ),
        dailyWage = 550000,
        requiredWorkers = 8,
        appliedWorkers = 2,
        workType = WorkType.ROAD_CONSTRUCTION,
        status = ProjectStatus.RECRUITING,
        startDate = LocalDate.of(2025, 1, 15),
        endDate = LocalDate.of(2025, 1, 25),
        workHours = WorkHours("22:00", "06:00"),
        requirements = listOf("도로공사 경험", "야간작업 가능"),
        benefits = listOf("야간수당 별도", "교통비 지원", "안전장비 지급"),
        isUrgent = true,
        companyName = "서울도로공사",
        companyRating = 4.7f,
        createdAt = LocalDateTime.now().minusDays(1),
        updatedAt = LocalDateTime.now().minusMinutes(30),
        tags = listOf("서울", "강남구", "도로", "야간작업", "보수"),
        paymentMethod = PaymentMethod.WEEKLY,
        hasAccommodation = false,
        hasMeals = false
      ),

      // 아파트 리모델링 (완료)
      Project(
        id = "project_004",
        title = "잠실 아파트 단지 리모델링",
        description = "잠실 주공아파트 외벽 및 내부 리모델링이 완료되었습니다.",
        location = ProjectLocation(
          city = "서울",
          district = "송파구",
          detail = "잠실동"
        ),
        dailyWage = 450000,
        requiredWorkers = 25,
        appliedWorkers = 25,
        workType = WorkType.INTERIOR,
        status = ProjectStatus.COMPLETED,
        startDate = LocalDate.of(2024, 11, 1),
        endDate = LocalDate.of(2024, 12, 20),
        workHours = WorkHours("08:30", "17:30"),
        requirements = listOf("리모델링 경험", "도색 작업 가능"),
        benefits = listOf("중식 제공", "완료 보너스"),
        isUrgent = false,
        companyName = "동서건설",
        companyRating = 4.3f,
        createdAt = LocalDateTime.now().minusDays(90),
        updatedAt = LocalDateTime.now().minusDays(15),
        tags = listOf("서울", "송파구", "아파트", "리모델링", "완료"),
        paymentMethod = PaymentMethod.MONTHLY,
        hasAccommodation = false,
        hasMeals = true
      ),

      // 공장 건설 (모집중, 긴급)
      Project(
        id = "project_005",
        title = "화성 반도체 공장 건설",
        description = "반도체 공장 신축 프로젝트입니다. 클린룸 시설 설치 작업도 포함됩니다.",
        location = ProjectLocation(
          city = "경기도",
          district = "화성시",
          detail = "동탄2신도시"
        ),
        dailyWage = 580000,
        requiredWorkers = 50,
        appliedWorkers = 12,
        workType = WorkType.GENERAL_CONSTRUCTION,
        status = ProjectStatus.RECRUITING,
        startDate = LocalDate.of(2025, 1, 8),
        endDate = LocalDate.of(2025, 4, 30),
        workHours = WorkHours("07:00", "19:00"),
        requirements = listOf("대형 건설현장 경험", "클린룸 작업 경험 우대"),
        benefits = listOf("3식 제공", "기숙사 제공", "교통버스 운행", "성과급"),
        isUrgent = true,
        companyName = "삼성물산",
        companyRating = 4.8f,
        createdAt = LocalDateTime.now().minusHours(6),
        updatedAt = LocalDateTime.now().minusMinutes(15),
        tags = listOf("경기도", "화성", "공장", "반도체", "신축", "클린룸"),
        paymentMethod = PaymentMethod.DIRECT,
        hasAccommodation = true,
        hasMeals = true
      ),

      // 다리 보수 (모집중)
      Project(
        id = "project_006",
        title = "한강대교 보수공사",
        description = "한강대교 교량 보수 및 도색 작업입니다. 고소작업이 포함됩니다.",
        location = ProjectLocation(
          city = "서울",
          district = "용산구",
          detail = "한강대교"
        ),
        dailyWage = 520000,
        requiredWorkers = 12,
        appliedWorkers = 8,
        workType = WorkType.PAINTING,
        status = ProjectStatus.RECRUITING,
        startDate = LocalDate.of(2025, 2, 1),
        endDate = LocalDate.of(2025, 3, 15),
        workHours = WorkHours("08:00", "17:00"),
        requirements = listOf("고소작업 가능", "도색 경험 필수", "교량공사 경험 우대"),
        benefits = listOf("위험수당", "중식 제공", "안전장비 지급"),
        isUrgent = false,
        companyName = "한국교량공사",
        companyRating = 4.4f,
        createdAt = LocalDateTime.now().minusDays(3),
        updatedAt = LocalDateTime.now().minusHours(2),
        tags = listOf("서울", "용산구", "교량", "도색", "고소작업"),
        paymentMethod = PaymentMethod.WEEKLY,
        hasAccommodation = false,
        hasMeals = true
      )
    )
  }

  /**
   * 프로젝트 요약 통계 생성
   */
  fun getSampleProjectSummary(): ProjectSummary {
    val projects = getSampleProjects()

    return ProjectSummary(
      totalProjects = projects.size,
      recruitingProjects = projects.count { it.status == ProjectStatus.RECRUITING },
      inProgressProjects = projects.count { it.status == ProjectStatus.IN_PROGRESS },
      completedProjects = projects.count { it.status == ProjectStatus.COMPLETED },
      cancelledProjects = projects.count { it.status == ProjectStatus.CANCELLED },
      urgentProjects = projects.count { it.isUrgent },
      totalRequiredWorkers = projects.sumOf { it.requiredWorkers },
      totalAppliedWorkers = projects.sumOf { it.appliedWorkers },
      averageDailyWage = projects.map { it.dailyWage }.average().toInt(),
      thisMonthProjects = projects.count {
        it.startDate.monthValue == LocalDate.now().monthValue
      }
    )
  }

  /**
   * 빈 상태 요약 통계
   */
  fun getEmptyProjectSummary(): ProjectSummary {
    return ProjectSummary()
  }

  /**
   * 검색 제안어 목록
   */
  fun getSearchSuggestions(): List<String> {
    return listOf(
      "부산 사하구",
      "인테리어",
      "도로공사",
      "신축",
      "리모델링",
      "긴급모집",
      "고임금",
      "숙소제공",
      "일반건설",
      "도색작업"
    )
  }

  /**
   * 북마크된 프로젝트 ID 목록 (샘플)
   */
  fun getBookmarkedProjectIds(): Set<String> {
    return setOf("project_001", "project_003", "project_005")
  }
}