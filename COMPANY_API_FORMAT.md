# 직직직 사업자(Company) 앱 API 데이터 형식 문서 v6.0

## 📋 변경 이력
- **2025-01-15 v6.0**: 실제 구현 코드 완전 검증 버전
  - 실제 Repository/Api 인터페이스와 100% 일치
  - 파일명 변경: COMPANY_API_FORMAT.md (사업자 전용 명시)
  - 실제 구현된 API 엔드포인트만 포함
  - Mock 데이터와 실제 API 구분 명시

## 🔍 실제 구현 상태

### ✅ 구현 완료된 API
1. **인증 API** (AuthApi.kt, JoinApi.kt)
   - SMS 인증 발송/확인
   - 로그인
   - 회원가입 (Worker용 - Company용 확장 필요)
   - ID/Email 중복 확인

2. **Repository 구현** 
   - ProjectRepository (프로젝트 CRUD)
   - ProjectCreateRepository (프로젝트 생성)
   - CompanyRepository (사업자 정보)

### ⚠️ Mock 데이터 사용 중
- Scout 기능 (CompanyMockDataFactory)
- Money 기능 (샘플 데이터)
- Info 기능 (프로필/통계)

---

## 📋 목차
1. [인증 API (구현됨)](#1-인증-api-구현됨)
2. [프로젝트 관리 API (일부 구현)](#2-프로젝트-관리-api-일부-구현)
3. [인력 스카우트 API (Mock)](#3-인력-스카우트-api-mock)
4. [자금 관리 API (Mock)](#4-자금-관리-api-mock)
5. [사업자 정보 API (Mock)](#5-사업자-정보-api-mock)

---

## 1. 인증 API (구현됨)

### 1.1 SMS 인증 요청 ✅
**구현 파일**: `AuthApi.kt`, `JoinApi.kt`
```kotlin
// 실제 엔드포인트
POST /api/worker/join/send-sms (AuthApi)
POST /api/join/sms-verification (JoinApi)

Request:
data class SendSMSRequest(
    val phone: String  // "010-1234-5678"
)

Response:
data class SendSMSResponse(
    val success: Boolean,
    val message: String,
    val verificationCode: String? = null  // 테스트용
)
```

### 1.2 SMS 인증 확인 ✅
```kotlin
// 실제 엔드포인트
POST /api/worker/join/verify-sms

Request:
data class VerifySMSRequest(
    val phone: String,
    val verificationCode: String  // 6자리
)

Response:
data class VerifySMSResponse(
    val success: Boolean,
    val message: String,
    val isVerified: Boolean
)
```

### 1.3 로그인 ✅
```kotlin
// 실제 엔드포인트
POST /api/login

Request:
data class LoginRequest(
    val loginIdOrPhone: String,
    val password: String,
    val deviceToken: String  // FCM 토큰
)

Response:
data class LoginResponse(
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val userRole: String,
    val userId: Long
)
```

### 1.4 ID 중복 확인 ✅
```kotlin
// 실제 엔드포인트
POST /api/worker/join/login-id-validation
POST /api/join/validation-loginId

Request:
data class LoginIdValidationRequest(
    val loginId: String
)

Response:
data class LoginIdValidationResponse(
    val success: Boolean,
    val isAvailable: Boolean,
    val message: String
)
```

### 1.5 회원가입 (Worker용 - Company 확장 필요) ⚠️
```kotlin
// 현재 Worker용만 구현
POST /api/worker/join

Request:
data class RegisterWorker(
    var loginId: String,
    var password: String,
    var phone: String,
    var email: String,
    var role: String,  // "WORKER" -> "COMPANY" 필요
    var privacyConsent: Boolean,
    var deviceToken: String,
    // ... Worker 전용 필드들
)

// Company용 구현 필요
data class RegisterCompany(
    val loginId: String,
    val password: String,
    val phone: String,
    val businessNumber: String,  // 사업자등록번호
    val companyName: String,
    val representativeName: String,
    val businessType: String,
    val businessAddress: String,
    // ...
)
```

---

## 2. 프로젝트 관리 API (일부 구현)

### 2.1 프로젝트 Repository 인터페이스 ✅
**구현 파일**: `ProjectRepository.kt`, `ProjectRepositoryImpl.kt`
```kotlin
interface ProjectRepository {
    suspend fun getProjects(): List<Project>
    suspend fun getProjectById(projectId: String): Project?
    suspend fun toggleBookmark(projectId: String): Boolean
    suspend fun createProject(project: Project): Boolean
    suspend fun updateProject(project: Project): Boolean
    suspend fun deleteProject(projectId: String): Boolean
}
```

### 2.2 프로젝트 생성 (단순화 버전) ✅
**구현 파일**: `ProjectCreateRepository.kt`
```kotlin
interface ProjectCreateRepository {
    suspend fun createProject(project: Project): Result<Project>
}

// ProjectCreateDialog.kt의 실제 데이터 구조
data class ProjectCreateData(
    val title: String = "",
    val location: String = "",
    val locationDetail: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val startDate: String = "",  // ISO 형식
    val endDate: String = ""     // ISO 형식
)
```

### 2.3 프로젝트 데이터 모델 ✅
**구현 파일**: `Project.kt`
```kotlin
data class Project(
    val id: String,
    val title: String,
    val location: String,
    val locationDetail: String? = null,
    val latitude: Double,
    val longitude: Double,
    val startDate: String,
    val endDate: String,
    val status: ProjectStatus,
    val progress: Int = 0,  // 날짜 기반 자동 계산
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ProjectStatus {
    RECRUITING,    // 모집중
    IN_PROGRESS,   // 진행중
    COMPLETED      // 완료
}
```

### 2.4 출근/퇴근 체크 (구현 예정) 🔄
```kotlin
// AttendanceCheckScreen.kt에 UI는 구현됨
// API 연동 필요

예상 엔드포인트:
POST /api/company/attendance/checkin
POST /api/company/attendance/checkout

data class CheckInRequest(
    val projectId: String,
    val workerId: String,
    val checkInTime: Long,
    val checkInMethod: String,  // "QR", "GPS", "MANUAL"
    val location: Location?
)
```

---

## 3. 인력 스카우트 API (Mock)

### 3.1 Mock 데이터 사용 중 ⚠️
**구현 파일**: `CompanyMockDataFactory.kt`
```kotlin
object CompanyMockDataFactory {
    fun getScoutWorkers(): List<Worker>
    fun getScoutProposals(): List<Proposal>
}

// 실제 사용 중인 데이터 모델
data class Worker(
    val id: String,
    val name: String,
    val jobTypes: List<String>,
    val experience: Int,
    val rating: Float,
    val completedProjects: Int,
    val distance: Double,
    val isAvailable: Boolean,
    val expectedWage: Int,
    val profileImage: String? = null,
    val phoneNumber: String? = null,
    val certifications: List<String> = emptyList()
)

data class Proposal(
    val id: String,
    val workerId: String,
    val workerName: String,
    val proposedWage: String,
    val message: String,
    val status: ProposalStatus,
    val createdAt: LocalDateTime,
    val jobTypes: List<String>,
    val distance: String,
    val workerPhone: String? = null,
    val rejectReason: String? = null
)
```

### 3.2 예상 API 구조 🔄
```kotlin
// 실제 API 구현 시 필요한 엔드포인트
GET /api/company/scout/workers
POST /api/company/scout/proposals
GET /api/company/scout/proposals
POST /api/company/scout/ai-recommend
```

---

## 4. 자금 관리 API (Mock)

### 4.1 Mock 데이터 사용 중 ⚠️
**구현 파일**: `CompanyMoneyViewModel.kt`
```kotlin
// getSamplePayments()로 Mock 데이터 생성 중

data class Payment(
    val id: String,
    val workerName: String,
    val workType: String,
    val projectName: String,
    val workDate: LocalDateTime,
    val workHours: Double,
    val hourlyWage: Int,
    val amount: Int,
    val status: PaymentStatus,
    val paidDate: LocalDateTime?,
    val description: String
)

enum class PaymentStatus(val displayName: String, val color: Color) {
    PENDING("지급 대기", Color(0xFFFF9800)),
    COMPLETED("지급 완료", Color(0xFF4CAF50)),
    OVERDUE("지급 지연", Color(0xFFF44336))
}
```

### 4.2 5% 수수료 절감 시스템 ✅
```kotlin
// 실제 구현된 계산 로직
val platformFee = amount * 0.05  // 5% 수수료
val competitorFee = amount * 0.10  // 경쟁사 10%
val savedAmount = competitorFee - platformFee  // 50% 절감

data class PaymentSummary(
    val totalPendingAmount: Int = 0,
    val totalCompletedAmount: Int = 0,
    val thisMonthAmount: Int = 0,
    val pendingCount: Int = 0,
    val completedCount: Int = 0
    // savedAmount 추가 필요
)
```

---

## 5. 사업자 정보 API (Mock)

### 5.1 Mock 데이터 사용 중 ⚠️
**구현 파일**: `CompanyInfoViewModel.kt`
```kotlin
// 현재 하드코딩된 데이터 사용

예상 API 구조:
GET /api/company/profile
PUT /api/company/profile
GET /api/company/statistics
GET /api/company/notifications
GET /api/company/notices
```

---

## 6. 공통 데이터 모델

### 6.1 위치 정보 ✅
```kotlin
data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val address: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
```

### 6.2 페이지네이션 (구현 예정) 🔄
```kotlin
data class PaginationInfo(
    val currentPage: Int,
    val totalPages: Int,
    val pageSize: Int,
    val totalItems: Int,
    val hasNext: Boolean,
    val hasPrev: Boolean
)
```

### 6.3 공통 응답 형식 ✅
```kotlin
// 실제 사용 중인 패턴
data class ApiResult<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: ErrorDetail? = null
)

data class ErrorDetail(
    val code: String,
    val message: String,
    val details: Map<String, Any>? = null
)
```

---

## 7. 구현 우선순위

### 🔴 긴급 (핵심 기능)
1. **Company 회원가입 API** - Worker용을 Company용으로 확장
2. **프로젝트 생성 실제 API** - Repository는 있으나 API 연동 필요
3. **출근/퇴근 체크 API** - UI는 완성, API 연동 필요

### 🟡 중요 (차별화 기능)
1. **Scout API 실제 구현** - Mock을 실제 API로 전환
2. **Money API 실제 구현** - 5% 수수료 시스템
3. **AI 추천 시스템** - 매칭 점수 알고리즘

### 🟢 보통 (부가 기능)
1. **Info API** - 프로필, 통계
2. **알림 시스템** - FCM 연동
3. **Webhook** - 실시간 이벤트

---

## 8. API 서버 설정

### 현재 설정 (RetrofitAPI.kt)
```kotlin
object RetrofitAPI {
    private const val BASE_URL = "https://api.jikjikjik.com/"  // 실제 서버
    // private const val BASE_URL = "http://10.0.2.2:8080/"  // 로컬 테스트
    
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    
    val joinApi: JoinApi by lazy {
        retrofit.create(JoinApi::class.java)
    }
    
    // TODO: Company 전용 API 추가 필요
    // val companyApi: CompanyApi
    // val projectApi: ProjectApi
    // val scoutApi: ScoutApi
    // val moneyApi: MoneyApi
}
```

---

## 📝 참고사항

1. **현재 상태**: Worker 앱 기반으로 Company 앱 확장 중
2. **Mock 데이터**: Scout, Money, Info는 실제 API 연동 전 Mock 사용
3. **5% 수수료**: UI와 계산 로직은 구현됨, API 연동 필요
4. **날짜 기반 진행률**: UI에서 계산 중, 서버 동기화 필요
5. **인증**: JWT 토큰 기반 구현 완료

---

문서 버전: v6.0
최종 업데이트: 2025-01-15
작성자: 직직직 개발팀
용도: 사업자(Company) 앱 전용