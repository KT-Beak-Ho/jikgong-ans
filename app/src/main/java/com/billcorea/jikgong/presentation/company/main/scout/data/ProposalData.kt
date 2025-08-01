
package com.billcorea.jikgong.presentation.company.main.scout.data

import java.time.LocalDateTime

/**
 * 스카웃 제안서 데이터 모델
 */
data class ProposalData(
    val id: String,
    val workerId: String,
    val workerName: String,
    val projectId: String?,
    val projectTitle: String?,
    val message: String,
    val status: ProposalStatus,
    val sentAt: LocalDateTime,
    val respondedAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime,
    val isUrgent: Boolean = false,
    val attachments: List<String> = emptyList(), // 첨부파일 URL 목록
    val responseMessage: String? = null, // 응답 메시지
    val rejectionReason: String? = null, // 거절 사유
    val readAt: LocalDateTime? = null, // 읽은 시간
    val companyInfo: CompanyInfo? = null, // 회사 정보
    val proposalType: ProposalType = ProposalType.INDIVIDUAL // 제안 유형
) {
    /**
     * 제안서 상태 색상
     */
    val statusColor: androidx.compose.ui.graphics.Color
        get() = when (status) {
            ProposalStatus.PENDING -> androidx.compose.ui.graphics.Color(0xFFFFA726)
            ProposalStatus.ACCEPTED -> androidx.compose.ui.graphics.Color(0xFF66BB6A)
            ProposalStatus.REJECTED -> androidx.compose.ui.graphics.Color(0xFFEF5350)
            ProposalStatus.EXPIRED -> androidx.compose.ui.graphics.Color(0xFF9E9E9E)
            ProposalStatus.CANCELLED -> androidx.compose.ui.graphics.Color(0xFFBDBDBD)
        }

    /**
     * 제안서 상태 한글명
     */
    val statusDisplayName: String
        get() = when (status) {
            ProposalStatus.PENDING -> "대기중"
            ProposalStatus.ACCEPTED -> "수락됨"
            ProposalStatus.REJECTED -> "거절됨"
            ProposalStatus.EXPIRED -> "만료됨"
            ProposalStatus.CANCELLED -> "취소됨"
        }

    /**
     * 만료까지 남은 시간 (시간 단위)
     */
    val hoursUntilExpiry: Long
        get() = java.time.Duration.between(LocalDateTime.now(), expiresAt).toHours()

    /**
     * 만료 여부
     */
    val isExpired: Boolean
        get() = LocalDateTime.now().isAfter(expiresAt)

    /**
     * 읽음 여부
     */
    val isRead: Boolean
        get() = readAt != null

    /**
     * 응답 대기 시간 (시간 단위)
     */
    val responseWaitingHours: Long?
        get() = respondedAt?.let {
            java.time.Duration.between(sentAt, it).toHours()
        }
}

/**
 * 제안서 상태
 */
enum class ProposalStatus {
    PENDING,    // 대기중
    ACCEPTED,   // 수락됨
    REJECTED,   // 거절됨
    EXPIRED,    // 만료됨
    CANCELLED   // 취소됨
}

/**
 * 제안 유형
 */
enum class ProposalType(val displayName: String) {
    INDIVIDUAL("개별 제안"),
    BULK("일괄 제안"),
    PROJECT_SPECIFIC("프로젝트 지정"),
    URGENT("긴급 제안")
}

/**
 * 회사 정보
 */
data class CompanyInfo(
    val id: String,
    val name: String,
    val businessNumber: String,
    val address: String,
    val phoneNumber: String,
    val logoUrl: String? = null,
    val rating: Float = 0f,
    val completedProjects: Int = 0,
    val establishedYear: Int? = null,
    val employeeCount: Int? = null,
    val description: String? = null
)
