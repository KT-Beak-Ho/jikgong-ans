package com.billcorea.jikgong.presentation.company.main.scout.data

import java.time.LocalDateTime

/**
 * 노동자 정보 데이터 모델
 */
data class Worker(
    val id: String,
    val name: String,
    val profileImageUrl: String? = null,
    val jobTypes: List<String>,        // 가능 직종 (철근공, 형틀목공 등)
    val experience: Int,                // 경력 (년)
    val distance: Double,               // 거리 (km)
    val rating: Float,                  // 평점 (1-5)
    val introduction: String? = null,   // 자기소개
    val desiredWage: String? = null,    // 희망 일당
    val isAvailable: Boolean = true,    // 현재 일 가능 여부
    val lastActiveAt: LocalDateTime? = null,
    val certifications: List<String> = emptyList(), // 보유 자격증
    val completedProjects: Int = 0,     // 완료한 프로젝트 수
    val phoneNumber: String? = null,    // 전화번호 (스카웃 수락 시에만 공개)
    val workArea: String? = null        // 선호 작업 지역
)