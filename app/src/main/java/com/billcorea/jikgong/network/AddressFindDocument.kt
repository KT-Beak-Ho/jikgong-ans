package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

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