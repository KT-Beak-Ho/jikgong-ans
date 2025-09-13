package com.billcorea.jikgong.api.models.auth

import com.google.gson.annotations.SerializedName

/**
 * 사업자 회원가입 응답 모델
 */
data class CompanyRegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("companyId")
    val companyId: Long? = null,
    
    @SerializedName("accessToken")
    val accessToken: String? = null,
    
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    
    @SerializedName("companyInfo")
    val companyInfo: CompanyBasicInfo? = null
)

data class CompanyBasicInfo(
    @SerializedName("companyId")
    val companyId: Long,
    
    @SerializedName("companyName")
    val companyName: String,
    
    @SerializedName("representativeName")
    val representativeName: String,
    
    @SerializedName("businessNumber")
    val businessNumber: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String
)