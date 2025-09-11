# 직직직 노동자(Worker) 앱 API 데이터 형식 문서 v1.0

## 📋 변경 이력
- **2025-01-15 v1.0**: 초기 문서 생성
  - Worker 앱 전체 API 구조 정의
  - 인증, 일자리, 정산, 프로필 API 명세
  - Company 앱과의 연동 포인트 명시

## 🔍 구현 상태 요약

### ✅ 구현 완료
- SMS 인증 발송/확인
- 로그인 (ID/비밀번호)
- ID 중복 확인
- 회원가입 (6단계 프로세스)

### 🔄 부분 구현
- 일자리 목록 조회 (Mock 데이터)
- 지원 현황 관리 (UI만 구현)
- 수입 관리 (그래프 UI)
- 프로필 관리 (기본 정보만)

### ❌ 미구현 (API)
- 일자리 상세 조회
- 일자리 지원/취소
- 출근/퇴근 체크
- 정산 내역 조회
- 평점/리뷰 시스템
- 알림 시스템
- 위치 기반 검색

---

## 📋 목차
1. [인증 API](#1-인증-api)
2. [일자리 API](#2-일자리-api)
3. [내 프로젝트 API](#3-내-프로젝트-api)
4. [수입 관리 API](#4-수입-관리-api)
5. [프로필 API](#5-프로필-api)
6. [출근 관리 API](#6-출근-관리-api)
7. [알림 API](#7-알림-api)

---

## 1. 인증 API

### 1.1 SMS 인증 요청 ✅
```kotlin
POST /api/worker/auth/sms/send

Request:
data class SendSMSRequest(
    val phone: String  // "010-1234-5678"
)

Response:
data class SendSMSResponse(
    val success: Boolean,
    val message: String,
    val authCode: String?, // 개발환경에서만 반환
    val expiresIn: Int = 180  // 3분
)
```

### 1.2 SMS 인증 확인 ✅
```kotlin
POST /api/worker/auth/sms/verify

Request:
data class VerifySMSRequest(
    val phone: String,
    val verificationCode: String  // 6자리
)

Response:
data class VerifySMSResponse(
    val success: Boolean,
    val message: String,
    val tempToken: String?  // 회원가입용 임시 토큰
)
```

### 1.3 ID 중복 확인 ✅
```kotlin
POST /api/worker/auth/check-id

Request:
data class CheckIdRequest(
    val loginId: String
)

Response:
data class CheckIdResponse(
    val success: Boolean,
    val isAvailable: Boolean,
    val message: String
)
```

### 1.4 회원가입 (6단계) ⚠️
```kotlin
POST /api/worker/auth/register

Request:
data class WorkerRegisterRequest(
    // Page1: SMS 인증
    val phoneNumber: String,
    val verificationCode: String,
    
    // Page2: 기본 정보
    val name: String,
    val birthDate: String,  // "1990-01-01"
    val gender: String,  // "M" or "F"
    
    // Page3: 계좌 정보
    val bankCode: String,
    val accountNumber: String,
    val accountHolder: String,
    
    // Page4: 주소
    val address: String,
    val detailAddress: String?,
    val zipCode: String,
    
    // Page5: 로그인 정보
    val loginId: String,
    val password: String,
    val email: String?,
    
    // Page6: 직종 선택
    val primaryJobType: String,
    val secondaryJobTypes: List<String>?,
    val experience: Int,  // 경력 년수
    val certifications: List<String>?,
    
    // 약관 동의
    val termsAgree: Boolean = true,
    val privacyAgree: Boolean = true,
    val marketingAgree: Boolean = false
)

Response:
data class WorkerRegisterResponse(
    val success: Boolean,
    val message: String,
    val workerId: Long?,
    val accessToken: String?,
    val refreshToken: String?
)
```

### 1.5 로그인 ✅
```kotlin
POST /api/worker/auth/login

Request:
data class WorkerLoginRequest(
    val loginId: String,
    val password: String,
    val deviceToken: String?
)

Response:
data class WorkerLoginResponse(
    val success: Boolean,
    val message: String,
    val accessToken: String?,
    val refreshToken: String?,
    val workerInfo: WorkerProfile?
)
```

---

## 2. 일자리 API

### 2.1 일자리 목록 조회 ❌
```kotlin
GET /api/worker/jobs

Query Parameters:
- latitude: 위도
- longitude: 경도
- distance: 검색 반경 (km)
- jobType: 직종 필터
- date: 근무일 필터
- minWage: 최소 일당
- page: 페이지 번호
- size: 페이지 크기

Response:
data class JobListResponse(
    val success: Boolean,
    val jobs: List<JobSummary>,
    val pagination: PaginationInfo
)

data class JobSummary(
    val id: String,
    val companyName: String,
    val title: String,
    val jobType: String,
    val workDate: String,
    val workTime: String,  // "08:00-18:00"
    val location: String,
    val distance: Double?,  // km
    val wage: Int,
    val requiredWorkers: Int,
    val currentApplicants: Int,
    val isUrgent: Boolean,
    val benefits: List<String>,  // ["식사제공", "교통비지원"]
    val createdAt: Long
)
```

### 2.2 일자리 상세 조회 ❌
```kotlin
GET /api/worker/jobs/{jobId}

Response:
data class JobDetailResponse(
    val success: Boolean,
    val job: JobDetail
)

data class JobDetail(
    val id: String,
    val company: CompanyInfo,
    val title: String,
    val jobType: String,
    val workDate: String,
    val startTime: String,
    val endTime: String,
    val location: LocationInfo,
    val wage: Int,
    val overtimePay: Int?,
    val weekendPay: Int?,
    val requiredWorkers: Int,
    val currentApplicants: Int,
    val description: String,
    val requirements: String,
    val benefits: BenefitInfo,
    val pickupInfo: PickupInfo?,
    val images: List<String>?,
    val managerContact: ContactInfo,
    val applicationStatus: String?,  // null, "APPLIED", "ACCEPTED", "REJECTED"
    val isBookmarked: Boolean
)

data class CompanyInfo(
    val id: String,
    val name: String,
    val rating: Float,
    val reviewCount: Int,
    val verified: Boolean
)

data class LocationInfo(
    val address: String,
    val detailAddress: String?,
    val latitude: Double,
    val longitude: Double,
    val distance: Double?
)

data class BenefitInfo(
    val provideMeals: Boolean,
    val provideTransport: Boolean,
    val provideAccommodation: Boolean,
    val parkingAvailable: Boolean
)

data class PickupInfo(
    val hasPickup: Boolean,
    val pickupLocations: List<String>,
    val pickupTime: String
)

data class ContactInfo(
    val name: String,
    val phone: String
)
```

### 2.3 일자리 지원 ❌
```kotlin
POST /api/worker/jobs/{jobId}/apply

Request:
data class JobApplyRequest(
    val coverLetter: String?,
    val expectedWage: Int?,
    val availableStartTime: String?,
    val hasOwnTransport: Boolean
)

Response:
data class JobApplyResponse(
    val success: Boolean,
    val message: String,
    val applicationId: String?
)
```

### 2.4 지원 취소 ❌
```kotlin
DELETE /api/worker/jobs/{jobId}/apply

Response:
data class CancelApplyResponse(
    val success: Boolean,
    val message: String
)
```

### 2.5 북마크 추가/제거 ❌
```kotlin
POST /api/worker/jobs/{jobId}/bookmark

Response:
data class BookmarkResponse(
    val success: Boolean,
    val isBookmarked: Boolean
)
```

---

## 3. 내 프로젝트 API

### 3.1 지원 현황 조회 ❌
```kotlin
GET /api/worker/my-projects

Query Parameters:
- status: PENDING, ACCEPTED, REJECTED, COMPLETED
- page: 페이지 번호
- size: 페이지 크기

Response:
data class MyProjectsResponse(
    val success: Boolean,
    val projects: List<MyProject>,
    val pagination: PaginationInfo
)

data class MyProject(
    val id: String,
    val jobId: String,
    val companyName: String,
    val jobTitle: String,
    val workDate: String,
    val workTime: String,
    val location: String,
    val wage: Int,
    val status: String,  // "PENDING", "ACCEPTED", "REJECTED", "COMPLETED"
    val appliedAt: String,
    val respondedAt: String?,
    val message: String?  // 거절 사유 또는 메시지
)
```

### 3.2 확정 일자리 상세 ❌
```kotlin
GET /api/worker/my-projects/{projectId}

Response:
data class AcceptedProjectDetail(
    val success: Boolean,
    val project: AcceptedProject
)

data class AcceptedProject(
    val id: String,
    val job: JobDetail,
    val attendanceInfo: AttendanceInfo?,
    val paymentInfo: PaymentInfo?,
    val managerContact: ContactInfo,
    val groupChat: ChatInfo?
)

data class AttendanceInfo(
    val checkInTime: String?,
    val checkOutTime: String?,
    val workHours: Double?,
    val status: String?  // "NOT_YET", "WORKING", "COMPLETED"
)

data class PaymentInfo(
    val basicWage: Int,
    val overtimePay: Int,
    val deductions: Int,
    val totalAmount: Int,
    val paymentStatus: String,  // "PENDING", "COMPLETED"
    val paymentDate: String?
)
```

---

## 4. 수입 관리 API

### 4.1 수입 대시보드 ❌
```kotlin
GET /api/worker/income/dashboard

Response:
data class IncomeDashboardResponse(
    val success: Boolean,
    val dashboard: IncomeDashboard
)

data class IncomeDashboard(
    val currentMonth: MonthlyIncome,
    val previousMonth: MonthlyIncome,
    val yearToDate: YearlyIncome,
    val recentPayments: List<PaymentSummary>
)

data class MonthlyIncome(
    val month: String,
    val totalIncome: Int,
    val workDays: Int,
    val averageDaily: Int,
    val projects: Int
)

data class YearlyIncome(
    val year: Int,
    val totalIncome: Long,
    val totalWorkDays: Int,
    val monthlyAverage: Int
)

data class PaymentSummary(
    val id: String,
    val companyName: String,
    val workDate: String,
    val amount: Int,
    val status: String,
    val paidAt: String?
)
```

### 4.2 수입 그래프 데이터 ❌
```kotlin
GET /api/worker/income/graph

Query Parameters:
- period: WEEKLY, MONTHLY, YEARLY
- startDate: 시작일
- endDate: 종료일

Response:
data class IncomeGraphResponse(
    val success: Boolean,
    val graphData: IncomeGraphData
)

data class IncomeGraphData(
    val period: String,
    val labels: List<String>,  // ["1월", "2월", ...]
    val values: List<Int>,     // [2500000, 2800000, ...]
    val average: Int,
    val total: Long,
    val trend: Float  // 증감률 %
)
```

### 4.3 정산 내역 조회 ❌
```kotlin
GET /api/worker/income/payments

Query Parameters:
- year: 연도
- month: 월
- status: ALL, PENDING, COMPLETED
- page: 페이지 번호
- size: 페이지 크기

Response:
data class PaymentListResponse(
    val success: Boolean,
    val payments: List<PaymentDetail>,
    val summary: PaymentSummaryInfo
)

data class PaymentDetail(
    val id: String,
    val companyName: String,
    val projectName: String,
    val workDate: String,
    val workHours: Double,
    val basicWage: Int,
    val overtimePay: Int,
    val weekendPay: Int,
    val deductions: Int,
    val netAmount: Int,
    val paymentMethod: String,
    val paymentStatus: String,
    val paidAt: String?,
    val taxInvoice: String?  // URL
)

data class PaymentSummaryInfo(
    val totalAmount: Long,
    val pendingAmount: Long,
    val completedAmount: Long,
    val taxAmount: Long
)
```

---

## 5. 프로필 API

### 5.1 내 정보 조회 ❌
```kotlin
GET /api/worker/profile

Response:
data class WorkerProfileResponse(
    val success: Boolean,
    val profile: WorkerProfile
)

data class WorkerProfile(
    val workerId: Long,
    val name: String,
    val birthDate: String,
    val gender: String,
    val phone: String,
    val email: String?,
    val address: String,
    val profileImage: String?,
    val primaryJobType: String,
    val secondaryJobTypes: List<String>,
    val experience: Int,
    val certifications: List<String>,
    val rating: Float,
    val reviewCount: Int,
    val completedProjects: Int,
    val attendanceRate: Float,
    val badges: List<Badge>,
    val bankAccount: BankAccountInfo,
    val joinedAt: String
)

data class BankAccountInfo(
    val bankCode: String,
    val bankName: String,
    val accountNumber: String,
    val accountHolder: String
)

data class Badge(
    val type: String,  // "TOP_WORKER", "VERIFIED", "INSURED"
    val name: String,
    val icon: String
)
```

### 5.2 프로필 수정 ❌
```kotlin
PUT /api/worker/profile

Request:
data class UpdateProfileRequest(
    val phone: String?,
    val email: String?,
    val address: String?,
    val detailAddress: String?,
    val profileImage: String?,
    val secondaryJobTypes: List<String>?,
    val certifications: List<String>?
)

Response:
data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val profile: WorkerProfile?
)
```

### 5.3 경력 추가 ❌
```kotlin
POST /api/worker/profile/experience

Request:
data class AddExperienceRequest(
    val companyName: String,
    val projectName: String,
    val jobType: String,
    val startDate: String,
    val endDate: String,
    val description: String?
)

Response:
data class AddExperienceResponse(
    val success: Boolean,
    val experienceId: String?
)
```

### 5.4 자격증 등록 ❌
```kotlin
POST /api/worker/profile/certification

Request:
data class AddCertificationRequest(
    val certificationName: String,
    val issuingOrganization: String,
    val issueDate: String,
    val expiryDate: String?,
    val certificationNumber: String,
    val imageUrl: String?
)

Response:
data class AddCertificationResponse(
    val success: Boolean,
    val certificationId: String?
)
```

### 5.5 계좌 정보 변경 ❌
```kotlin
PUT /api/worker/profile/bank-account

Request:
data class UpdateBankAccountRequest(
    val bankCode: String,
    val accountNumber: String,
    val accountHolder: String
)

Response:
data class UpdateBankAccountResponse(
    val success: Boolean,
    val message: String
)
```

---

## 6. 출근 관리 API

### 6.1 QR 체크인 ❌
```kotlin
POST /api/worker/attendance/check-in

Request:
data class CheckInRequest(
    val jobId: String,
    val qrCode: String?,
    val location: LocationData?,
    val checkInTime: String,
    val deviceInfo: DeviceInfo?
)

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float
)

Response:
data class CheckInResponse(
    val success: Boolean,
    val message: String,
    val attendanceId: String?,
    val checkInTime: String?,
    val status: String?  // "NORMAL", "LATE"
)
```

### 6.2 체크아웃 ❌
```kotlin
POST /api/worker/attendance/check-out

Request:
data class CheckOutRequest(
    val attendanceId: String,
    val checkOutTime: String,
    val location: LocationData?
)

Response:
data class CheckOutResponse(
    val success: Boolean,
    val message: String,
    val workHours: Double,
    val calculatedWage: Int
)
```

### 6.3 출근 기록 조회 ❌
```kotlin
GET /api/worker/attendance/history

Query Parameters:
- year: 연도
- month: 월
- page: 페이지 번호
- size: 페이지 크기

Response:
data class AttendanceHistoryResponse(
    val success: Boolean,
    val history: List<AttendanceRecord>,
    val summary: AttendanceSummary
)

data class AttendanceRecord(
    val id: String,
    val jobId: String,
    val companyName: String,
    val workDate: String,
    val checkInTime: String?,
    val checkOutTime: String?,
    val workHours: Double?,
    val status: String,  // "NORMAL", "LATE", "ABSENT", "EARLY_LEAVE"
    val wage: Int
)

data class AttendanceSummary(
    val totalDays: Int,
    val normalDays: Int,
    val lateDays: Int,
    val absentDays: Int,
    val attendanceRate: Float
)
```

---

## 7. 알림 API

### 7.1 알림 목록 조회 ❌
```kotlin
GET /api/worker/notifications

Query Parameters:
- type: ALL, JOB, APPLICATION, PAYMENT, ATTENDANCE
- isRead: true/false
- page: 페이지 번호
- size: 페이지 크기

Response:
data class NotificationListResponse(
    val success: Boolean,
    val notifications: List<Notification>,
    val unreadCount: Int
)

data class Notification(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val data: Map<String, Any>?,
    val isRead: Boolean,
    val createdAt: String
)
```

### 7.2 알림 읽음 처리 ❌
```kotlin
PUT /api/worker/notifications/{notificationId}/read

Response:
data class MarkReadResponse(
    val success: Boolean,
    val message: String
)
```

### 7.3 푸시 토큰 등록 ❌
```kotlin
POST /api/worker/notifications/register-token

Request:
data class RegisterTokenRequest(
    val deviceToken: String,
    val deviceType: String  // "ANDROID", "IOS"
)

Response:
data class RegisterTokenResponse(
    val success: Boolean,
    val message: String
)
```

### 7.4 알림 설정 조회/수정 ❌
```kotlin
GET /api/worker/settings/notifications

Response:
data class NotificationSettingsResponse(
    val success: Boolean,
    val settings: NotificationSettings
)

data class NotificationSettings(
    val jobAlert: Boolean,          // 새 일자리 알림
    val applicationResponse: Boolean, // 지원 응답 알림
    val paymentComplete: Boolean,    // 정산 완료 알림
    val attendanceReminder: Boolean, // 출근 리마인더
    val marketing: Boolean,          // 마케팅 알림
    val doNotDisturbStart: String?,  // "22:00"
    val doNotDisturbEnd: String?     // "07:00"
)

PUT /api/worker/settings/notifications
Request/Response: 동일
```

---

## 8. 공통 데이터 모델

### 8.1 페이지네이션
```kotlin
data class PaginationInfo(
    val currentPage: Int,
    val totalPages: Int,
    val pageSize: Int,
    val totalElements: Long,
    val hasNext: Boolean,
    val hasPrev: Boolean
)
```

### 8.2 공통 응답 형식
```kotlin
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: ErrorInfo? = null
)

data class ErrorInfo(
    val code: String,
    val message: String,
    val field: String? = null
)
```

### 8.3 직종 코드
```kotlin
enum class JobType(val code: String, val koreanName: String) {
    CARPENTER("carpenter", "목수"),
    REBAR_WORKER("rebar", "철근공"),
    MASON("mason", "미장공"),
    PAINTER("painter", "도장공"),
    ELECTRICIAN("electrician", "전기공"),
    PLUMBER("plumber", "배관공"),
    WELDER("welder", "용접공"),
    SCAFFOLDER("scaffolder", "비계공"),
    HELPER("helper", "조공"),
    GENERAL_LABORER("general", "일반인부")
}
```

### 8.4 은행 코드
```kotlin
enum class BankCode(val code: String, val name: String) {
    KB("004", "KB국민은행"),
    SHINHAN("088", "신한은행"),
    WOORI("020", "우리은행"),
    HANA("081", "하나은행"),
    NH("011", "NH농협은행"),
    IBK("003", "기업은행"),
    KAKAO("090", "카카오뱅크"),
    TOSS("092", "토스뱅크")
}
```

---

## 9. API 서버 설정

### 9.1 Base URL
```kotlin
// 개발 서버
const val DEV_BASE_URL = "https://dev-api.jikjikjik.com/"

// 운영 서버
const val PROD_BASE_URL = "https://api.jikjikjik.com/"

// 로컬 테스트
const val LOCAL_BASE_URL = "http://10.0.2.2:8080/"
```

### 9.2 공통 헤더
```kotlin
Headers:
- Authorization: Bearer {accessToken}
- Content-Type: application/json
- X-App-Version: 1.0.0
- X-Device-Type: ANDROID
- X-Device-Id: {deviceId}
- X-User-Type: WORKER
```

### 9.3 인증 토큰 관리
```kotlin
// Access Token 만료 시간: 2시간
// Refresh Token 만료 시간: 30일

// 토큰 갱신
POST /api/auth/refresh

Request:
data class RefreshTokenRequest(
    val refreshToken: String
)

Response:
data class RefreshTokenResponse(
    val success: Boolean,
    val accessToken: String?,
    val refreshToken: String?
)
```

---

## 10. 구현 우선순위

### 🔴 1순위 (핵심 기능)
1. 일자리 목록/상세 조회 API
2. 일자리 지원/취소 API
3. 출근/퇴근 체크 API
4. 정산 내역 조회 API

### 🟡 2순위 (중요 기능)
1. 프로필 관리 API
2. 지원 현황 관리 API
3. 수입 대시보드 API
4. 알림 시스템 API

### 🟢 3순위 (부가 기능)
1. 경력/자격증 관리 API
2. 리뷰/평점 시스템 API
3. 북마크 기능 API
4. 그룹 채팅 API

---

문서 버전: v1.0
최종 업데이트: 2025-01-15
작성자: 직직직 개발팀
용도: 노동자(Worker) 앱 API 명세서