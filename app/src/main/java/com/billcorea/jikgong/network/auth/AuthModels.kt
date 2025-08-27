package com.billcorea.jikgong.network.auth

import java.util.Dictionary

// ==================== 로그인 관련 ====================

data class LoginRequest(
    val loginIdOrPhone: String,
    val password: String,
    val deviceToken: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val userRole: String,
    val userId: Long
)

data class LoginErrorData(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

// ==================== 회원가입 관련 ====================

data class RegisterWorker(
    var loginId: String,
    var password: String,
    var phone: String,
    var email: String,
    var role: String,
    var privacyConsent: Boolean,
    var deviceToken: String,
    var isNotification: Boolean,
    var workerName: String,
    var birth: String,
    var gender: String,
    var nationality: String,
    var accountHolder: String,
    var account: String,
    var bank: String,
    var workerCardNumber: String,
    var hasVisa: Boolean,
    var credentialLiabilityConsent: Boolean,
    val workExperienceRequest: List<WorkExperience>,
    var address: String,
    var latitude: Double,
    var longitude: Double
)

data class WorkExperience(
    var tech: String,
    var experienceMonths: Int
)

data class RegisterWorkerResponse(
    val success: Boolean,
    val message: String,
    val data: Any?
)

data class RegisterWorkerErrorResponse(
    val success: Boolean,
    val message: String,
    val errors: Map<String, String>
)

// ==================== SMS 인증 관련 ====================

data class SendSMSRequest(
    val phone: String
)

data class SendSMSResponse(
    val success: Boolean,
    val message: String,
    val verificationCode: String? = null
)

data class VerifySMSRequest(
    val phone: String,
    val verificationCode: String
)

data class VerifySMSResponse(
    val success: Boolean,
    val message: String,
    val isVerified: Boolean
)

data class SmsVerificationRequest(
    val phone: String,
    val verificationCode: String
)

data class SmsVerificationResponse(
    val success: Boolean,
    val message: String,
    val isVerified: Boolean
)

data class SmsVerificationData(
    val phone: String,
    val code: String,
    val expiresAt: Long,
    val isVerified: Boolean = false
)

// ==================== 전화번호 검증 관련 ====================

data class PhoneValidationRequest(
    val phone: String
)

data class PhoneValidationResponse(
    val success: Boolean,
    val message: String,
    val isAvailable: Boolean
)

// ==================== 로그인 ID 검증 관련 ====================

data class LoginIdValidationRequest(
    val loginId: String
)

data class LoginIdValidationResponse(
    val success: Boolean,
    val message: String,
    val isAvailable: Boolean
)

// ==================== 비자 만료일 관련 ====================

data class VisaExpiryDateRequest(
    val userId: Long,
    val expiryDate: String
)