package com.billcorea.jikgong.network.model.location

import com.google.gson.annotations.SerializedName

// Coord2AddressResponse.kt 내용
data class Coord2AddressResponse(
  val meta: Coord2Meta,
  val documents: List<Coord2Document>
)

// Coord2Meta.kt 내용
data class Coord2Meta(
  @SerializedName("total_count")
  val totalCount: Int
)

// Coord2Document.kt 내용
data class Coord2Document(
  val address: Coord2Address?,
  @SerializedName("road_address")
  val roadAddress: Coord2RoadAddress?
)

// Coord2Address.kt 내용
data class Coord2Address(
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

// Coord2RoadAddress.kt 내용
data class Coord2RoadAddress(
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