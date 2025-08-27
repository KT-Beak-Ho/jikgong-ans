package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

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