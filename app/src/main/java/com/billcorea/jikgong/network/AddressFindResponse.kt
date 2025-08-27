package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class AddressFindResponse(
    @SerializedName("documents")
    var documents: List<AddressFindDocument>,
    @SerializedName("meta")
    var meta: AddressFindMeta
)