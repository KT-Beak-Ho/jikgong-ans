package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class Coord2Document(
    @SerializedName("address")
    var address: Coord2Address,
    @SerializedName("road_address")
    var roadAddress: Coord2RoadAddress
)