package com.billcorea.jikgong.api.models.auth

import com.google.gson.annotations.SerializedName

/**
 * 사업자 회원가입 요청 모델
 */
data class CompanyRegisterRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    
    @SerializedName("verificationCode")
    val verificationCode: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("loginId")
    val loginId: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("businessNumber")
    val businessNumber: String,
    
    @SerializedName("companyName")
    val companyName: String,
    
    @SerializedName("inquiry")
    val inquiry: String? = null,
    
    // 서버에서 기본값 설정
    @SerializedName("businessType")
    val businessType: String? = null,
    
    @SerializedName("businessAddress")
    val businessAddress: String? = null,
    
    @SerializedName("hasInsurance")
    val hasInsurance: Boolean = false,
    
    @SerializedName("termsAgree")
    val termsAgree: Boolean = true,
    
    @SerializedName("privacyAgree")
    val privacyAgree: Boolean = true,
    
    @SerializedName("marketingAgree")
    val marketingAgree: Boolean = false,
    
    @SerializedName("deviceToken")
    val deviceToken: String? = null
)