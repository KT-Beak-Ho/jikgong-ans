package com.billcorea.jikgong.network.model.common

/**
 * 사용자 역할
 */
enum class UserRole {
  WORKER,         // 노동자
  COMPANY,        // 기업
  ADMIN          // 관리자
}

/**
 * 성별
 */
enum class Gender {
  MALE,          // 남성
  FEMALE,        // 여성
  OTHER          // 기타
}

/**
 * 직종 타입
 */
enum class JobType {
  GENERAL_LABOR,      // 일반인부
  REBAR_WORKER,       // 철근공
  FORM_WORKER,        // 형틀공
  CONCRETE_WORKER,    // 콘크리트공
  CARPENTER,          // 목공
  ELECTRICIAN,        // 전기공
  PLUMBER,            // 배관공
  WELDER,             // 용접공
  PAINTER,            // 도장공
  TILER,              // 타일공
  MASON,              // 조적공
  LANDSCAPER,         // 조경공
  EQUIPMENT_OPERATOR, // 장비기사
  OTHER              // 기타
}

/**
 * 프로젝트 타입
 */
enum class ProjectType {
  CONSTRUCTION,       // 건설
  CIVIL_ENGINEERING,  // 토목
  RENOVATION,         // 리모델링
  MAINTENANCE,        // 유지보수
  DEMOLITION,         // 철거
  OTHER              // 기타
}

/**
 * 프로젝트 카테고리
 */
enum class ProjectCategory {
  APARTMENT,          // 아파트
  OFFICE_BUILDING,    // 오피스텔/사무실
  COMMERCIAL,         // 상가
  FACTORY,            // 공장
  WAREHOUSE,          // 창고
  ROAD,              // 도로
  BRIDGE,            // 교량
  INFRASTRUCTURE,     // 인프라
  OTHER              // 기타
}

/**
 * 프로젝트 상태
 */
enum class ProjectStatus {
  DRAFT,             // 초안
  RECRUITING,        // 모집중
  IN_PROGRESS,       // 진행중
  COMPLETED,         // 완료
  CANCELLED,         // 취소
  SUSPENDED          // 중단
}

/**
 * 요일
 */
enum class DayOfWeek {
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY,
  SUNDAY
}

/**
 * 임금 타입
 */
enum class WageType {
  DAILY,             // 일급
  MONTHLY,           // 월급
  HOURLY,            // 시급
  PROJECT_BASED      // 프로젝트 단위
}

/**
 * 지급 방법
 */
enum class PaymentMethod {
  BANK_TRANSFER,     // 계좌이체
  CASH,              // 현금
  CHECK,             // 수표
  MIXED              // 혼합
}

/**
 * 지급 스케줄
 */
enum class PaymentSchedule {
  IMMEDIATE,         // 즉시
  DAILY,             // 일일 정산
  WEEKLY,            // 주간 정산
  MONTHLY,           // 월간 정산
  PROJECT_END        // 프로젝트 종료 후
}

/**
 * 근무 타입
 */
enum class WorkType {
  DAILY,             // 일용직
  CONTRACT,          // 계약직
  REGULAR,           // 정규직
  PART_TIME          // 파트타임
}

/**
 * 언어 능력 수준
 */
enum class LanguageProficiency {
  NATIVE,            // 원어민
  FLUENT,            // 유창함
  CONVERSATIONAL,    // 대화 가능
  BASIC              // 기초
}

/**
 * 건강 수준
 */
enum class HealthLevel {
  EXCELLENT,         // 매우 좋음
  GOOD,              // 좋음
  NORMAL,            // 보통
  POOR               // 나쁨
}

/**
 * 체력 수준
 */
enum class FitnessLevel {
  VERY_HIGH,         // 매우 높음
  HIGH,              // 높음
  MODERATE,          // 보통
  LOW                // 낮음
}

/**
 * 매칭 상태
 */
enum class MatchingStatus {
  PENDING,           // 대기중
  ACCEPTED,          // 수락됨
  REJECTED,          // 거절됨
  CANCELLED,         // 취소됨
  CONFIRMED,         // 확정됨
  EXPIRED            // 만료됨
}

/**
 * 근태 상태
 */
enum class AttendanceStatus {
  CHECKED_IN,        // 출근
  CHECKED_OUT,       // 퇴근
  ABSENT,            // 결근
  LATE,              // 지각
  EARLY_LEAVE,       // 조퇴
  HOLIDAY            // 휴일
}

/**
 * 결제 상태
 */
enum class PaymentStatus {
  PENDING,           // 대기
  PROCESSING,        // 처리중
  COMPLETED,         // 완료
  FAILED,            // 실패
  REFUNDED,          // 환불
  CANCELLED          // 취소
}

/**
 * 알림 타입
 */
enum class NotificationType {
  JOB_MATCH,         // 일자리 매칭
  PROJECT_UPDATE,    // 프로젝트 업데이트
  PAYMENT,           // 결제
  URGENT,            // 긴급
  CHAT,              // 채팅
  SYSTEM,            // 시스템
  MARKETING          // 마케팅
}

/**
 * 채팅 메시지 타입
 */
enum class MessageType {
  TEXT,              // 텍스트
  IMAGE,             // 이미지
  FILE,              // 파일
  LOCATION,          // 위치
  SYSTEM             // 시스템 메시지
}

/**
 * 인증 방법
 */
enum class VerificationMethod {
  GPS,               // GPS 위치 확인
  QR_CODE,           // QR 코드 스캔
  PHOTO,             // 사진 인증
  BIOMETRIC,         // 생체 인증
  PIN,               // PIN 번호
  MANUAL             // 수동 확인
}

/**
 * 알림 우선순위
 */
enum class NotificationPriority {
  HIGH,              // 높음
  MEDIUM,            // 중간
  LOW                // 낮음
}

/**
 * 기업 타입
 */
enum class CompanyType {
  GENERAL_CONTRACTOR,    // 종합건설업
  SPECIAL_CONTRACTOR,    // 전문건설업
  SUBCONTRACTOR,        // 하도급업체
  MANPOWER_AGENCY,      // 인력파견업체
  OTHER                 // 기타
}

/**
 * 기업 상태
 */
enum class CompanyStatus {
  ACTIVE,            // 활성
  INACTIVE,          // 비활성
  SUSPENDED,         // 정지
  PENDING_APPROVAL,  // 승인대기
  REJECTED           // 거절
}

/**
 * 보험 타입
 */
enum class InsuranceType {
  INDUSTRIAL_ACCIDENT,   // 산재보험
  EMPLOYMENT,           // 고용보험
  HEALTH,              // 건강보험
  PENSION,             // 국민연금
  LIABILITY,           // 배상책임보험
  NONE                 // 없음
}

/**
 * 직무 상태
 */
enum class JobStatus {
  OPEN,              // 모집중
  CLOSED,            // 마감
  IN_PROGRESS,       // 진행중
  COMPLETED,         // 완료
  CANCELLED          // 취소
}

/**
 * 작업 강도
 */
enum class WorkIntensity {
  VERY_LIGHT,        // 매우 가벼움
  LIGHT,             // 가벼움
  MODERATE,          // 보통
  HEAVY,             // 무거움
  VERY_HEAVY         // 매우 무거움
}

/**
 * 위험 수준
 */
enum class RiskLevel {
  VERY_LOW,          // 매우 낮음
  LOW,               // 낮음
  MEDIUM,            // 중간
  HIGH,              // 높음
  VERY_HIGH          // 매우 높음
}

/**
 * 경력 수준
 */
enum class ExperienceLevel {
  BEGINNER,          // 초급 (1년 미만)
  JUNIOR,            // 주니어 (1-3년)
  INTERMEDIATE,      // 중급 (3-5년)
  SENIOR,            // 고급 (5-10년)
  EXPERT             // 전문가 (10년 이상)
}

/**
 * 첨부파일 타입
 */
enum class AttachmentType {
  IMAGE,             // 이미지
  VIDEO,             // 동영상
  DOCUMENT,          // 문서
  AUDIO,             // 오디오
  OTHER              // 기타
}

/**
 * 기업 규모
 */
enum class CompanySize {
  MICRO,             // 5인 미만
  SMALL,             // 5-50인
  MEDIUM,            // 50-300인
  LARGE,             // 300인 이상
  ENTERPRISE         // 대기업
}