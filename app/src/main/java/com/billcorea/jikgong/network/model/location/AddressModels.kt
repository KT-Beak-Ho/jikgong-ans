package com.billcorea.jikgong.network.model.location

import com.google.gson.annotations.SerializedName

// AddressFindResponse.kt 내용
data class AddressFindResponse(
  val documents: List<AddressFindDocument>,
  val meta: AddressFindMeta
)

// AddressFindDocument.kt 내용
data class AddressFindDocument(
  val address: AddressFindAddress?,
  @SerializedName("road_address")
  val roadAddress: AddressFindRoadAddress?,
  @SerializedName("address_name")
  val addressName: String,
  @SerializedName("address_type")
  val addressType: String,
  val x: String,
  val y: String
)

// AddressFindAddress.kt 내용
data class AddressFindAddress(
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
  val subAddressNo: String,
  val x: String?,
  val y: String?
)

// AddressFindRoadAddress.kt 내용
data class AddressFindRoadAddress(
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

// AddressFindMeta.kt 내용
data class AddressFindMeta(
  @SerializedName("total_count")
  val totalCount: Int,
  @SerializedName("pageable_count")
  val pageableCount: Int,
  @SerializedName("is_end")
  val isEnd: Boolean
)