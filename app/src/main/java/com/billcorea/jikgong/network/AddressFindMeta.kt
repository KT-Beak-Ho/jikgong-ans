package com.billcorea.jikgong.network


import com.google.gson.annotations.SerializedName

data class AddressFindMeta(
    @SerializedName("is_end")
    var isEnd: Boolean,
    @SerializedName("pageable_count")
    var pageableCount: Int,
    @SerializedName("total_count")
    var totalCount: Int
)