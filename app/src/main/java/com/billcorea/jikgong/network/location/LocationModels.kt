package com.billcorea.jikgong.network.location

// ==================== 주소 검색 관련 ====================

data class AddressFindResponse(
    val documents: List<AddressFindDocument>,
    val meta: AddressFindMeta
)

data class AddressFindDocument(
    val address: AddressFindAddress?,
    val road_address: AddressFindRoadAddress?,
    val address_name: String,
    val address_type: String,
    val x: String, // longitude
    val y: String  // latitude
)

data class AddressFindAddress(
    val address_name: String,
    val b_code: String,
    val h_code: String,
    val main_address_no: String,
    val mountain_yn: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_h_name: String,
    val region_3depth_name: String,
    val sub_address_no: String,
    val x: String,
    val y: String
)

data class AddressFindRoadAddress(
    val address_name: String,
    val building_name: String,
    val main_building_no: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val sub_building_no: String,
    val underground_yn: String,
    val x: String,
    val y: String,
    val zone_no: String
)

data class AddressFindMeta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)

// ==================== 좌표 → 주소 변환 관련 ====================

data class Coord2AddressResponse(
    val documents: List<Coord2Document>,
    val meta: Coord2Meta
)

data class Coord2Document(
    val address: Coord2Address?,
    val road_address: Coord2RoadAddress?
)

data class Coord2Address(
    val address_name: String,
    val main_address_no: String,
    val mountain_yn: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_h_name: String,
    val region_3depth_name: String,
    val sub_address_no: String
)

data class Coord2RoadAddress(
    val address_name: String,
    val building_name: String,
    val main_building_no: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val sub_building_no: String,
    val underground_yn: String,
    val zone_no: String
)

data class Coord2Meta(
    val total_count: Int
)