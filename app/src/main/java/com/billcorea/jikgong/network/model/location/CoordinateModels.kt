package com.billcorea.jikgong.network.model.location

import com.google.gson.annotations.SerializedName

/**
 * 좌표-주소 변환(Coordinate to Address) 관련 모델
 * Kakao 좌표→주소 변환 API 응답 모델들
 */

// ============================================
// 좌표→주소 변환 응답
// ============================================
data class Coord2AddressResponse(
  @SerializedName("documents")
  var documents: List<Coord2Document>,
  @SerializedName("meta")
  var meta: Coord2Meta
)

// ============================================
// 좌표 변환 문서
// ============================================
data class Coord2Document(
  @SerializedName("address")
  var address: Coord2Address,
  @SerializedName("road_address")
  var roadAddress: Coord2RoadAddress
)

// ============================================
// 좌표 변환 지번 주소
// ============================================
data class Coord2Address(
  @SerializedName("address_name")
  var addressName: String,
  @SerializedName("main_address_no")
  var mainAddressNo: String,
  @SerializedName("mountain_yn")
  var mountainYn: String,
  @SerializedName("region_1depth_name")
  var region1depthName: String,
  @SerializedName("region_2depth_name")
  var region2depthName: String,
  @SerializedName("region_3depth_name")
  var region3depthName: String,
  @SerializedName("sub_address_no")
  var subAddressNo: String,
  @SerializedName("zip_code")
  var zipCode: String
)

// ============================================
// 좌표 변환 도로명 주소
// ============================================
data class Coord2RoadAddress(
  @SerializedName("address_name")
  var addressName: String,
  @SerializedName("building_name")
  var buildingName: String,
  @SerializedName("main_building_no")
  var mainBuildingNo: String,
  @SerializedName("region_1depth_name")
  var region1depthName: String,
  @SerializedName("region_2depth_name")
  var region2depthName: String,
  @SerializedName("region_3depth_name")
  var region3depthName: String,
  @SerializedName("road_name")
  var roadName: String,
  @SerializedName("sub_building_no")
  var subBuildingNo: String,
  @SerializedName("underground_yn")
  var undergroundYn: String,
  @SerializedName("zone_no")
  var zoneNo: String
)

// ============================================
// 좌표 변환 메타 정보
// ============================================
data class Coord2Meta(
  @SerializedName("total_count")
  var totalCount: Int
)