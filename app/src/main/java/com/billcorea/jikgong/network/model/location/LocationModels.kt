// ============================================
// LocationModels.kt - Kakao 지도 API 관련 모델
// ============================================
package com.billcorea.jikgong.network.model.location

import com.google.gson.annotations.SerializedName

// ===== 주소 검색 (Address Search) =====
data class AddressFindResponse(
  @SerializedName("meta")
  val meta: AddressMeta,

  @SerializedName("documents")
  val documents: List<AddressFindRoadAddress>
)

data class AddressMeta(
  @SerializedName("total_count")
  val totalCount: Int,

  @SerializedName("pageable_count")
  val pageableCount: Int,

  @SerializedName("is_end")
  val isEnd: Boolean
)

data class AddressFindRoadAddress(
  @SerializedName("address_name")
  val addressName: String,

  @SerializedName("road_name")
  val roadName: String? = null,

  @SerializedName("building_name")
  val buildingName: String? = null,

  @SerializedName("zone_no")
  val zoneNo: String? = null,

  @SerializedName("x")
  val x: String,

  @SerializedName("y")
  val y: String,

  @SerializedName("address_type")
  val addressType: String? = null,

  @SerializedName("region_1depth_name")
  val region1depthName: String? = null,

  @SerializedName("region_2depth_name")
  val region2depthName: String? = null,

  @SerializedName("region_3depth_name")
  val region3depthName: String? = null
)

// ===== 좌표 -> 주소 변환 (Coord to Address) =====
data class Coord2AddressResponse(
  @SerializedName("meta")
  val meta: Coord2Meta,

  @SerializedName("documents")
  val documents: List<Coord2RoadAddress>
)

data class Coord2Meta(
  @SerializedName("total_count")
  val totalCount: Int
)

data class Coord2RoadAddress(
  @SerializedName("address_name")
  val addressName: String,

  @SerializedName("road_name")
  val roadName: String? = null,

  @SerializedName("building_name")
  val buildingName: String? = null,

  @SerializedName("zone_no")
  val zoneNo: String? = null,

  @SerializedName("x")
  val x: String? = null,

  @SerializedName("y")
  val y: String? = null,

  @SerializedName("region_1depth_name")
  val region1depthName: String? = null,

  @SerializedName("region_2depth_name")
  val region2depthName: String? = null,

  @SerializedName("region_3depth_name")
  val region3depthName: String? = null,

  @SerializedName("address")
  val address: Address? = null,

  @SerializedName("road_address")
  val roadAddress: RoadAddress? = null
)

data class Address(
  @SerializedName("address_name")
  val addressName: String,

  @SerializedName("region_1depth_name")
  val region1depthName: String,

  @SerializedName("region_2depth_name")
  val region2depthName: String,

  @SerializedName("region_3depth_name")
  val region3depthName: String,

  @SerializedName("mountain_yn")
  val mountainYn: String,

  @SerializedName("main_address_no")
  val mainAddressNo: String,

  @SerializedName("sub_address_no")
  val subAddressNo: String
)

data class RoadAddress(
  @SerializedName("address_name")
  val addressName: String,

  @SerializedName("region_1depth_name")
  val region1depthName: String,

  @SerializedName("region_2depth_name")
  val region2depthName: String,

  @SerializedName("region_3depth_name")
  val region3depthName: String,

  @SerializedName("road_name")
  val roadName: String,

  @SerializedName("underground_yn")
  val undergroundYn: String,

  @SerializedName("main_building_no")
  val mainBuildingNo: String,

  @SerializedName("sub_building_no")
  val subBuildingNo: String,

  @SerializedName("building_name")
  val buildingName: String,

  @SerializedName("zone_no")
  val zoneNo: String
)

// ===== 좌표계 변환 =====
data class CoordinateConversionResponse(
  @SerializedName("meta")
  val meta: ConversionMeta,

  @SerializedName("documents")
  val documents: List<ConvertedCoordinate>
)

data class ConversionMeta(
  @SerializedName("total_count")
  val totalCount: Int
)

data class ConvertedCoordinate(
  @SerializedName("x")
  val x: Double,

  @SerializedName("y")
  val y: Double
)

// ===== 키워드 검색 =====
data class KeywordSearchResponse(
  @SerializedName("meta")
  val meta: KeywordMeta,

  @SerializedName("documents")
  val documents: List<Place>
)

data class KeywordMeta(
  @SerializedName("total_count")
  val totalCount: Int,

  @SerializedName("pageable_count")
  val pageableCount: Int,

  @SerializedName("is_end")
  val isEnd: Boolean,

  @SerializedName("same_name")
  val sameName: RegionInfo? = null
)

data class RegionInfo(
  @SerializedName("region")
  val region: List<String>,

  @SerializedName("keyword")
  val keyword: String,

  @SerializedName("selected_region")
  val selectedRegion: String
)

data class Place(
  @SerializedName("id")
  val id: String,

  @SerializedName("place_name")
  val placeName: String,

  @SerializedName("category_name")
  val categoryName: String,

  @SerializedName("category_group_code")
  val categoryGroupCode: String,

  @SerializedName("category_group_name")
  val categoryGroupName: String,

  @SerializedName("phone")
  val phone: String,

  @SerializedName("address_name")
  val addressName: String,

  @SerializedName("road_address_name")
  val roadAddressName: String,

  @SerializedName("x")
  val x: String,

  @SerializedName("y")
  val y: String,

  @SerializedName("place_url")
  val placeUrl: String,

  @SerializedName("distance")
  val distance: String
)

// ===== Location 관련 일반 모델 =====
data class LocationData(
  val latitude: Double,
  val longitude: Double,
  val address: String? = null,
  val detailAddress: String? = null,
  val postalCode: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)