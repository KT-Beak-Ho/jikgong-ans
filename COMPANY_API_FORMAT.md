# 직직직 사업자(Company) 앱 API 데이터 형식 문서 v7.0

## 📋 변경 이력
- **2025-01-15 v7.0**: 전체 API 엔드포인트 재정리
  - 각 화면별 필요 API 상세 정의
  - Request/Response 형식 구체화
  - Mock 데이터와 실제 API 명확히 구분
  - 구현 우선순위 설정

## 🔍 구현 상태 요약

### ✅ 구현 완료
- SMS 인증 발송/확인
- 로그인 (Worker용)
- ID 중복 확인 (Worker용)

### 🔄 부분 구현
- 프로젝트 CRUD (Repository만 구현, API 미연동)
- 회원가입 (Worker용만 구현)

### ⚠️ Mock 데이터 사용
- Scout (인력 스카우트)
- Money (자금 관리)
- Info (사업자 정보)

### ❌ 미구현
- Company 전용 회원가입
- 출근/퇴근 체크
- 정산/송금
- AI 매칭
- 알림 시스템

---

## 📋 목차
1. [인증 API](#1-인증-api)
2. [프로젝트 관리 API](#2-프로젝트-관리-api)
3. [일자리 관리 API](#3-일자리-관리-api)
4. [인력 스카우트 API](#4-인력-스카우트-api)
5. [자금 관리 API](#5-자금-관리-api)
6. [사업자 정보 API](#6-사업자-정보-api)
7. [출근 관리 API](#7-출근-관리-api)
8. [알림 API](#8-알림-api)

---

## 1. 인증 API

### 1.1 SMS 인증 요청 ✅
```kotlin
POST /api/company/auth/sms/send

Request:
data class SendSMSRequest(
    val phone: String  // "010-1234-5678"
)

Response:
data class SendSMSResponse(
    val success: Boolean,
    val message: String,
    val verificationId: String,
    val expiresIn: Int = 180  // 3분
)
```

### 1.2 SMS 인증 확인 ✅
```kotlin
POST /api/company/auth/sms/verify

Request:
data class VerifySMSRequest(
    val phone: String,
    val verificationId: String,
    val verificationCode: String  // 6자리
)

Response:
data class VerifySMSResponse(
    val success: Boolean,
    val message: String,
    val tempToken: String?  // 회원가입용 임시 토큰
)
```

### 1.3 사업자등록번호 검증 ❌
```kotlin
POST /api/company/auth/validate-business

Request:
data class ValidateBusinessRequest(
    val businessNumber: String,  // "123-45-67890"
    val representativeName: String
)

Response:
data class ValidateBusinessResponse(
    val success: Boolean,
    val isValid: Boolean,
    val companyInfo: CompanyBasicInfo?
)

data class CompanyBasicInfo(
    val companyName: String,
    val businessType: String,
    val businessStatus: String,  // "영업중", "휴업", "폐업"
    val businessAddress: String
)
```

### 1.4 회원가입 ❌
```kotlin
POST /api/company/auth/register

Request:
data class CompanyRegisterRequest(
    val tempToken: String,  // SMS 인증 토큰
    
    // 사업자 정보
    val businessNumber: String,
    val companyName: String,
    val representativeName: String,
    val businessType: String,
    val businessAddress: String,
    val businessDetailAddress: String?,
    
    // 계정 정보
    val loginId: String,
    val password: String,
    val email: String,
    
    // 보험 정보
    val hasInsurance: Boolean,
    val insuranceType: String?,  // "산재보험", "고용보험", "둘다"
    val insuranceNumber: String?,
    
    // 약관 동의
    val termsAgree: Boolean,
    val privacyAgree: Boolean,
    val marketingAgree: Boolean,
    
    // FCM 토큰
    val deviceToken: String
)

Response:
data class CompanyRegisterResponse(
    val success: Boolean,
    val message: String,
    val companyId: Long?,
    val accessToken: String?,
    val refreshToken: String?
)
```

### 1.5 로그인 ⚠️ (Worker용만 구현)
```kotlin
POST /api/company/auth/login

Request:
data class CompanyLoginRequest(
    val loginId: String,
    val password: String,
    val deviceToken: String
)

Response:
data class CompanyLoginResponse(
    val success: Boolean,
    val message: String,
    val accessToken: String?,
    val refreshToken: String?,
    val companyInfo: CompanyProfile?
)
```

### 1.6 ID 중복 확인 ⚠️
```kotlin
POST /api/company/auth/check-id

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

### 1.7 이메일 중복 확인 ❌
```kotlin
POST /api/company/auth/check-email

Request:
data class CheckEmailRequest(
    val email: String
)

Response:
data class CheckEmailResponse(
    val success: Boolean,
    val isAvailable: Boolean,
    val message: String
)
```

---

## 2. 프로젝트 관리 API

### 2.1 프로젝트 목록 조회 ❌
```kotlin
GET /api/company/projects?status={status}&page={page}&size={size}

Query Parameters:
- status: ALL, RECRUITING, IN_PROGRESS, COMPLETED
- page: 페이지 번호 (0부터 시작)
- size: 페이지 크기 (기본 20)

Response:
data class ProjectListResponse(
    val success: Boolean,
    val data: ProjectPageData
)

data class ProjectPageData(
    val content: List<ProjectSummary>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Long,
    val hasNext: Boolean
)

data class ProjectSummary(
    val id: String,
    val title: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val status: String,
    val progress: Int,  // 날짜 기반 진행률
    val totalJobs: Int,  // 총 일자리 수
    val activeJobs: Int,  // 진행중 일자리 수
    val totalWorkers: Int,  // 총 근로자 수
    val createdAt: Long
)
```

### 2.2 프로젝트 생성 ❌
```kotlin
POST /api/company/projects

Request:
data class ProjectCreateRequest(
    val title: String,
    val startDate: String,  // "2025-08-01"
    val endDate: String,    // "2025-08-31"
    val location: LocationData,
    val description: String?
)

data class LocationData(
    val address: String,
    val detailAddress: String?,
    val latitude: Double,
    val longitude: Double
)

Response:
data class ProjectCreateResponse(
    val success: Boolean,
    val message: String,
    val projectId: String?,
    val project: ProjectDetail?
)
```

### 2.3 프로젝트 상세 조회 ❌
```kotlin
GET /api/company/projects/{projectId}

Response:
data class ProjectDetailResponse(
    val success: Boolean,
    val project: ProjectDetail
)

data class ProjectDetail(
    val id: String,
    val title: String,
    val location: LocationData,
    val startDate: String,
    val endDate: String,
    val status: String,
    val progress: Int,
    val description: String?,
    val jobs: List<JobSummary>,
    val statistics: ProjectStatistics,
    val createdAt: Long,
    val updatedAt: Long
)

data class ProjectStatistics(
    val totalJobs: Int,
    val completedJobs: Int,
    val totalWorkers: Int,
    val totalPayments: Long,
    val savedFees: Long  // 절감된 수수료
)
```

### 2.4 프로젝트 수정 ❌
```kotlin
PUT /api/company/projects/{projectId}

Request:
data class ProjectUpdateRequest(
    val title: String?,
    val startDate: String?,
    val endDate: String?,
    val location: LocationData?,
    val description: String?,
    val status: String?
)

Response:
data class ProjectUpdateResponse(
    val success: Boolean,
    val message: String,
    val project: ProjectDetail?
)
```

### 2.5 프로젝트 삭제 ❌
```kotlin
DELETE /api/company/projects/{projectId}

Response:
data class DeleteResponse(
    val success: Boolean,
    val message: String
)
```

---

## 3. 일자리 관리 API

### 3.1 일자리 생성 ❌
```kotlin
POST /api/company/projects/{projectId}/jobs

Request:
data class JobCreateRequest(
    val title: String,
    val jobType: String,  // "철근공", "목수", "전기공" 등
    val workDate: String,
    val startTime: String,  // "08:00"
    val endTime: String,    // "18:00"
    val requiredWorkers: Int,
    val wage: Int,
    val description: String,
    val requirements: String,
    val isUrgent: Boolean,
    
    // 부가 혜택
    val provideMeals: Boolean,
    val provideTransport: Boolean,
    val provideAccommodation: Boolean,
    
    // 픽업 정보
    val hasPickup: Boolean,
    val pickupLocation: LocationData?,
    val pickupTime: String?,
    
    // 사진
    val imageUrls: List<String>?
)

Response:
data class JobCreateResponse(
    val success: Boolean,
    val message: String,
    val jobId: String?,
    val job: JobDetail?
)
```

### 3.2 일자리 목록 조회 ❌
```kotlin
GET /api/company/projects/{projectId}/jobs

Response:
data class JobListResponse(
    val success: Boolean,
    val jobs: List<JobDetail>
)

data class JobDetail(
    val id: String,
    val projectId: String,
    val title: String,
    val jobType: String,
    val workDate: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val wage: Int,
    val requiredWorkers: Int,
    val currentApplicants: Int,
    val confirmedWorkers: Int,
    val description: String,
    val requirements: String,
    val status: String,  // "RECRUITING", "CLOSED", "COMPLETED"
    val isUrgent: Boolean,
    val createdAt: Long
)
```

### 3.3 일자리 임시저장 ❌
```kotlin
POST /api/company/jobs/temp-save

Request:
data class TempSaveJobRequest(
    val title: String?,
    val jobType: String?,
    val workDate: String?,
    val startTime: String?,
    val endTime: String?,
    val location: String?,
    val wage: Int?,
    val requiredWorkers: Int?,
    val description: String?,
    val requirements: String?
)

Response:
data class TempSaveJobResponse(
    val success: Boolean,
    val message: String,
    val tempJobId: String?,
    val completionRate: Int  // 작성 완료율
)
```

### 3.4 임시저장 목록 조회 ❌
```kotlin
GET /api/company/jobs/temp-saved

Response:
data class TempSavedJobsResponse(
    val success: Boolean,
    val tempJobs: List<TempSavedJob>
)

data class TempSavedJob(
    val id: String,
    val title: String,
    val jobType: String?,
    val workDate: String?,
    val completionRate: Int,
    val savedDate: String
)
```

### 3.5 이전 일자리 복사 ❌
```kotlin
POST /api/company/jobs/{jobId}/copy

Request:
data class CopyJobRequest(
    val targetProjectId: String,
    val workDate: String?  // 새로운 날짜
)

Response:
data class CopyJobResponse(
    val success: Boolean,
    val message: String,
    val newJobId: String?,
    val job: JobDetail?
)
```

---

## 4. 인력 스카우트 API

### 4.1 인력 검색 ❌
```kotlin
GET /api/company/scout/workers

Query Parameters:
- projectId: 프로젝트 ID (AI 매칭용)
- jobType: 직종 필터
- distance: 거리 필터 (km)
- minRating: 최소 평점
- minExperience: 최소 경력
- useAI: AI 추천 사용 여부

Response:
data class WorkerSearchResponse(
    val success: Boolean,
    val workers: List<WorkerProfile>
)

data class WorkerProfile(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val primaryJob: String,
    val secondaryJobs: List<String>,
    val experience: Int,
    val rating: Float,
    val completedProjects: Int,
    val attendanceRate: Float,
    val distance: Double,
    val expectedWage: Int,
    val profileImage: String?,
    val certifications: List<String>,
    val isAvailable: Boolean,
    val aiMatchScore: Int?,  // AI 매칭 점수 (0-100)
    val matchReasons: List<String>?  // AI 추천 이유
)
```

### 4.2 인력 상세 조회 ❌
```kotlin
GET /api/company/scout/workers/{workerId}

Response:
data class WorkerDetailResponse(
    val success: Boolean,
    val worker: WorkerDetailProfile
)

data class WorkerDetailProfile(
    val basicInfo: WorkerProfile,
    val contactInfo: ContactInfo,
    val workHistory: List<WorkHistory>,
    val reviews: List<WorkerReview>,
    val badges: List<Badge>
)

data class ContactInfo(
    val phone: String,
    val email: String?,
    val address: String
)

data class WorkHistory(
    val projectName: String,
    val companyName: String,
    val period: String,
    val jobType: String,
    val rating: Float?
)

data class WorkerReview(
    val reviewId: String,
    val companyName: String,
    val rating: Float,
    val comment: String,
    val date: String
)

data class Badge(
    val type: String,  // "TOP_5_PERCENT", "VERIFIED", "INSURED"
    val name: String,
    val icon: String
)
```

### 4.3 제안 발송 ❌
```kotlin
POST /api/company/scout/proposals

Request:
data class SendProposalRequest(
    val workerId: String,
    val jobId: String,
    val proposedWage: Int,
    val message: String,
    val urgentBonus: Int?  // 긴급 보너스
)

Response:
data class SendProposalResponse(
    val success: Boolean,
    val message: String,
    val proposalId: String?
)
```

### 4.4 제안 목록 조회 ❌
```kotlin
GET /api/company/scout/proposals?status={status}

Query Parameters:
- status: ALL, PENDING, VIEWED, ACCEPTED, REJECTED, EXPIRED

Response:
data class ProposalListResponse(
    val success: Boolean,
    val proposals: List<ProposalDetail>
)

data class ProposalDetail(
    val id: String,
    val workerId: String,
    val workerName: String,
    val jobId: String,
    val jobTitle: String,
    val proposedWage: Int,
    val message: String,
    val status: String,
    val workerResponse: String?,
    val createdAt: String,
    val expiresAt: String,
    val viewedAt: String?
)
```

### 4.5 일괄 제안 발송 ❌
```kotlin
POST /api/company/scout/proposals/bulk

Request:
data class BulkProposalRequest(
    val workerIds: List<String>,
    val jobId: String,
    val proposedWage: Int,
    val message: String,
    val scheduleTime: String?  // 예약 발송
)

Response:
data class BulkProposalResponse(
    val success: Boolean,
    val message: String,
    val totalSent: Int,
    val failedWorkerIds: List<String>?
)
```

---

## 5. 자금 관리 API

### 5.1 정산 대시보드 ❌
```kotlin
GET /api/company/payments/dashboard

Response:
data class PaymentDashboardResponse(
    val success: Boolean,
    val dashboard: PaymentDashboard
)

data class PaymentDashboard(
    val monthlyTotal: Long,
    val pendingAmount: Long,
    val pendingCount: Int,
    val completedAmount: Long,
    val completedCount: Int,
    val savedFees: Long,  // 절감된 수수료
    val savingsRate: Float,  // 절감률
    val weeklyTrend: List<WeeklyPayment>,
    val projectPayments: List<ProjectPaymentSummary>
)

data class WeeklyPayment(
    val week: String,
    val amount: Long,
    val count: Int
)

data class ProjectPaymentSummary(
    val projectId: String,
    val projectName: String,
    val totalWorkers: Int,
    val totalAmount: Long,
    val paidAmount: Long,
    val pendingAmount: Long,
    val serviceFee: Long,
    val savedFee: Long
)
```

### 5.2 정산 목록 조회 ❌
```kotlin
GET /api/company/payments

Query Parameters:
- projectId: 프로젝트 ID
- status: PENDING, PROCESSING, COMPLETED
- startDate: 시작일
- endDate: 종료일
- page: 페이지 번호
- size: 페이지 크기

Response:
data class PaymentListResponse(
    val success: Boolean,
    val payments: List<PaymentDetail>,
    val pagination: PaginationInfo
)

data class PaymentDetail(
    val id: String,
    val workerId: String,
    val workerName: String,
    val projectName: String,
    val jobType: String,
    val workDate: String,
    val workHours: Double,
    val basicWage: Int,
    val overtimePay: Int,
    val weekendPay: Int,
    val deductions: Int,
    val totalAmount: Int,
    val serviceFee: Int,  // 5% 수수료
    val netAmount: Int,  // 실지급액
    val status: String,
    val accountInfo: AccountInfo?,
    val paidAt: String?
)

data class AccountInfo(
    val bankName: String,
    val accountNumber: String,
    val accountHolder: String
)
```

### 5.3 개별 정산 처리 ❌
```kotlin
POST /api/company/payments/{paymentId}/pay

Request:
data class ProcessPaymentRequest(
    val password: String,  // 결제 비밀번호
    val memo: String?
)

Response:
data class ProcessPaymentResponse(
    val success: Boolean,
    val message: String,
    val transactionId: String?,
    val paidAt: String?
)
```

### 5.4 일괄 정산 처리 ❌
```kotlin
POST /api/company/payments/bulk-pay

Request:
data class BulkPaymentRequest(
    val paymentIds: List<String>,
    val password: String,
    val totalAmount: Long,
    val totalServiceFee: Long
)

Response:
data class BulkPaymentResponse(
    val success: Boolean,
    val message: String,
    val successCount: Int,
    val failedCount: Int,
    val failedPayments: List<FailedPayment>?
)

data class FailedPayment(
    val paymentId: String,
    val workerName: String,
    val reason: String
)
```

### 5.5 절감액 통계 ❌
```kotlin
GET /api/company/payments/savings-stats

Query Parameters:
- period: DAILY, WEEKLY, MONTHLY, YEARLY
- startDate: 시작일
- endDate: 종료일

Response:
data class SavingsStatsResponse(
    val success: Boolean,
    val stats: SavingsStatistics
)

data class SavingsStatistics(
    val period: String,
    val totalPayments: Long,
    val standardFee: Long,  // 기존 10% 수수료
    val currentFee: Long,   // 직직직 5% 수수료
    val totalSaved: Long,   // 절감액
    val savingsRate: Float,  // 절감률
    val chartData: List<SavingsChartPoint>,
    val projectedYearlySavings: Long
)

data class SavingsChartPoint(
    val date: String,
    val amount: Long,
    val saved: Long
)
```

---

## 6. 사업자 정보 API

### 6.1 프로필 조회 ❌
```kotlin
GET /api/company/profile

Response:
data class CompanyProfileResponse(
    val success: Boolean,
    val profile: CompanyProfile
)

data class CompanyProfile(
    val companyId: Long,
    val businessNumber: String,
    val companyName: String,
    val representativeName: String,
    val businessType: String,
    val businessAddress: String,
    val email: String,
    val phone: String,
    val profileImage: String?,
    val rating: Float,
    val reviewCount: Int,
    val badges: List<String>,
    val membershipType: String,  // "BASIC", "STANDARD", "PREMIUM"
    val joinedAt: String
)
```

### 6.2 프로필 수정 ❌
```kotlin
PUT /api/company/profile

Request:
data class UpdateProfileRequest(
    val phone: String?,
    val email: String?,
    val businessAddress: String?,
    val profileImage: String?
)

Response:
data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val profile: CompanyProfile?
)
```

### 6.3 통계 조회 ❌
```kotlin
GET /api/company/statistics

Response:
data class CompanyStatisticsResponse(
    val success: Boolean,
    val stats: CompanyStatistics
)

data class CompanyStatistics(
    val totalProjects: Int,
    val activeProjects: Int,
    val totalWorkers: Int,
    val averageRating: Float,
    val rehireRate: Float,
    val responseRate: Float,
    val monthlySpending: Long,
    val monthlySavings: Long,
    val totalSavings: Long,
    val jobTypeDistribution: List<JobTypeStats>,
    val monthlyTrend: List<MonthlyStats>
)

data class JobTypeStats(
    val jobType: String,
    val count: Int,
    val percentage: Float
)

data class MonthlyStats(
    val month: String,
    val projects: Int,
    val workers: Int,
    val spending: Long,
    val savings: Long
)
```

### 6.4 공지사항 조회 ❌
```kotlin
GET /api/company/notices

Response:
data class NoticeListResponse(
    val success: Boolean,
    val notices: List<Notice>
)

data class Notice(
    val id: String,
    val title: String,
    val content: String,
    val category: String,  // "IMPORTANT", "UPDATE", "EVENT", "MAINTENANCE"
    val isImportant: Boolean,
    val createdAt: String,
    val isRead: Boolean
)
```

### 6.5 알림 설정 조회/수정 ❌
```kotlin
GET /api/company/settings/notifications

Response:
data class NotificationSettingsResponse(
    val success: Boolean,
    val settings: NotificationSettings
)

data class NotificationSettings(
    val attendanceAlert: Boolean,
    val proposalResponse: Boolean,
    val paymentComplete: Boolean,
    val urgentRecruit: Boolean,
    val marketing: Boolean,
    val doNotDisturbStart: String?,  // "22:00"
    val doNotDisturbEnd: String?     // "07:00"
)

PUT /api/company/settings/notifications

Request: NotificationSettings
Response: NotificationSettingsResponse
```

---

## 7. 출근 관리 API

### 7.1 QR 코드 생성 ❌
```kotlin
POST /api/company/attendance/qr-generate

Request:
data class QRGenerateRequest(
    val jobId: String,
    val workDate: String,
    val validUntil: String  // QR 유효시간
)

Response:
data class QRGenerateResponse(
    val success: Boolean,
    val qrCode: String,  // Base64 encoded QR image
    val qrId: String,
    val expiresAt: String
)
```

### 7.2 출근 체크 (QR) ❌
```kotlin
POST /api/company/attendance/check-in

Request:
data class CheckInRequest(
    val workerId: String,
    val jobId: String,
    val qrId: String?,
    val checkInMethod: String,  // "QR", "GPS", "MANUAL"
    val location: LocationData?,
    val checkInTime: String,
    val deviceInfo: DeviceInfo?
)

data class DeviceInfo(
    val deviceId: String,
    val deviceModel: String,
    val osVersion: String
)

Response:
data class CheckInResponse(
    val success: Boolean,
    val message: String,
    val attendanceId: String?,
    val checkInTime: String?,
    val status: String?  // "NORMAL", "LATE", "EARLY"
)
```

### 7.3 퇴근 체크 ❌
```kotlin
POST /api/company/attendance/check-out

Request:
data class CheckOutRequest(
    val attendanceId: String,
    val checkOutTime: String,
    val workHours: Double,
    val overtimeHours: Double?,
    val notes: String?
)

Response:
data class CheckOutResponse(
    val success: Boolean,
    val message: String,
    val totalWorkHours: Double,
    val calculatedWage: Int
)
```

### 7.4 출근 현황 조회 ❌
```kotlin
GET /api/company/attendance/status

Query Parameters:
- jobId: 일자리 ID
- workDate: 작업일

Response:
data class AttendanceStatusResponse(
    val success: Boolean,
    val attendance: AttendanceStatus
)

data class AttendanceStatus(
    val jobId: String,
    val workDate: String,
    val totalWorkers: Int,
    val checkedInWorkers: Int,
    val lateWorkers: Int,
    val absentWorkers: Int,
    val workers: List<WorkerAttendance>
)

data class WorkerAttendance(
    val workerId: String,
    val workerName: String,
    val status: String,  // "CHECKED_IN", "LATE", "ABSENT", "NOT_YET"
    val checkInTime: String?,
    val checkOutTime: String?,
    val workHours: Double?
)
```

---

## 8. 알림 API

### 8.1 알림 목록 조회 ❌
```kotlin
GET /api/company/notifications

Query Parameters:
- type: ALL, ATTENDANCE, PROPOSAL, PAYMENT, SYSTEM
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
    val data: Map<String, Any>?,  // 추가 데이터
    val isRead: Boolean,
    val createdAt: String
)
```

### 8.2 알림 읽음 처리 ❌
```kotlin
PUT /api/company/notifications/{notificationId}/read

Response:
data class MarkReadResponse(
    val success: Boolean,
    val message: String
)
```

### 8.3 푸시 알림 등록 ❌
```kotlin
POST /api/company/notifications/register-token

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

---

## 9. 공통 데이터 모델

### 9.1 페이지네이션
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

### 9.2 공통 응답 형식
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
    val field: String? = null,
    val details: Map<String, Any>? = null
)
```

### 9.3 공통 에러 코드
```kotlin
enum class ErrorCode(val code: String, val message: String) {
    // 인증 관련
    AUTH_001("AUTH_001", "인증 토큰이 만료되었습니다"),
    AUTH_002("AUTH_002", "권한이 없습니다"),
    AUTH_003("AUTH_003", "잘못된 인증 정보입니다"),
    
    // 검증 관련
    VALID_001("VALID_001", "필수 입력값이 누락되었습니다"),
    VALID_002("VALID_002", "유효하지 않은 형식입니다"),
    VALID_003("VALID_003", "중복된 값입니다"),
    
    // 비즈니스 로직
    BIZ_001("BIZ_001", "이미 처리된 요청입니다"),
    BIZ_002("BIZ_002", "잔액이 부족합니다"),
    BIZ_003("BIZ_003", "정원이 초과되었습니다"),
    
    // 시스템
    SYS_001("SYS_001", "서버 오류가 발생했습니다"),
    SYS_002("SYS_002", "서비스 점검 중입니다")
}
```

---

## 10. API 서버 설정

### 10.1 Base URL
```kotlin
// 개발 서버
const val DEV_BASE_URL = "https://dev-api.jikjikjik.com/"

// 운영 서버
const val PROD_BASE_URL = "https://api.jikjikjik.com/"

// 로컬 테스트
const val LOCAL_BASE_URL = "http://10.0.2.2:8080/"
```

### 10.2 공통 헤더
```kotlin
Headers:
- Authorization: Bearer {accessToken}
- Content-Type: application/json
- X-App-Version: 1.0.0
- X-Device-Type: ANDROID
- X-Device-Id: {deviceId}
```

### 10.3 인증 토큰 관리
```kotlin
// Access Token 만료 시간: 2시간
// Refresh Token 만료 시간: 14일

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

## 11. 구현 우선순위

### 🔴 1순위 (핵심 기능)
1. Company 회원가입 API
2. 프로젝트 CRUD API
3. 일자리 생성/조회 API
4. 출근 체크 API

### 🟡 2순위 (차별화 기능)
1. Scout 인력 검색/제안 API
2. Money 정산/송금 API
3. 절감액 통계 API
4. AI 매칭 API

### 🟢 3순위 (부가 기능)
1. 프로필/통계 API
2. 알림 시스템 API
3. 공지사항 API
4. 임시저장 API

---

## 📝 참고사항

1. **날짜 형식**: ISO 8601 (YYYY-MM-DD, HH:mm:ss)
2. **금액 단위**: 원(KRW), Long 타입 사용
3. **좌표 체계**: WGS84 (위도, 경도)
4. **문자 인코딩**: UTF-8
5. **이미지**: Base64 또는 S3 URL
6. **에러 처리**: 모든 API는 공통 에러 형식 사용

---

문서 버전: v7.0
최종 업데이트: 2025-01-15
작성자: 직직직 개발팀
용도: 사업자(Company) 앱 API 명세서