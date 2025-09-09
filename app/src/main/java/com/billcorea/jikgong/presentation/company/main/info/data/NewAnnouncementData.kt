package com.billcorea.jikgong.presentation.company.main.info.data

import java.time.LocalDateTime

// ==================== 새로운 공지사항 데이터 ====================

object NewAnnouncementContent {
    
    val announcements = listOf(
        Announcement(
            id = "ann_001",
            title = "🏢 2024년 건설업 안전규정 개정사항",
            summary = "건설현장 안전규정 강화 내용과 추가 안전장비 지급 기준 안내",
            content = """
🏢 2024년 건설업 안전규정 개정 안내

## 주요 변경사항

### 1. 안전장비 지급 기준 강화
- 보호구 지급: 기존 50,000원 → 80,000원 인상
- 안전벨트, 안전모 지급 의무화
- 안전화 품질 기준 강화 (KS 인증 필수)

### 2. 현장 안전 점검 강화
- 일일 안전 점검 의무화
- 안전 관리자 상주 의무
- 위험 작업 시 2인 1조 작업 원칙

### 3. 교육 의무 확대
- 신규 작업자 4시간 안전교육 필수
- 월 1회 정기 안전교육 실시
- 특수 작업 별도 자격 교육 이수

## 시행일정
- 시행일: 2024년 3월 1일
- 유예기간: 6개월 (기존 현장 적용)

자세한 내용은 고용노동부 홈페이지를 참고하시기 바랍니다.
""".trimIndent(),
            type = AnnouncementType.LAW_REGULATION,
            priority = AnnouncementPriority.URGENT,
            isPinned = true,
            createdAt = LocalDateTime.now().minusDays(1)
        ),
        
        Announcement(
            id = "ann_002",
            title = "🚀 AI 기반 인력 자동 매칭 서비스 출시",
            summary = "AI가 자동으로 최적의 인력을 찾아주는 새로운 서비스가 출시됩니다.",
            content = """
🚀 AI 기반 인력 자동 매칭 서비스

## 주요 기능

### 1. 스마트 인력 추천
- 현장 업무와 경력을 분석하여 최적의 인력 추천
- 95% 이상의 매칭 정확도
- 실시간 추천 알고리즘

### 2. 예측 기능
- 업무 완성도 예측
- 인력 이직률 분석
- 현장 적응도 평가

### 3. 비용 절약
- 기존 중개업체 대비 15-20% 수수료 절약
- 빠른 인력 보충으로 생산성 향상
- 관리 비용 절감

### 4. 자동화된 관리
- 출입 관리 자동화
- 급여 계산 자동화
- 근무 시간 추적

자세한 내용은 앱 내 '인력관리' 페이지에서 확인하실 수 있습니다.
""".trimIndent(),
            type = AnnouncementType.SERVICE_UPDATE,
            priority = AnnouncementPriority.HIGH,
            isPinned = false,
            createdAt = LocalDateTime.now().minusDays(3)
        ),
        
        Announcement(
            id = "ann_003",
            title = "⚠️ 건설현장 안전사고 예방 가이드",
            summary = "추락 및 낙하 사고 예방을 위한 필수 안전 수칙 안내",
            content = """
⚠️ 건설현장 안전사고 예방 가이드

## 주요 사고 유형

### 1. 추락 사고 (전체의 35%)
- 안전벨트 착용 필수
- 발판, 비계 점검 체크
- 높이 2m 이상 작업시 안전네트 설치

### 2. 낙하물 사고 (전체의 25%)
- 헬멧 착용 의무
- 작업구역 통제 철저
- 중장비 운전시 안전 수신호 사용

### 3. 전기 사고 (전체의 15%)
- 전기 작업 전 차단기 확인
- 절연 장갑 착용 필수
- 습기 차단 조치

## 안전장비 지급
- 보호구: 80,000원/월
- 안전대 수당: 50,000원/월
- 특수 작업 추가 수당: 100,000원/월

## 응급상황 대처
- 119 신고 → 회사 보고 → 안전 관리자 연락
- 응급처치 후 병원 이송
- 사고 현장 보존

자세한 사항은 고용노동부 홈페이지를 참고하시기 바랍니다.
""".trimIndent(),
            type = AnnouncementType.SAFETY_INFO,
            priority = AnnouncementPriority.HIGH,
            isPinned = false,
            createdAt = LocalDateTime.now().minusDays(5)
        ),
        
        Announcement(
            id = "ann_004",
            title = "📈 2024년 건설업 임금 인상률 전망",
            summary = "건설업 최저임금 인상과 전문직종 인부족 현상 분석",
            content = """
📈 2024년 건설업 임금 동향 분석

## 임금 인상 현황

### 1. 전반적 인상률
- 일반 건설 노무자: 전년 대비 8-12% 인상
- 전문 기능직: 전년 대비 15-20% 인상
- 전기, 용접 기능사: 전년 대비 25% 인상

### 2. 지역별 편차
- 수도권: 일당 200,000원 이상
- 경상도: 일당 180,000원 수준
- 사업비 지역: 일당 220,000원 대

### 3. 직종별 세분화
- 철근공: 일당 250,000원
- 형틀목공: 일당 230,000원
- 조적공: 일당 200,000원
- 일반 노무자: 일당 180,000원

### 4. 직직직 학습 데이터
- 평균 구인단가: 185,000원/일
- 효율성: 기존 중개업체 대비 15% 비용 절약
- 매칭 성공률: 92%

## 전문직종 부족 현상
- 전기 기능사 부족률: 25%
- 용접 기능사 부족률: 30% 
- 건축 시공 기술자 부족률: 20%

직직직을 통한 효율적 인력 관리로 인건비 부담을 줄여보세요.
""".trimIndent(),
            type = AnnouncementType.INDUSTRY_TREND,
            priority = AnnouncementPriority.MEDIUM,
            isPinned = false,
            createdAt = LocalDateTime.now().minusWeeks(1)
        ),
        
        Announcement(
            id = "ann_005",
            title = "📄 건설기술진흥법 개정 주요내용",
            summary = "스마트 건설 기술 지원 확대 및 상주건설업 규정 개정 사항",
            content = """
📄 건설기술진흥법 개정 주요내용

## 주요 개정사항

### 1. 스마트 건설 기술 지원 확대
- IoT, AI, 드론 등 신기술 도입 지원
- 지원 금액: 프로젝트당 최대 5억원
- 중소기업 우선 지원 정책

### 2. 상주건설업 규정 강화
- 연간 수주 이연 3회 이상시 제재 처벌
- 인건비 지급 지연 방지 조치 강화
- 불법 하도급 단속 강화

### 3. 디지털 인력 관리 시스템
- 전자적 근무기록 관리 의무화
- 모바일 기반 출입 관리 시스템 도입 권장
- QR코드 기반 출입 통제 시스템

### 4. 안전 관리 디지털화
- 안전 점검 기록 디지털화
- 실시간 위험 상황 알림 시스템
- AI 기반 안전사고 예방 시스템

## 시행 일정
- 1차 시행: 2024년 6월 1일
- 전면 시행: 2025년 1월 1일
- 유예기간: 기존 현장 12개월

직직직의 디지털 인력관리 솔루션으로 이러한 변화에 대비하세요.
""".trimIndent(),
            type = AnnouncementType.POLICY_CHANGE,
            priority = AnnouncementPriority.MEDIUM,
            isPinned = false,
            createdAt = LocalDateTime.now().minusWeeks(2)
        ),
        
        Announcement(
            id = "ann_006",
            title = "🏗️ 동절기 건설현장 안전관리 특별점검",
            summary = "겨울철 건설현장 특별 안전관리 지침 및 점검 사항",
            content = """
🏗️ 동절기 건설현장 안전관리 특별점검

## 동절기 주요 위험요소

### 1. 결빙 및 미끄러짐 사고
- 작업 시작 전 결빙 구간 점검
- 염화칼슘 살포 및 논슬립 설치
- 안전화 교체 (겨울용 안전화 착용)

### 2. 화재 위험 증가
- 난방기구 사용 시 안전점검 강화
- 가연성 물질 보관 관리 철저
- 소화기 점검 및 비상 대피로 확보

### 3. 혹한기 작업 관리
- 작업자 건강상태 수시 점검
- 온수 및 휴게시설 운영
- 혹한기 작업 중단 기준 마련

## 특별 점검 기간
- 점검 기간: 2024년 12월 ~ 2025년 2월
- 점검 대상: 전국 모든 건설현장
- 점검 주체: 지방고용노동관서

## 준비 사항
### 1. 안전장비 점검
- 겨울용 안전장비 준비
- 방한용품 지급 확인  
- 안전표지판 시인성 점검

### 2. 비상 계획 수립
- 응급상황 대응 매뉴얼 점검
- 비상연락망 최신화
- 혹한기 작업 중단 기준 설정

동절기 안전사고 예방에 만전을 기해주시기 바랍니다.
""".trimIndent(),
            type = AnnouncementType.SAFETY_INFO,
            priority = AnnouncementPriority.HIGH,
            isPinned = false,
            createdAt = LocalDateTime.now().minusWeeks(3)
        )
    )
    
    // 타입별 공지사항 필터링
    fun getAnnouncementsByType(type: AnnouncementType): List<Announcement> {
        return announcements.filter { it.type == type }
    }
    
    // 중요 공지사항만 가져오기
    fun getImportantAnnouncements(): List<Announcement> {
        return announcements.filter { it.isPinned || it.isImportant }
    }
    
    // 최신 공지사항 가져오기
    fun getRecentAnnouncements(count: Int = 3): List<Announcement> {
        return announcements.sortedByDescending { it.createdAt }.take(count)
    }
}