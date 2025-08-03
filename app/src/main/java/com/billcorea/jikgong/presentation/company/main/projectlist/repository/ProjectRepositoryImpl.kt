package com.billcorea.jikgong.presentation.company.main.projectlist.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.data.*
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class ProjectRepositoryImpl : ProjectRepository {

  // 건설업 특화 더미 데이터
  private val dummyProjects = mutableListOf(
    Project(
      id = "1",
      title = "강남구 신축 아파트 건설 현장 철근공 모집",
      description = "20층 규모의 신축 아파트 건설 현장에서 경험있는 철근공을 모집합니다. 안전한 작업환경과 합리적인 임금을 보장합니다.",
      location = "서울시 강남구 역삼동",
      category = ProjectCategory.BUILDING,
      status = ProjectStatus.RECRUITING,
      dailyWage = 150000,
      startDate = LocalDateTime.now().plusDays(3),
      endDate = LocalDateTime.now().plusDays(33),
      requiredWorkers = 5,
      appliedWorkers = 2,
      companyName = "삼성건설",
      contactNumber = "010-1234-5678",
      requirements = listOf("철근 작업 경험 3년 이상", "안전교육 이수", "건설기능인증 보유"),
      benefits = listOf("식사 제공", "교통비 지급", "4대보험", "휴일 근무수당"),
      isBookmarked = false,
      createdAt = LocalDateTime.now().minusDays(1),
      updatedAt = LocalDateTime.now()
    ),
    Project(
      id = "2",
      title = "인천 국제공항 제3터미널 전기공사",
      description = "인천공항 제3터미널 건설 프로젝트의 전기설비 공사에 참여할 전기기능사를 모집합니다.",
      location = "인천시 중구 공항로",
      category = ProjectCategory.ELECTRICAL,
      status = ProjectStatus.IN_PROGRESS,
      dailyWage = 180000,
      startDate = LocalDateTime.now().minusDays(10),
      endDate = LocalDateTime.now().plusDays(50),
      requiredWorkers = 8,
      appliedWorkers = 6,
      companyName = "현대건설",
      contactNumber = "010-2345-6789",
      requirements = listOf("전기기능사 자격증", "대형 프로젝트 경험", "영어 기초회화 가능"),
      benefits = listOf("월급제 가능", "숙박 제공", "해외연수 기회", "성과급"),
      isBookmarked = true,
      createdAt = LocalDateTime.now().minusDays(15),
      updatedAt = LocalDateTime.now().minusDays(2)
    ),
    Project(
      id = "3",
      title = "부산 해운대 지하철 연장선 토목공사",
      description = "부산 도시철도 해운대선 연장 구간의 터널 굴착 및 토목공사 작업자를 모집합니다.",
      location = "부산시 해운대구",
      category = ProjectCategory.CIVIL_ENGINEERING,
      status = ProjectStatus.RECRUITING,
      dailyWage = 140000,
      startDate = LocalDateTime.now().plusDays(7),
      endDate = LocalDateTime.now().plusDays(120),
      requiredWorkers = 15,
      appliedWorkers = 8,
      companyName = "대림산업",
      contactNumber = "010-3456-7890",
      requirements = listOf("토목 작업 경험", "지하작업 가능", "중장비 조작 가능"),
      benefits = listOf("장기근무 우대", "기술교육 제공", "승진 기회"),
      isBookmarked = false,
      createdAt = LocalDateTime.now().minusDays(3),
      updatedAt = LocalDateTime.now().minusDays(1)
    ),
    Project(
      id = "4",
      title = "세종시 정부청사 내부 인테리어 공사",
      description = "세종시 정부청사 리모델링 프로젝트의 내부 인테리어 시공 전문가를 모집합니다.",
      location = "세종시 정부청사로",
      category = ProjectCategory.INTERIOR,
      status = ProjectStatus.RECRUITING,
      dailyWage = 160000,
      startDate = LocalDateTime.now().plusDays(5),
      endDate = LocalDateTime.now().plusDays(45),
      requiredWorkers = 6,
      appliedWorkers = 3,
      companyName = "인테리어전문",
      contactNumber = "010-4567-8901",
      requirements = listOf("인테리어 경험 2년 이상", "마감재 시공 가능"),
      benefits = listOf("정부 프로젝트", "안정적 급여", "기술교육"),
      isBookmarked = false,
      createdAt = LocalDateTime.now().minusDays(2),
      updatedAt = LocalDateTime.now()
    ),
    Project(
      id = "5",
      title = "대구 산업단지 도로포장 공사",
      description = "대구 달성군 산업단지 내 도로포장 및 배수시설 공사에 참여할 인력을 모집합니다.",
      location = "대구시 달성군",
      category = ProjectCategory.ROAD,
      status = ProjectStatus.RECRUITING,
      dailyWage = 135000,
      startDate = LocalDateTime.now().plusDays(10),
      endDate = LocalDateTime.now().plusDays(60),
      requiredWorkers = 12,
      appliedWorkers = 4,
      companyName = "도로공사",
      contactNumber = "010-5678-9012",
      requirements = listOf("도로공사 경험", "중장비 면허 보유"),
      benefits = listOf("장기 프로젝트", "보너스 지급", "숙박 지원"),
      isBookmarked = false,
      createdAt = LocalDateTime.now().minusDays(4),
      updatedAt = LocalDateTime.now().minusDays(1)
    )
  )

  override suspend fun getProjects(): List<Project> {
    delay(500) // 네트워크 지연 시뮬레이션
    return dummyProjects.toList()
  }

  override suspend fun getMoreProjects(currentSize: Int): List<Project> {
    delay(800)

    // 더미 데이터 추가 생성
    val moreProjects = (1..3).map { index ->
      val projectIndex = currentSize + index
      Project(
        id = "project_$projectIndex",
        title = "추가 프로젝트 $projectIndex - ${listOf("아파트", "상가", "공장", "도로", "교량").random()} 공사",
        description = "더미 프로젝트 설명입니다. 경험있는 작업자를 모집합니다.",
        location = listOf("서울시", "경기도", "인천시", "부산시", "대구시").random() + " " +
          listOf("강남구", "송파구", "중구", "남구", "북구").random(),
        category = ProjectCategory.values().random(),
        status = ProjectStatus.values().random(),
        dailyWage = (100000..200000).random(),
        startDate = LocalDateTime.now().plusDays((1..30).random().toLong()),
        endDate = LocalDateTime.now().plusDays((31..90).random().toLong()),
        requiredWorkers = (3..10).random(),
        appliedWorkers = (0..5).random(),
        companyName = "건설회사${projectIndex}",
        contactNumber = "010-0000-000${projectIndex % 10}",
        requirements = listOf("경험 ${(1..5).random()}년 이상"),
        benefits = listOf("4대보험", "식사제공"),
        isBookmarked = false,
        createdAt = LocalDateTime.now().minusDays((1..10).random().toLong()),
        updatedAt = LocalDateTime.now()
      )
    }

    return moreProjects
  }

  override suspend fun updateBookmark(projectId: String, isBookmarked: Boolean) {
    delay(200)
    val index = dummyProjects.indexOfFirst { it.id == projectId }
    if (index != -1) {
      dummyProjects[index] = dummyProjects[index].copy(isBookmarked = isBookmarked)
    }
  }

  override suspend fun applyToProject(projectId: String) {
    delay(500)
    val index = dummyProjects.indexOfFirst { it.id == projectId }
    if (index != -1) {
      dummyProjects[index] = dummyProjects[index].copy(
        appliedWorkers = dummyProjects[index].appliedWorkers + 1
      )
    }
  }

  override suspend fun deleteProject(projectId: String) {
    delay(300)
    dummyProjects.removeIf { it.id == projectId }
  }

  override suspend fun createProject(project: Project): Project {
    delay(1000)
    val newProject = project.copy(
      id = "new_${System.currentTimeMillis()}",
      createdAt = LocalDateTime.now(),
      updatedAt = LocalDateTime.now()
    )
    dummyProjects.add(0, newProject)
    return newProject
  }

  override suspend fun updateProject(project: Project): Project {
    delay(500)
    val index = dummyProjects.indexOfFirst { it.id == project.id }
    if (index != -1) {
      val updatedProject = project.copy(updatedAt = LocalDateTime.now())
      dummyProjects[index] = updatedProject
      return updatedProject
    }
    throw Exception("프로젝트를 찾을 수 없습니다.")
  }

  override suspend fun getProjectById(projectId: String): Project? {
    delay(300)
    return dummyProjects.find { it.id == projectId }
  }
}