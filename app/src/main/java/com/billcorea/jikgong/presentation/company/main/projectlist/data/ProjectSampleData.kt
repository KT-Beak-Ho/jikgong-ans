// 📄 data/ProjectSampleData.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDateTime

/**
 * 프로젝트 샘플 데이터
 */
object ProjectSampleData {

  fun getSampleProjects(): List<Project> {
    return listOf(
      Project(
        id = "1",
        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
        location = "부산 사하구",
        workType = "일반 건설",
        dailyWage = 510000,
        status = ProjectStatus.RECRUITING,
        recruitCount = 15,
        currentCount = 3,
        startDate = LocalDateTime.now().plusDays(5),
        endDate = LocalDateTime.now().plusDays(35),
        description = "낙동강 온도 측정을 위한 센터 건물 신축 공사",
        requiredSkills = listOf("건설업 경력 3년 이상", "안전교육 수료"),
        contactPhone = "010-1234-5678",
        isUrgent = true,
        transportationFee = 20000,
        mealProvided = true,
        companyName = "한국건설(주)",
        address = "부산시 사하구 낙동대로 123"
      ),
      Project(
        id = "2",
        title = "인천 물류센터 건설",
        location = "인천 연수구",
        workType = "철근공",
        dailyWage = 280000,
        status = ProjectStatus.IN_PROGRESS,
        recruitCount = 8,
        currentCount = 6,
        startDate = LocalDateTime.now().minusDays(10),
        endDate = LocalDateTime.now().plusDays(50),
        description = "대형 물류센터 철골 구조 작업",
        requiredSkills = listOf("철근공 자격증", "고소작업 가능"),
        contactPhone = "010-2345-6789",
        transportationFee = 15000,
        accommodationProvided = true,
        companyName = "태평양건설",
        address = "인천시 연수구 송도대로 456"
      ),
      Project(
        id = "3",
        title = "강남 오피스텔 인테리어",
        location = "서울 강남구",
        workType = "내장공",
        dailyWage = 350000,
        status = ProjectStatus.RECRUITING,
        recruitCount = 12,
        currentCount = 8,
        startDate = LocalDateTime.now().plusDays(3),
        endDate = LocalDateTime.now().plusDays(25),
        description = "고급 오피스텔 실내 인테리어 공사",
        requiredSkills = listOf("내장공 경력", "마감재 시공 경험"),
        contactPhone = "010-3456-7890",
        transportationFee = 30000,
        mealProvided = true,
        companyName = "서울인테리어",
        address = "서울시 강남구 테헤란로 789"
      ),
      Project(
        id = "4",
        title = "판교 상가 전기공사",
        location = "경기도 성남시 분당구",
        workType = "전기공",
        dailyWage = 320000,
        status = ProjectStatus.COMPLETED,
        recruitCount = 4,
        currentCount = 4,
        startDate = LocalDateTime.now().minusDays(30),
        endDate = LocalDateTime.now().minusDays(5),
        description = "상가 전기 배선 및 조명 설치 작업",
        requiredSkills = listOf("전기기능사", "고압전기 경험"),
        contactPhone = "010-4567-8901",
        companyName = "경기전기공사",
        address = "경기도 성남시 분당구 판교역로 321"
      ),
      Project(
        id = "5",
        title = "잠실 아파트 배관공사",
        location = "서울특별시 송파구",
        workType = "배관공",
        dailyWage = 290000,
        status = ProjectStatus.IN_PROGRESS,
        recruitCount = 6,
        currentCount = 4,
        startDate = LocalDateTime.now().minusDays(5),
        endDate = LocalDateTime.now().plusDays(20),
        description = "아파트 급수관 교체 및 보수 작업",
        requiredSkills = listOf("배관공 경력 3년 이상", "용접 가능"),
        contactPhone = "010-5678-9012",
        mealProvided = true,
        companyName = "서울배관공사",
        address = "서울시 송파구 잠실로 654"
      ),
      Project(
        id = "6",
        title = "부천 공장 도장공사",
        location = "경기도 부천시",
        workType = "도장공",
        dailyWage = 260000,
        status = ProjectStatus.RECRUITING,
        recruitCount = 10,
        currentCount = 2,
        startDate = LocalDateTime.now().plusDays(7),
        endDate = LocalDateTime.now().plusDays(28),
        description = "제조업 공장 외벽 및 내부 도장 작업",
        requiredSkills = listOf("도장 경력", "안전교육 이수"),
        contactPhone = "010-6789-0123",
        isUrgent = true,
        transportationFee = 25000,
        companyName = "경기도장",
        address = "경기도 부천시 소사로 987"
      ),
      Project(
        id = "7",
        title = "대전 대학교 리모델링",
        location = "대전광역시 유성구",
        workType = "미장공",
        dailyWage = 270000,
        status = ProjectStatus.PAUSED,
        recruitCount = 8,
        currentCount = 5,
        startDate = LocalDateTime.now().plusDays(14),
        endDate = LocalDateTime.now().plusDays(42),
        description = "대학교 건물 내부 미장 및 마감 작업",
        requiredSkills = listOf("미장공 자격증", "교육시설 공사 경험"),
        contactPhone = "010-7890-1234",
        accommodationProvided = true,
        companyName = "대전건설",
        address = "대전시 유성구 대학로 147"
      ),
      Project(
        id = "8",
        title = "광주 병원 증축공사",
        location = "광주광역시 서구",
        workType = "타일공",
        dailyWage = 300000,
        status = ProjectStatus.RECRUITING,
        recruitCount = 5,
        currentCount = 1,
        startDate = LocalDateTime.now().plusDays(10),
        endDate = LocalDateTime.now().plusDays(40),
        description = "종합병원 신관 타일 시공 작업",
        requiredSkills = listOf("타일공 경력", "의료시설 공사 경험"),
        contactPhone = "010-8901-2345",
        transportationFee = 20000,
        mealProvided = true,
        companyName = "광주타일",
        address = "광주시 서구 상무대로 258"
      )
    )
  }

  fun calculateSummary(projects: List<Project>): ProjectSummary {
    val total = projects.size
    val recruiting = projects.count { it.status == ProjectStatus.RECRUITING }
    val inProgress = projects.count { it.status == ProjectStatus.IN_PROGRESS }
    val completed = projects.count { it.status == ProjectStatus.COMPLETED }

    val thisMonth = LocalDateTime.now().month
    val thisYear = LocalDateTime.now().year
    val thisMonthRecruits = projects
      .filter { it.createdAt.month == thisMonth && it.createdAt.year == thisYear }
      .sumOf { it.currentCount }

    val averageDailyWage = if (projects.isNotEmpty()) {
      projects.map { it.dailyWage }.average().toLong()
    } else 0L

    val totalRecruitCount = projects.sumOf { it.recruitCount }
    val totalCurrentCount = projects.sumOf { it.currentCount }

    return ProjectSummary(
      total = total,
      recruiting = recruiting,
      inProgress = inProgress,
      completed = completed,
      thisMonthRecruits = thisMonthRecruits,
      averageDailyWage = averageDailyWage,
      totalRecruitCount = totalRecruitCount,
      totalCurrentCount = totalCurrentCount
    )
  }

  fun getEmptyProjectSummary(): ProjectSummary {
    return ProjectSummary()
  }
}