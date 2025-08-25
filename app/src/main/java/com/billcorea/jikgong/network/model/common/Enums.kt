package com.billcorea.jikgong.network.model.common

/**
 * 모든 Enum 정의를 한 곳에서 관리
 */

// 사용자 역할
enum class UserRole {
  WORKER,     // 노동자
  COMPANY,    // 기업
  ADMIN       // 관리자
}

// 성별
enum class Gender {
  MALE,       // 남성
  FEMALE,     // 여성
  OTHER       // 기타
}

// 직종
enum class JobType {
  GENERAL_LABOR,           // 보통인부
  CARPENTER,               // 목수
  PLUMBER,                 // 배관공
  REBAR_WORKER,           // 철근공
  ELECTRICIAN,            // 전기공
  WELDER,                 // 용접공
  PAINTER,                // 도장공
  TILE_WORKER,            // 타일공
  CONCRETE_WORKER,        // 콘크리트공
  HEAVY_EQUIPMENT,        // 중장비 기사
  SCAFFOLDER,             // 비계공
  WATERPROOFER,           // 방수공
  MASON,                  // 조적공
  LANDSCAPER,             // 조경공
  DEMOLITION_WORKER       // 철거공
}

// 근무 형태
enum class WorkType {
  DAILY,          // 일용직
  MONTHLY,        // 월급제
  PROJECT_BASED,  // 프로젝트 단위
  PART_TIME      // 시간제
}

// 임금 유형
enum class WageType {
  HOURLY,     // 시급
  DAILY,      // 일급
  MONTHLY,    // 월급
  PROJECT     // 프로젝트 단위
}

// 프로젝트 유형
enum class ProjectType {
  PUBLIC,     // 공공
  PRIVATE     // 민간
}

// 프로젝트 카테고리
enum class ProjectCategory {
  CONSTRUCTION,       // 건설
  CIVIL_ENGINEERING, // 토목
  ELECTRICAL,        // 전기
  PLUMBING,         // 배관
  INTERIOR,         // 인테리어
  DEMOLITION,       // 철거
  RENOVATION,       // 리모델링
  LANDSCAPING       // 조경
}

// 프로젝트 상태
enum class ProjectStatus {
  PLANNING,       // 계획중
  RECRUITING,     // 모집중
  IN_PROGRESS,    // 진행중
  COMPLETED,      // 완료
  SUSPENDED,      // 중단
  CANCELLED       // 취소
}

// 일자리 상태
enum class JobStatus {
  OPEN,           // 모집중
  CLOSED,         // 마감
  IN_PROGRESS,    // 진행중
  COMPLETED,      // 완료
  CANCELLED       // 취소
}

// 근무 강도
enum class WorkIntensity {
  LOW,        // 낮음
  MEDIUM,     // 보통
  HIGH,       // 높음
  VERY_HIGH   // 매우 높음
}

// 위험도
enum class RiskLevel {
  SAFE,       // 안전
  MODERATE,   // 보통
  HIGH,       // 높음
  VERY_HIGH   // 매우 높음
}

// 경력 수준
enum class ExperienceLevel {
  BEGINNER,       // 초급 (1년 미만)
  INTERMEDIATE,   // 중급 (1-3년)
  ADVANCED,       // 고급 (3-5년)
  EXPERT,         // 전문가 (5년 이상)
  MASTER          // 마스터 (10년 이상)
}

// 결제 방법
enum class PaymentMethod {
  BANK_TRANSFER,  // 계좌이체
  CASH,          // 현금
  CHECK,         // 수표
  CARD           // 카드
}

// 결제 주기
enum class PaymentSchedule {
  DAILY,      // 일일
  WEEKLY,     // 주간
  BIWEEKLY,   // 격주
  MONTHLY     // 월간
}

// 결제 상태
enum class PaymentStatus {
  PENDING,        // 대기중
  PROCESSING,     // 처리중
  COMPLETED,      // 완료
  FAILED,         // 실패
  CANCELLED,      // 취소
  REFUNDED        // 환불
}

// 매칭 상태
enum class MatchingStatus {
  REQUESTED,      // 요청됨
  REVIEWING,      // 검토중
  CONFIRMED,      // 확인됨
  REJECTED,       // 거절됨
  CANCELLED,      // 취소됨
  EXPIRED         // 만료됨
}

// 근태 상태
enum class AttendanceStatus {
  NORMAL,         // 정상
  LATE,           // 지각
  EARLY_LEAVE,    // 조퇴
  ABSENT,         // 결근
  HOLIDAY,        // 휴일
  SICK_LEAVE      // 병가
}

// 인증 방법
enum class VerificationMethod {
  GPS,              // GPS
  QR_CODE,          // QR코드
  FACE_RECOGNITION, // 얼굴인식
  MANUAL           // 수동
}

// 알림 유형
enum class NotificationType {
  JOB_MATCH,      // 일자리 매칭
  PAYMENT,        // 결제
  URGENT,         // 긴급
  SYSTEM,         // 시스템
  CHAT,           // 채팅
  REVIEW,         // 리뷰
  PROJECT,        // 프로젝트
  ATTENDANCE      // 출퇴근
}

// 알림 우선순위
enum class NotificationPriority {
  LOW,        // 낮음
  NORMAL,     // 보통
  HIGH,       // 높음
  URGENT      // 긴급
}

// 메시지 유형
enum class MessageType {
  TEXT,       // 텍스트
  IMAGE,      // 이미지
  FILE,       // 파일
  LOCATION,   // 위치
  SYSTEM      // 시스템
}

// 첨부파일 유형
enum class AttachmentType {
  IMAGE,      // 이미지
  VIDEO,      // 비디오
  DOCUMENT,   // 문서
  AUDIO,      // 오디오
  OTHER       // 기타
}

// 언어 능력
enum class LanguageProficiency {
  BASIC,          // 기초
  CONVERSATIONAL, // 일상회화
  FLUENT,         // 유창
  NATIVE          // 원어민
}

// 건강 수준
enum class HealthLevel {
  EXCELLENT,  // 매우 좋음
  GOOD,       // 좋음
  FAIR,       // 보통
  POOR        // 나쁨
}

// 체력 수준
enum class FitnessLevel {
  HIGH,       // 높음
  MEDIUM,     // 보통
  LOW         // 낮음
}

// 보험 유형
enum class InsuranceType {
  INDUSTRIAL_ACCIDENT,    // 산재보험
  EMPLOYMENT,            // 고용보험
  HEALTH,               // 건강보험
  NATIONAL_PENSION,     // 국민연금
  LIABILITY,            // 배상책임보험
  CONSTRUCTION          // 건설공제
}

// 기업 유형
enum class CompanyType {
  BASIC,      // 기본
  PREMIUM,    // 프리미엄
  ENTERPRISE  // 엔터프라이즈
}

// 기업 상태
enum class CompanyStatus {
  ACTIVE,     // 활성
  INACTIVE,   // 비활성
  SUSPENDED,  // 정지
  PENDING     // 승인 대기
}

// 요일
enum class DayOfWeek {
  MONDAY,     // 월요일
  TUESDAY,    // 화요일
  WEDNESDAY,  // 수요일
  THURSDAY,   // 목요일
  FRIDAY,     // 금요일
  SATURDAY,   // 토요일
  SUNDAY      // 일요일
}