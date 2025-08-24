package com.billcorea.jikgong.presentation.company.main.scout.data

import java.time.LocalDateTime

/**
 * 인력 데이터 모델
 */
data class WorkerData(
    val id: String,
    val name: String,
    val experience: Int, // 경력 (년)
    val location: String,
    val distance: Double, // 거리 (km)
    val rating: Float, // 평점 (0.0 ~ 5.0)
    val skills: List<String>, // 보유 기술
    val isOnline: Boolean, // 온라인 상태
    val isFavorite: Boolean = false, // 즐겨찾기 여부
    val isNewWorker: Boolean = false, // 신규 인력 여부
    val profileImageUrl: String? = null, // 프로필 이미지 URL
    val phone: String? = null, // 전화번호
    val lastActiveAt: LocalDateTime? = null, // 마지막 활동 시간
    val description: String? = null, // 자기소개
    val completedProjects: Int = 0, // 완료한 프로젝트 수
    val responseRate: Float = 0f, // 응답률 (0.0 ~ 1.0)
    val averageResponseTime: Int = 0, // 평균 응답 시간 (분)
    val certifications: List<String> = emptyList(), // 자격증 목록
    val workingAreas: List<String> = emptyList(), // 작업 가능 지역
    val preferredWorkType: List<String> = emptyList(), // 선호 작업 유형
    val isVerified: Boolean = false, // 신원 확인 여부
    val joinedDate: LocalDateTime? = null // 가입 날짜
) {
    /**
     * 경력 수준 계산
     */
    val experienceLevel: String
        get() = when {
            experience == 0 -> "신입"
            experience < 3 -> "초급"
            experience < 7 -> "중급"
            experience < 15 -> "고급"
            else -> "전문가"
        }

    /**
     * 거리 표시 문자열
     */
    val distanceDisplay: String
        get() = when {
            distance < 1.0 -> "${(distance * 1000).toInt()}m"
            distance < 10.0 -> String.format("%.1fkm", distance)
            else -> "${distance.toInt()}km"
        }

    /**
     * 평점 표시 문자열
     */
    val ratingDisplay: String
        get() = String.format("%.1f", rating)

    /**
     * 응답률 표시 문자열
     */
    val responseRateDisplay: String
        get() = "${(responseRate * 100).toInt()}%"

    /**
     * 평균 응답 시간 표시 문자열
     */
    val responseTimeDisplay: String
        get() = when {
            averageResponseTime < 60 -> "${averageResponseTime}분"
            averageResponseTime < 1440 -> "${averageResponseTime / 60}시간"
            else -> "${averageResponseTime / 1440}일"
        }

    /**
     * 신뢰도 점수 계산 (0.0 ~ 1.0)
     */
    val trustScore: Float
        get() {
            var score = 0f

            // 평점 기반 (40%)
            score += (rating / 5f) * 0.4f

            // 응답률 기반 (30%)
            score += responseRate * 0.3f

            // 완료 프로젝트 수 기반 (20%)
            val projectScore = when {
                completedProjects >= 50 -> 1f
                completedProjects >= 20 -> 0.8f
                completedProjects >= 10 -> 0.6f
                completedProjects >= 5 -> 0.4f
                completedProjects >= 1 -> 0.2f
                else -> 0f
            }
            score += projectScore * 0.2f

            // 인증 여부 (10%)
            if (isVerified) score += 0.1f

            return score.coerceIn(0f, 1f)
        }

    /**
     * 추천 점수 계산 (0.0 ~ 1.0)
     */
    fun calculateRecommendationScore(searchFilters: SearchFilters): Float {
        var score = 0f

        // 기본 신뢰도 점수 (50%)
        score += trustScore * 0.5f

        // 거리 점수 (20%)
        val distanceScore = when {
            distance <= 5 -> 1f
            distance <= 10 -> 0.8f
            distance <= 20 -> 0.6f
            distance <= 30 -> 0.4f
            else -> 0.2f
        }
        score += distanceScore * 0.2f

        // 온라인 상태 (15%)
        if (isOnline) score += 0.15f

        // 스킬 매칭 (15%)
        if (searchFilters.jobType != "전체" && skills.any { it.contains(searchFilters.jobType) }) {
            score += 0.15f
        }

        return score.coerceIn(0f, 1f)
    }
}