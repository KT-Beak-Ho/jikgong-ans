package com.billcorea.jikgong.presentation.company.main.info.data

import java.time.LocalDateTime

// ==================== 공지사항 데이터 모델 ====================

enum class AnnouncementType {
    SYSTEM,          // 시스템 공지
    SERVICE,         // 서비스 관련
    EVENT,           // 이벤트
    MAINTENANCE,     // 점검 공지
    UPDATE,          // 업데이트
    POLICY           // 정책 변경
}

enum class AnnouncementPriority {
    LOW,            // 일반
    MEDIUM,         // 중요
    HIGH,           // 긴급
    URGENT          // 매우 긴급
}

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val summary: String,
    val type: AnnouncementType,
    val priority: AnnouncementPriority,
    val isImportant: Boolean = false,
    val isPinned: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val validUntil: LocalDateTime? = null,
    val imageUrl: String? = null,
    val attachmentUrls: List<String> = emptyList(),
    val viewCount: Int = 0,
    val isRead: Boolean = false
) {
    // 우선순위별 색상
    val priorityColor: androidx.compose.ui.graphics.Color
        get() = when (priority) {
            AnnouncementPriority.LOW -> androidx.compose.ui.graphics.Color(0xFF6B7280)
            AnnouncementPriority.MEDIUM -> androidx.compose.ui.graphics.Color(0xFF3B82F6)
            AnnouncementPriority.HIGH -> androidx.compose.ui.graphics.Color(0xFFF59E0B)
            AnnouncementPriority.URGENT -> androidx.compose.ui.graphics.Color(0xFFEF4444)
        }
    
    // 우선순위별 한글명
    val priorityDisplayName: String
        get() = when (priority) {
            AnnouncementPriority.LOW -> "일반"
            AnnouncementPriority.MEDIUM -> "중요"
            AnnouncementPriority.HIGH -> "긴급"
            AnnouncementPriority.URGENT -> "매우 긴급"
        }
    
    // 타입별 한글명
    val typeDisplayName: String
        get() = when (type) {
            AnnouncementType.SYSTEM -> "시스템"
            AnnouncementType.SERVICE -> "서비스"
            AnnouncementType.EVENT -> "이벤트"
            AnnouncementType.MAINTENANCE -> "점검"
            AnnouncementType.UPDATE -> "업데이트"
            AnnouncementType.POLICY -> "정책"
        }
    
    // 타입별 아이콘
    val typeIcon: String
        get() = when (type) {
            AnnouncementType.SYSTEM -> "⚙️"
            AnnouncementType.SERVICE -> "🔧"
            AnnouncementType.EVENT -> "🎉"
            AnnouncementType.MAINTENANCE -> "🔧"
            AnnouncementType.UPDATE -> "🆙"
            AnnouncementType.POLICY -> "📋"
        }
    
    // 유효성 확인
    val isValid: Boolean
        get() = validUntil?.isAfter(LocalDateTime.now()) ?: true
}

// ==================== 공지사항 컨텐츠 ====================

object AnnouncementContent {
    
    val announcements = listOf(
        Announcement(
            id = "ann_001",
            title = "🎉 직직직 2.0 업데이트 출시!",
            summary = "더욱 편리해진 인력 매칭과 새로운 기능들을 만나보세요.",
            content = """
🎉 직직직 2.0 업데이트가 출시되었습니다!

## 주요 업데이트 내용

### 1. 향상된 인력 매칭 시스템
- AI 기반 인력 추천 알고리즘 도입
- 작업자 숙련도와 현장 요구사항 자동 매칭
- 매칭 정확도 85% 향상

### 2. 실시간 현장 관리
- 실시간 출근체크 및 위치 확인
- 작업 진행상황 실시간 모니터링
- 안전사고 예방을 위한 위험 구역 알림

### 3. 간편한 급여 관리
- 원클릭 급여 지급 시스템
- 자동 급여 계산 및 세무 처리
- 급여 내역 투명한 공개

### 4. 강화된 보안 시스템
- 2단계 인증 도입
- 개인정보 암호화 강화
- 결제 보안 시스템 업그레이드

### 5. 모바일 앱 성능 개선
- 앱 실행 속도 40% 향상
- 배터리 사용량 30% 절약
- 안정성 및 사용성 개선

## 혜택 이벤트

업데이트 기념으로 특별 이벤트를 진행합니다:

**🎁 신규 기능 체험 이벤트**
- 기간: 2024.01.15 ~ 2024.02.15
- 혜택: AI 매칭 서비스 1개월 무료 이용
- 참여방법: 앱 업데이트 후 신규 기능 1회 이상 사용

**💰 수수료 할인 이벤트**
- 기간: 2024.01.15 ~ 2024.01.31
- 혜택: 플랫폼 수수료 50% 할인
- 대상: 모든 기업회원

**🏆 리뷰 작성 이벤트**
- 기간: 상시 진행
- 혜택: 리뷰 작성 시 포인트 적립
- 포인트 활용: 수수료 할인 쿠폰으로 교환 가능

## 업데이트 방법

1. **자동 업데이트**
   - 설정 > 자동 업데이트 활성화 시 자동 설치

2. **수동 업데이트**
   - Google Play Store 또는 App Store에서 직직직 검색
   - '업데이트' 버튼 클릭

3. **웹 버전**
   - 브라우저에서 자동으로 최신 버전 적용

## 문의사항

업데이트 관련 문의사항이 있으시면 언제든지 연락주세요.

- **고객센터**: 1588-0000 (평일 09:00-18:00)
- **이메일**: support@jikjikjik.co.kr
- **카카오톡**: @직직직고객센터

감사합니다.
직직직 팀 드림
            """.trimIndent(),
            type = AnnouncementType.UPDATE,
            priority = AnnouncementPriority.HIGH,
            isImportant = true,
            isPinned = true,
            createdAt = LocalDateTime.of(2024, 1, 15, 10, 0),
            validUntil = LocalDateTime.of(2024, 2, 15, 23, 59),
            viewCount = 1547
        ),
        
        Announcement(
            id = "ann_002",
            title = "🔧 시스템 점검 안내 (1월 20일)",
            summary = "서비스 품질 향상을 위한 정기 시스템 점검이 예정되어 있습니다.",
            content = """
🔧 시스템 점검 안내

안녕하세요, 직직직입니다.

더 나은 서비스 제공을 위해 시스템 점검을 실시할 예정입니다.

## 점검 일정

**📅 점검 일시**: 2024년 1월 20일 (토) 02:00 ~ 06:00 (4시간)

**🚫 서비스 중단 시간**: 02:00 ~ 06:00
- 모든 서비스 일시 중단
- 앱 및 웹사이트 접속 불가
- 결제 기능 일시 중단

## 점검 내용

### 1. 서버 성능 최적화
- 데이터베이스 성능 튜닝
- 서버 하드웨어 업그레이드
- 네트워크 인프라 개선

### 2. 보안 시스템 강화
- 방화벽 정책 업데이트
- 취약점 패치 적용
- 접근 권한 시스템 점검

### 3. 백업 시스템 점검
- 데이터 백업 및 복구 테스트
- 재해 복구 시스템 점검
- 이중화 시스템 동작 확인

## 이용 안내

### 점검 전 준비사항
- **중요한 작업은 점검 전에 완료해 주세요**
- **미지급 급여는 1월 19일까지 처리 권장**
- **출근체크는 점검 시간을 피해서 진행**

### 점검 중 이용 불가 서비스
- 앱 로그인 및 모든 기능
- 웹사이트 접속
- 급여 지급 및 결제
- 출근체크 및 현장 관리
- 고객센터 온라인 문의

### 점검 중에도 이용 가능
- 전화 고객센터 (긴급상황 시): 1588-0000
- SMS 긴급 알림 서비스

## 예상 개선 효과

점검 완료 후 다음과 같은 개선 효과를 기대할 수 있습니다:

- **앱 실행 속도 50% 향상**
- **서버 응답 속도 30% 개선**
- **시스템 안정성 강화**
- **보안 수준 향상**

## 보상 안내

점검으로 인한 불편함을 드려 죄송합니다.
보상으로 다음 혜택을 제공합니다:

**🎁 점검 보상**
- 대상: 모든 회원
- 혜택: 플랫폼 수수료 20% 할인 쿠폰
- 유효기간: 1월 21일 ~ 1월 31일

## 문의사항

점검 관련 문의사항이 있으시면 고객센터로 연락주세요.

- **전화**: 1588-0000 (24시간 운영)
- **이메일**: maintenance@jikjikjik.co.kr

서비스 이용에 불편을 드려 죄송합니다.
더 나은 서비스로 보답하겠습니다.

감사합니다.
직직직 운영팀
            """.trimIndent(),
            type = AnnouncementType.MAINTENANCE,
            priority = AnnouncementPriority.URGENT,
            isImportant = true,
            isPinned = true,
            createdAt = LocalDateTime.of(2024, 1, 18, 14, 30),
            validUntil = LocalDateTime.of(2024, 1, 21, 0, 0),
            viewCount = 892
        ),
        
        Announcement(
            id = "ann_003",
            title = "💰 신규 결제 시스템 도입 안내",
            summary = "더욱 안전하고 편리한 결제 시스템이 도입됩니다.",
            content = """
💰 신규 결제 시스템 도입 안내

안녕하세요, 직직직입니다.

보다 안전하고 편리한 결제 서비스 제공을 위해 새로운 결제 시스템을 도입합니다.

## 새로운 결제 시스템

### 🔐 강화된 보안
- **SSL 인증서 업그레이드**: 최신 보안 프로토콜 적용
- **토큰 기반 결제**: 카드 정보 직접 저장하지 않음
- **이상 거래 탐지**: AI 기반 실시간 모니터링

### ⚡ 빠른 결제 속도
- **원클릭 결제**: 한 번 등록으로 간편 결제
- **실시간 처리**: 결제 완료 시간 70% 단축
- **자동 영수증**: 결제 즉시 자동 발송

### 💳 다양한 결제 수단
기존 결제 수단 외에 추가로 지원하는 결제 방법:

**간편결제**
- 삼성페이, LG페이
- 페이코, 시럽페이
- 스마일페이, 엘페이

**가상계좌**
- 모든 은행 가상계좌 지원
- 실시간 입금 확인
- 자동 환불 시스템

**해외 결제**
- VISA, MasterCard, JCB
- PayPal, AliPay
- 다중 통화 지원

## 적용 일정

**📅 단계별 적용**

### 1단계 (1월 25일)
- 신규 보안 시스템 적용
- 기존 결제 시스템과 병행 운영

### 2단계 (2월 1일)
- 간편결제 서비스 추가
- 가상계좌 시스템 개선

### 3단계 (2월 15일)
- 해외 결제 시스템 도입
- 기존 시스템 완전 교체

## 이용 방법

### 신규 결제 수단 등록
1. **마이페이지 > 결제 관리** 접속
2. **새 결제수단 추가** 클릭
3. 원하는 결제 방법 선택 및 인증
4. 등록 완료 후 즉시 사용 가능

### 기존 결제 정보
- **자동 마이그레이션**: 기존 정보 자동 이전
- **재등록 불필요**: 별도 절차 없이 계속 사용
- **보안 강화**: 동일한 정보, 더 안전한 저장

## 수수료 안내

**🎉 도입 기념 특별 혜택**

### 결제 수수료 면제
- 기간: 2024.01.25 ~ 2024.02.29
- 대상: 모든 결제 방법
- 혜택: 결제 수수료 100% 면제

### 간편결제 추가 할인
- 기간: 상시 적용
- 대상: 간편결제 이용 시
- 혜택: 플랫폼 수수료 추가 5% 할인

## 보안 및 개인정보

### 개인정보 보호
- **PCI DSS 인증**: 국제 보안 표준 준수
- **개인정보 분리 저장**: 결제 정보와 개인정보 별도 관리
- **정기 보안 점검**: 월 1회 전문 기관 점검

### 이상 거래 대응
- **24시간 모니터링**: 실시간 이상 거래 탐지
- **즉시 차단**: 의심 거래 자동 차단
- **고객 알림**: SMS/앱 푸시를 통한 즉시 알림

## 고객 지원

### 결제 관련 문의
- **전용 상담**: 1588-1234 (결제 전용)
- **실시간 채팅**: 앱 내 채팅 상담
- **이메일**: payment@jikjikjik.co.kr

### 결제 오류 신고
- **긴급 신고**: 1588-9999 (24시간)
- **온라인 신고**: 홈페이지 > 결제 오류 신고
- **평균 처리 시간**: 30분 이내

더 안전하고 편리한 결제 서비스로 찾아뵙겠습니다.

감사합니다.
직직직 결제팀
            """.trimIndent(),
            type = AnnouncementType.SERVICE,
            priority = AnnouncementPriority.MEDIUM,
            isImportant = false,
            isPinned = false,
            createdAt = LocalDateTime.of(2024, 1, 22, 9, 0),
            validUntil = LocalDateTime.of(2024, 2, 29, 23, 59),
            viewCount = 634
        ),
        
        Announcement(
            id = "ann_004",
            title = "🏆 1월 우수 기업 선정 이벤트",
            summary = "우수한 근무 환경을 제공하는 기업을 선정하여 특별 혜택을 드립니다.",
            content = """
🏆 1월 우수 기업 선정 이벤트

직직직과 함께하는 모든 기업 회원님들께 감사드립니다.

매월 진행되는 '우수 기업 선정 이벤트'를 통해 근로자들에게 좋은 근무환경을 제공하는 기업을 찾고 있습니다.

## 선정 기준

### 📊 정량 평가 (60%)
- **근로자 만족도**: 4.5점 이상 (5점 만점)
- **급여 지급 신속성**: 평균 1일 이내
- **안전사고 발생률**: 0건 (월 기준)
- **재고용 요청률**: 80% 이상

### 🌟 정성 평가 (40%)
- **근무환경 개선 노력**
- **근로자 복지 제공**
- **소통 및 피드백 적극성**
- **안전 교육 및 관리**

## 이번 달 우수 기업

### 🥇 최우수상: (주)건설왕
- **위치**: 서울 강남구
- **주요 실적**:
  - 근로자 만족도 4.9/5.0
  - 급여 지급 평균 시간: 당일
  - 안전사고 0건 (6개월 연속)
  - 재고용 요청률 95%

**우수사례**:
- 매주 안전교육 실시
- 근로자 휴게시설 완비
- 날씨에 따른 작업 조건 조정
- 명절 상여금 및 선물 제공

### 🥈 우수상: 한국토목건설
- **위치**: 경기 성남시  
- **주요 실적**:
  - 근로자 만족도 4.7/5.0
  - 급여 지급 평균 시간: 1일
  - 안전사고 0건 (3개월 연속)
  - 재고용 요청률 88%

### 🥉 장려상: 대한건축
- **위치**: 인천 남동구
- **주요 실적**:
  - 근로자 만족도 4.6/5.0
  - 급여 지급 평균 시간: 1일
  - 안전사고 0건 (1개월)
  - 재고용 요청률 82%

## 시상 혜택

### 🎁 최우수상 혜택
- **상금**: 100만원 상당의 플랫폼 이용권
- **수수료 할인**: 3개월간 50% 할인
- **우선 매칭**: 우수 인력 우선 배정
- **홍보 지원**: 홈페이지 메인 배너 광고

### 🏆 우수상 혜택  
- **상금**: 50만원 상당의 플랫폼 이용권
- **수수료 할인**: 2개월간 30% 할인
- **우선 매칭**: 인력 매칭 우선 순위

### 🌟 장려상 혜택
- **상금**: 30만원 상당의 플랫폼 이용권
- **수수료 할인**: 1개월간 20% 할인

## 참여 방법

### 자동 참여
- **대상**: 모든 기업 회원 (자동 참여)
- **기간**: 매월 1일 ~ 말일
- **발표**: 익월 첫째 주

### 추가 가점 활동
다음 활동 시 추가 점수를 받을 수 있습니다:

**📝 근로자 피드백 적극 수용**
- 건의사항 24시간 내 답변: +5점
- 개선사항 실제 적용: +10점

**🎯 안전관리 우수 활동**  
- 매일 안전점검 실시: +3점
- 안전장비 100% 지급: +5점
- 안전교육 정기 실시: +7점

**💝 근로자 복지 제공**
- 식사 지원: +3점
- 교통비 지원: +3점
- 간식 및 음료 제공: +2점
- 휴게공간 제공: +5점

## 2월 이벤트 예고

### 🎨 특별 테마: '안전한 현장'
2월에는 '안전한 현장' 테마로 특별히 안전관리에 우수한 기업을 선정합니다.

**추가 평가 항목**:
- 안전관리자 배치 현황
- 안전장비 지급 및 관리
- 안전교육 이수율
- 위험상황 대응 매뉴얼 구비

**특별 혜택**:
- 안전관리 컨설팅 무료 제공
- 안전장비 구매 할인 쿠폰
- 산업안전보건교육 무료 제공

## 문의사항

우수 기업 선정 관련 문의사항이 있으시면 연락주세요.

- **담당팀**: 기업서비스팀
- **전화**: 1588-2345
- **이메일**: awards@jikjikjik.co.kr

좋은 근무환경을 만들어 주시는 모든 기업 회원님들께 다시 한번 감사드립니다.

직직직 기업서비스팀 드림
            """.trimIndent(),
            type = AnnouncementType.EVENT,
            priority = AnnouncementPriority.LOW,
            isImportant = false,
            isPinned = false,
            createdAt = LocalDateTime.of(2024, 1, 5, 11, 30),
            validUntil = LocalDateTime.of(2024, 2, 5, 23, 59),
            viewCount = 423
        ),
        
        Announcement(
            id = "ann_005",
            title = "📋 개인정보처리방침 개정 안내",
            summary = "개인정보 보호 강화를 위한 처리방침이 개정됩니다.",
            content = """
📋 개인정보처리방침 개정 안내

안녕하세요, 직직직입니다.

개인정보 보호 강화 및 관련 법령 준수를 위해 개인정보처리방침을 개정합니다.

## 개정 일정

**📅 시행일**: 2024년 2월 1일
**📅 고지 기간**: 2024년 1월 10일 ~ 1월 31일 (22일간)

## 주요 개정 내용

### 1. 개인정보 수집 항목 명확화

**기존**
- 필수: 이름, 연락처, 이메일
- 선택: 주소, 경력사항

**개정 후**
- **필수**: 이름, 휴대폰번호, 이메일, 주민등록번호(사업자 등록 시)
- **선택**: 주소, 경력사항, 자격증 정보, 프로필 사진
- **자동수집**: 기기정보, 위치정보, 서비스 이용기록

### 2. 개인정보 이용 목적 구체화

**📝 기본 서비스 제공**
- 회원가입 및 본인확인
- 서비스 제공 및 계약이행
- 고객상담 및 불만처리

**💰 결제 및 정산 서비스**
- 급여 지급 및 정산
- 세무처리 및 신고
- 결제 및 환불 처리

**📊 서비스 개선**
- 서비스 이용 통계 분석
- 신규 서비스 개발
- 맞춤형 서비스 제공

### 3. 개인정보 보유기간 세분화

| 정보 유형 | 보유기간 | 근거 |
|----------|---------|------|
| 회원정보 | 회원탈퇴 후 1년 | 서비스 이용 기록 |
| 계약정보 | 계약종료 후 5년 | 전자상거래법 |
| 결제정보 | 결제완료 후 5년 | 전자상거래법 |
| 세무정보 | 신고 후 5년 | 국세기본법 |
| 분쟁정보 | 분쟁해결 후 3년 | 소비자분쟁조정법 |

### 4. 제3자 제공 명확화

**📤 제3자 제공 대상 및 목적**

**금융기관 (급여 지급)**
- 제공대상: 국민은행, 신한은행 등
- 제공정보: 성명, 계좌번호
- 제공목적: 급여 지급

**공공기관 (법적 의무)**
- 제공대상: 국세청, 근로복지공단 등
- 제공정보: 소득 및 세무정보
- 제공목적: 세무신고, 사회보험

**서비스 제공업체 (업무 위탁)**
- SMS 발송: 휴대폰번호
- 이메일 발송: 이메일주소  
- 결제처리: 결제정보

### 5. 개인정보 보호 강화 조치

**🔒 기술적 보호조치**
- 개인정보 암호화 (AES-256)
- 접근통제 시스템 구축
- 침입탐지시스템 운영
- 보안프로그램 설치 운영

**📋 관리적 보호조치**
- 개인정보 보호책임자 지정
- 개인정보 취급자 교육
- 개인정보 파기 절차 수립
- 정기 보안점검 실시

## 주요 변경사항 요약

### ✅ 개인정보 주체 권리 강화
- **열람권**: 처리현황 언제든지 확인 가능
- **정정권**: 잘못된 정보 즉시 수정 요청
- **삭제권**: 불필요한 정보 삭제 요청
- **처리정지권**: 개인정보 처리 중단 요청

### 🔔 동의 절차 개선
- **단계별 동의**: 필수/선택 동의 분리
- **구체적 설명**: 수집 목적별 상세 안내
- **철회 간편화**: 동의 철회 원클릭 지원

### 📞 개인정보 보호책임자 변경
- **기존**: 김개인 (privacy@jikjikjik.co.kr)
- **신규**: 박보호 (privacy@jikjikjik.co.kr, 02-1234-5678)

## 동의 및 적용

### 📋 동의 절차
**자동 동의** (기존 회원)
- 별도 동의 절차 없이 자동 적용
- 동의하지 않을 시 서비스 이용 제한 가능

**신규 동의** (신규 회원)  
- 회원가입 시 개정된 약관 동의 필수
- 단계별 동의 절차 적용

### ⚠️ 거부 시 제한사항
개정된 처리방침에 동의하지 않을 경우:
- 신규 서비스 이용 불가
- 개인정보 처리가 필요한 기능 제한
- 급여 지급 등 필수 서비스는 계속 제공

## 문의 및 신고

### 📞 개인정보 관련 문의
- **보호책임자**: 박보호
- **전화**: 02-1234-5678  
- **이메일**: privacy@jikjikjik.co.kr

### 🏛️ 개인정보 분쟁조정위원회
- **전화**: 1833-6972
- **홈페이지**: privacy.go.kr

### 👮 개인정보 보호위원회
- **전화**: 02-2100-3000  
- **홈페이지**: pipc.go.kr

개인정보 보호를 위한 지속적인 노력을 약속드립니다.

감사합니다.
직직직 개인정보보호팀
            """.trimIndent(),
            type = AnnouncementType.POLICY,
            priority = AnnouncementPriority.MEDIUM,
            isImportant = true,
            isPinned = false,
            createdAt = LocalDateTime.of(2024, 1, 10, 16, 45),
            validUntil = LocalDateTime.of(2024, 3, 1, 23, 59),
            viewCount = 756
        )
    )
    
    // 필터링 함수들
    fun getAnnouncementsByType(type: AnnouncementType) = 
        announcements.filter { it.type == type }
    
    fun getImportantAnnouncements() = 
        announcements.filter { it.isImportant }
    
    fun getPinnedAnnouncements() = 
        announcements.filter { it.isPinned }
    
    fun getValidAnnouncements() = 
        announcements.filter { it.isValid }
    
    fun getUnreadAnnouncements() = 
        announcements.filter { !it.isRead }
    
    fun getAnnouncementById(id: String) = 
        announcements.find { it.id == id }
}