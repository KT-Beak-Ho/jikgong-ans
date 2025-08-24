package com.billcorea.jikgong.presentation.company.main.scout.data

import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.random.Random

class ScoutRepository {

  // Mock 데이터 - 추후 Spring Boot API로 대체
  private val mockWorkers = mutableListOf<Worker>()
  private val mockProposals = mutableListOf<Proposal>()

  init {
    // 샘플 노동자 데이터 생성
    createMockWorkers()
    createMockProposals()
  }

  private fun createMockWorkers() {
    val names = listOf("김철수", "이영희", "박민수", "정수진", "최준호", "장미란", "윤대현", "한소희")
    val jobTypes = listOf("철근공", "목수", "용접공", "조적공", "방수공", "타일공", "도장공", "전기공")
    val experiences = listOf(1, 3, 5, 10, 15)

    repeat(20) { index ->
      mockWorkers.add(
        Worker(
          id = "worker_$index",
          name = names.random(),
          jobTypes = listOf(jobTypes.random(), jobTypes.random()).distinct(),
          experience = experiences.random(),
          distance = Random.nextDouble(0.5, 10.0),
          rating = Random.nextFloat() * 1.5f + 3.5f, // 3.5 ~ 5.0
          introduction = "성실하고 경험이 풍부한 ${jobTypes.random()}입니다.",
          desiredWage = "일당 ${Random.nextInt(15, 30)}만원",
          isAvailable = Random.nextBoolean(),
          completedProjects = Random.nextInt(10, 200),
          profileImage = null,
          certificates = if (Random.nextBoolean())
            listOf("${jobTypes.random()} 기능사", "안전교육 이수증")
          else emptyList()
        )
      )
    }
  }

  private fun createMockProposals() {
    val statuses = ProposalStatus.values()

    repeat(5) { index ->
      val worker = mockWorkers.random()
      mockProposals.add(
        Proposal(
          id = "proposal_$index",
          workerId = worker.id,
          workerName = worker.name,
          proposedWage = "${Random.nextInt(18, 25)}만원",
          message = "함께 일하고 싶습니다. 좋은 조건으로 모시겠습니다.",
          status = statuses.random(),
          createdAt = LocalDateTime.now().minusDays(Random.nextLong(0, 7)),
          respondedAt = if (statuses.random() != ProposalStatus.PENDING)
            LocalDateTime.now().minusDays(Random.nextLong(0, 3))
          else null,
          jobTypes = worker.jobTypes,
          distance = "${worker.distance}km",
          workerPhone = if (statuses.random() == ProposalStatus.ACCEPTED) "010-1234-5678" else null,
          rejectReason = if (statuses.random() == ProposalStatus.REJECTED) "일정이 맞지 않습니다" else null
        )
      )
    }
  }

  suspend fun getNearbyWorkers(): List<Worker> {
    // 네트워크 지연 시뮬레이션
    delay(500)
    return mockWorkers.shuffled()
  }

  suspend fun getMyProposals(): List<Proposal> {
    delay(500)
    return mockProposals.sortedByDescending { it.createdAt }
  }

  suspend fun sendProposal(proposal: Proposal) {
    delay(1000)
    mockProposals.add(0, proposal)
  }

  suspend fun getWorkerDetail(workerId: String): Worker? {
    delay(300)
    return mockWorkers.find { it.id == workerId }
  }

  suspend fun updateProposalStatus(proposalId: String, status: ProposalStatus) {
    delay(500)
    val index = mockProposals.indexOfFirst { it.id == proposalId }
    if (index != -1) {
      mockProposals[index] = mockProposals[index].copy(
        status = status,
        respondedAt = LocalDateTime.now()
      )
    }
  }

  // 추후 Spring Boot API 연동 메서드들
  /*
  suspend fun getNearbyWorkersFromApi(
      latitude: Double,
      longitude: Double,
      radius: Int
  ): List<Worker> {
      // Retrofit을 통한 API 호출
      return apiService.getNearbyWorkers(latitude, longitude, radius)
  }

  suspend fun sendProposalToApi(proposal: ProposalRequest): ProposalResponse {
      return apiService.sendProposal(proposal)
  }
  */
}