package com.billcorea.jikgong.presentation.company.main.scout.data

/**
 * 검색 필터 데이터 모델
 */
data class SearchFilters(
    val radius: Int = 10, // 검색 반경 (km)
    val jobType: String = "전체", // 직종
    val experience: String = "상관없음", // 경력
    val location: String = "", // 지역
    val minRating: Float = 0f, // 최소 평점
    val isOnlineOnly: Boolean = false, // 온라인만
    val isNewWorkerOnly: Boolean = false, // 신규 인력만
    val isVerifiedOnly: Boolean = false, // 인증된 인력만
    val minResponseRate: Float = 0f, // 최소 응답률
    val maxResponseTime: Int = 1440, // 최대 응답 시간 (분)
    val skills: List<String> = emptyList(), // 필수 스킬
    val certifications: List<String> = emptyList(), // 필요 자격증
    val sortBy: SortOption = SortOption.RECOMMENDATION, // 정렬 기준
    val sortOrder: SortOrder = SortOrder.DESCENDING // 정렬 순서
) {
    /**
     * 활성화된 필터가 있는지 확인
     */
    val hasActiveFilters: Boolean
        get() = jobType != "전체" ||
                experience != "상관없음" ||
                location.isNotEmpty() ||
                minRating > 0f ||
                isOnlineOnly ||
                isNewWorkerOnly ||
                isVerifiedOnly ||
                minResponseRate > 0f ||
                maxResponseTime < 1440 ||
                skills.isNotEmpty() ||
                certifications.isNotEmpty()

    /**
     * 활성화된 필터 개수
     */
    val activeFiltersCount: Int
        get() {
            var count = 0
            if (jobType != "전체") count++
            if (experience != "상관없음") count++
            if (location.isNotEmpty()) count++
            if (minRating > 0f) count++
            if (isOnlineOnly) count++
            if (isNewWorkerOnly) count++
            if (isVerifiedOnly) count++
            if (minResponseRate > 0f) count++
            if (maxResponseTime < 1440) count++
            if (skills.isNotEmpty()) count++
            if (certifications.isNotEmpty()) count++
            return count
        }

    /**
     * 경력 필터를 숫자로 변환
     */
    val experienceYears: Int?
        get() = when (experience) {
            "신입" -> 0
            "1년+" -> 1
            "3년+" -> 3
            "5년+" -> 5
            "10년+" -> 10
            else -> null
        }

    /**
     * 필터 설명 텍스트 생성
     */
    val filterDescription: String
        get() {
            val descriptions = mutableListOf<String>()

            if (jobType != "전체") descriptions.add(jobType)
            if (experience != "상관없음") descriptions.add("${experience} 경력")
            if (minRating > 0f) descriptions.add("${String.format("%.1f", minRating)}점 이상")
            if (isOnlineOnly) descriptions.add("온라인만")
            if (isNewWorkerOnly) descriptions.add("신규 인력")
            if (isVerifiedOnly) descriptions.add("인증됨")

            return if (descriptions.isEmpty()) {
                "필터 없음"
            } else {
                descriptions.joinToString(", ")
            }
        }
}

/**
 * 정렬 옵션
 */
enum class SortOption(val displayName: String) {
    RECOMMENDATION("추천순"),
    DISTANCE("거리순"),
    RATING("평점순"),
    EXPERIENCE("경력순"),
    RESPONSE_RATE("응답률순"),
    LAST_ACTIVE("최근활동순"),
    JOINED_DATE("가입순")
}

/**
 * 정렬 순서
 */
enum class SortOrder {
    ASCENDING,  // 오름차순
    DESCENDING  // 내림차순
}
