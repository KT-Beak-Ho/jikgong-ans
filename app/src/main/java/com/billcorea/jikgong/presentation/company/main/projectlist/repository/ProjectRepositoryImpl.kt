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
      location = "세종특별자치시",
      category = ProjectCategory.INTERIOR,
      status = ProjectStatus.PAUSED,
      dailyWage = 130000,
      startDate = LocalDateTime.now().plusDays(30),
      endDate = LocalDateTime.now().plusDays(80),
      requiredWorkers = 6,
      appliedWorkers = 3,
      companyName = "롯데건설",
      contactNumber = "010-4567-8901",
      requirements = listOf("인테리어 시공 경험", "목공 기술", "도면 해독 가능"),
      benefits = listOf("정부 프로젝트", "안정적 근무환경", "기술발전 기회"),
      isBookmarked = true,
      createdAt = LocalDateTime.now().minusDays(5),
      updatedAt = LocalDateTime.now().minusDays(1)
    ),
    Project(
      id = "5",
      title = "제주도 풍력발전단지 조경공사",
      description = "제주도 신재생에너지 풍력발전단지 주변 조경 및 녹지조성 작업에 참여할 조경기능사를 모집합니다.",
      location = "제주특별자치도 서귀포시",
      category = ProjectCategory.LANDSCAPING,
      status = ProjectStatus.RECRUITING,
      dailyWage = 120000,
      startDate = LocalDateTime.now().plusDays(14),
      endDate = LocalDateTime.now().plusDays(44),
      requiredWorkers = 4,
      appliedWorkers = 1,
      companyName = "GS건설",
      contactNumber = "010-5678-9012",
      requirements = listOf("조경기능사 자격증", "식물 관리 경험", "차량 운전 가능"),
      benefits = listOf("제주도 근무", "숙박비 지원", "관광 기회", "자연친화적 작업"),
      isBookmarked = false,
      createdAt = LocalDateTime.now().minusDays(2),
      updatedAt = LocalDateTime.now()
    )
  )

  override suspend fun getProjects(): List<Project> {
    delay(500) // 네트워크 지연 시뮬레이션
    return dummyProjects.toList()
  }

  override suspend fun getMoreProjects(offset: Int): List<Project> {
    delay(1000)

    // 추가 더미 데이터 생성
    val moreProjects = (1..3).map { index ->
      Project(
        id = "more_${offset + index}",
        title = "추가 프로젝트 ${offset + index}번",
        description = "추가로 로드된 프로젝트 설명입니다.",
        location = "서울시 강남구",
        category = ProjectCategory.values().random(),
        status = ProjectStatus.values().random(),
        dailyWage = (100000..200000).random(),
        startDate = LocalDateTime.now().plusDays((1..30).random().toLong()),
        endDate = LocalDateTime.now().plusDays((31..90).random().toLong()),
        requiredWorkers = (3..10).random(),
        appliedWorkers = (0..5).random(),
        companyName = "건설회사${index}",
        contactNumber = "010-0000-000${index}",
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