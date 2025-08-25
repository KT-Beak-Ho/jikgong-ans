package com.billcorea.jikgong.network.model.location

/**
 * 주소 검색 응답
 */
data class AddressFindResponse(
  val status: String,
  val meta: AddressFindMeta,
  val addresses: List<AddressFindAddress>,
  val roadAddresses: List<AddressFindRoadAddress>,
  val errorMessage: String?
)

/**
 * 주소 검색 메타 정보
 */
data class AddressFindMeta(
  val totalCount: Int,
  val page: Int,
  val count: Int
)

/**
 * 지번 주소
 */
data class AddressFindAddress(
  val roadAddress: String,
  val jibunAddress: String,
  val englishAddress: String,
  val addressElements: List<AddressElement>,
  val x: String,
  val y: String,
  val distance: Double
)

/**
 * 도로명 주소
 */
data class AddressFindRoadAddress(
  val roadAddress: String,
  val jibunAddress: String,
  val englishAddress: String,
  val addressElements: List<AddressElement>,
  val x: String,
  val y: String,
  val distance: Double
)

/**
 * 주소 요소
 */
data class AddressElement(
  val types: List<String>,
  val longName: String,
  val shortName: String,
  val code: String
)

/**
 * 주소 문서
 */
data class AddressFindDocument(
  val addressName: String,
  val addressType: String,
  val x: String,
  val y: String,
  val address: AddressFindAddress?,
  val roadAddress: AddressFindRoadAddress?
)

/**
 * 좌표를 주소로 변환 응답
 */
data class Coord2AddressResponse(
  val status: Int,
  val result: List<Coord2Address>
)

/**
 * 좌표 주소
 */
data class Coord2Address(
  val name: String,
  val code: CoordCode,
  val region: CoordRegion
)

/**
 * 좌표 코드
 */
data class CoordCode(
  val id: String,
  val type: String,
  val mappingId: String
)

/**
 * 좌표 지역
 */
data class CoordRegion(
  val area0: CoordArea,
  val area1: CoordArea,
  val area2: CoordArea,
  val area3: CoordArea,
  val area4: CoordArea
)

/**
 * 좌표 지역 상세
 */
data class CoordArea(
  val name: String,
  val coords: CoordCenter,
  val alias: String? = null
)

/**
 * 좌표 중심점
 */
data class CoordCenter(
  val center: CoordPoint
)

/**
 * 좌표 포인트
 */
data class CoordPoint(
  val crs: String,
  val x: Double,
  val y: Double
)

/**
 * 좌표 메타 정보
 */
data class Coord2Meta(
  val totalCount: Int,
  val page: Int,
  val count: Int
)

/**
 * 좌표 문서
 */
data class Coord2Document(
  val regionType: String,
  val addressName: String,
  val region1depthName: String,
  val region2depthName: String,
  val region3depthName: String,
  val region4depthName: String,
  val code: String,
  val x: Double,
  val y: Double
)

/**
 * 좌표 도로명 주소
 */
data class Coord2RoadAddress(
  val addressName: String,
  val region1depthName: String,
  val region2depthName: String,
  val region3depthName: String,
  val roadName: String,
  val undergroundYn: String,
  val mainBuildingNo: String,
  val subBuildingNo: String,
  val buildingName: String,
  val zoneNo: String
)