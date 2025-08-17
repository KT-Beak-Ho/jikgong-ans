package com.billcorea.jikgong.presentation.company.main.scout.data

import java.time.LocalDateTime

/**
 * 스카웃 샘플 데이터
 */
object ScoutSampleData {

    fun getSampleWorkers(): List<WorkerData> {
        return listOf(
            WorkerData(
                id = "worker1",
                name = "김철수",
                experience = 5,
                location = "서울 강남구",
                distance = 2.5,
                rating = 4.8f,
                skills = listOf("보통인부", "철근공", "콘크리트공"),
                isOnline = true,
                isFavorite = false,
                isNewWorker = false,
                phone = "010-1234-5678",
                description = "성실하고 책임감 있는 작업자입니다.",
                completedProjects = 25,
                responseRate = 0.95f,
                averageResponseTime = 15,
                certifications = listOf("건설기능사"),
                workingAreas = listOf("서울 전체", "경기 일부"),
                isVerified = true,
                joinedDate = LocalDateTime.now().minusMonths(12)
            ),
            WorkerData(
                id = "worker2",
                name = "이영희",
                experience = 3,
                location = "서울 서초구",
                distance = 3.2,
                rating = 4.6f,
                skills = listOf("콘크리트공", "타일공", "배관공"),
                isOnline = false,
                isFavorite = true,
                isNewWorker = false,
                phone = "010-2345-6789",
                lastActiveAt = LocalDateTime.now().minusHours(2),
                description = "전문성과 신속함을 겸비한 작업자입니다.",
                completedProjects = 18,
                responseRate = 0.88f,
                averageResponseTime = 30,
                certifications = listOf("타일기능사", "배관기능사"),
                workingAreas = listOf("서울 강남", "서울 서초", "서울 송파"),
                isVerified = true,
                joinedDate = LocalDateTime.now().minusMonths(8)
            ),
            WorkerData(
                id = "worker3",
                name = "박민수",
                experience = 8,
                location = "서울 송파구",
                distance = 5.1,
                rating = 4.9f,
                skills = listOf("용접공", "전기공", "철근공"),
                isOnline = true,
                isFavorite = false,
                isNewWorker = false,
                phone = "010-3456-7890",
                description = "8년 경력의 숙련된 용접 전문가입니다.",
                completedProjects = 45,
                responseRate = 0.98f,
                averageResponseTime = 8,
                certifications = listOf("용접기능사", "전기기능사"),
                workingAreas = listOf("서울 전체", "경기 성남", "경기 용인"),
                isVerified = true,
                joinedDate = LocalDateTime.now().minusYears(2)
            ),
            WorkerData(
                id = "worker4",
                name = "정수진",
                experience = 1,
                location = "서울 마포구",
                distance = 8.3,
                rating = 4.2f,
                skills = listOf("보통인부", "도장공"),
                isOnline = false,
                isFavorite = false,
                isNewWorker = true,
                phone = "010-4567-8901",
                lastActiveAt = LocalDateTime.now().minusDays(1),
                description = "열정적이고 배우려는 의지가 강한 신입 작업자입니다.",
                completedProjects = 3,
                responseRate = 0.75f,
                averageResponseTime = 60,
                certifications = emptyList(),
                workingAreas = listOf("서울 서부"),
                isVerified = false,
                joinedDate = LocalDateTime.now().minusWeeks(2)
            ),
            WorkerData(
                id = "worker5",
                name = "최영호",
                experience = 12,
                location = "서울 영등포구",
                distance = 7.8,
                rating = 4.7f,
                skills = listOf("작업반장", "철근공", "콘크리트공", "용접공"),
                isOnline = true,
                isFavorite = true,
                isNewWorker = false,
                phone = "010-5678-9012",
                description = "현장 관리 경험이 풍부한 베테랑 작업자입니다.",
                completedProjects = 78,
                responseRate = 0.93f,
                averageResponseTime = 12,
                certifications = listOf("건설기능사", "용접기능사", "안전관리자"),
                workingAreas = listOf("서울 전체", "경기 전체"),
                isVerified = true,
                joinedDate = LocalDateTime.now().minusYears(3)
            )
        )
    }

    fun getSampleProposals(): List<ProposalData> {
        return listOf(
            ProposalData(
                id = "proposal1",
                workerId = "worker1",
                workerName = "김철수",
                projectId = "project1",
                projectTitle = "강남구 아파트 신축공사",
                message = "안녕하세요. 저희 프로젝트에 참여해주시면 감사하겠습니다.",
                status = ProposalStatus.PENDING,
                sentAt = LocalDateTime.now().minusHours(2),
                expiresAt = LocalDateTime.now().plusDays(3),
                isUrgent = false,
                proposalType = ProposalType.PROJECT_SPECIFIC
            ),
            ProposalData(
                id = "proposal2",
                workerId = "worker2",
                workerName = "이영희",
                projectId = null,
                projectTitle = null,
                message = "향후 프로젝트 협업을 위해 연락드립니다.",
                status = ProposalStatus.ACCEPTED,
                sentAt = LocalDateTime.now().minusDays(1),
                respondedAt = LocalDateTime.now().minusHours(6),
                expiresAt = LocalDateTime.now().plusDays(6),
                responseMessage = "참여하겠습니다. 언제든 연락주세요.",
                proposalType = ProposalType.INDIVIDUAL
            ),
            ProposalData(
                id = "proposal3",
                workerId = "worker3",
                workerName = "박민수",
                projectId = "project2",
                projectTitle = "인천 물류센터 건설",
                message = "긴급 프로젝트입니다. 빠른 참여 부탁드립니다.",
                status = ProposalStatus.REJECTED,
                sentAt = LocalDateTime.now().minusDays(2),
                respondedAt = LocalDateTime.now().minusDays(1),
                expiresAt = LocalDateTime.now().plusDays(5),
                responseMessage = "죄송하지만 다른 프로젝트로 인해 참여가 어렵습니다.",
                rejectionReason = "일정 중복",
                isUrgent = true,
                proposalType = ProposalType.URGENT
            )
        )
    }

    fun getSearchFiltersPresets(): List<Pair<String, SearchFilters>> {
        return listOf(
            "전체" to SearchFilters(),
            "근거리 숙련공" to SearchFilters(
                radius = 5,
                experience = "3년+",
                minRating = 4.0f,
                isOnlineOnly = true
            ),
            "철근공 전문가" to SearchFilters(
                jobType = "철근공",
                experience = "5년+",
                minRating = 4.5f,
                isVerifiedOnly = true
            ),
            "신규 인력" to SearchFilters(
                experience = "신입",
                isNewWorkerOnly = true,
                radius = 15
            ),
            "고평점 작업자" to SearchFilters(
                minRating = 4.5f,
                minResponseRate = 0.9f,
                isVerifiedOnly = true
            )
        )
    }
}