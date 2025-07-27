package com.billcorea.jikgong.presentation.company.main.projectlist.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

// 프로젝트 상태
enum class ProjectStatus {
    RECRUITING,   // 모집중
    IN_PROGRESS,  // 진행중
    COMPLETED,    // 완료
    CANCELLED     // 취소
}

// 작업 타입
enum class JobType {
    GENERAL_WORKER,    // 보통인부
    SKILLED_WORKER,    // 기능공
    SPECIALIST,        // 전문기술자
    SUPERVISOR,        // 작업반장
    DRIVER,           // 운전기사
    CRANE_OPERATOR,   // 크레인 기사
    EXCAVATOR_OPERATOR // 굴삭기 기사
}

// 프로젝트 데이터
data class ProjectData(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val detailAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distance: Double = 0.0, // km 단위

    // 작업 정보
    val jobTypes: List<JobType>,
    val totalWorkers: Int,
    val completedWorkers: Int = 0,
    val dailyWage: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val startTime: String,
    val endTime: String,

    // 프로젝트 상태
    val status: ProjectStatus = ProjectStatus.RECRUITING,
    val isUrgent: Boolean = false,
    val priority: Int = 0, // 0: 일반, 1: 중요, 2: 매우중요

    // 추가 정보
    val requirements: List<String> = emptyList(),
    val providedItems: List<String> = emptyList(), // 제공 사항 (점심, 교통비 등)
    val notes: String = "",

    // 메타데이터
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val createdBy: String = "",
    val companyId: String = ""
) {
    // 진행률 계산
    val progressPercentage: Float
        get() = if (totalWorkers > 0) {
            (completedWorkers.toFloat() / totalWorkers.toFloat()) * 100f
        } else 0f

    // 상태별 색상
    val statusColor: Color
        get() = when (status) {
            ProjectStatus.RECRUITING -> Color(0xFFFFA726)
            ProjectStatus.IN_PROGRESS -> Color(0xFF42A5F5)
            ProjectStatus.COMPLETED -> Color(0xFF66BB6A)
            ProjectStatus.CANCELLED -> Color(0xFFEF5350)
        }

    // 작업 시간 표시
    val workTimeDisplay: String
        get() = "$startTime - $endTime"

    // 작업 기간 계산 (일 단위)
    val workDurationDays: Long
        get() = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1

    // 모집 남은 인원
    val remainingWorkers: Int
        get() = (totalWorkers - completedWorkers).coerceAtLeast(0)

    // 모집 완료 여부
    val isRecruitmentCompleted: Boolean
        get() = completedWorkers >= totalWorkers

    // 긴급 모집 여부 (마감일 3일 이내)
    val isUrgentRecruitment: Boolean
        get() = isUrgent || java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), startDate) <= 3

    // 주요 직종 표시 (최대 2개)
    val mainJobTypesDisplay: String
        get() = jobTypes.take(2).joinToString(", ") { it.displayName } +
                if (jobTypes.size > 2) " 외 ${jobTypes.size - 2}개" else ""
}

// 직종별 한글명
val JobType.displayName: String
    get() = when (this) {
        JobType.GENERAL_WORKER -> "보통인부"
        JobType.SKILLED_WORKER -> "기능공"
        JobType.SPECIALIST -> "전문기술자"
        JobType.SUPERVISOR -> "작업반장"
        JobType.DRIVER -> "운전기사"
        JobType.CRANE_OPERATOR -> "크레인기사"
        JobType.EXCAVATOR_OPERATOR -> "굴삭기기사"
    }

// 프로젝트 필터
data class ProjectFilter(
    val status: ProjectStatus? = null,
    val jobTypes: List<JobType> = emptyList(),
    val location: String? = null,
    val minWage: Int? = null,
    val maxWage: Int? = null,
    val dateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val isUrgentOnly: Boolean = false,
    val maxDistance: Double? = null
)

// 프로젝트 정렬 옵션
enum class ProjectSortBy {
    CREATED_DATE,     // 등록일순
    START_DATE,       // 시작일순
    WAGE,            // 임금순
    DISTANCE,        // 거리순
    PRIORITY,        // 우선순위순
    STATUS           // 상태순
}