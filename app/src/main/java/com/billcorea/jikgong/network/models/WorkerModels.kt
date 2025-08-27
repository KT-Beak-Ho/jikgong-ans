package com.billcorea.jikgong.network.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// ==================== 작업자 정보 ====================

data class Worker(
    val id: String,
    val name: String,
    val jobTypes: List<String>,     // 직종 (철근공, 목수 등)
    val experience: Int,            // 경력 (년)
    val distance: Double,           // 거리 (km)
    val rating: Float,              // 평점
    val introduction: String? = null,   // 자기소개
    val desiredWage: String? = null,    // 희망 일당
    val isAvailable: Boolean = true,    // 현재 일 가능 여부
    val completedProjects: Int = 0,     // 완료한 프로젝트 수
    val profileImage: String? = null,   // 프로필 이미지
    val certificates: List<String> = emptyList(),  // 자격증
    val phone: String? = null,          // 연락처
    val hourlyWage: Int? = null,        // 시급
    val dailyWage: String? = null,      // 일당 표시
    val location: String? = null        // 위치
)

// ==================== 제안 관련 ====================

data class Proposal(
    val id: String,
    val workerId: String,
    val workerName: String,
    val proposedWage: String,
    val message: String,
    val status: ProposalStatus,
    val createdAt: LocalDateTime,
    val respondedAt: LocalDateTime? = null,
    val jobTypes: List<String>,
    val distance: String,
    val workerPhone: String? = null,      // 수락 시 연락처
    val rejectReason: String? = null      // 거절 사유
) {
    fun toDisplayInfo(): String {
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")
        return createdAt.format(formatter)
    }
}

enum class ProposalStatus {
    PENDING,   // 대기중
    ACCEPTED,  // 수락됨
    REJECTED   // 거절됨
}

// ==================== API 연동용 모델 ====================

data class WorkerResponse(
    val workers: List<Worker>,
    val totalCount: Int,
    val hasNext: Boolean
)

data class WorkerDetailResponse(
    val worker: Worker,
    val recentWorks: List<WorkHistory>,
    val reviews: List<WorkerReview>
)

data class WorkHistory(
    val id: String,
    val companyName: String,
    val workType: String,
    val period: String,
    val location: String
)

data class WorkerReview(
    val id: String,
    val companyName: String,
    val rating: Double,
    val comment: String,
    val createdAt: String
)

data class ProposalRequest(
    val workerId: String,
    val proposedWage: Int,
    val message: String,
    val workDate: String? = null,
    val workLocation: String? = null
)

data class ProposalResponse(
    val success: Boolean,
    val proposalId: String?,
    val message: String
)