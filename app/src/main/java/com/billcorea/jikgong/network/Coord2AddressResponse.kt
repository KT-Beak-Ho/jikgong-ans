package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class Coord2AddressResponse(
    @SerializedName("documents")
    var documents: List<Coord2Document>,
    @SerializedName("meta")
    var meta: Coord2Meta
)