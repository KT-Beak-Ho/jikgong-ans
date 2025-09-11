# 🏗️ 직직직 노동자(Worker) 앱 아키텍처 문서

> **최종 업데이트**: 2025-01-15  
> **버전**: 1.0  
> **문서 용도**: 노동자(Worker) 앱 전용 아키텍처

## 📚 목차
1. [프로젝트 개요](#1-프로젝트-개요)
2. [전체 파일 구조](#2-전체-파일-구조)
3. [인증 모듈 (Auth)](#3-인증-모듈-auth)
4. [일자리 목록 (ProjectList)](#4-일자리-목록-projectlist)
5. [내 프로젝트 (MyProject)](#5-내-프로젝트-myproject)
6. [수입 관리 (Income)](#6-수입-관리-income)
7. [내 정보 (MyInfo)](#7-내-정보-myinfo)
8. [공통 컴포넌트 (Common)](#8-공통-컴포넌트-common)
9. [주요 화면 플로우](#9-주요-화면-플로우)

---

## 1. 프로젝트 개요

### 📱 앱 정보
- **앱 이름**: 직직직 노동자
- **패키지명**: com.billcorea.jikgong
- **주요 기능**: 건설/일용직 일자리 매칭 플랫폼
- **타겟 사용자**: 건설 현장 일용직 노동자

### 🎯 주요 특징
- ✅ **간편한 일자리 검색**: 위치 기반, 직종별 필터링
- ✅ **빠른 지원 프로세스**: 원터치 지원
- ✅ **실시간 출근 관리**: QR 코드 체크인/아웃
- ✅ **투명한 정산**: 실시간 급여 확인
- ✅ **경력 관리**: 자동 경력 증명서 발급

---

## 2. 전체 파일 구조

```
presentation/worker/
├── 📁 auth/                          # 인증 관련
│   ├── 📁 common/
│   │   ├── 📁 components/
│   │   │   ├── CommonBottomSheetInput.kt  # 바텀시트 입력
│   │   │   └── CommonWorkerTopBar.kt      # 상단바
│   │   └── 📁 constants/
│   │       └── WorkerJoinConstants.kt     # 회원가입 상수
│   ├── 📁 join/                      # 회원가입 (6단계)
│   │   ├── 📁 page1/
│   │   │   └── WorkerJoinPage1Screen.kt   # SMS 인증
│   │   ├── 📁 page2/
│   │   │   ├── WorkerJoinPage2Screen.kt   # 기본 정보
│   │   │   └── 📁 components/
│   │   │       └── CommonDatePicker.kt    # 생년월일 선택
│   │   ├── 📁 page3/
│   │   │   ├── WorkerJoinPage3Screen.kt   # 계좌 정보
│   │   │   └── 📁 components/
│   │   │       └── BankSelectList.kt      # 은행 선택
│   │   ├── 📁 page4/
│   │   │   └── WorkerJoinPage4Screen.kt   # 주소 입력
│   │   ├── 📁 page5/
│   │   │   └── WorkerJoinPage5Screen.kt   # 로그인 정보
│   │   ├── 📁 page6/
│   │   │   ├── WorkerJoinPage6Screen.kt   # 직종 선택
│   │   │   └── 📁 components/
│   │   │       └── JobSelectList.kt       # 직종 목록
│   │   └── 📁 shared/
│   │       ├── WorkerJoinSharedViewModel.kt
│   │       ├── WorkerJoinSharedUiState.kt
│   │       └── WorkerJoinSharedEvent.kt
│   └── 📁 login/
│       ├── 📁 page1/
│       │   ├── WorkerLoginPage.kt         # 로그인 화면
│       │   └── LoginBottomMiddleView.kt   # 로그인 옵션
│       └── 📁 shared/
│           ├── WorkerLoginViewModel.kt
│           ├── WorkerLoginUiState.kt
│           └── WorkerLoginSharedEvent.kt
│
├── 📁 projectList/                   # 일자리 목록
│   ├── 📁 page1/
│   │   └── WorkerProjectList.kt      # 일자리 목록 화면
│   ├── 📁 page2/
│   │   ├── WorkerProject.kt          # 일자리 상세
│   │   └── 📁 components/
│   │       └── WorkingDatesCalendar.kt # 근무일 달력
│   └── 📁 page3/
│       └── ApplyProject.kt           # 지원하기
│
├── 📁 myProject/                     # 내 프로젝트
│   ├── 📁 page1/
│   │   └── WorkerMyProjectAcceptedScreen.kt  # 확정된 일자리
│   └── 📁 page2/
│       └── WorkerMyProjectPendingScreen.kt   # 대기중 지원
│
├── 📁 income/                        # 수입 관리
│   ├── 📁 page1/
│   │   └── IncomeManagementScreen.kt # 수입 대시보드
│   └── 📁 page2/
│       └── GraphScreen.kt            # 수입 그래프
│
├── 📁 myInfo/                        # 내 정보
│   └── 📁 page1/
│       └── MyInfo.kt                 # 프로필 화면
│
└── 📁 common/                        # 공통 컴포넌트
    └── WorkerBottomNav.kt            # 하단 네비게이션
```

---

## 3. 인증 모듈 (Auth)

### 📱 회원가입 플로우 (6단계)

#### Page 1: SMS 인증
**파일**: `WorkerJoinPage1Screen.kt`
```kotlin
주요 기능:
- 전화번호 입력 (010-XXXX-XXXX)
- SMS 인증번호 발송
- 6자리 인증번호 확인
- 3분 타이머
- 재발송 기능
```

#### Page 2: 기본 정보
**파일**: `WorkerJoinPage2Screen.kt`
```kotlin
필수 입력:
- 이름 (실명)
- 생년월일 (DatePicker)
- 성별 선택 (남/여)
- 국적 (내국인/외국인)

컴포넌트:
- CommonDatePicker: 생년월일 선택
- 최소 만 18세 이상 검증
```

#### Page 3: 계좌 정보
**파일**: `WorkerJoinPage3Screen.kt`
```kotlin
필수 입력:
- 은행 선택 (BottomSheet)
- 계좌번호
- 예금주명

컴포넌트:
- BankSelectList: 은행 목록
  * KB국민은행
  * 신한은행
  * 우리은행
  * 하나은행
  * NH농협은행
  * 카카오뱅크
  * 토스뱅크
```

#### Page 4: 주소
**파일**: `WorkerJoinPage4Screen.kt`
```kotlin
필수 입력:
- 기본 주소 (카카오 주소 API)
- 상세 주소
- 우편번호

기능:
- 주소 검색 다이얼로그
- 현재 위치 기반 자동 입력
```

#### Page 5: 로그인 정보
**파일**: `WorkerJoinPage5Screen.kt`
```kotlin
필수 입력:
- 로그인 ID (중복 확인)
- 비밀번호 (8자 이상)
- 비밀번호 확인
- 이메일 (선택)

검증:
- ID 중복 확인 API
- 비밀번호 강도 체크
- 비밀번호 일치 확인
```

#### Page 6: 직종 선택
**파일**: `WorkerJoinPage6Screen.kt`
```kotlin
필수 선택:
- 주 직종 (1개)
- 보조 직종 (복수 선택)
- 경력 년수

직종 목록:
- 목수
- 철근공
- 미장공
- 도장공
- 전기공
- 배관공
- 용접공
- 비계공
- 조공
- 일반인부

컴포넌트:
- JobSelectList: 직종 선택 리스트
- 경력 입력 (0-50년)
```

### 🔐 로그인
**파일**: `WorkerLoginPage.kt`
```kotlin
기능:
- ID/비밀번호 로그인
- 자동 로그인 (체크박스)
- ID 저장
- 비밀번호 찾기
- 회원가입 링크

컴포넌트:
- LoginBottomMiddleView: 
  * ID 찾기
  * 비밀번호 찾기
  * 회원가입
```

### 📊 상태 관리
```kotlin
WorkerJoinSharedViewModel:
- 6단계 상태 통합 관리
- 입력 검증
- API 통신
- 네비게이션 제어

WorkerJoinSharedUiState:
- 모든 입력 필드 상태
- 검증 에러
- 로딩 상태
- 완료 상태

WorkerJoinSharedEvent:
- 사용자 입력 이벤트
- 검증 이벤트
- API 호출 이벤트
- 네비게이션 이벤트
```

---

## 4. 일자리 목록 (ProjectList)

### 📋 메인 목록
**파일**: `WorkerProjectList.kt`

#### 필터링 옵션
```kotlin
위치 필터:
- 현재 위치 기반
- 1km, 3km, 5km, 10km 반경
- 전체 지역

직종 필터:
- 전체
- 주 직종
- 보조 직종
- 관심 직종

급여 필터:
- 전체
- 10만원 이상
- 15만원 이상
- 20만원 이상

기타 필터:
- 긴급 모집
- 식사 제공
- 교통비 지원
- 픽업 서비스
```

#### 정렬 옵션
```kotlin
- 최신순 (기본)
- 거리순
- 급여 높은순
- 마감 임박순
```

#### 일자리 카드 UI
```kotlin
JobCard 표시 정보:
┌─────────────────────────────────┐
│ [긴급] 강남 오피스텔 신축        │
│ (주)건설왕 ⭐4.5                │
│                                 │
│ 📍 강남구 역삼동 (2.3km)        │
│ 📅 2025.08.15 (금)              │
│ ⏰ 08:00 - 18:00                │
│ 💰 일당 180,000원               │
│                                 │
│ 혜택: 🍚 식사 🚌 교통비         │
│                                 │
│ 모집: 5/10명 | 지원: 23명       │
│                                 │
│ [즉시 지원] [북마크]            │
└─────────────────────────────────┘
```

### 📄 일자리 상세
**파일**: `WorkerProject.kt`

#### 상세 정보 탭
```kotlin
1. 기본 정보 탭:
   - 회사 정보 (이름, 평점, 리뷰)
   - 근무 정보 (날짜, 시간, 장소)
   - 급여 정보 (기본급, 추가수당)
   - 모집 정보 (인원, 마감일)

2. 상세 설명 탭:
   - 업무 내용
   - 자격 요건
   - 우대 사항
   - 준비물

3. 혜택 정보 탭:
   - 식사 제공
   - 교통비 지원
   - 숙소 제공
   - 픽업 서비스
   - 주차 가능

4. 현장 사진 탭:
   - 작업 현장 사진
   - 숙소 사진
   - 식당 사진
```

#### 하단 액션 바
```kotlin
┌─────────────────────────────────┐
│ 💰 일당: 180,000원              │
│                                 │
│ [북마크] [공유] [지원하기]      │
└─────────────────────────────────┘
```

### 📝 지원하기
**파일**: `ApplyProject.kt`

#### 지원 폼
```kotlin
지원 정보:
- 희망 급여 (자동 입력)
- 출근 가능 시간
- 자기소개 (선택)
- 보유 장비 체크

확인 사항:
☑ 근무 조건을 확인했습니다
☑ 출근 가능합니다
☑ 개인정보 제공에 동의합니다

컴포넌트:
- WorkingDatesCalendar: 
  * 복수 날짜 선택
  * 불가능 날짜 표시
  * 선택된 날짜 요약
```

---

## 5. 내 프로젝트 (MyProject)

### ✅ 확정된 일자리
**파일**: `WorkerMyProjectAcceptedScreen.kt`

#### 확정 일자리 카드
```kotlin
AcceptedJobCard:
┌─────────────────────────────────┐
│ ✅ 강남 오피스텔 신축           │
│ (주)건설왕                      │
│                                 │
│ 📅 2025.08.15 (금) D-3          │
│ ⏰ 08:00 - 18:00                │
│ 📍 강남구 역삼동                │
│                                 │
│ 담당자: 김현장 010-1234-5678    │
│                                 │
│ [📞 전화] [💬 메시지] [📍 길찾기]│
│                                 │
│ [QR 체크인] [출근 취소]         │
└─────────────────────────────────┘
```

#### 출근 관리
```kotlin
QR 체크인:
- QR 코드 스캔
- GPS 위치 확인
- 시간 자동 기록

체크아웃:
- 퇴근 시간 기록
- 근무 시간 계산
- 일당 자동 계산
```

### ⏳ 대기중 지원
**파일**: `WorkerMyProjectPendingScreen.kt`

#### 지원 상태별 표시
```kotlin
PendingJobCard:
┌─────────────────────────────────┐
│ ⏳ 대기중                       │
│ 강남 오피스텔 신축              │
│ (주)건설왕                      │
│                                 │
│ 지원일: 2025.08.10              │
│ 근무일: 2025.08.15              │
│ 급여: 180,000원                 │
│                                 │
│ 지원자: 23명 / 모집: 10명       │
│                                 │
│ [지원 취소] [수정]              │
└─────────────────────────────────┘

상태 구분:
- ⏳ 대기중 (노란색)
- ✅ 수락됨 (초록색)
- ❌ 거절됨 (빨간색)
- ⏰ 마감됨 (회색)
```

---

## 6. 수입 관리 (Income)

### 💰 수입 대시보드
**파일**: `IncomeManagementScreen.kt`

#### 월간 요약
```kotlin
MonthSummaryCard:
┌─────────────────────────────────┐
│ 2025년 8월 수입                 │
│                                 │
│ 총 수입: ₩3,600,000            │
│ 근무일수: 20일                  │
│ 일 평균: ₩180,000              │
│                                 │
│ 전월 대비: +15% ↑              │
└─────────────────────────────────┘
```

#### 정산 내역
```kotlin
PaymentHistoryList:
┌─────────────────────────────────┐
│ 08.07 (수) - 완료 ✅            │
│ 강남 오피스텔                   │
│ ₩180,000                       │
├─────────────────────────────────┤
│ 08.06 (화) - 대기중 ⏳         │
│ 판교 주상복합                   │
│ ₩165,000                       │
├─────────────────────────────────┤
│ 08.05 (월) - 완료 ✅            │
│ 여의도 리모델링                 │
│ ₩200,000                       │
└─────────────────────────────────┘
```

### 📊 수입 그래프
**파일**: `GraphScreen.kt`

#### 그래프 옵션
```kotlin
기간 선택:
- 주간 (7일)
- 월간 (30일)
- 분기 (3개월)
- 연간 (12개월)

그래프 타입:
- 막대 그래프 (기본)
- 선 그래프
- 파이 차트 (직종별)

표시 데이터:
- 총 수입
- 근무 일수
- 평균 일당
- 직종별 비율
```

---

## 7. 내 정보 (MyInfo)

### 👤 프로필 관리
**파일**: `MyInfo.kt`

#### 프로필 섹션
```kotlin
ProfileSection:
┌─────────────────────────────────┐
│ [프로필 사진]                   │
│                                 │
│ 김철수 (남, 45세)               │
│ ⭐ 4.8 (리뷰 52개)              │
│                                 │
│ 주직종: 목수 (경력 15년)        │
│ 보조: 철근공, 조공              │
│                                 │
│ 📞 010-1234-5678                │
│ 📍 서울시 강남구                │
│                                 │
│ [프로필 수정]                   │
└─────────────────────────────────┘
```

#### 경력 관리
```kotlin
ExperienceSection:
- 총 경력: 15년 3개월
- 완료 프로젝트: 523개
- 출근율: 98.5%
- 재고용률: 85%

경력 증명서:
- PDF 다운로드
- 이메일 발송
- 프린트
```

#### 자격증 관리
```kotlin
CertificationSection:
- 건축목공기능사
- 거푸집기능사
- 안전교육 이수증

기능:
- 자격증 등록
- 사진 업로드
- 유효기간 관리
```

#### 계좌 정보
```kotlin
BankAccountSection:
- 주 계좌: KB국민은행
- 계좌번호: ****-****-1234
- 예금주: 김철수

기능:
- 계좌 변경
- 복수 계좌 등록
```

#### 설정 메뉴
```kotlin
SettingsMenu:
- 알림 설정
- 개인정보 수정
- 비밀번호 변경
- 약관 및 정책
- 고객센터
- 로그아웃
- 회원 탈퇴
```

---

## 8. 공통 컴포넌트 (Common)

### 🧭 하단 네비게이션
**파일**: `WorkerBottomNav.kt`

```kotlin
네비게이션 구성:
┌────┬────┬────┬────┬────┐
│ 🏗️ │ 📋 │ ➕ │ 💰 │ 👤 │
├────┼────┼────┼────┼────┤
│일자리│내일│지원│수입│내정보│
└────┴────┴────┴────┴────┘

enum class WorkerBottomNavItem:
- JOBS: 일자리 목록
- MY_PROJECTS: 내 프로젝트
- APPLY: 빠른 지원
- INCOME: 수입 관리
- MY_INFO: 내 정보
```

### 🎨 공통 UI 컴포넌트

#### CommonWorkerTopBar
```kotlin
기능:
- 뒤로가기 버튼
- 제목 표시
- 액션 버튼 (최대 2개)
- 진행 표시 (회원가입)
```

#### CommonBottomSheetInput
```kotlin
용도:
- 은행 선택
- 직종 선택
- 지역 선택

특징:
- 검색 기능
- 다중 선택
- 체크박스/라디오버튼
```

---

## 9. 주요 화면 플로우

### 🔄 회원가입 플로우
```
SMS인증 → 기본정보 → 계좌정보 → 주소 → 로그인정보 → 직종선택 → 완료
```

### 🔍 일자리 지원 플로우
```
일자리 목록 → 상세 보기 → 지원하기 → 지원 완료 → 내 프로젝트
```

### ✅ 출근 플로우
```
확정 일자리 → QR 체크인 → 근무 → 체크아웃 → 정산 대기
```

### 💰 정산 플로우
```
근무 완료 → 정산 요청 → 승인 대기 → 입금 완료 → 수입 내역
```

---

## 🛠️ 기술 스택

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Navigation**: Compose Destinations
- **State Management**: StateFlow + SharedFlow
- **DI**: Koin
- **Network**: Retrofit + OkHttp
- **Local Storage**: 
  - Room (데이터베이스)
  - SharedPreferences (설정)
- **Map**: Kakao Map SDK
- **QR**: ZXing
- **Chart**: MPAndroidChart

---

## 📱 지원 환경

- **최소 Android**: API 21 (Android 5.0)
- **권장 Android**: API 30+ (Android 11+)
- **화면 크기**: 4.7" ~ 6.7"
- **방향**: Portrait only
- **언어**: 한국어

---

## 🔒 보안 사항

- JWT 토큰 기반 인증
- 자동 로그아웃 (30분)
- SSL/TLS 암호화
- 개인정보 암호화 저장
- 위치 정보 권한 관리