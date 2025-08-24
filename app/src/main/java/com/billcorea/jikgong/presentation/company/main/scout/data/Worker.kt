package com.billcorea.jikgong.presentation.company.main.scout.data

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
    val certificates: List<String> = emptyList()  // 자격증
)

// Spring Boot API 연동용 모델
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