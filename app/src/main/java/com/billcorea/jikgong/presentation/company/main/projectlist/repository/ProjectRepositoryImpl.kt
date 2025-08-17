package com.billcorea.jikgong.presentation.company.main.projectlist.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class ProjectRepositoryImpl : ProjectRepository {

  // 임시 데이터 저장소 (실제로는 Room DB나 Remote API 사용)
  private val projects = mutableListOf(
    Project(
      id = "1",
      title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
      company = "대림건설(주)",
      location = "부산광역시 사하구",
      category = "철근공",
      status = "RECRUITING",
      startDate = LocalDateTime.now().plusDays(3),
      endDate = LocalDateTime.now().plusMonths(2),
      wage = 200000,
      description = "낙동강 온도 측정 센터 신축 현장에서 함께 일하실 철근공을 모집합니다.",
      requirements = listOf("경력 3년 이상", "안전교육 이수자"),
      benefits = listOf("중식 제공", "안전장비 지급"),
      currentApplicants = 8,
      maxApplicants = 15,
      isUrgent = true
    ),
    Project(
      id = "2",
      title = "사무실 인테리어 목공 인력 모집",
      company = "현대인테리어",
      location = "서울시 종로구",
      category = "목공",
      status = "IN_PROGRESS",
      startDate = LocalDateTime.now().minusDays(5),
      endDate = LocalDateTime.now().plusDays(20),
      wage = 180000,
      currentApplicants = 12,
      maxApplicants = 12,
      isBookmarked = true
    )
  )

  override suspend fun getProjects(): List<Project> {
    delay(500) // 네트워크 시뮬레이션
    return projects.toList()
  }

  override suspend fun getProjectById(projectId: String): Project? {
    delay(300)
    return projects.find { it.id == projectId }
  }

  override suspend fun toggleBookmark(projectId: String): Boolean {
    delay(200)
    val index = projects.indexOfFirst { it.id == projectId }
    if (index != -1) {
      projects[index] = projects[index].copy(
        isBookmarked = !projects[index].isBookmarked
      )
      return true
    }
    return false
  }

  override suspend fun createProject(project: Project): Boolean {
    delay(500)
    val newProject = project.copy(
      id = System.currentTimeMillis().toString()
    )
    projects.add(newProject)
    return true
  }

  override suspend fun updateProject(project: Project): Boolean {
    delay(300)
    val index = projects.indexOfFirst { it.id == project.id }
    if (index != -1) {
      projects[index] = project.copy(updatedAt = LocalDateTime.now())
      return true
    }
    return false
  }

  override suspend fun deleteProject(projectId: String): Boolean {
    delay(300)
    return projects.removeIf { it.id == projectId }
  }
}
