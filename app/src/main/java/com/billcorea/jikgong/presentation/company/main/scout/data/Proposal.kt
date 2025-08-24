package com.billcorea.jikgong.presentation.company.main.scout.data

import java.time.LocalDateTime

/**
 * 스카웃 제안 데이터 모델
 */
data class Proposal(
    val id: String,
    val workerId: String,
    val workerName: String,
    val proposedWage: String,           // 제안 임금
    val message: String,                // 스카웃 메시지
    val status: ProposalStatus,         // 제안 상태
    val createdAt: LocalDateTime,       // 제안 일시
    val respondedAt: LocalDateTime? = null, // 응답 일시
    val jobTypes: List<String>,         // 직종
    val distance: Double,               // 거리
    val workerPhone: String? = null,    // 수락 시 노동자 연락처
    val rejectReason: String? = null    // 거절 사유
)

/**
 * 제안 상태
 */
enum class ProposalStatus {
    PENDING,    // 대기중
    ACCEPTED,   // 수락됨
    REJECTED,   // 거절됨
    EXPIRED,    // 만료됨
    CANCELLED   // 취소됨
}

/**
 * 제안 상태별 UI 정보
 */
fun ProposalStatus.toDisplayInfo(): ProposalDisplayInfo {
    return when (this) {
        ProposalStatus.PENDING -> ProposalDisplayInfo(
            text = "대기중",
            color = 0xFFFFA726,
            icon = "⏳"
        )
        ProposalStatus.ACCEPTED -> ProposalDisplayInfo(
            text = "수락됨",
            color = 0xFF66BB6A,
            icon = "✅"
        )
        ProposalStatus.REJECTED -> ProposalDisplayInfo(
            text = "거절됨",
            color = 0xFFEF5350,
            icon = "❌"
        )
        ProposalStatus.EXPIRED -> ProposalDisplayInfo(
            text = "만료됨",
            color = 0xFF9E9E9E,
            icon = "⌛"
        )
        ProposalStatus.CANCELLED -> ProposalDisplayInfo(
            text = "취소됨",
            color = 0xFF757575,
            icon = "🚫"
        )
    }
}

data class ProposalDisplayInfo(
    val text: String,
    val color: Long,
    val icon: String
)