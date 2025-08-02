package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 프로젝트 샘플 데이터 - 직직직 플랫폼용
 */
object ProjectSampleData {

  fun getSampleProjects(): List<Project> {
    val now = LocalDateTime.now()
    val today = LocalDate.now()

    return listOf(
      // 1. 사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사 (이미지와 동일)
      Project(
        id = "proj_001",
        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
        description = "부산 사하구에 위치한 낙동강 온도 측정을 위한 센터 건물 신축 공사입니다. 최신 IoT 기술을 적용한 스마트 측정 시설로 건설됩니다.",
        location = "부산 사하구",
        detailAddress = "부산 사하구 낙동남로 1234",
        workType = WorkType.BUILDING_CONSTRUCTION,
        status = ProjectStatus.RECRUITING,
        startDate = "01/05",
        endDate = "02/04",
        fullStartDate = today.plusDays(2),
        fullEndDate = today.plusDays(32),
        dailyWage = 510_000L,
        requiredWorkers = 15,
        currentWorkers = 3,
        workingHours = WorkingHours("08:00", "18:00", 60),
        companyName = "대한건설 주식회사",
        companyId = "company_001",
        contactPerson = "김현장",
        contactNumber = "010-1234-5678",
        requirements = listOf("건설업 경력 1년 이상", "안전교육 이수", "건강상태 양호"),
        benefits = listOf("4대보험 완비", "중식 제공", "교통비 지원", "안전장비 지급"),
        isUrgent = true,
        isBookmarked = false,
        createdAt = now.minusDays(1),
        updatedAt = now.minusHours(2),
        views = 127,
        applicationCount = 8,
        safetyLevel = SafetyLevel.HIGH,
        settlementType = SettlementType.DAILY
      ),

      // 2. 인천 물류센터 건설
      Project(
        id = "proj_002",
        title = "인천 물류센터 건설",
        description = "대규모 물류센터 건설 프로젝트로, 자동화 시설이 포함된 최신 물류 허브를 구축합니다.",
        location = "인천 연수구",
        detailAddress = "인천 연수구 송도국제대로 123",
        workType = WorkType.BUILDING_CONSTRUCTION,
        status = ProjectStatus.IN_PROGRESS,
        startDate = "12/30",
        endDate = "02/13",
        fullStartDate = today.minusDays(4),
        fullEndDate = today.plusDays(41),
        dailyWage = 480_000L,
        requiredWorkers = 25,
        currentWorkers = 22,
        workingHours = WorkingHours("07:30", "17:30", 60),
        companyName = "현대건설산업 주식회사",
        companyId = "company_002",
        contactPerson = "이팀장",
        contactNumber = "010-2345-6789",
        requirements = listOf("중장비 운전 가능", "고소작업 가능", "팀워크 중시"),
        benefits = listOf("숙박 제공", "조식/중식 제공", "주말 특근수당", "성과급"),
        isUrgent = false,
        isBookmarked = true,
        createdAt = now.minusDays(5),
        updatedAt = now.minusHours(6),
        views = 234,
        applicationCount = 31,
        safetyLevel = SafetyLevel.HIGH,
        settlementType = SettlementType.WEEKLY
      ),

      // 3. 서울 아파트 리모델링
      Project(
        id = "proj_003",
        title = "강남구 대치동 아파트 리모델링",
        description = "30년된 아파트 단지의 전면 리모델링 작업입니다. 외벽, 내부 시설 교체가 포함됩니다.",
        location = "서울 강남구",
        detailAddress = "서울 강남구 대치동 123-45",
        workType = WorkType.HOUSE_RENOVATION,
        status = ProjectStatus.RECRUITING,
        startDate = "01/10",
        endDate = "03/15",
        fullStartDate = today.plusDays(7),
        fullEndDate = today.plusDays(72),
        dailyWage = 420_000L,
        requiredWorkers = 12,
        currentWorkers = 5,
        workingHours = WorkingHours("09:00", "18:00", 60),
        companyName = "서울인테리어",
        companyId = "company_003",
        contactPerson = "박소장",
        contactNumber = "010-3456-7890",
        requirements = listOf("인테리어 경력", "도구 사용 가능", "꼼꼼한 성격"),
        benefits = listOf("기술교육 제공", "중식비 지원", "교통비 지원"),
        isUrgent = false,
        isBookmarked = false,
        createdAt = now.minusDays(2),
        updatedAt = now.minusHours(1),
        views = 89,
        applicationCount = 12,
        safetyLevel = SafetyLevel.STANDARD,
        settlementType = SettlementType.DAILY
      ),

      // 4. 대구 도로 확장 공사
      Project(
        id = "proj_004",
        title = "대구 순환도로 확장 공사",
        description = "기존 2차선 도로를 4차선으로 확장하는 대규모 도로 공사입니다.",
        location = "대구 달서구",
        detailAddress = "대구 달서구 월성동 일원",
        workType = WorkType.ROAD_CONSTRUCTION,
        status = ProjectStatus.RECRUITING,
        startDate = "01/15",
        endDate = "04/30",
        fullStartDate = today.plusDays(12),
        fullEndDate = today.plusDays(117),
        dailyWage = 450_000L,
        requiredWorkers = 20,
        currentWorkers = 8,
        workingHours = WorkingHours("06:00", "15:00", 60),
        companyName = "대구도로공사",
        companyId = "company_004",
        contactPerson = "최과장",
        contactNumber = "010-4567-8901",
        requirements = listOf("토목 경력 2년 이상", "중장비 자격증", "체력 우수"),
        benefits = listOf("위험수당", "야간수당", "중식 제공", "안전보험"),
        isUrgent = true,
        isBookmarked = false,
        createdAt = now.minusDays(3),
        updatedAt = now.minusHours(4),
        views = 156,
        applicationCount = 19,
        safetyLevel = SafetyLevel.STANDARD,
        settlementType = SettlementType.WEEKLY
      ),

      // 5. 부산 전기 공사
      Project(
        id = "proj_005",
        title = "해운대 마린시티 전기설비 공사",
        description = "신축 상업건물의 전기설비 설치 및 배선 작업입니다.",
        location = "부산 해운대구",
        detailAddress = "부산 해운대구 마린시티2로 33",
        workType = WorkType.ELECTRICAL_WORK,
        status = ProjectStatus.RECRUITING,
        startDate = "01/08",
        endDate = "02/28",
        fullStartDate = today.plusDays(5),
        fullEndDate = today.plusDays(56),
        dailyWage = 380_000L,
        requiredWorkers = 8,
        currentWorkers = 8,
        workingHours = WorkingHours("08:30", "17:30", 60),
        companyName = "부산전기공사",
        companyId = "company_005",
        contactPerson = "정기사",
        contactNumber = "010-5678-9012",
        requirements = listOf("전기기사 자격증", "전기공사 경력", "정밀작업 가능"),
        benefits = listOf("자격수당", "기술교육", "중식 제공"),
        isUrgent = false,
        isBookmarked = true,
        createdAt = now.minusDays(4),
        updatedAt = now.minusHours(8),
        views = 67,
        applicationCount = 15,
        safetyLevel = SafetyLevel.HIGH,
        settlementType = SettlementType.DAILY
      ),

      // 6. 완료된 프로젝트 예시
      Project(
        id = "proj_006",
        title = "울산 공장 증축 공사",
        description = "기존 공장 건물의 증축 및 설비 확장 공사가 성공적으로 완료되었습니다.",
        location = "울산 남구",
        detailAddress = "울산 남구 여천동 산업단지",
        workType = WorkType.BUILDING_CONSTRUCTION,
        status = ProjectStatus.COMPLETED,
        startDate = "11/15",
        endDate = "12/30",
        fullStartDate = today.minusDays(50),
        fullEndDate = today.minusDays(4),
        dailyWage = 460_000L,
        requiredWorkers = 18,
        currentWorkers = 18,
        workingHours = WorkingHours("08:00", "17:00", 60),
        companyName = "울산건설",
        companyId = "company_006",
        contactPerson = "송현장",
        contactNumber = "010-6789-0123",
        requirements = listOf("건설업 경력", "안전교육 이수"),
        benefits = listOf("완료보너스", "4대보험", "중식 제공"),
        isUrgent = false,
        isBookmarked = false,
        createdAt = now.minusDays(55),
        updatedAt = now.minusDays(4),
        views = 312,
        applicationCount = 28,
        safetyLevel = SafetyLevel.STANDARD,
        settlementType = SettlementType.PROJECT_END
      )
    )
  }

  /**
   * 프로젝트 요약 정보 생성
   */
  fun getSampleProjectSummary(): ProjectSummary {
    val projects = getSampleProjects()

    return ProjectSummary(
      totalProjects = projects.size,
      recruitingProjects = projects.count { it.status == ProjectStatus.RECRUITING },
      inProgressProjects = projects.count { it.status == ProjectStatus.IN_PROGRESS },
      completedProjects = projects.count { it.status == ProjectStatus.COMPLETED },
      urgentProjects = projects.count { it.isUrgent && it.status == ProjectStatus.RECRUITING },
      totalWorkers = projects.sumOf { it.requiredWorkers },
      averageDailyWage = if (projects.isNotEmpty()) {
        projects.map { it.dailyWage }.average().toLong()
      } else 0L,
      totalApplications = projects.sumOf { it.applicationCount }
    )
  }

  /**
   * 검색 제안어 목록
   */
  fun getSearchSuggestions(): List<String> {
    return listOf(
      "아파트 건설", "도로 공사", "인테리어", "전기 공사",
      "부산", "서울", "인천", "대구", "울산",
      "건축", "토목", "전문공사", "설비",
      "일급 50만원", "긴급 모집", "신축", "리모델링"
    )
  }
}