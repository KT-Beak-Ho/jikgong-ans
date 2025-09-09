# 직직직 사업자 앱 API 데이터 형식 문서

## 📋 목차
1. [인증 API](#1-인증-api-auth)
2. [프로젝트 관리 API](#2-프로젝트-관리-api-projectlist)
3. [인력 스카우트 API](#3-인력-스카우트-api-scout)
4. [자금 관리 API](#4-자금-관리-api-money)
5. [사업자 정보 API](#5-사업자-정보-api-info)
6. [공통 Response 형식](#6-공통-response-형식)

---

## 1. 인증 API (Auth)

### 1.1 SMS 인증 요청
```kotlin
// Request
POST /api/auth/sms/send
data class SendSMSRequest(
    val phoneNumber: String  // "010-1234-5678"
)

// Response
data class SendSMSResponse(
    val success: Boolean,
    val message: String,
    val verificationId: String,  // 인증 세션 ID
    val expiresAt: Long         // 만료 시간 (Unix timestamp)
)
```

### 1.2 SMS 인증 확인
```kotlin
// Request
POST /api/auth/sms/verify
data class VerifySMSRequest(
    val phoneNumber: String,
    val verificationCode: String,  // 6자리 인증번호
    val verificationId: String
)

// Response
data class VerifySMSResponse(
    val success: Boolean,
    val message: String,
    val verified: Boolean,
    val tempToken: String?  // 임시 토큰 (회원가입 진행용)
)
```

### 1.3 회원가입
```kotlin
// Request
POST /api/auth/company/register
data class CompanyRegisterRequest(
    val tempToken: String,           // SMS 인증 후 받은 토큰
    val businessNumber: String,      // 사업자등록번호
    val companyName: String,
    val representativeName: String,
    val businessType: String,         // 업종
    val businessAddress: String,
    val detailAddress: String,
    val loginId: String,
    val password: String,
    val email: String,
    val marketingAgree: Boolean,
    val insuranceInfo: InsuranceInfo?
)

data class InsuranceInfo(
    val hasInsurance: Boolean,
    val insuranceType: String?,      // "산재보험", "고용보험" 등
    val insuranceNumber: String?
)

// Response
data class CompanyRegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val companyInfo: CompanyData
)
```

### 1.4 로그인
```kotlin
// Request
POST /api/auth/login
data class LoginRequest(
    val loginId: String,
    val password: String,
    val deviceToken: String?,  // FCM 토큰
    val autoLogin: Boolean
)

// Response
data class LoginResponse(
    val success: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Int,        // 초 단위
    val user: UserInfo
)

data class UserInfo(
    val userId: String,
    val userType: String,       // "COMPANY"
    val name: String,
    val companyId: String,
    val companyName: String,
    val profileImageUrl: String?,
    val lastLoginAt: String
)
```

---

## 2. 프로젝트 관리 API (ProjectList)

### 2.1 프로젝트 목록 조회
```kotlin
// Request
GET /api/projects
Query Parameters:
- status: String? (RECRUITING, IN_PROGRESS, COMPLETED)
- page: Int
- size: Int
- sortBy: String (createdAt, startDate, wage)

// Response
data class ProjectListResponse(
    val success: Boolean,
    val data: List<SimpleProject>,
    val pagination: PaginationInfo
)

data class SimpleProject(
    val id: String,
    val title: String,
    val location: String,
    val detailLocation: String,
    val startDate: String,        // "2025-08-01"
    val endDate: String,
    val workStartTime: String,    // "08:00"
    val workEndTime: String,      // "17:00"
    val wage: Int,               // 일당
    val wageType: String,        // "DAILY", "HOURLY"
    val jobCategories: List<JobCategory>,
    val currentWorkers: Int,
    val requiredWorkers: Int,
    val status: String,
    val isUrgent: Boolean,
    val createdAt: String,
    val daysRemaining: Int?      // 모집 마감까지 남은 일수
)

data class JobCategory(
    val categoryId: String,
    val categoryName: String,     // "목수", "철근공", "조공"
    val requiredCount: Int,
    val confirmedCount: Int,
    val skillLevel: String       // "BEGINNER", "INTERMEDIATE", "EXPERT"
)
```

### 2.2 프로젝트 생성
```kotlin
// Request
POST /api/projects
data class ProjectCreateRequest(
    val title: String,
    val description: String,
    val location: Address,
    val workPeriod: WorkPeriod,
    val jobRequirements: List<JobRequirement>,
    val paymentInfo: PaymentInfo,
    val additionalInfo: ProjectAdditionalInfo
)

data class Address(
    val roadAddress: String,
    val jibunAddress: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double,
    val zipCode: String
)

data class WorkPeriod(
    val startDate: String,
    val endDate: String,
    val workDays: List<String>,   // ["MON", "TUE", "WED", ...]
    val workStartTime: String,
    val workEndTime: String,
    val breakTime: BreakTime?
)

data class BreakTime(
    val startTime: String,
    val endTime: String
)

data class JobRequirement(
    val jobCategoryId: String,
    val requiredCount: Int,
    val skillLevel: String,
    val description: String?,
    val preferredExperience: Int?  // 년 단위
)

data class PaymentInfo(
    val wageType: String,
    val wage: Int,
    val overtimePay: Int?,        // 시간외 수당
    val weekendPay: Int?,         // 주말 수당
    val paymentMethod: String,    // "DAILY", "WEEKLY", "MONTHLY"
    val paymentDay: Int?          // 월급일 (1-31)
)

data class ProjectAdditionalInfo(
    val provideMeals: Boolean,
    val provideTransport: Boolean,
    val provideAccommodation: Boolean,
    val safetyEquipment: List<String>,  // ["안전모", "안전화", ...]
    val requirements: List<String>,      // 기타 요구사항
    val isUrgent: Boolean,
    val isRecurring: Boolean,
    val recurringInfo: RecurringInfo?
)

data class RecurringInfo(
    val frequency: String,         // "WEEKLY", "MONTHLY"
    val endDate: String?
)
```

### 2.3 출근 체크
```kotlin
// Request
POST /api/projects/{projectId}/attendance
data class AttendanceCheckRequest(
    val projectId: String,
    val workDate: String,
    val attendanceList: List<WorkerAttendance>
)

data class WorkerAttendance(
    val workerId: String,
    val status: String,           // "PRESENT", "ABSENT", "LATE", "EARLY_LEAVE"
    val checkInTime: String?,
    val checkOutTime: String?,
    val checkInMethod: String,    // "QR", "GPS", "MANUAL"
    val gpsLocation: GpsLocation?,
    val note: String?
)

data class GpsLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float
)

// Response
data class AttendanceCheckResponse(
    val success: Boolean,
    val message: String,
    val processedCount: Int,
    val failedList: List<FailedAttendance>?
)

data class FailedAttendance(
    val workerId: String,
    val reason: String
)
```

---

## 3. 인력 스카우트 API (Scout)

### 3.1 인력 검색
```kotlin
// Request
GET /api/scout/workers
Query Parameters:
- latitude: Double
- longitude: Double
- radius: Int (km)
- jobCategories: String (comma separated)
- minExperience: Int?
- maxWage: Int?
- availableDays: String?
- useAiRecommendation: Boolean

// Response
data class WorkerListResponse(
    val success: Boolean,
    val data: List<Worker>,
    val totalCount: Int,
    val aiRecommended: List<String>?  // AI 추천 worker IDs
)

data class Worker(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val phoneNumber: String,
    val location: String,
    val distance: Double,         // km 단위
    val profileImage: String?,
    val jobCategories: List<String>,
    val primaryJob: String,
    val experience: Int,          // 년 단위
    val rating: Float,            // 0.0 ~ 5.0
    val completedProjects: Int,
    val attendanceRate: Float,    // 0.0 ~ 100.0
    val reemploymentRate: Float,  // 0.0 ~ 100.0
    val certifications: List<String>,
    val badges: List<WorkerBadge>,
    val preferredWage: Int,
    val availableDays: List<String>,
    val isVerified: Boolean,
    val hasInsurance: Boolean,
    val lastActiveAt: String,
    val aiMatchScore: Float?      // AI 매칭 점수 (0.0 ~ 100.0)
)

data class WorkerBadge(
    val type: String,             // "EXCELLENT", "VERIFIED", "CERTIFIED", "INSURED"
    val name: String,
    val icon: String
)
```

### 3.2 인력 상세 정보
```kotlin
// Request
GET /api/scout/workers/{workerId}

// Response
data class WorkerDetailResponse(
    val success: Boolean,
    val data: WorkerDetail
)

data class WorkerDetail(
    val basicInfo: Worker,
    val workHistory: List<WorkHistory>,
    val reviews: List<WorkerReview>,
    val skills: List<Skill>,
    val availability: Availability,
    val documents: List<Document>
)

data class WorkHistory(
    val projectId: String,
    val projectName: String,
    val companyName: String,
    val period: String,
    val jobCategory: String,
    val rating: Float?,
    val review: String?
)

data class WorkerReview(
    val reviewId: String,
    val projectId: String,
    val companyName: String,
    val rating: Float,
    val comment: String,
    val createdAt: String,
    val tags: List<String>        // ["성실함", "시간엄수", ...]
)

data class Skill(
    val category: String,
    val level: String,            // "BEGINNER", "INTERMEDIATE", "EXPERT"
    val experience: Int
)

data class Availability(
    val availableDays: List<String>,
    val preferredAreas: List<String>,
    val maxDistance: Int,
    val unavailableDates: List<String>
)

data class Document(
    val type: String,             // "CERTIFICATION", "LICENSE", "INSURANCE"
    val name: String,
    val issuedBy: String,
    val issuedDate: String,
    val expiryDate: String?,
    val verified: Boolean
)
```

### 3.3 제안 발송
```kotlin
// Request
POST /api/scout/proposals
data class ProposalRequest(
    val workerIds: List<String>,
    val projectId: String,
    val proposalDetails: ProposalDetails
)

data class ProposalDetails(
    val message: String,
    val wage: Int,
    val workPeriod: WorkPeriod,
    val benefits: List<String>,
    val expiresAt: String         // 제안 만료 시간
)

// Response
data class ProposalResponse(
    val success: Boolean,
    val proposalIds: List<String>,
    val sentCount: Int,
    val failedWorkers: List<FailedProposal>?
)

data class FailedProposal(
    val workerId: String,
    val reason: String
)
```

### 3.4 제안 목록 조회
```kotlin
// Request
GET /api/scout/proposals
Query Parameters:
- status: String? (PENDING, VIEWED, ACCEPTED, REJECTED, EXPIRED)
- projectId: String?
- page: Int
- size: Int

// Response
data class ProposalListResponse(
    val success: Boolean,
    val data: List<Proposal>,
    val pagination: PaginationInfo
)

data class Proposal(
    val id: String,
    val projectId: String,
    val projectName: String,
    val worker: ProposalWorker,
    val status: String,
    val message: String,
    val wage: Int,
    val sentAt: String,
    val viewedAt: String?,
    val respondedAt: String?,
    val expiresAt: String,
    val response: ProposalResponse?
)

data class ProposalWorker(
    val id: String,
    val name: String,
    val profileImage: String?,
    val rating: Float,
    val primaryJob: String
)

data class ProposalResponse(
    val accepted: Boolean,
    val message: String?,
    val counterOffer: CounterOffer?
)

data class CounterOffer(
    val wage: Int,
    val availableDates: List<String>,
    val message: String
)
```

---

## 4. 자금 관리 API (Money)

### 4.1 정산 목록 조회
```kotlin
// Request
GET /api/payments
Query Parameters:
- status: String? (PENDING, PROCESSING, COMPLETED, FAILED)
- projectId: String?
- startDate: String?
- endDate: String?
- page: Int
- size: Int

// Response
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
    val serviceFee: Long,          // 플랫폼 수수료 (5%)
    val savedAmount: Long,         // 절감액 (기존 대비)
    val finalAmount: Long,         // 실 지급액
    val isPaid: Boolean,
    val paidAt: String?
)

data class WorkerPaymentInfo(
    val workerId: String,
    val workerName: String,
    val phoneNumber: String,
    val bankName: String,
    val accountNumber: String,
    val workDays: Int,
    val baseWage: Long,
    val overtimePay: Long,
    val weekendPay: Long,
    val deductions: Long,
    val totalAmount: Long,
    val paymentStatus: String,
    val paymentMethod: String      // "BANK_TRANSFER", "CASH"
)

data class PaymentSummary(
    val totalPending: Long,
    val totalCompleted: Long,
    val totalFailed: Long,
    val pendingCount: Int,
    val completedCount: Int,
    val monthlyTotal: Long,
    val monthlySaved: Long,        // 이번 달 절감액
    val yearlyProjected: Long      // 연간 예상 절감액
)
```

### 4.2 급여 계산
```kotlin
// Request
POST /api/payments/calculate
data class PaymentCalculateRequest(
    val projectId: String,
    val workDate: String,
    val workers: List<WorkerPaymentCalc>
)

data class WorkerPaymentCalc(
    val workerId: String,
    val attendanceStatus: String,
    val workHours: Float,
    val overtimeHours: Float,
    val isWeekend: Boolean,
    val isHoliday: Boolean,
    val additionalPay: Long?,
    val deductions: Long?
)

// Response
data class PaymentCalculateResponse(
    val success: Boolean,
    val calculations: List<PaymentCalculation>,
    val summary: CalculationSummary
)

data class PaymentCalculation(
    val workerId: String,
    val baseWage: Long,
    val overtimePay: Long,
    val weekendPay: Long,
    val holidayPay: Long,
    val additionalPay: Long,
    val subtotal: Long,
    val deductions: Long,
    val serviceFee: Long,          // 5% 수수료
    val originalServiceFee: Long,  // 10% 기준 (비교용)
    val savedAmount: Long,
    val finalAmount: Long
)

data class CalculationSummary(
    val totalWorkers: Int,
    val totalBaseWage: Long,
    val totalAdditionalPay: Long,
    val totalDeductions: Long,
    val totalServiceFee: Long,
    val totalSaved: Long,
    val totalFinalAmount: Long
)
```

### 4.3 일괄 송금
```kotlin
// Request
POST /api/payments/bulk-transfer
data class BulkTransferRequest(
    val paymentIds: List<String>,
    val transferDetails: TransferDetails
)

data class TransferDetails(
    val transferDate: String,
    val memo: String?,
    val sendNotification: Boolean,
    val generateTaxInvoice: Boolean
)

// Response
data class BulkTransferResponse(
    val success: Boolean,
    val transferId: String,
    val totalAmount: Long,
    val transferCount: Int,
    val successCount: Int,
    val failedTransfers: List<FailedTransfer>?,
    val estimatedCompletionTime: String
)

data class FailedTransfer(
    val paymentId: String,
    val workerId: String,
    val reason: String,
    val canRetry: Boolean
)
```

### 4.4 절감액 통계
```kotlin
// Request
GET /api/payments/savings-stats
Query Parameters:
- period: String (DAILY, WEEKLY, MONTHLY, YEARLY)
- startDate: String?
- endDate: String?

// Response
data class SavingsStatsResponse(
    val success: Boolean,
    val period: String,
    val stats: SavingsStats
)

data class SavingsStats(
    val totalProjects: Int,
    val totalWorkers: Int,
    val totalPayments: Long,
    val originalServiceFee: Long,   // 10% 기준
    val currentServiceFee: Long,    // 5% 실제
    val totalSaved: Long,
    val savingsRate: Float,         // 절감률 (%)
    val periodComparison: PeriodComparison,
    val projectedYearlySavings: Long,
    val chartData: List<ChartDataPoint>
)

data class PeriodComparison(
    val previousPeriodSaved: Long,
    val currentPeriodSaved: Long,
    val growthRate: Float
)

data class ChartDataPoint(
    val date: String,
    val saved: Long,
    val projects: Int
)
```

---

## 5. 사업자 정보 API (Info)

### 5.1 사업자 프로필 조회
```kotlin
// Request
GET /api/company/profile

// Response
data class CompanyProfileResponse(
    val success: Boolean,
    val data: CompanyData
)

data class CompanyData(
    val companyId: String,
    val businessNumber: String,
    val companyName: String,
    val representativeName: String,
    val businessType: String,
    val businessAddress: String,
    val phoneNumber: String,
    val email: String,
    val profileImage: String?,
    val rating: Float,
    val reviewCount: Int,
    val completedProjects: Int,
    val totalHired: Int,
    val memberSince: String,
    val verificationStatus: VerificationStatus,
    val premiumStatus: PremiumStatus?,
    val badges: List<CompanyBadge>
)

data class VerificationStatus(
    val businessVerified: Boolean,
    val phoneVerified: Boolean,
    val emailVerified: Boolean,
    val documentVerified: Boolean
)

data class PremiumStatus(
    val isPremium: Boolean,
    val plan: String?,             // "BASIC", "STANDARD", "PREMIUM"
    val expiresAt: String?,
    val benefits: List<String>
)

data class CompanyBadge(
    val type: String,
    val name: String,
    val description: String,
    val earnedAt: String
)
```

### 5.2 통계 조회
```kotlin
// Request
GET /api/company/stats
Query Parameters:
- period: String (WEEKLY, MONTHLY, YEARLY)

// Response
data class CompanyStatsResponse(
    val success: Boolean,
    val period: String,
    val stats: CompanyStats
)

data class CompanyStats(
    val hiringStats: HiringStats,
    val paymentStats: PaymentStats,
    val performanceStats: PerformanceStats,
    val savingsStats: SavingsOverview
)

data class HiringStats(
    val totalHired: Int,
    val byJobCategory: Map<String, Int>,
    val averageRating: Float,
    val reemploymentRate: Float,
    val topWorkers: List<TopWorker>
)

data class PaymentStats(
    val totalPaid: Long,
    val averageWage: Long,
    val onTimePaymentRate: Float,
    val paymentMethods: Map<String, Int>
)

data class PerformanceStats(
    val projectCompletionRate: Float,
    val averageProjectDuration: Int,
    val workerSatisfaction: Float,
    val responseRate: Float
)

data class SavingsOverview(
    val totalSaved: Long,
    val monthlyAverage: Long,
    val projectedYearly: Long,
    val comparedToCompetitors: Float  // 타사 대비 절감률
)

data class TopWorker(
    val workerId: String,
    val name: String,
    val projectCount: Int,
    val rating: Float
)
```

### 5.3 공지사항
```kotlin
// Request
GET /api/notices
Query Parameters:
- category: String? (SYSTEM, UPDATE, EVENT, MAINTENANCE)
- page: Int
- size: Int

// Response
data class NoticeListResponse(
    val success: Boolean,
    val data: List<Notice>,
    val pagination: PaginationInfo
)

data class Notice(
    val id: String,
    val category: String,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: String,
    val updatedAt: String?,
    val isImportant: Boolean,
    val isPinned: Boolean,
    val attachments: List<Attachment>?,
    val viewCount: Int
)

data class Attachment(
    val id: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String,
    val downloadUrl: String
)
```

---

## 6. 공통 Response 형식

### 6.1 성공 Response
```kotlin
data class ApiResponse<T>(
    val success: Boolean = true,
    val message: String = "Success",
    val data: T?,
    val timestamp: Long = System.currentTimeMillis()
)
```

### 6.2 에러 Response
```kotlin
data class ErrorResponse(
    val success: Boolean = false,
    val error: ErrorDetail,
    val timestamp: Long = System.currentTimeMillis()
)

data class ErrorDetail(
    val code: String,              // "AUTH_001"
    val message: String,
    val details: Map<String, Any>?,
    val path: String,
    val method: String
)
```

### 6.3 페이지네이션 정보
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

### 6.4 API 에러 코드
```kotlin
// 인증 관련
AUTH_001: 인증 실패
AUTH_002: 토큰 만료
AUTH_003: 권한 없음
AUTH_004: 중복된 계정

// 프로젝트 관련
PROJECT_001: 프로젝트 없음
PROJECT_002: 모집 마감
PROJECT_003: 수정 권한 없음

// 결제 관련
PAYMENT_001: 잔액 부족
PAYMENT_002: 계좌 정보 오류
PAYMENT_003: 이미 처리됨

// 시스템 관련
SYSTEM_001: 서버 오류
SYSTEM_002: 유지보수 중
SYSTEM_003: 요청 제한 초과
```

---

## 📌 참고사항

1. **날짜/시간 형식**: ISO 8601 형식 사용 (예: "2025-08-01T09:00:00Z")
2. **금액 단위**: 원(KRW) 단위, Long 타입 사용
3. **인증**: Bearer 토큰 방식 (Authorization: Bearer {token})
4. **API 버전**: /api/v1 prefix 사용
5. **Rate Limiting**: 분당 100회 요청 제한
6. **파일 업로드**: multipart/form-data 사용, 최대 10MB