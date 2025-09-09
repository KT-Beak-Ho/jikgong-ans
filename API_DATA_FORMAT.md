# 직직직 사업자 앱 API 데이터 형식 문서

## 📋 변경 이력
- **2024-01-15 v3.0**: 프로젝트 관리 API 전면 개편
  - 프로젝트 생성: 4개 필수 필드로 단순화 (title, startDate, endDate, location)
  - 프로젝트 목록: wage/worker 정보 제거, 날짜 기반 진행률 추가
  - 진행률 계산 방식 변경: 인력 기반 → 날짜 기반
  - 작업일별 관리 API 추가 (출근체크, 퇴근체크, 임금지급)
  - 인부 지원 현황 API 추가 (날짜별 지원자/확정자 관리)
  - 지급내역서 및 영수증 발급 API 추가
  - 일자리 등록 API 추가 (프로젝트 내 작업일 추가)
  - 플랫폼 수수료 5% 명시 (기존 10% 대비 50% 절감)

## 📋 목차
1. [인증 API](#1-인증-api-auth)
   - 1.1 SMS 인증 요청
   - 1.2 SMS 인증 확인
   - 1.3 사업자 가입
   - 1.4 로그인
2. [프로젝트 관리 API](#2-프로젝트-관리-api-projectlist)
   - 2.1 프로젝트 목록 조회
   - 2.2 프로젝트 생성
   - 2.3 프로젝트 작업일 관리
   - 2.4 인부 지원 현황 관리
   - 2.5 출근/퇴근 체크
   - 2.6 임금 지급 내역서
   - 2.7 일자리 등록 (작업일 추가)
3. [인력 스카우트 API](#3-인력-스카우트-api-scout)
   - 3.1 인력 검색
   - 3.2 인력 상세 조회
   - 3.3 스카우트 제안
   - 3.4 제안 목록 조회
4. [자금 관리 API](#4-자금-관리-api-money)
   - 4.1 정산 목록 조회
   - 4.2 급여 계산
   - 4.3 일괄 급여 지급
   - 4.4 송금 처리
5. [사업자 정보 API](#5-사업자-정보-api-info)
   - 5.1 프로필 조회
   - 5.2 통계 조회
   - 5.3 공지사항
6. [공통 Response 형식](#6-공통-response-형식)
   - 6.1 성공 Response
   - 6.2 에러 Response
   - 6.3 페이지네이션 정보
   - 6.4 API 에러 코드

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
- sortBy: String (createdAt, startDate, endDate)

// Response
data class ProjectListResponse(
    val success: Boolean,
    val data: List<SimpleProject>,
    val pagination: PaginationInfo
)

data class SimpleProject(
    val id: String,
    val title: String,
    val location: String,         // "서울특별시 강남구 역삼동"
    val locationDetail: String?,  // "101동 지하 1층"
    val startDate: String,        // "2024-03-01"
    val endDate: String,          // "2024-06-30"
    val category: String,         // "건축", "토목", "전기", "설비"
    val status: String,           // "RECRUITING", "IN_PROGRESS", "COMPLETED"
    val isUrgent: Boolean,
    val progressPercentage: Int,  // 날짜 기반 진행률 (0-100)
    val totalDays: Int,           // 전체 공사 기간 (일)
    val elapsedDays: Int,         // 경과 일수
    val remainingDays: Int,       // 남은 일수
    val createdAt: String,
    val updatedAt: String
)

// 예시 응답
{
    "success": true,
    "data": [
        {
            "id": "proj_123456",
            "title": "강남 오피스텔 신축 공사",
            "location": "서울특별시 강남구 역삼동",
            "locationDetail": "101동",
            "startDate": "2024-03-01",
            "endDate": "2024-06-30",
            "category": "건축",
            "status": "IN_PROGRESS",
            "isUrgent": false,
            "progressPercentage": 45,
            "totalDays": 121,
            "elapsedDays": 54,
            "remainingDays": 67,
            "createdAt": "2024-01-15T09:00:00Z",
            "updatedAt": "2024-04-24T15:30:00Z"
        },
        {
            "id": "proj_789012",
            "title": "판교 오피스 리모델링",
            "location": "경기도 성남시 분당구 판교동",
            "locationDetail": null,
            "startDate": "2024-05-01",
            "endDate": "2024-07-31",
            "category": "건축",
            "status": "RECRUITING",
            "isUrgent": true,
            "progressPercentage": 0,
            "totalDays": 91,
            "elapsedDays": 0,
            "remainingDays": 91,
            "createdAt": "2024-04-20T10:00:00Z",
            "updatedAt": "2024-04-20T10:00:00Z"
        }
    ],
    "pagination": {
        "page": 1,
        "size": 20,
        "totalPages": 5,
        "totalElements": 87
    }
}
```

### 2.2 프로젝트 생성
```kotlin
// Request
POST /api/projects
data class ProjectCreateRequest(
    val title: String,           // 프로젝트 이름 (필수)
    val startDate: String,        // 착공일 ISO 8601 형식 "2024-03-01" (필수)
    val endDate: String,          // 준공일 ISO 8601 형식 "2024-06-30" (필수)
    val location: String,         // 작업장소 주소 "서울특별시 강남구 역삼동 123-45" (필수)
    val locationDetail: String?,  // 상세 주소 "101동 지하 1층" (선택)
    val latitude: Double,         // 위도 37.5665 (필수)
    val longitude: Double         // 경도 126.9780 (필수)
)

// Response
data class ProjectCreateResponse(
    val success: Boolean,
    val projectId: String,
    val message: String,
    val project: ProjectDetail
)

data class ProjectDetail(
    val id: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val locationDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val status: String,           // "RECRUITING", "IN_PROGRESS", "COMPLETED"
    val progressPercentage: Int,  // 날짜 기반 진행률 (0-100)
    val createdAt: String,
    val updatedAt: String
)

// Error Response
data class ProjectCreateErrorResponse(
    val success: Boolean,
    val error: String,
    val details: Map<String, String>?  // 필드별 에러 메시지
)

// 예시 요청
{
    "title": "강남 오피스텔 신축 공사",
    "startDate": "2024-03-01",
    "endDate": "2024-06-30",
    "location": "서울특별시 강남구 역삼동 123-45",
    "locationDetail": "101동 지하 1층",
    "latitude": 37.5665,
    "longitude": 126.9780
}

// 예시 성공 응답
{
    "success": true,
    "projectId": "proj_123456",
    "message": "프로젝트가 성공적으로 생성되었습니다",
    "project": {
        "id": "proj_123456",
        "title": "강남 오피스텔 신축 공사",
        "startDate": "2024-03-01",
        "endDate": "2024-06-30",
        "location": "서울특별시 강남구 역삼동 123-45",
        "locationDetail": "101동 지하 1층",
        "latitude": 37.5665,
        "longitude": 126.9780,
        "status": "RECRUITING",
        "progressPercentage": 0,
        "createdAt": "2024-01-15T09:00:00Z",
        "updatedAt": "2024-01-15T09:00:00Z"
    }
}

// 예시 실패 응답
{
    "success": false,
    "error": "VALIDATION_ERROR",
    "details": {
        "title": "프로젝트 이름은 필수입니다",
        "endDate": "준공일은 착공일 이후여야 합니다"
    }
}
```

### 2.3 프로젝트 작업일 관리
```kotlin
// Request - 작업일별 상세 조회
GET /api/projects/{projectId}/workdays
Query Parameters:
- status: String? (IN_PROGRESS, UPCOMING, COMPLETED)
- month: String? ("2024-03")

// Response
data class WorkDayListResponse(
    val success: Boolean,
    val data: List<WorkDay>,
    val summary: WorkDaySummary
)

data class WorkDay(
    val id: String,
    val projectId: String,
    val title: String,           // "보통인부 15명 모집"
    val date: String,             // "2024-03-15"
    val startTime: String,        // "08:00"
    val endTime: String,          // "18:00"
    val recruitPeriod: String,    // "2024-03-01 ~ 2024-03-07"
    val applicants: Int,          // 날짜별 지원자 수
    val confirmed: Int,           // 확정된 인원
    val maxWorkers: Int,          // 최대 모집 인원
    val status: String,           // "IN_PROGRESS", "UPCOMING", "COMPLETED"
    val attendanceInfo: AttendanceInfo?
)

data class AttendanceInfo(
    val hasCheckedIn: Boolean,
    val hasCheckedOut: Boolean,
    val hasPaymentRecord: Boolean,
    val checkedInCount: Int,     // 출근 체크한 인원
    val checkedOutCount: Int     // 퇴근 체크한 인원
)

data class WorkDaySummary(
    val totalWorkDays: Int,
    val completedDays: Int,
    val upcomingDays: Int,
    val inProgressDays: Int
)
```

### 2.4 인부 지원 현황 관리
```kotlin
// Request - 날짜별 지원자 조회
GET /api/projects/{projectId}/workdays/{date}/applicants

// Response
data class ApplicantListResponse(
    val success: Boolean,
    val projectId: String,
    val date: String,
    val applicants: List<ApplicantWorker>,
    val confirmedWorkers: List<ConfirmedWorker>
)

data class ApplicantWorker(
    val id: String,
    val name: String,
    val jobType: String,          // "철근공", "목수", "보통인부"
    val experienceYears: Int,
    val phone: String,
    val distance: String,          // "2.5km"
    val rating: Double,            // 4.5
    val completedProjects: Int,    // 완료한 프로젝트 수
    val status: String,            // "PENDING", "ACCEPTED", "REJECTED"
    val appliedAt: String,
    val profileImage: String?
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
    val checkInTime: String?,     // "08:05"
    val checkOutTime: String?,    // "18:10"
    val workHours: Double?        // 10.08
)

// Request - 지원자 수락/거절
POST /api/projects/{projectId}/applicants/{applicantId}/status
data class ApplicantStatusRequest(
    val action: String,           // "ACCEPT" or "REJECT"
    val workDate: String,
    val reason: String?           // 거절 사유 (선택)
)

// Response
data class ApplicantStatusResponse(
    val success: Boolean,
    val message: String,
    val updatedApplicant: ApplicantWorker
)
```

### 2.5 출근/퇴근 체크
```kotlin
// Request - 출근 체크
POST /api/projects/{projectId}/attendance/checkin
data class AttendanceCheckInRequest(
    val workDate: String,
    val workers: List<WorkerCheckIn>
)

data class WorkerCheckIn(
    val workerId: String,
    val checkInTime: String,      // "08:05"
    val location: LocationInfo?
)

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float
)

// Response
data class AttendanceCheckResponse(
    val success: Boolean,
    val checkedInCount: Int,
    val message: String,
    val results: List<CheckResult>
)

data class CheckResult(
    val workerId: String,
    val workerName: String,
    val success: Boolean,
    val checkTime: String,
    val message: String?
)

// Request - 퇴근 체크
POST /api/projects/{projectId}/attendance/checkout
data class AttendanceCheckOutRequest(
    val workDate: String,
    val workers: List<WorkerCheckOut>
)

data class WorkerCheckOut(
    val workerId: String,
    val checkOutTime: String,     // "18:10"
    val workHours: Double,        // 10.08
    val overtimeHours: Double?    // 2.0
)

// Request - 출근 현황 조회
GET /api/projects/{projectId}/attendance/{date}

// Response
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
    val totalWorkHours: Double
)

data class WorkerAttendanceDetail(
    val workerId: String,
    val workerName: String,
    val jobType: String,
    val checkInTime: String?,
    val checkOutTime: String?,
    val workHours: Double?,
    val status: String,           // "CHECKED_IN", "CHECKED_OUT", "ABSENT"
    val wage: Int,
    val totalWage: Long
)
```

### 2.6 임금 지급 내역서
```kotlin
// Request - 프로젝트별 임금 지급 내역 조회
GET /api/projects/{projectId}/payments
Query Parameters:
- date: String? ("2024-03-15")
- status: String? (PENDING, COMPLETED, FAILED)

// Response  
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
    val deductions: Long,         // 공제액 (플랫폼 수수료 등)
    val finalAmount: Long,        // 실지급액
    val status: String,           // "PENDING", "COMPLETED", "FAILED"
    val paymentDate: String?,
    val bankName: String,
    val accountNumber: String,
    val transactionId: String?
)

data class PaymentSummary(
    val totalWorkers: Int,
    val totalAmount: Long,
    val paidAmount: Long,
    val pendingAmount: Long,
    val completedCount: Int,
    val pendingCount: Int,
    val platformFee: Long,        // 플랫폼 수수료 (5%)
    val savedAmount: Long         // 기존 10% → 5%로 절약한 금액
)

// Request - 임금 지급 승인
POST /api/projects/{projectId}/payments/approve
data class PaymentApprovalRequest(
    val paymentIds: List<String>,
    val approvalType: String      // "INDIVIDUAL" or "BULK"
)

// Response
data class PaymentApprovalResponse(
    val success: Boolean,
    val approvedCount: Int,
    val failedCount: Int,
    val totalAmount: Long,
    val message: String,
    val results: List<ApprovalResult>
)

data class ApprovalResult(
    val paymentId: String,
    val workerId: String,
    val success: Boolean,
    val amount: Long,
    val transactionId: String?,
    val errorMessage: String?
)

// Request - 지급 내역서 다운로드
GET /api/projects/{projectId}/payments/receipt/{paymentId}

// Response
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
    val downloadUrl: String       // PDF 다운로드 URL
)

data class ProjectInfo(
    val projectId: String,
    val projectTitle: String,
    val company: String,
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
    val workDate: String,
    val workHours: Double,
    val hourlyWage: Int,
    val baseWage: Long,
    val overtimePay: Long?,
    val deductions: Long,
    val platformFee: Long,
    val finalAmount: Long,
    val paymentMethod: String,
    val paymentDate: String,
    val transactionId: String
)
```

### 2.7 일자리 등록 (작업일 추가)
```kotlin
// Request - 프로젝트에 새 작업일 추가
POST /api/projects/{projectId}/workdays
data class JobRegistrationRequest(
    val title: String,            // "보통인부 15명 모집"
    val date: String,             // "2024-03-20"
    val startTime: String,        // "08:00"
    val endTime: String,          // "18:00"
    val maxWorkers: Int,          // 15
    val jobTypes: List<JobTypeRequirement>,
    val wage: WageInfo,
    val requirements: List<String>?,
    val benefits: List<String>?
)

data class JobTypeRequirement(
    val jobType: String,          // "철근공"
    val requiredCount: Int,       // 5
    val experienceLevel: String?  // "3년 이상"
)

data class WageInfo(
    val type: String,             // "DAILY" or "HOURLY"
    val amount: Int,              // 200000 (일당) or 25000 (시급)
    val overtimeRate: Double?     // 1.5 (시간외 수당 배율)
)

// Response
data class JobRegistrationResponse(
    val success: Boolean,
    val workDayId: String,
    val message: String,
    val workDay: WorkDay
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