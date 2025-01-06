package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

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