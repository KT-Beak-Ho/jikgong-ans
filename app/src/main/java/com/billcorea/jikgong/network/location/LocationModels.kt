package com.billcorea.jikgong.network.location

import com.google.gson.annotations.SerializedName

// ==================== 주소 검색 관련 ====================

data class AddressFindResponse(
    @SerializedName("documents")
    var documents: List<AddressFindDocument>,
    @SerializedName("meta")
    var meta: AddressFindMeta
)

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

data class AddressFindMeta(
    @SerializedName("is_end")
    var isEnd: Boolean,
    @SerializedName("pageable_count")
    var pageableCount: Int,
    @SerializedName("total_count")
    var totalCount: Int
)

// ==================== 좌표 → 주소 변환 관련 ====================

data class Coord2AddressResponse(
    @SerializedName("documents")
    var documents: List<Coord2Document>,
    @SerializedName("meta")
    var meta: Coord2Meta
)

data class Coord2Document(
    @SerializedName("address")
    var address: Coord2Address,
    @SerializedName("road_address")
    var roadAddress: Coord2RoadAddress
)

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

data class Coord2Meta(
    @SerializedName("total_count")
    var totalCount: Int
)