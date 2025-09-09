# 직직직 사업자 앱 완전한 API 데이터 형식 문서 v4.0

## 📋 변경 이력
- **2024-01-15 v4.0**: 전체 기능 세부 분석 및 완전 재작성
  - 모든 repository와 presentation 레이어 철저 분석
  - Scout 모듈: AI 추천, 필터링, 위치 기반 검색, 스카우트 제안 상세
  - Money 모듈: 개별/일괄 지급, 절감액 추적, 지급 내역서 발급
  - Info 모듈: 프로필 관리, 통계, 알림 설정, 고객센터
  - 작은 기능 하나도 빠짐없이 문서화

## 📋 목차
1. [인증 API](#1-인증-api)
2. [프로젝트 관리 API](#2-프로젝트-관리-api)
3. [인력 스카우트 API](#3-인력-스카우트-api)
4. [자금 관리 API](#4-자금-관리-api)
5. [사업자 정보 API](#5-사업자-정보-api)
6. [공통 데이터 모델](#6-공통-데이터-모델)
7. [에러 처리](#7-에러-처리)

---

## 1. 인증 API

### 1.1 SMS 인증 요청
```kotlin
POST /api/auth/sms/send

Request:
data class SendSMSRequest(
    val phoneNumber: String,      // "010-1234-5678"
    val purpose: String           // "SIGNUP", "LOGIN", "RESET_PASSWORD"
)

Response:
data class SendSMSResponse(
    val success: Boolean,
    val verificationId: String,   // 인증 세션 ID
    val expiresAt: Long,          // 만료 시간 (Unix timestamp)
    val remainingAttempts: Int,   // 남은 재시도 횟수
    val message: String
)
```

### 1.2 SMS 인증 확인
```kotlin
POST /api/auth/sms/verify

Request:
data class VerifySMSRequest(
    val verificationId: String,
    val code: String,             // 6자리 인증 코드
    val phoneNumber: String
)

Response:
data class VerifySMSResponse(
    val success: Boolean,
    val verified: Boolean,
    val authToken: String?,       // 인증 성공 시 임시 토큰
    val message: String,
    val errorCode: String?        // "EXPIRED", "INVALID_CODE", "MAX_ATTEMPTS"
)
```

### 1.3 사업자 가입
```kotlin
POST /api/auth/company/signup

Request:
data class CompanySignupRequest(
    val authToken: String,        // SMS 인증 토큰
    val businessNumber: String,   // 사업자등록번호 "123-45-67890"
    val companyName: String,
    val representativeName: String,
    val businessType: String,     // "CONSTRUCTION", "CIVIL", "ELECTRIC", "PLUMBING"
    val businessAddress: Address,
    val email: String,
    val agreeTerms: Boolean,
    val agreePrivacy: Boolean,
    val agreeMarketing: Boolean
)

data class Address(
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val zipCode: String,
    val latitude: Double,
    val longitude: Double
)

Response:
data class CompanySignupResponse(
    val success: Boolean,
    val companyId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val company: CompanyInfo
)

data class CompanyInfo(
    val id: String,
    val businessNumber: String,
    val companyName: String,
    val representativeName: String,
    val verificationStatus: String, // "PENDING", "VERIFIED", "REJECTED"
    val createdAt: String
)
```

### 1.4 로그인
```kotlin
POST /api/auth/login

Request:
data class LoginRequest(
    val phoneNumber: String,
    val verificationCode: String?, // SMS 인증 코드
    val deviceId: String,
    val deviceType: String,        // "ANDROID", "IOS"
    val pushToken: String?
)

Response:
data class LoginResponse(
    val success: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: UserInfo,
    val requiresProfileUpdate: Boolean
)

data class UserInfo(
    val userId: String,
    val companyId: String,
    val role: String,              // "OWNER", "MANAGER", "STAFF"
    val name: String,
    val phoneNumber: String,
    val lastLoginAt: String
)
```

### 1.5 토큰 갱신
```kotlin
POST /api/auth/refresh

Request:
data class RefreshTokenRequest(
    val refreshToken: String
)

Response:
data class RefreshTokenResponse(
    val success: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
```

---

## 2. 프로젝트 관리 API

### 2.1 프로젝트 목록 조회
```kotlin
GET /api/projects

Query Parameters:
- status: String? ("RECRUITING", "IN_PROGRESS", "COMPLETED")
- page: Int (default: 0)
- size: Int (default: 20)
- sortBy: String ("createdAt", "startDate", "endDate")
- sortOrder: String ("ASC", "DESC")

Response:
data class ProjectListResponse(
    val success: Boolean,
    val data: List<SimpleProject>,
    val pagination: PaginationInfo,
    val summary: ProjectSummary
)

data class SimpleProject(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val locationDetail: String?,
    val category: String,          // "건축", "토목", "전기", "설비"
    val status: String,
    val startDate: String,
    val endDate: String,
    val progressPercentage: Int,   // 날짜 기반 진행률
    val totalDays: Int,
    val elapsedDays: Int,
    val remainingDays: Int,
    val isUrgent: Boolean,
    val isBookmarked: Boolean,
    val createdAt: String,
    val updatedAt: String
)

data class ProjectSummary(
    val totalProjects: Int,
    val recruitingCount: Int,
    val inProgressCount: Int,
    val completedCount: Int
)

data class PaginationInfo(
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)
```

### 2.2 프로젝트 생성 (단순화된 버전)
```kotlin
POST /api/projects

Request:
data class ProjectCreateRequest(
    val title: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val locationDetail: String?,
    val latitude: Double,
    val longitude: Double
)

Response:
data class ProjectCreateResponse(
    val success: Boolean,
    val projectId: String,
    val message: String,
    val project: ProjectDetail
)

data class ProjectDetail(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val locationDetail: String?,
    val category: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val requirements: List<String>,
    val benefits: List<String>,
    val latitude: Double,
    val longitude: Double,
    val progressPercentage: Int,
    val createdAt: String,
    val updatedAt: String
)
```

### 2.3 프로젝트 상세 조회
```kotlin
GET /api/projects/{projectId}

Response:
data class ProjectDetailResponse(
    val success: Boolean,
    val project: ProjectDetail,
    val workDays: List<WorkDay>,
    val statistics: ProjectStatistics
)

data class WorkDay(
    val id: String,
    val projectId: String,
    val title: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val recruitPeriod: String,
    val applicants: Int,
    val confirmed: Int,
    val maxWorkers: Int,
    val status: String,
    val attendanceInfo: AttendanceInfo?
)

data class AttendanceInfo(
    val hasCheckedIn: Boolean,
    val hasCheckedOut: Boolean,
    val hasPaymentRecord: Boolean,
    val checkedInCount: Int,
    val checkedOutCount: Int
)

data class ProjectStatistics(
    val totalWorkDays: Int,
    val completedDays: Int,
    val totalWorkers: Int,
    val totalPayments: Long,
    val averageRating: Float
)
```

### 2.4 프로젝트 수정
```kotlin
PUT /api/projects/{projectId}

Request:
data class ProjectUpdateRequest(
    val title: String?,
    val description: String?,
    val requirements: List<String>?,
    val benefits: List<String>?,
    val status: String?
)

Response:
data class ProjectUpdateResponse(
    val success: Boolean,
    val message: String,
    val project: ProjectDetail
)
```

### 2.5 프로젝트 삭제
```kotlin
DELETE /api/projects/{projectId}

Response:
data class DeleteResponse(
    val success: Boolean,
    val message: String
)
```

### 2.6 프로젝트 북마크 토글
```kotlin
POST /api/projects/{projectId}/bookmark

Response:
data class BookmarkResponse(
    val success: Boolean,
    val isBookmarked: Boolean,
    val message: String
)
```

### 2.7 작업일 관리

#### 2.7.1 작업일 추가 (일자리 등록)
```kotlin
POST /api/projects/{projectId}/workdays

Request:
data class JobRegistrationRequest(
    val title: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val maxWorkers: Int,
    val jobTypes: List<JobTypeRequirement>,
    val wage: WageInfo,
    val requirements: List<String>?,
    val benefits: List<String>?
)

data class JobTypeRequirement(
    val jobType: String,           // "철근공", "목수", "보통인부"
    val requiredCount: Int,
    val experienceLevel: String?   // "초급", "중급", "고급"
)

data class WageInfo(
    val type: String,              // "DAILY", "HOURLY"
    val amount: Int,
    val overtimeRate: Double?
)

Response:
data class JobRegistrationResponse(
    val success: Boolean,
    val workDayId: String,
    val message: String,
    val workDay: WorkDay
)
```

#### 2.7.2 작업일별 지원자 조회
```kotlin
GET /api/projects/{projectId}/workdays/{date}/applicants

Response:
data class ApplicantListResponse(
    val success: Boolean,
    val projectId: String,
    val date: String,
    val applicants: List<ApplicantWorker>,
    val confirmedWorkers: List<ConfirmedWorker>,
    val statistics: ApplicantStatistics
)

data class ApplicantWorker(
    val id: String,
    val name: String,
    val jobType: String,
    val experienceYears: Int,
    val phone: String,
    val distance: String,
    val rating: Double,
    val completedProjects: Int,
    val status: String,             // "PENDING", "ACCEPTED", "REJECTED"
    val appliedAt: String,
    val profileImage: String?,
    val recentWorkHistory: List<WorkHistory>
)

data class ConfirmedWorker(
    val id: String,
    val name: String,
    val jobType: String,
    val phone: String,
    val confirmedAt: String,
    val attendanceStatus: AttendanceStatus?
)

data class AttendanceStatus(
    val checkInTime: String?,
    val checkOutTime: String?,
    val workHours: Double?,
    val overtimeHours: Double?
)

data class ApplicantStatistics(
    val totalApplicants: Int,
    val pendingCount: Int,
    val acceptedCount: Int,
    val rejectedCount: Int
)

data class WorkHistory(
    val projectName: String,
    val workDate: String,
    val rating: Float?
)
```

#### 2.7.3 지원자 수락/거절
```kotlin
POST /api/projects/{projectId}/applicants/{applicantId}/status

Request:
data class ApplicantStatusRequest(
    val action: String,            // "ACCEPT", "REJECT"
    val workDate: String,
    val reason: String?,           // 거절 사유
    val notifyWorker: Boolean      // 인력에게 알림 전송 여부
)

Response:
data class ApplicantStatusResponse(
    val success: Boolean,
    val message: String,
    val updatedApplicant: ApplicantWorker,
    val notificationSent: Boolean
)
```

### 2.8 출근/퇴근 체크

#### 2.8.1 출근 체크
```kotlin
POST /api/projects/{projectId}/attendance/checkin

Request:
data class AttendanceCheckInRequest(
    val workDate: String,
    val workers: List<WorkerCheckIn>
)

data class WorkerCheckIn(
    val workerId: String,
    val checkInTime: String,
    val checkInMethod: String,     // "QR", "GPS", "MANUAL"
    val location: LocationInfo?,
    val deviceInfo: DeviceInfo?
)

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val address: String?
)

data class DeviceInfo(
    val deviceId: String,
    val deviceType: String,
    val appVersion: String
)

Response:
data class AttendanceCheckResponse(
    val success: Boolean,
    val checkedInCount: Int,
    val failedCount: Int,
    val message: String,
    val results: List<CheckResult>
)

data class CheckResult(
    val workerId: String,
    val workerName: String,
    val success: Boolean,
    val checkTime: String,
    val message: String?,
    val errorCode: String?         // "ALREADY_CHECKED", "INVALID_LOCATION", "OUTSIDE_HOURS"
)
```

#### 2.8.2 퇴근 체크
```kotlin
POST /api/projects/{projectId}/attendance/checkout

Request:
data class AttendanceCheckOutRequest(
    val workDate: String,
    val workers: List<WorkerCheckOut>
)

data class WorkerCheckOut(
    val workerId: String,
    val checkOutTime: String,
    val workHours: Double,
    val overtimeHours: Double?,
    val breakTime: Double?,
    val workReport: String?        // 작업 보고서
)

Response:
// AttendanceCheckResponse와 동일
```

#### 2.8.3 출근 현황 조회
```kotlin
GET /api/projects/{projectId}/attendance/{date}

Response:
data class AttendanceStatusResponse(
    val success: Boolean,
    val projectId: String,
    val date: String,
    val summary: AttendanceSummary,
    val workers: List<WorkerAttendanceDetail>
)

data class AttendanceSummary(
    val totalWorkers: Int,
    val checkedInWorkers: Int,
    val checkedOutWorkers: Int,
    val absentWorkers: Int,
    val lateWorkers: Int,
    val earlyLeaveWorkers: Int,
    val totalWorkHours: Double,
    val totalOvertimeHours: Double
)

data class WorkerAttendanceDetail(
    val workerId: String,
    val workerName: String,
    val jobType: String,
    val checkInTime: String?,
    val checkOutTime: String?,
    val workHours: Double?,
    val overtimeHours: Double?,
    val status: String,             // "CHECKED_IN", "CHECKED_OUT", "ABSENT", "LATE"
    val wage: Int,
    val baseWage: Long,
    val overtimePay: Long?,
    val totalWage: Long,
    val notes: String?
)
```

### 2.9 임금 지급 관리

#### 2.9.1 프로젝트별 임금 지급 내역 조회
```kotlin
GET /api/projects/{projectId}/payments

Query Parameters:
- date: String?
- status: String? ("PENDING", "PROCESSING", "COMPLETED", "FAILED")
- workerId: String?

Response:
data class ProjectPaymentListResponse(
    val success: Boolean,
    val projectId: String,
    val payments: List<PaymentRecord>,
    val summary: PaymentSummary
)

data class PaymentRecord(
    val id: String,
    val workDate: String,
    val workerId: String,
    val workerName: String,
    val jobType: String,
    val workHours: Double,
    val hourlyWage: Int,
    val baseWage: Long,
    val overtimePay: Long?,
    val weekendPay: Long?,
    val holidayPay: Long?,
    val mealAllowance: Long?,
    val transportAllowance: Long?,
    val deductions: Long,            // 공제액
    val platformFee: Long,          // 플랫폼 수수료 (5%)
    val finalAmount: Long,          // 실지급액
    val status: String,
    val paymentDate: String?,
    val paymentMethod: String,      // "BANK_TRANSFER", "CASH"
    val bankName: String,
    val accountNumber: String,
    val accountHolder: String,
    val transactionId: String?,
    val receiptUrl: String?
)

data class PaymentSummary(
    val totalWorkers: Int,
    val totalAmount: Long,
    val paidAmount: Long,
    val pendingAmount: Long,
    val completedCount: Int,
    val pendingCount: Int,
    val platformFee: Long,
    val savedAmount: Long,          // 10% → 5%로 절약한 금액
    val estimatedPaymentDate: String?
)
```

#### 2.9.2 임금 계산
```kotlin
POST /api/projects/{projectId}/payments/calculate

Request:
data class PaymentCalculateRequest(
    val workDate: String,
    val workers: List<WorkerPaymentCalc>
)

data class WorkerPaymentCalc(
    val workerId: String,
    val attendanceStatus: String,
    val workHours: Double,
    val overtimeHours: Double?,
    val isWeekend: Boolean,
    val isHoliday: Boolean,
    val additionalPay: Long?,
    val deductions: Long?,
    val notes: String?
)

Response:
data class PaymentCalculateResponse(
    val success: Boolean,
    val calculations: List<PaymentCalculation>,
    val summary: CalculationSummary
)

data class PaymentCalculation(
    val workerId: String,
    val workerName: String,
    val baseWage: Long,
    val overtimePay: Long,
    val weekendPay: Long,
    val holidayPay: Long,
    val additionalPay: Long,
    val subtotal: Long,
    val deductions: Long,
    val platformFee: Long,
    val originalFee: Long,          // 타사 10% 기준
    val savedAmount: Long,
    val finalAmount: Long
)

data class CalculationSummary(
    val totalWorkers: Int,
    val totalBaseWage: Long,
    val totalAdditionalPay: Long,
    val totalDeductions: Long,
    val totalPlatformFee: Long,
    val totalSavedAmount: Long,
    val totalFinalAmount: Long
)
```

#### 2.9.3 임금 지급 승인
```kotlin
POST /api/projects/{projectId}/payments/approve

Request:
data class PaymentApprovalRequest(
    val paymentIds: List<String>,
    val approvalType: String,      // "INDIVIDUAL", "BULK"
    val paymentDate: String,
    val paymentMethod: String,
    val approverNote: String?
)

Response:
data class PaymentApprovalResponse(
    val success: Boolean,
    val approvedCount: Int,
    val failedCount: Int,
    val totalAmount: Long,
    val message: String,
    val results: List<ApprovalResult>,
    val estimatedTransferTime: String
)

data class ApprovalResult(
    val paymentId: String,
    val workerId: String,
    val success: Boolean,
    val amount: Long,
    val transactionId: String?,
    val errorMessage: String?
)
```

#### 2.9.4 지급 내역서 발급
```kotlin
GET /api/projects/{projectId}/payments/receipt/{paymentId}

Response:
data class PaymentReceiptResponse(
    val success: Boolean,
    val receipt: PaymentReceipt
)

data class PaymentReceipt(
    val receiptNumber: String,
    val issueDate: String,
    val projectInfo: ProjectInfo,
    val workerInfo: WorkerInfo,
    val paymentDetails: PaymentDetails,
    val companyInfo: CompanyInfo,
    val downloadUrl: String,         // PDF 다운로드 URL
    val qrCode: String              // 검증용 QR 코드
)

data class ProjectInfo(
    val projectId: String,
    val projectTitle: String,
    val workDate: String,
    val location: String
)

data class WorkerInfo(
    val workerId: String,
    val name: String,
    val phone: String,
    val jobType: String,
    val bankName: String,
    val accountNumber: String
)

data class PaymentDetails(
    val workHours: Double,
    val hourlyWage: Int,
    val baseWage: Long,
    val overtimePay: Long?,
    val additionalPay: Long?,
    val deductions: Long,
    val platformFee: Long,
    val finalAmount: Long,
    val paymentMethod: String,
    val paymentDate: String,
    val transactionId: String
)
```

---

## 3. 인력 스카우트 API

### 3.1 인력 검색
```kotlin
GET /api/scout/workers

Query Parameters:
- latitude: Double
- longitude: Double
- radius: Int (km, default: 10)
- jobTypes: String? (comma separated: "철근공,목수,보통인부")
- minExperience: Int?
- maxDistance: Double?
- minRating: Float?
- availableOnly: Boolean (default: false)
- useAIRecommendation: Boolean (default: false)
- page: Int
- size: Int

Response:
data class WorkerListResponse(
    val success: Boolean,
    val data: List<Worker>,
    val totalCount: Int,
    val aiRecommended: List<String>?,  // AI 추천 worker IDs
    val searchCriteria: SearchCriteria,
    val pagination: PaginationInfo
)

data class Worker(
    val id: String,
    val name: String,
    val profileImage: String?,
    val jobTypes: List<String>,
    val primaryJobType: String,
    val experience: Int,            // 경력 년수
    val distance: Double,           // km
    val rating: Float,
    val reviewCount: Int,
    val completedProjects: Int,
    val reemploymentRate: Float,    // 재고용률
    val isAvailable: Boolean,
    val availableDates: List<String>,
    val hourlyWage: Int?,
    val dailyWage: Int?,
    val certifications: List<String>,
    val skills: List<String>,
    val introduction: String?,
    val lastActiveAt: String,
    val aiMatchScore: Float?        // AI 매칭 점수 (0-100)
)

data class SearchCriteria(
    val location: String,
    val radius: Int,
    val filters: WorkerFilters
)

data class WorkerFilters(
    val jobTypes: Set<String>,
    val minExperience: Int,
    val maxDistance: Double,
    val minRating: Float,
    val availableOnly: Boolean
)
```

### 3.2 인력 상세 조회
```kotlin
GET /api/scout/workers/{workerId}

Response:
data class WorkerDetailResponse(
    val success: Boolean,
    val worker: WorkerDetail
)

data class WorkerDetail(
    val id: String,
    val name: String,
    val profileImage: String?,
    val phone: String,              // 마스킹 처리
    val jobTypes: List<String>,
    val experience: Int,
    val age: Int?,
    val gender: String?,
    val distance: Double,
    val rating: Float,
    val reviewCount: Int,
    val completedProjects: Int,
    val reemploymentRate: Float,
    val hourlyWage: Int?,
    val dailyWage: Int?,
    val preferredWorkDays: List<String>,
    val preferredLocations: List<String>,
    val certifications: List<Certification>,
    val skills: List<String>,
    val introduction: String?,
    val portfolio: List<Portfolio>,
    val reviews: List<WorkerReview>,
    val workHistory: List<WorkHistory>,
    val availability: WorkerAvailability
)

data class Certification(
    val name: String,
    val issuer: String,
    val issueDate: String,
    val expiryDate: String?,
    val verified: Boolean
)

data class Portfolio(
    val id: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val projectDate: String
)

data class WorkerReview(
    val id: String,
    val projectName: String,
    val companyName: String,
    val rating: Float,
    val comment: String,
    val createdAt: String
)

data class WorkerAvailability(
    val isAvailable: Boolean,
    val availableFrom: String?,
    val availableTo: String?,
    val blockedDates: List<String>
)
```

### 3.3 스카우트 제안 발송
```kotlin
POST /api/scout/proposals

Request:
data class ProposalRequest(
    val workerIds: List<String>,
    val projectId: String,
    val workDates: List<String>,
    val proposalDetails: ProposalDetails
)

data class ProposalDetails(
    val title: String,
    val message: String,
    val wage: Int,
    val wageType: String,           // "DAILY", "HOURLY"
    val workStartTime: String,
    val workEndTime: String,
    val benefits: List<String>,
    val requirements: List<String>,
    val location: String,
    val locationDetail: String?,
    val expiresAt: String,          // 제안 만료 시간
    val urgentHiring: Boolean
)

Response:
data class ProposalResponse(
    val success: Boolean,
    val proposalIds: List<String>,
    val sentCount: Int,
    val failedWorkers: List<FailedProposal>?,
    val estimatedResponseTime: String
)

data class FailedProposal(
    val workerId: String,
    val reason: String,             // "WORKER_UNAVAILABLE", "ALREADY_PROPOSED", "BLOCKED"
    val message: String
)
```

### 3.4 제안 목록 조회
```kotlin
GET /api/scout/proposals

Query Parameters:
- status: String? ("PENDING", "VIEWED", "ACCEPTED", "REJECTED", "EXPIRED", "CANCELLED")
- projectId: String?
- workerId: String?
- dateFrom: String?
- dateTo: String?
- page: Int
- size: Int

Response:
data class ProposalListResponse(
    val success: Boolean,
    val data: List<Proposal>,
    val statistics: ProposalStatistics,
    val pagination: PaginationInfo
)

data class Proposal(
    val id: String,
    val projectId: String,
    val projectName: String,
    val workerId: String,
    val workerName: String,
    val workerPhone: String?,       // 수락 시에만 표시
    val jobTypes: List<String>,
    val distance: String,
    val proposedWage: String,
    val message: String,
    val status: String,
    val createdAt: String,
    val viewedAt: String?,
    val respondedAt: String?,
    val expiresAt: String,
    val rejectReason: String?,
    val counterOffer: CounterOffer?
)

data class CounterOffer(
    val wage: Int,
    val availableDates: List<String>,
    val message: String,
    val createdAt: String
)

data class ProposalStatistics(
    val totalSent: Int,
    val pendingCount: Int,
    val viewedCount: Int,
    val acceptedCount: Int,
    val rejectedCount: Int,
    val expiredCount: Int,
    val responseRate: Float,
    val averageResponseTime: String
)
```

### 3.5 제안 취소
```kotlin
POST /api/scout/proposals/{proposalId}/cancel

Request:
data class ProposalCancelRequest(
    val reason: String,
    val notifyWorker: Boolean
)

Response:
data class ProposalCancelResponse(
    val success: Boolean,
    val message: String,
    val refundAmount: Long?         // 환불 금액 (있는 경우)
)
```

### 3.6 AI 추천 필터
```kotlin
POST /api/scout/workers/ai-filter

Request:
data class AIFilterRequest(
    val projectId: String,
    val requiredSkills: List<String>,
    val projectType: String,
    val urgency: String,            // "LOW", "MEDIUM", "HIGH"
    val budgetRange: BudgetRange,
    val preferredExperience: String // "BEGINNER", "INTERMEDIATE", "EXPERT"
)

data class BudgetRange(
    val min: Long,
    val max: Long,
    val currency: String = "KRW"
)

Response:
data class AIFilterResponse(
    val success: Boolean,
    val recommendedWorkers: List<AIRecommendedWorker>,
    val filterCriteria: AIFilterCriteria,
    val confidenceScore: Float      // AI 추천 신뢰도 (0-100)
)

data class AIRecommendedWorker(
    val worker: Worker,
    val matchScore: Float,          // 매칭 점수 (0-100)
    val matchReasons: List<String>, // 추천 이유
    val strengths: List<String>,
    val considerations: List<String>
)

data class AIFilterCriteria(
    val skillMatch: Float,
    val experienceMatch: Float,
    val availabilityMatch: Float,
    val priceMatch: Float,
    val locationMatch: Float
)
```

### 3.7 위치 설정
```kotlin
POST /api/scout/location

Request:
data class LocationUpdateRequest(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val searchRadius: Int,
    val saveAsDefault: Boolean
)

Response:
data class LocationUpdateResponse(
    val success: Boolean,
    val location: LocationInfo,
    val nearbyWorkersCount: Int
)

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String,           // 구/군
    val city: String,               // 시/도
    val searchRadius: Int
)
```

---

## 4. 자금 관리 API

### 4.1 정산 목록 조회
```kotlin
GET /api/payments

Query Parameters:
- status: String? ("PENDING", "PROCESSING", "COMPLETED", "FAILED")
- projectId: String?
- startDate: String?
- endDate: String?
- page: Int
- size: Int

Response:
data class PaymentListResponse(
    val success: Boolean,
    val data: List<ProjectPaymentData>,
    val summary: PaymentSummary,
    val pagination: PaginationInfo
)

data class ProjectPaymentData(
    val id: String,
    val projectId: String,
    val projectTitle: String,
    val projectLocation: String,
    val paymentDate: String,
    val status: String,
    val workers: List<WorkerPaymentInfo>,
    val totalWorkers: Int,
    val totalAmount: Long,
    val platformFee: Long,           // 5%
    val savedAmount: Long,          // 10% → 5% 절감액
    val finalAmount: Long,
    val isPaid: Boolean,
    val paidAt: String?,
    val nextPaymentDate: String?
)

data class WorkerPaymentInfo(
    val workerId: String,
    val workerName: String,
    val phoneNumber: String,
    val bankName: String,
    val accountNumber: String,
    val workDays: Int,
    val totalWorkHours: Double,
    val baseWage: Long,
    val overtimePay: Long,
    val weekendPay: Long,
    val additionalPay: Long,
    val deductions: Long,
    val totalAmount: Long,
    val paymentStatus: String,
    val paymentMethod: String
)

data class PaymentSummary(
    val totalPending: Long,
    val totalProcessing: Long,
    val totalCompleted: Long,
    val totalFailed: Long,
    val pendingCount: Int,
    val processingCount: Int,
    val completedCount: Int,
    val failedCount: Int,
    val monthlyTotal: Long,
    val monthlySaved: Long,
    val yearlyProjectedSavings: Long,
    val averagePaymentTime: String   // 평균 지급 소요 시간
)
```

### 4.2 일괄 급여 지급
```kotlin
POST /api/payments/bulk

Request:
data class BulkPaymentRequest(
    val projectId: String,
    val paymentIds: List<String>,
    val paymentDate: String,
    val paymentMethod: String,      // "BANK_TRANSFER", "BATCH_TRANSFER"
    val approverInfo: ApproverInfo
)

data class ApproverInfo(
    val approverId: String,
    val approverName: String,
    val approverRole: String,
    val approvalNote: String?
)

Response:
data class BulkPaymentResponse(
    val success: Boolean,
    val batchId: String,
    val totalCount: Int,
    val successCount: Int,
    val failedCount: Int,
    val totalAmount: Long,
    val results: List<PaymentResult>,
    val estimatedCompletionTime: String
)

data class PaymentResult(
    val paymentId: String,
    val workerId: String,
    val amount: Long,
    val status: String,
    val transactionId: String?,
    val errorMessage: String?
)
```

### 4.3 송금 처리
```kotlin
POST /api/payments/transfer

Request:
data class TransferRequest(
    val paymentId: String,
    val transferDetails: TransferDetails
)

data class TransferDetails(
    val bankCode: String,
    val accountNumber: String,
    val accountHolder: String,
    val amount: Long,
    val transferNote: String?,
    val scheduledTime: String?      // 예약 송금
)

Response:
data class TransferResponse(
    val success: Boolean,
    val transactionId: String,
    val status: String,
    val transferredAmount: Long,
    val fee: Long,
    val completedAt: String?,
    val receipt: TransferReceipt
)

data class TransferReceipt(
    val receiptNumber: String,
    val bankName: String,
    val accountNumber: String,
    val amount: Long,
    val timestamp: String,
    val downloadUrl: String
)
```

### 4.4 절감액 통계
```kotlin
GET /api/payments/savings

Query Parameters:
- period: String ("DAILY", "WEEKLY", "MONTHLY", "YEARLY", "ALL")
- projectId: String?

Response:
data class SavingsResponse(
    val success: Boolean,
    val period: String,
    val savings: SavingsData,
    val comparison: ComparisonData
)

data class SavingsData(
    val totalSaved: Long,           // 총 절감액
    val platformFeeRate: Float,     // 현재 수수료율 (5%)
    val competitorFeeRate: Float,   // 타사 수수료율 (10%)
    val totalTransactions: Int,
    val averageSavingPerTransaction: Long,
    val projectedYearlySavings: Long,
    val savingsByProject: List<ProjectSavings>
)

data class ProjectSavings(
    val projectId: String,
    val projectName: String,
    val totalSaved: Long,
    val transactionCount: Int
)

data class ComparisonData(
    val ourPlatformTotal: Long,
    val competitorTotal: Long,
    val savedAmount: Long,
    val savedPercentage: Float
)
```

### 4.5 지급 완료 내역
```kotlin
GET /api/payments/completed

Query Parameters:
- startDate: String
- endDate: String
- projectId: String?
- workerId: String?
- page: Int
- size: Int

Response:
data class CompletedPaymentsResponse(
    val success: Boolean,
    val payments: List<CompletedPayment>,
    val summary: CompletedPaymentSummary,
    val pagination: PaginationInfo
)

data class CompletedPayment(
    val id: String,
    val projectName: String,
    val workerName: String,
    val workDate: String,
    val amount: Long,
    val paidAt: String,
    val transactionId: String,
    val receiptUrl: String,
    val bankName: String,
    val accountNumber: String
)

data class CompletedPaymentSummary(
    val totalAmount: Long,
    val totalCount: Int,
    val averageAmount: Long,
    val totalPlatformFee: Long,
    val totalSaved: Long
)
```

### 4.6 대기중 지급 내역
```kotlin
GET /api/payments/pending

Query Parameters:
- dueDate: String?
- priority: String? ("LOW", "MEDIUM", "HIGH", "URGENT")
- projectId: String?

Response:
data class PendingPaymentsResponse(
    val success: Boolean,
    val payments: List<PendingPayment>,
    val urgentPayments: List<PendingPayment>,
    val summary: PendingPaymentSummary
)

data class PendingPayment(
    val id: String,
    val projectName: String,
    val workerCount: Int,
    val totalAmount: Long,
    val dueDate: String,
    val daysOverdue: Int?,
    val priority: String,
    val blockedReason: String?      // 지급 차단 사유
)

data class PendingPaymentSummary(
    val totalPending: Long,
    val urgentCount: Int,
    val overdueCount: Int,
    val blockedCount: Int,
    val estimatedProcessingTime: String
)
```

---

## 5. 사업자 정보 API

### 5.1 프로필 조회
```kotlin
GET /api/company/profile

Response:
data class CompanyProfileResponse(
    val success: Boolean,
    val company: CompanyProfile
)

data class CompanyProfile(
    val id: String,
    val businessNumber: String,
    val companyName: String,
    val representativeName: String,
    val businessType: String,
    val businessAddress: Address,
    val phoneNumber: String,
    val email: String,
    val profileImage: String?,
    val coverImage: String?,
    val rating: Float,
    val reviewCount: Int,
    val completedProjects: Int,
    val totalHiredWorkers: Int,
    val memberSince: String,
    val verificationStatus: VerificationStatus,
    val premiumStatus: PremiumStatus?,
    val badges: List<CompanyBadge>,
    val bankAccount: BankAccount?
)

data class VerificationStatus(
    val businessVerified: Boolean,
    val phoneVerified: Boolean,
    val emailVerified: Boolean,
    val documentVerified: Boolean,
    val verifiedAt: String?
)

data class PremiumStatus(
    val isPremium: Boolean,
    val plan: String?,              // "BASIC", "STANDARD", "PREMIUM"
    val expiresAt: String?,
    val benefits: List<String>,
    val discount: Float             // 추가 수수료 할인율
)

data class CompanyBadge(
    val type: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val earnedAt: String
)

data class BankAccount(
    val bankName: String,
    val accountNumber: String,
    val accountHolder: String,
    val isVerified: Boolean
)
```

### 5.2 프로필 수정
```kotlin
PUT /api/company/profile

Request:
data class ProfileUpdateRequest(
    val companyName: String?,
    val businessType: String?,
    val phoneNumber: String?,
    val email: String?,
    val businessAddress: Address?,
    val profileImage: String?,
    val coverImage: String?,
    val introduction: String?
)

Response:
data class ProfileUpdateResponse(
    val success: Boolean,
    val message: String,
    val updatedProfile: CompanyProfile
)
```

### 5.3 통계 조회
```kotlin
GET /api/company/stats

Query Parameters:
- period: String ("WEEKLY", "MONTHLY", "YEARLY", "ALL")

Response:
data class CompanyStatsResponse(
    val success: Boolean,
    val period: String,
    val stats: CompanyStats
)

data class CompanyStats(
    val projectStats: ProjectStats,
    val hiringStats: HiringStats,
    val paymentStats: PaymentStats,
    val savingsStats: SavingsStats,
    val performanceStats: PerformanceStats
)

data class ProjectStats(
    val totalProjects: Int,
    val activeProjects: Int,
    val completedProjects: Int,
    val completionRate: Float,
    val averageDuration: Int,       // days
    val projectsByCategory: Map<String, Int>
)

data class HiringStats(
    val totalHired: Int,
    val activeWorkers: Int,
    val byJobCategory: Map<String, Int>,
    val averageRating: Float,
    val reemploymentRate: Float,
    val topWorkers: List<TopWorker>,
    val responseRate: Float,
    val averageHiringTime: String
)

data class PaymentStats(
    val totalPaid: Long,
    val averageWage: Long,
    val onTimePaymentRate: Float,
    val paymentMethods: Map<String, Int>,
    val averagePaymentDelay: String
)

data class SavingsStats(
    val totalSaved: Long,
    val monthlyAverage: Long,
    val projectedYearly: Long,
    val comparedToCompetitors: Float,
    val savingsTrend: List<SavingsTrend>
)

data class SavingsTrend(
    val period: String,
    val amount: Long,
    val growthRate: Float
)

data class PerformanceStats(
    val projectCompletionRate: Float,
    val workerSatisfaction: Float,
    val clientSatisfaction: Float,
    val disputeRate: Float,
    val averageResponseTime: String
)

data class TopWorker(
    val workerId: String,
    val name: String,
    val jobType: String,
    val projectCount: Int,
    val rating: Float,
    val totalEarnings: Long
)
```

### 5.4 공지사항
```kotlin
GET /api/notices

Query Parameters:
- category: String? ("SYSTEM", "UPDATE", "EVENT", "MAINTENANCE")
- isImportant: Boolean?
- page: Int
- size: Int

Response:
data class NoticeListResponse(
    val success: Boolean,
    val notices: List<Notice>,
    val hasUnread: Boolean,
    val pagination: PaginationInfo
)

data class Notice(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val isImportant: Boolean,
    val isPinned: Boolean,
    val attachments: List<Attachment>,
    val createdAt: String,
    val updatedAt: String,
    val readAt: String?,
    val author: String
)

data class Attachment(
    val id: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String,
    val downloadUrl: String
)
```

### 5.5 공지사항 읽음 처리
```kotlin
POST /api/notices/{noticeId}/read

Response:
data class NoticeReadResponse(
    val success: Boolean,
    val readAt: String
)
```

### 5.6 알림 설정
```kotlin
GET /api/company/notification-settings

Response:
data class NotificationSettingsResponse(
    val success: Boolean,
    val settings: NotificationSettings
)

data class NotificationSettings(
    val pushEnabled: Boolean,
    val emailEnabled: Boolean,
    val smsEnabled: Boolean,
    val categories: NotificationCategories,
    val quietHours: QuietHours?
)

data class NotificationCategories(
    val projectUpdates: Boolean,
    val workerApplications: Boolean,
    val paymentReminders: Boolean,
    val proposalResponses: Boolean,
    val systemNotices: Boolean,
    val marketingMessages: Boolean
)

data class QuietHours(
    val enabled: Boolean,
    val startTime: String,          // "22:00"
    val endTime: String             // "08:00"
)
```

### 5.7 알림 설정 수정
```kotlin
PUT /api/company/notification-settings

Request:
data class NotificationSettingsUpdateRequest(
    val pushEnabled: Boolean?,
    val emailEnabled: Boolean?,
    val smsEnabled: Boolean?,
    val categories: NotificationCategories?,
    val quietHours: QuietHours?
)

Response:
data class NotificationSettingsUpdateResponse(
    val success: Boolean,
    val message: String,
    val settings: NotificationSettings
)
```

### 5.8 고객센터
```kotlin
// 문의 목록 조회
GET /api/support/inquiries

Query Parameters:
- status: String? ("PENDING", "IN_PROGRESS", "RESOLVED", "CLOSED")
- category: String? ("PAYMENT", "PROJECT", "WORKER", "ACCOUNT", "OTHER")
- page: Int
- size: Int

Response:
data class InquiryListResponse(
    val success: Boolean,
    val inquiries: List<Inquiry>,
    val pagination: PaginationInfo
)

data class Inquiry(
    val id: String,
    val category: String,
    val subject: String,
    val content: String,
    val status: String,
    val priority: String,           // "LOW", "MEDIUM", "HIGH"
    val createdAt: String,
    val updatedAt: String,
    val responses: List<InquiryResponse>
)

data class InquiryResponse(
    val id: String,
    val content: String,
    val isFromSupport: Boolean,
    val createdAt: String,
    val attachments: List<Attachment>
)
```

### 5.9 문의 작성
```kotlin
POST /api/support/inquiries

Request:
data class InquiryCreateRequest(
    val category: String,
    val subject: String,
    val content: String,
    val attachments: List<String>?, // 파일 업로드 후 받은 URLs
    val priority: String
)

Response:
data class InquiryCreateResponse(
    val success: Boolean,
    val inquiryId: String,
    val message: String,
    val estimatedResponseTime: String
)
```

### 5.10 약관 및 정책
```kotlin
GET /api/terms

Query Parameters:
- type: String? ("TERMS_OF_SERVICE", "PRIVACY_POLICY", "PAYMENT_TERMS", "WORKER_AGREEMENT")

Response:
data class TermsResponse(
    val success: Boolean,
    val terms: List<Terms>
)

data class Terms(
    val id: String,
    val type: String,
    val title: String,
    val content: String,
    val version: String,
    val effectiveDate: String,
    val isRequired: Boolean,
    val lastUpdated: String
)
```

---

## 6. 공통 데이터 모델

### 6.1 페이지네이션
```kotlin
data class PaginationInfo(
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
```

### 6.2 주소
```kotlin
data class Address(
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val zipCode: String,
    val latitude: Double,
    val longitude: Double,
    val district: String,           // 구/군
    val city: String,               // 시/도
    val country: String = "KR"
)
```

### 6.3 파일 업로드
```kotlin
POST /api/files/upload

Request:
- Multipart form data
- file: MultipartFile
- type: String ("IMAGE", "DOCUMENT", "VIDEO")

Response:
data class FileUploadResponse(
    val success: Boolean,
    val fileId: String,
    val fileName: String,
    val fileUrl: String,
    val fileSize: Long,
    val mimeType: String
)
```

---

## 7. 에러 처리

### 7.1 에러 Response
```kotlin
data class ErrorResponse(
    val success: Boolean = false,
    val error: ErrorDetail,
    val timestamp: Long
)

data class ErrorDetail(
    val code: String,
    val message: String,
    val details: Map<String, Any>?,
    val path: String,
    val method: String,
    val traceId: String             // 디버깅용 추적 ID
)
```

### 7.2 에러 코드
```kotlin
// 인증 관련 (AUTH_XXX)
AUTH_001: 인증 실패
AUTH_002: 토큰 만료
AUTH_003: 권한 없음
AUTH_004: 중복된 계정
AUTH_005: SMS 인증 실패
AUTH_006: 비즈니스 검증 실패

// 프로젝트 관련 (PROJECT_XXX)
PROJECT_001: 프로젝트 없음
PROJECT_002: 모집 마감
PROJECT_003: 수정 권한 없음
PROJECT_004: 중복 지원
PROJECT_005: 인원 초과

// 결제 관련 (PAYMENT_XXX)
PAYMENT_001: 잔액 부족
PAYMENT_002: 계좌 정보 오류
PAYMENT_003: 이미 처리됨
PAYMENT_004: 송금 실패
PAYMENT_005: 한도 초과

// 스카우트 관련 (SCOUT_XXX)
SCOUT_001: 인력 없음
SCOUT_002: 이미 제안함
SCOUT_003: 제안 만료
SCOUT_004: 인력 차단됨
SCOUT_005: 제안 한도 초과

// 시스템 관련 (SYSTEM_XXX)
SYSTEM_001: 서버 오류
SYSTEM_002: 데이터베이스 오류
SYSTEM_003: 외부 서비스 오류
SYSTEM_004: 네트워크 오류
SYSTEM_005: 유지보수 중

// 검증 관련 (VALIDATION_XXX)
VALIDATION_001: 필수 필드 누락
VALIDATION_002: 형식 오류
VALIDATION_003: 범위 초과
VALIDATION_004: 유효하지 않은 값
VALIDATION_005: 데이터 불일치
```

### 7.3 에러 처리 예시
```kotlin
// 400 Bad Request
{
    "success": false,
    "error": {
        "code": "VALIDATION_001",
        "message": "필수 필드가 누락되었습니다",
        "details": {
            "missingFields": ["title", "startDate"]
        },
        "path": "/api/projects",
        "method": "POST",
        "traceId": "abc123def456"
    },
    "timestamp": 1642123456789
}

// 401 Unauthorized
{
    "success": false,
    "error": {
        "code": "AUTH_002",
        "message": "토큰이 만료되었습니다",
        "details": {
            "expiredAt": "2024-01-15T10:00:00Z"
        },
        "path": "/api/projects",
        "method": "GET",
        "traceId": "xyz789ghi012"
    },
    "timestamp": 1642123456789
}

// 500 Internal Server Error
{
    "success": false,
    "error": {
        "code": "SYSTEM_001",
        "message": "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요",
        "details": null,
        "path": "/api/payments/transfer",
        "method": "POST",
        "traceId": "error123trace456"
    },
    "timestamp": 1642123456789
}
```

---

## 📌 중요 사항

1. **플랫폼 수수료**: 모든 결제 API에서 5% 수수료 적용 (타사 10% 대비 50% 절감)
2. **날짜 형식**: ISO 8601 형식 사용 ("2024-01-15", "2024-01-15T10:00:00Z")
3. **금액 단위**: 모든 금액은 원(KRW) 단위, Long 타입 사용
4. **인증**: 모든 API는 Bearer Token 인증 필요 (인증 API 제외)
5. **Rate Limiting**: 
   - 일반 API: 분당 60회
   - 검색 API: 분당 30회
   - 파일 업로드: 분당 10회
6. **응답 시간**: 
   - 일반 조회: < 500ms
   - 복잡한 검색: < 2s
   - 파일 업로드: < 10s
7. **데이터 보안**:
   - 개인정보 마스킹 처리
   - 민감 정보 암호화
   - HTTPS 필수
8. **버전 관리**: API 버전은 헤더에 명시 (X-API-Version: 1.0)

---

*이 문서는 직직직 사업자 앱의 모든 API를 완전하게 정의한 것입니다. 실제 구현 시 세부 사항이 조정될 수 있습니다.*

**마지막 업데이트**: 2024-01-15
**문서 버전**: 4.0
**작성자**: 직직직 개발팀