package com.billcorea.jikgong.presentation.company.main.projectlist.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class ProjectRepositoryImpl : ProjectRepository {

  private val mockProjects = generateMockProjects().toMutableList()

  override suspend fun getProjects(): List<Project> {
    delay(500) // 네트워크 지연 시뮬레이션
    return mockProjects.toList()
  }

  override suspend fun getProjectById(projectId: String): Project? {
    delay(300)
    return mockProjects.find { it.id == projectId }
  }

  override suspend fun toggleBookmark(projectId: String): Boolean {
    delay(200)
    val index = mockProjects.indexOfFirst { it.id == projectId }
    return if (index != -1) {
      mockProjects[index] = mockProjects[index].copy(
        isBookmarked = !mockProjects[index].isBookmarked
      )
      true
    } else false
  }

  override suspend fun createProject(project: Project): Boolean {
    delay(500)
    mockProjects.add(0, project)
    return true
  }

  override suspend fun updateProject(project: Project): Boolean {
    delay(400)
    val index = mockProjects.indexOfFirst { it.id == project.id }
    return if (index != -1) {
      mockProjects[index] = project
      true
    } else false
  }

  override suspend fun deleteProject(projectId: String): Boolean {
    delay(300)
    return mockProjects.removeIf { it.id == projectId }
  }

  private fun generateMockProjects(): List<Project> {
    val now = LocalDateTime.now()
    return listOf(
      Project(
        id = "PRJ001",
        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
        company = "대한건설(주)",
        location = "부산 사하구",
        category = "CONSTRUCTION",
        status = "RECRUITING",
        startDate = now.plusDays(3),
        endDate = now.plusDays(45),
        wage = 510000,
        description = "낙동강 온도 측정 센터 신축 공사 진행",
        requirements = listOf("건설 경력 2년 이상", "안전교육 이수자"),
        benefits = listOf("4대보험", "중식 제공", "교통비 지원"),
        currentApplicants = 3,
        maxApplicants = 15,
        isUrgent = true,
        isBookmarked = false
      ),
      Project(
        id = "PRJ002",
        title = "인천 물류센터 건설",
        company = "현대건설",
        location = "인천 연수구",
        category = "CONSTRUCTION",
        status = "IN_PROGRESS",
        startDate = now.minusDays(30),
        endDate = now.plusDays(83),
        wage = 300000,
        description = "대규모 물류센터 건설 프로젝트",
        requirements = listOf("건설 경력 필수", "팀워크 중시"),
        benefits = listOf("4대보험", "중식 제공", "기숙사 제공"),
        currentApplicants = 12,
        maxApplicants = 12,
        isUrgent = false,
        isBookmarked = true
      ),
      Project(
        id = "PRJ003",
        title = "강남구 오피스 리모델링",
        company = "삼성물산",
        location = "서울 강남구",
        category = "INTERIOR",
        status = "RECRUITING",
        startDate = now.plusDays(7),
        endDate = now.plusDays(60),
        wage = 350000,
        description = "고급 오피스 공간 리모델링 작업",
        requirements = listOf("인테리어 경력 3년 이상", "포트폴리오 제출"),
        benefits = listOf("4대보험", "성과급", "프로젝트 보너스"),
        currentApplicants = 5,
        maxApplicants = 10,
        isUrgent = false,
        isBookmarked = false
      ),
      Project(
        id = "PRJ004",
        title = "부산항 신항만 확장공사",
        company = "GS건설",
        location = "부산 강서구",
        category = "CIVIL",
        status = "RECRUITING",
        startDate = now.plusDays(14),
        endDate = now.plusDays(365),
        wage = 450000,
        description = "부산항 신항만 2단계 확장 공사",
        requirements = listOf("항만공사 경력", "중장비 운전 가능자 우대"),
        benefits = listOf("4대보험", "숙식 제공", "장기 프로젝트 보너스"),
        currentApplicants = 20,
        maxApplicants = 50,
        isUrgent = true,
        isBookmarked = false
      ),
      Project(
        id = "PRJ005",
        title = "경기도 데이터센터 건립",
        company = "포스코건설",
        location = "경기 용인시",
        category = "CONSTRUCTION",
        status = "COMPLETED",
        startDate = now.minusDays(120),
        endDate = now.minusDays(5),
        wage = 400000,
        description = "AI 데이터센터 건립 프로젝트",
        requirements = listOf("전기공사 경력", "안전관리 자격증"),
        benefits = listOf("4대보험", "프로젝트 완료 보너스"),
        currentApplicants = 30,
        maxApplicants = 30,
        isUrgent = false,
        isBookmarked = false
      )
    )
  }
}