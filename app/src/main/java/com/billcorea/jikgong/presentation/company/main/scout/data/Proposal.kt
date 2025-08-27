package com.billcorea.jikgong.presentation.company.main.scout.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

// Spring Boot API 연동용 Request/Response 모델
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