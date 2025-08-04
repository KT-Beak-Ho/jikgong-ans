// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/repository/ProjectRepositoryImpl.kt
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
        isUrgent = true,
        isBookmarked = false
      ),
      Project(
        id = "PRJ002",
        title = "사무실 인테리어 목공 인력 모집",
        company = "현대인테리어",
        location = "서울시 종로구",
        category = "CARPENTER",
        status = "IN_PROGRESS",
        startDate = now.minusDays(5),
        endDate = now.plusDays(10),
        wage = 180000,
        description = "1000평 규모 사무실 인테리어 목공 작업",
        requirements = listOf("목공 경력 1년 이상", "성실한 분"),
        benefits = listOf("중식 제공", "교통비 지원"),
        currentApplicants = 12,
        maxApplicants = 12,
        isUrgent = false,
        isBookmarked = true
      ),
      Project(
        id = "PRJ003",
        title = "빌라 리모델링 타일공 급구",
        company = "삼성건설",
        location = "경기도 성남시",
        category = "TILE_WORKER",
        status = "RECRUITING",
        startDate = now.plusDays(1),
        endDate = now.plusDays(20),
        wage = 220000,
        description = "3층 빌라 전체 타일 교체 작업",
        requirements = listOf("타일 시공 경력 3년 이상", "자격증 보유자 우대"),
        benefits = listOf("4대보험", "중식 제공", "숙소 제공", "성과급"),
        currentApplicants = 3,
        maxApplicants = 8,
        isUrgent = true,
        isBookmarked = false
      ),
      Project(
        id = "PRJ004",
        title = "상가 전기공사 전기기술자 모집",
        company = "한국전기공사",
        location = "인천시 연수구",
        category = "ELECTRICIAN",
        status = "COMPLETED",
        startDate = now.minusDays(30),
        endDate = now.minusDays(5),
        wage = 250000,
        description = "대형 상가 전기 배선 및 조명 설치",
        requirements = listOf("전기기능사 자격증 필수", "경력 5년 이상"),
        benefits = listOf("4대보험", "중식 제공", "교통비 지원"),
        currentApplicants = 10,
        maxApplicants = 10,
        isUrgent = false,
        isBookmarked = false
      )
    )
  }
}