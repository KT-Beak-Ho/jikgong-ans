package com.billcorea.jikgong.presentation.company.main.projectlist.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class ProjectRepositoryImpl : ProjectRepository {

  private val mockProjects = generateMockProjects()

  override suspend fun getProjects(): List<Project> {
    delay(800) // 네트워크 시뮬레이션
    return mockProjects.take(5) // 처음 5개만
  }

  override suspend fun getMoreProjects(currentSize: Int): List<Project> {
    delay(600)
    val remainingProjects = mockProjects.drop(currentSize)
    return remainingProjects.take(3) // 3개씩 추가
  }

  override suspend fun updateBookmark(projectId: String, isBookmarked: Boolean) {
    delay(300)
    // 실제 구현에서는 서버 API 호출
  }

  override suspend fun applyToProject(projectId: String) {
    delay(500)
    // 실제 구현에서는 지원 API 호출
  }

  override suspend fun deleteProject(projectId: String) {
    delay(400)
    // 실제 구현에서는 삭제 API 호출
  }

  override suspend fun createProject(project: Project): Project {
    delay(600)
    return project.copy(
      id = "NEW_${System.currentTimeMillis()}",
      createdAt = LocalDateTime.now(),
      updatedAt = LocalDateTime.now()
    )
  }

  override suspend fun updateProject(project: Project): Project {
    delay(500)
    return project.copy(updatedAt = LocalDateTime.now())
  }

  override suspend fun getProjectById(projectId: String): Project? {
    delay(300)
    return mockProjects.find { it.id == projectId }
  }

  private fun generateMockProjects(): List<Project> {
    val now = LocalDateTime.now()
    return listOf(
      Project(
        id = "PRJ001",
        title = "아파트 신축공사 철근 작업자 모집",
        company = "대한건설(주)",
        location = "서울시 강남구 역삼동",
        category = "REBAR_WORKER",
        status = "RECRUITING",
        startDate = now.plusDays(3),
        endDate = now.plusDays(45),
        wage = 200000,
        description = "35층 아파트 신축공사 철근 조립 및 설치 작업",
        requirements = listOf("철근 작업 경력 2년 이상", "건설기능사 자격증", "안전교육 이수"),
        benefits = listOf("4대보험 완비", "중식 제공", "교통비 지원", "성과급 지급"),
        currentApplicants = 8,
        maxApplicants = 15,
        isBookmarked = false,
        isUrgent = true,
        createdAt = now.minusDays(2),
        updatedAt = now.minusDays(1)
      ),
      Project(
        id = "PRJ002",
        title = "상업시설 전기공사 전기기능사 모집",
        company = "서울전기공사",
        location = "서울시 서초구 서초동",
        category = "ELECTRICIAN",
        status = "RECRUITING",
        startDate = now.plusDays(7),
        endDate = now.plusDays(30),
        wage = 180000,
        description = "대형 쇼핑몰 전기설비 설치 및 배선 작업",
        requirements = listOf("전기기능사 자격증 필수", "고압 작업 경험", "3년 이상 경력"),
        benefits = listOf("주말 특근비 별도", "숙박 제공", "기술교육 지원"),
        currentApplicants = 5,
        maxApplicants = 10,
        isBookmarked = true,
        isUrgent = false,
        createdAt = now.minusDays(1),
        updatedAt = now
      ),
      Project(
        id = "PRJ003",
        title = "도로공사 토목 작업자 급구",
        company = "한국도로공사",
        location = "경기도 성남시 분당구",
        category = "CIVIL_ENGINEER",
        status = "RECRUITING",
        startDate = now.plusDays(1),
        endDate = now.plusDays(60),
        wage = 170000,
        description = "고속도로 확장공사 토목 작업 및 포장 작업",
        requirements = listOf("토목 관련 경력", "중장비 조작 가능", "야간작업 가능자"),
        benefits = listOf("야간수당 추가", "장기근무 인센티브", "건강검진 지원"),
        currentApplicants = 12,
        maxApplicants = 20,
        isBookmarked = false,
        isUrgent = true,
        createdAt = now.minusDays(3),
        updatedAt = now.minusHours(6)
      ),
      Project(
        id = "PRJ004",
        title = "주택 리모델링 목공 전문가 모집",
        company = "리빙스페이스",
        location = "서울시 마포구 홍대앞",
        category = "CARPENTER",
        status = "IN_PROGRESS",
        startDate = now.minusDays(5),
        endDate = now.plusDays(20),
        wage = 220000,
        description = "단독주택 인테리어 목공 작업 및 가구 제작",
        requirements = listOf("목공 전문 기술", "인테리어 경험", "정밀 작업 가능"),
        benefits = listOf("고급 기술 습득 기회", "포트폴리오 제작 지원", "추천서 제공"),
        currentApplicants = 3,
        maxApplicants = 5,
        isBookmarked = true,
        isUrgent = false,
        createdAt = now.minusDays(10),
        updatedAt = now.minusDays(5)
      ),
      Project(
        id = "PRJ005",
        title = "공장 건설 용접 전문 기술자",
        company = "대성산업",
        location = "인천시 남동구 구월동",
        category = "WELDER",
        status = "RECRUITING",
        startDate = now.plusDays(14),
        endDate = now.plusDays(90),
        wage = 250000,
        description = "화학공장 건설 구조물 용접 및 배관 용접 작업",
        requirements = listOf("용접기능사 자격증", "특수용접 경험", "5년 이상 경력"),
        benefits = listOf("최고 수준 급여", "기술 인센티브", "해외 프로젝트 기회"),
        currentApplicants = 2,
        maxApplicants = 8,
        isBookmarked = false,
        isUrgent = false,
        createdAt = now.minusDays(1),
        updatedAt = now.minusHours(12)
      ),
      Project(
        id = "PRJ006",
        title = "아파트 외벽 도장 작업자 모집",
        company = "한솔페인트",
        location = "서울시 송파구 잠실동",
        category = "PAINTER",
        status = "RECRUITING",
        startDate = now.plusDays(5),
        endDate = now.plusDays(25),
        wage = 160000,
        description = "고층 아파트 외벽 도장 및 마감 작업",
        requirements = listOf("고소 작업 가능", "도장 경력 1년 이상"),
        benefits = listOf("안전장비 지급", "기술 교육"),
        currentApplicants = 7,
        maxApplicants = 12,
        isBookmarked = false,
        isUrgent = false,
        createdAt = now.minusDays(4),
        updatedAt = now.minusDays(2)
      ),
      Project(
        id = "PRJ007",
        title = "지하철 연장 공사 일반 작업자",
        company = "서울지하철공사",
        location = "서울시 영등포구 여의도동",
        category = "GENERAL_LABORER",
        status = "RECRUITING",
        startDate = now.plusDays(10),
        endDate = now.plusDays(120),
        wage = 150000,
        description = "지하철 9호선 연장 구간 토목 및 일반 작업",
        requirements = listOf("체력 필수", "야간 작업 가능"),
        benefits = listOf("장기 근무", "교통비 지원"),
        currentApplicants = 15,
        maxApplicants = 30,
        isBookmarked = true,
        isUrgent = false,
        createdAt = now.minusDays(6),
        updatedAt = now.minusDays(3)
      ),
      Project(
        id = "PRJ008",
        title = "오피스텔 배관 설치 전문가",
        company = "동양배관",
        location = "경기도 고양시 일산동구",
        category = "PLUMBER",
        status = "COMPLETED",
        startDate = now.minusDays(20),
        endDate = now.minusDays(5),
        wage = 190000,
        description = "신축 오피스텔 상하수도 배관 설치 작업",
        requirements = listOf("배관 기능사", "3년 이상 경력"),
        benefits = listOf("완료 프로젝트", "우수 평가"),
        currentApplicants = 4,
        maxApplicants = 6,
        isBookmarked = false,
        isUrgent = false,
        createdAt = now.minusDays(25),
        updatedAt = now.minusDays(5)
      )
    )
  }
}