package com.billcorea.jikgong.network.model.location

import com.google.gson.annotations.SerializedName

/**
 * Location 관련 모델 통합 파일
 * 기존 레거시 파일들(network/)을 그대로 유지
 * 변경사항 없음 - 코드 100% 동일
 */

// ============================================
// AddressFindAddress.kt 그대로
// ============================================
data class AddressFindAddress(
  @SerializedName("address_name")
  var addressName: String,
  @SerializedName("b_code")
  var bCode: String,
  @SerializedName("h_code")
  var hCode: String,
  @SerializedName("main_address_no")
  var mainAddressNo: String,
  @SerializedName("mountain_yn")
  var mountainYn: String,
  @SerializedName("region_1depth_name")
  var region1depthName: String,
  @SerializedName("region_2depth_name")
  var region2depthName: String,
  @SerializedName("region_3depth_h_name")
  var region3depthHName: String,
  @SerializedName("region_3depth_name")
  var region3depthName: String,
  @SerializedName("sub_address_no")
  var subAddressNo: String,
  @SerializedName("x")
  var x: String,
  @SerializedName("y")
  var y: String
)

// ============================================
// AddressFindDocument.kt 그대로
// ============================================
data class AddressFindDocument(
  @SerializedName("address")
  var address: AddressFindAddress,
  @SerializedName("address_name")
  var addressName: String,
  @SerializedName("address_type")
  var addressType: String,
  @SerializedName("road_address")
  var roadAddress: AddressFindRoadAddress,
  @SerializedName("x")
  var x: String,
  @SerializedName("y")
  var y: String
)

// ============================================
// AddressFindMeta.kt 그대로
// ============================================
data class AddressFindMeta(
  @SerializedName("is_end")
  var isEnd: Boolean,
  @SerializedName("pageable_count")
  var pageableCount: Int,
  @SerializedName("total_count")
  var totalCount: Int
)

// ============================================
// AddressFindResponse.kt 그대로
// ============================================
data class AddressFindResponse(
  @SerializedName("documents")
  var documents: List<AddressFindDocument>,
  @SerializedName("meta")
  var meta: AddressFindMeta
)

// ============================================
// AddressFindRoadAddress.kt 그대로
// ============================================
data class AddressFindRoadAddress(
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
  @SerializedName("x")
  var x: String,
  @SerializedName("y")
  var y: String,
  @SerializedName("zone_no")
  var zoneNo: String
)
