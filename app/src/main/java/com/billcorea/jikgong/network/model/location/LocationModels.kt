package com.billcorea.jikgong.network.model.location

/**
 * 위치 관련 공통 데이터 모델
 * 프로젝트 전반에서 사용하는 위치 데이터 모델들
 */

// ============================================
// 기본 위치 데이터
// ============================================
data class LocationData(
  val latitude: Double,
  val longitude: Double,
  val address: String? = null,
  val detailAddress: String? = null,
  val postalCode: String? = null,
  val timestamp: String? = null
)

// ============================================
// 위치 정보 (상세)
// ============================================
data class LocationInfo(
  val latitude: Double,
  val longitude: Double,
  val address: String,
  val detailAddress: String? = null,
  val postalCode: String? = null,
  val region1depth: String? = null,  // 시/도
  val region2depth: String? = null,  // 구/군
  val region3depth: String? = null,  // 동/읍/면
  val roadAddress: String? = null,
  val buildingName: String? = null
)

// ============================================
// 지역 정보
// ============================================
data class RegionData(
  val code: String,
  val name: String,
  val level: RegionLevel,
  val parentCode: String? = null
)

enum class RegionLevel {
  SIDO,      // 시/도
  SIGUNGU,   // 시/군/구
  DONG       // 동/읍/면
}

// ============================================
// 거리 계산 유틸
// ============================================
data class DistanceInfo(
  val fromLocation: LocationData,
  val toLocation: LocationData,
  val distance: Double,  // 미터 단위
  val duration: Int? = null  // 초 단위
)

// ============================================
// 위치 검색 요청
// ============================================
data class LocationSearchRequest(
  val query: String,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val radius: Int? = null,  // 미터 단위
  val page: Int = 1,
  val size: Int = 10
)

// ============================================
// 위치 검색 결과
// ============================================
data class LocationSearchResult(
  val locations: List<LocationInfo>,
  val totalCount: Int,
  val pageableCount: Int,
  val isEnd: Boolean
)

// ============================================
// 근처 위치 정보
// ============================================
data class NearbyLocation(
  val location: LocationInfo,
  val distance: Double,  // 미터 단위
  val type: LocationType
)

enum class LocationType {
  SUBWAY_STATION,  // 지하철역
  BUS_STOP,       // 버스정류장
  PARKING,        // 주차장
  CONVENIENCE,    // 편의점
  RESTAURANT,     // 음식점
  CAFE,          // 카페
  BANK,          // 은행
  HOSPITAL,      // 병원
  OTHER
}

// ============================================
// 작업 현장 위치
// ============================================
data class WorkSiteLocation(
  val siteId: String,
  val siteName: String,
  val location: LocationInfo,
  val boundary: List<LocationData>? = null,  // 현장 경계
  val checkInRadius: Double = 100.0,  // 출근 체크 가능 반경 (미터)
  val landmarks: List<String> = emptyList()  // 주요 랜드마크
)