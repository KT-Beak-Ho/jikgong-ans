package com.billcorea.jikgong.presentation.company.main.info.data

import java.time.LocalDate
import java.time.LocalDateTime

// ==================== 프로필 데이터 모델 ====================

enum class CompanySize {
    STARTUP,        // 스타트업 (1-10명)
    SMALL,         // 중소기업 (11-50명)
    MEDIUM,        // 중견기업 (51-300명)
    LARGE,         // 대기업 (300명+)
    INDIVIDUAL     // 개인사업자
}

enum class BusinessType {
    CONSTRUCTION,      // 건설업
    ARCHITECTURE,      // 건축업
    CIVIL_ENGINEERING, // 토목공사업
    INTERIOR,         // 실내건축업
    ELECTRICAL,       // 전기공사업
    PLUMBING,        // 배관공사업
    PAINTING,        // 도장공사업
    LANDSCAPING,     // 조경공사업
    DEMOLITION,      // 철거공사업
    OTHER           // 기타
}

data class CompanyProfile(
    val id: String,
    val companyName: String,
    val businessRegistrationNumber: String,
    val ceoName: String,
    val establishedDate: LocalDate,
    val companySize: CompanySize,
    val businessType: BusinessType,
    val description: String,
    val website: String? = null,
    val logoUrl: String? = null,
    
    // 연락처 정보
    val phoneNumber: String,
    val faxNumber: String? = null,
    val email: String,
    val emergencyContact: String? = null,
    
    // 주소 정보
    val address: Address,
    val workingAreas: List<String> = emptyList(),
    
    // 자격 및 인증
    val licenses: List<BusinessLicense> = emptyList(),
    val certifications: List<Certification> = emptyList(),
    val insurances: List<Insurance> = emptyList(),
    
    // 운영 정보
    val workingHours: WorkingHours,
    val paymentTerms: PaymentTerms,
    val safetyRating: SafetyRating,
    
    // 앱 설정
    val notificationSettings: NotificationSettings,
    val privacySettings: PrivacySettings,
    
    // 메타데이터
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val lastActiveAt: LocalDateTime
) {
    val companySizeDisplayName: String
        get() = when (companySize) {
            CompanySize.STARTUP -> "스타트업 (1-10명)"
            CompanySize.SMALL -> "중소기업 (11-50명)"
            CompanySize.MEDIUM -> "중견기업 (51-300명)"
            CompanySize.LARGE -> "대기업 (300명+)"
            CompanySize.INDIVIDUAL -> "개인사업자"
        }
    
    val businessTypeDisplayName: String
        get() = when (businessType) {
            BusinessType.CONSTRUCTION -> "건설업"
            BusinessType.ARCHITECTURE -> "건축업"
            BusinessType.CIVIL_ENGINEERING -> "토목공사업"
            BusinessType.INTERIOR -> "실내건축업"
            BusinessType.ELECTRICAL -> "전기공사업"
            BusinessType.PLUMBING -> "배관공사업"
            BusinessType.PAINTING -> "도장공사업"
            BusinessType.LANDSCAPING -> "조경공사업"
            BusinessType.DEMOLITION -> "철거공사업"
            BusinessType.OTHER -> "기타"
        }
    
    val isProfileComplete: Boolean
        get() = companyName.isNotBlank() &&
                businessRegistrationNumber.isNotBlank() &&
                ceoName.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                email.isNotBlank() &&
                address.isComplete
}

data class Address(
    val zipCode: String,
    val fullAddress: String,
    val detailAddress: String? = null,
    val city: String,
    val district: String,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val isComplete: Boolean
        get() = zipCode.isNotBlank() && fullAddress.isNotBlank() && city.isNotBlank()
    
    val displayAddress: String
        get() = buildString {
            append(fullAddress)
            if (!detailAddress.isNullOrBlank()) {
                append(" ")
                append(detailAddress)
            }
        }
}

data class BusinessLicense(
    val id: String,
    val name: String,
    val licenseNumber: String,
    val issuedBy: String,
    val issuedDate: LocalDate,
    val expiryDate: LocalDate? = null,
    val status: LicenseStatus,
    val attachmentUrl: String? = null
) {
    enum class LicenseStatus {
        ACTIVE,     // 유효
        EXPIRED,    // 만료
        SUSPENDED,  // 정지
        REVOKED     // 취소
    }
    
    val statusDisplayName: String
        get() = when (status) {
            LicenseStatus.ACTIVE -> "유효"
            LicenseStatus.EXPIRED -> "만료"
            LicenseStatus.SUSPENDED -> "정지"
            LicenseStatus.REVOKED -> "취소"
        }
    
    val isValid: Boolean
        get() = status == LicenseStatus.ACTIVE && 
                (expiryDate?.isAfter(LocalDate.now()) ?: true)
}

data class Certification(
    val id: String,
    val name: String,
    val issuingOrganization: String,
    val certificationDate: LocalDate,
    val expiryDate: LocalDate? = null,
    val attachmentUrl: String? = null
) {
    val isValid: Boolean
        get() = expiryDate?.isAfter(LocalDate.now()) ?: true
}

data class Insurance(
    val id: String,
    val type: InsuranceType,
    val provider: String,
    val policyNumber: String,
    val coverageAmount: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val attachmentUrl: String? = null
) {
    enum class InsuranceType {
        LIABILITY,          // 책임보험
        WORKERS_COMP,       // 산재보험
        GENERAL_LIABILITY,  // 일반배상책임보험
        PROPERTY,          // 재산보험
        OTHER             // 기타
    }
    
    val typeDisplayName: String
        get() = when (type) {
            InsuranceType.LIABILITY -> "책임보험"
            InsuranceType.WORKERS_COMP -> "산재보험"
            InsuranceType.GENERAL_LIABILITY -> "일반배상책임보험"
            InsuranceType.PROPERTY -> "재산보험"
            InsuranceType.OTHER -> "기타"
        }
    
    val isActive: Boolean
        get() = endDate.isAfter(LocalDate.now())
}

data class WorkingHours(
    val weekdayStart: String,   // "09:00"
    val weekdayEnd: String,     // "18:00"
    val saturdayStart: String? = null,
    val saturdayEnd: String? = null,
    val sundayStart: String? = null,
    val sundayEnd: String? = null,
    val holidays: List<LocalDate> = emptyList()
) {
    val weekdayHours: String
        get() = "$weekdayStart ~ $weekdayEnd"
    
    val weekendHours: String?
        get() = when {
            saturdayStart != null && saturdayEnd != null -> "$saturdayStart ~ $saturdayEnd"
            else -> null
        }
}

data class PaymentTerms(
    val defaultPaymentDays: Int = 1,        // 기본 지급일 (당일, 익일 등)
    val minimumAmount: Long = 0,            // 최소 지급 금액
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val autoPaymentEnabled: Boolean = false
) {
    enum class PaymentMethod {
        BANK_TRANSFER,  // 계좌이체
        MOBILE_PAYMENT, // 모바일 결제
        DIGITAL_WALLET, // 디지털 지갑
        CHECK,         // 수표
        CASH          // 현금
    }
    
    val paymentDaysDisplayName: String
        get() = when (defaultPaymentDays) {
            0 -> "당일 지급"
            1 -> "익일 지급"
            else -> "${defaultPaymentDays}일 후 지급"
        }
}

data class SafetyRating(
    val overallRating: Float,               // 전체 안전등급 (1.0 ~ 5.0)
    val accidentCount: Int = 0,             // 사고 발생 건수 (연간)
    val safetyTrainingHours: Int = 0,       // 안전교육 시간 (연간)
    val safetyEquipmentScore: Float = 0.0f, // 안전장비 점수 (1.0 ~ 5.0)
    val lastInspectionDate: LocalDate? = null,
    val nextInspectionDate: LocalDate? = null
) {
    val ratingGrade: String
        get() = when {
            overallRating >= 4.5 -> "A+"
            overallRating >= 4.0 -> "A"
            overallRating >= 3.5 -> "B+"
            overallRating >= 3.0 -> "B"
            overallRating >= 2.5 -> "C+"
            overallRating >= 2.0 -> "C"
            else -> "D"
        }
    
    val ratingColor: androidx.compose.ui.graphics.Color
        get() = when {
            overallRating >= 4.0 -> androidx.compose.ui.graphics.Color(0xFF10B981) // Green
            overallRating >= 3.0 -> androidx.compose.ui.graphics.Color(0xFFF59E0B) // Yellow  
            overallRating >= 2.0 -> androidx.compose.ui.graphics.Color(0xFFF97316) // Orange
            else -> androidx.compose.ui.graphics.Color(0xFFEF4444) // Red
        }
}

data class NotificationSettings(
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = true,
    val smsNotifications: Boolean = true,
    val marketingNotifications: Boolean = false,
    val jobMatchingAlerts: Boolean = true,
    val paymentAlerts: Boolean = true,
    val safetyAlerts: Boolean = true,
    val systemUpdates: Boolean = true
)

data class PrivacySettings(
    val profileVisibility: ProfileVisibility = ProfileVisibility.PUBLIC,
    val contactInfoVisible: Boolean = true,
    val workHistoryVisible: Boolean = true,
    val locationSharingEnabled: Boolean = true,
    val analyticsEnabled: Boolean = true
) {
    enum class ProfileVisibility {
        PRIVATE,    // 비공개
        CONTACTS,   // 연락처만
        PUBLIC      // 전체 공개
    }
    
    val visibilityDisplayName: String
        get() = when (profileVisibility) {
            ProfileVisibility.PRIVATE -> "비공개"
            ProfileVisibility.CONTACTS -> "연락처만"
            ProfileVisibility.PUBLIC -> "전체 공개"
        }
}

// ==================== 프로필 콘텐츠 ====================

object ProfileContent {
    
    fun createMockCompanyProfile(): CompanyProfile {
        return CompanyProfile(
            id = "comp_001",
            companyName = "김직공건설",
            businessRegistrationNumber = "123-45-67890",
            ceoName = "김직공",
            establishedDate = LocalDate.of(2018, 3, 15),
            companySize = CompanySize.SMALL,
            businessType = BusinessType.CONSTRUCTION,
            description = """
                김직공건설은 2018년 설립된 건설 전문업체로, 주택 및 상업시설 건축을 전문으로 하고 있습니다. 
                안전하고 품질 높은 시공을 통해 고객 만족을 최우선으로 하며, 숙련된 전문 인력과 최신 장비를 
                보유하고 있어 다양한 건설 프로젝트를 성공적으로 완수하고 있습니다.
                
                주요 전문 분야:
                • 주거용 건물 신축 및 리모델링
                • 상업시설 건축 및 인테리어
                • 토목공사 및 도로포장
                • 안전관리 및 품질관리
            """.trimIndent(),
            website = "https://kimjikgong.co.kr",
            
            phoneNumber = "02-1234-5678",
            faxNumber = "02-1234-5679",
            email = "info@kimjikgong.co.kr",
            emergencyContact = "010-1234-5678",
            
            address = Address(
                zipCode = "06288",
                fullAddress = "서울특별시 강남구 테헤란로 123",
                detailAddress = "김직공빌딩 5층",
                city = "서울특별시",
                district = "강남구",
                latitude = 37.5012767,
                longitude = 127.0396597
            ),
            workingAreas = listOf("서울", "경기", "인천", "충청남도"),
            
            licenses = listOf(
                BusinessLicense(
                    id = "lic_001",
                    name = "일반건설업 면허",
                    licenseNumber = "서울-2018-0123",
                    issuedBy = "서울특별시청",
                    issuedDate = LocalDate.of(2018, 3, 20),
                    expiryDate = LocalDate.of(2025, 3, 19),
                    status = BusinessLicense.LicenseStatus.ACTIVE
                ),
                BusinessLicense(
                    id = "lic_002", 
                    name = "전문건설업 면허 (토목)",
                    licenseNumber = "서울-2019-0456",
                    issuedBy = "서울특별시청",
                    issuedDate = LocalDate.of(2019, 6, 10),
                    expiryDate = LocalDate.of(2026, 6, 9),
                    status = BusinessLicense.LicenseStatus.ACTIVE
                )
            ),
            
            certifications = listOf(
                Certification(
                    id = "cert_001",
                    name = "ISO 9001 품질경영시스템",
                    issuingOrganization = "한국품질인증원",
                    certificationDate = LocalDate.of(2020, 8, 15),
                    expiryDate = LocalDate.of(2025, 8, 14)
                ),
                Certification(
                    id = "cert_002",
                    name = "건설업 안전관리 우수업체",
                    issuingOrganization = "한국산업안전보건공단",
                    certificationDate = LocalDate.of(2021, 12, 1),
                    expiryDate = LocalDate.of(2024, 11, 30)
                )
            ),
            
            insurances = listOf(
                Insurance(
                    id = "ins_001",
                    type = Insurance.InsuranceType.LIABILITY,
                    provider = "삼성화재",
                    policyNumber = "2024-LB-001234",
                    coverageAmount = 1000000000L, // 10억원
                    startDate = LocalDate.of(2024, 1, 1),
                    endDate = LocalDate.of(2024, 12, 31)
                ),
                Insurance(
                    id = "ins_002",
                    type = Insurance.InsuranceType.WORKERS_COMP,
                    provider = "근로복지공단",
                    policyNumber = "2024-WC-005678",
                    coverageAmount = 50000000L, // 5천만원
                    startDate = LocalDate.of(2024, 1, 1),
                    endDate = LocalDate.of(2024, 12, 31)
                )
            ),
            
            workingHours = WorkingHours(
                weekdayStart = "08:00",
                weekdayEnd = "18:00",
                saturdayStart = "08:00",
                saturdayEnd = "15:00"
            ),
            
            paymentTerms = PaymentTerms(
                defaultPaymentDays = 1,
                minimumAmount = 100000L,
                paymentMethods = listOf(
                    PaymentTerms.PaymentMethod.BANK_TRANSFER,
                    PaymentTerms.PaymentMethod.MOBILE_PAYMENT
                ),
                autoPaymentEnabled = true
            ),
            
            safetyRating = SafetyRating(
                overallRating = 4.2f,
                accidentCount = 0,
                safetyTrainingHours = 240,
                safetyEquipmentScore = 4.5f,
                lastInspectionDate = LocalDate.of(2023, 11, 15),
                nextInspectionDate = LocalDate.of(2024, 11, 15)
            ),
            
            notificationSettings = NotificationSettings(
                pushNotifications = true,
                emailNotifications = true,
                smsNotifications = true,
                marketingNotifications = false,
                jobMatchingAlerts = true,
                paymentAlerts = true,
                safetyAlerts = true,
                systemUpdates = true
            ),
            
            privacySettings = PrivacySettings(
                profileVisibility = PrivacySettings.ProfileVisibility.PUBLIC,
                contactInfoVisible = true,
                workHistoryVisible = true,
                locationSharingEnabled = true,
                analyticsEnabled = true
            ),
            
            createdAt = LocalDateTime.of(2018, 3, 15, 9, 0),
            updatedAt = LocalDateTime.of(2024, 1, 15, 14, 30),
            lastActiveAt = LocalDateTime.now()
        )
    }
}