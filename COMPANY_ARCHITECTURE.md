# 🏗️ 직직직 사업자(Company) 앱 완전 통합 아키텍처 문서

> **최종 업데이트**: 2025-01-15  
> **버전**: 3.0  
> **주요 변경사항**: Company 전용 문서로 재구성, 실제 구현 검증 완료
> **문서 용도**: 사업자(Company) 앱 전용 아키텍처

## 🔄 최근 변경사항 (2025-09-09)
- ✅ **ProjectCreateDialog**: 4개 필수 필드로 간소화 (프로젝트명, 착공일, 준공일, 작업장소)
- ✅ **대형 다이얼로그 UI**: 화면의 95% 사용, 스크롤 가능
- ✅ **날짜 선택기**: Material3 DatePicker 통합
- ✅ **지도 API 준비**: LocationPickerDialog 추가
- ✅ **ProjectCard 변경**: 일당/인원 정보 제거, 날짜 기반 진행도로 변경
- ✅ **진행도 계산**: (경과일수/총일수) * 100으로 자동 계산

## 📚 목차
1. [프로젝트 개요](#1-프로젝트-개요)
2. [전체 파일 구조](#2-전체-파일-구조)
3. [인증 모듈 (Auth)](#3-인증-모듈-auth)
4. [프로젝트 관리 (ProjectList)](#4-프로젝트-관리-projectlist)
5. [인력 스카우트 (Scout)](#5-인력-스카우트-scout)
6. [자금 관리 (Money)](#6-자금-관리-money)
7. [사업자 정보 (Info)](#7-사업자-정보-info)
8. [공통 컴포넌트 (Common)](#8-공통-컴포넌트-common)
9. [API 데이터 형식](#9-api-데이터-형식)

---

## 1. 프로젝트 개요

### 📱 앱 정보
- **앱 이름**: 직직직 사업자
- **패키지명**: com.billcorea.jikgong
- **주요 기능**: 건설/일용직 인력 매칭 플랫폼
- **핵심 차별점**: 기존 인력사무소 대비 50% 절감된 수수료 (10% → 5%)

### 🎯 주요 특징
- ✅ **수수료 50% 절감**: 실시간 절감액 표시
- ✅ **AI 인력 매칭**: 과거 채용 패턴 기반 추천
- ✅ **실시간 출근 관리**: QR/GPS 기반 출근 체크
- ✅ **일괄 정산 시스템**: 원클릭 다중 송금

---

## 2. 전체 파일 구조

```
presentation/company/
├── 📁 auth/                          # 인증 관련
│   ├── 📁 common/
│   │   ├── 📁 components/
│   │   │   ├── CommonButton.kt       # 공통 버튼
│   │   │   ├── CommonTextInput.kt    # 공통 입력 필드
│   │   │   ├── CommonTopBar.kt       # 공통 상단바
│   │   │   └── LabelText.kt          # 라벨 텍스트
│   │   └── 📁 constants/
│   │       └── JoinConstants.kt      # 회원가입 상수
│   ├── 📁 join/                      # 회원가입
│   │   ├── 📁 page1/
│   │   │   ├── CompanyJoinPage1Screen.kt  # SMS 인증
│   │   │   └── 📁 components/
│   │   │       ├── _PhoneNumberInput.kt   # 전화번호 입력
│   │   │       └── _VerificationCodeInput.kt # 인증번호 입력
│   │   ├── 📁 page2/
│   │   │   ├── CompanyJoinPage2Screen.kt  # 사업자 정보
│   │   │   └── 📁 components/
│   │   │       └── PhoneNumberDisplay.kt  # 전화번호 표시
│   │   ├── 📁 page3/
│   │   │   └── CompanyJoinPage3Screen.kt  # 추가 정보
│   │   └── 📁 shared/
│   │       ├── CompanyJoinSharedViewModel.kt
│   │       ├── CompanyJoinSharedUiState.kt
│   │       └── CompanyJoinSharedEvent.kt
│   └── 📁 login/                     # 로그인
│       ├── 📁 page1/
│       │   └── CompanyLoginPage1Screen.kt
│       └── 📁 shared/
│           ├── CompanyLoginSharedViewModel.kt
│           ├── CompanyLoginSharedUiState.kt
│           └── CompanyLoginSharedEvent.kt
│
├── 📁 main/
│   ├── 📁 common/                    # 공통 컴포넌트
│   │   ├── CompanyBottomBar.kt       # 하단 네비게이션
│   │   ├── CompanyTopBar.kt          # 메인 상단바
│   │   └── CompanySharedViewModel.kt # 공유 뷰모델
│   │
│   ├── 📁 projectlist/               # 프로젝트 관리
│   │   ├── 📁 data/
│   │   │   ├── Project.kt            # 프로젝트 모델
│   │   │   ├── ProjectFilter.kt      # 필터 모델
│   │   │   └── ProjectStats.kt       # 통계 모델
│   │   ├── 📁 feature/
│   │   │   ├── 📁 create/            # 프로젝트 생성
│   │   │   │   ├── 📁 screen/
│   │   │   │   │   ├── ProjectCreateDialog.kt
│   │   │   │   │   └── JobCreationScreen.kt
│   │   │   │   ├── 📁 components/
│   │   │   │   │   └── UrgentRecruitmentDialog.kt
│   │   │   │   ├── 📁 model/
│   │   │   │   │   ├── ProjectCreateEvent.kt
│   │   │   │   │   ├── ProjectCreateUiEvent.kt
│   │   │   │   │   └── ProjectCreateUiState.kt
│   │   │   │   └── 📁 viewmodel/
│   │   │   │       └── ProjectCreateViewModel.kt
│   │   │   ├── 📁 detail/            # 프로젝트 상세
│   │   │   │   └── 📁 screen/
│   │   │   │       ├── ProjectDetailScreen.kt    # 상세 정보
│   │   │   │       ├── AttendanceCheckScreen.kt  # 출근 체크
│   │   │   │       ├── CheckoutScreen.kt         # 퇴근 체크
│   │   │   │       ├── WorkerManagementScreen.kt # 인력 관리
│   │   │   │       ├── WorkerInfoScreen.kt       # 근로자 정보
│   │   │   │       ├── PaymentSummaryScreen.kt   # 정산 요약
│   │   │   │       ├── ExistingJobScreen.kt      # 기존 작업
│   │   │   │       ├── PreviousJobPostsScreen.kt # 이전 게시물
│   │   │   │       └── TempSaveScreen.kt         # 임시 저장
│   │   │   └── 📁 period/            # 기간 연장
│   │   │       └── 📁 screen/
│   │   │           └── ProjectPeriodExtendScreen.kt
│   │   └── 📁 presentation/
│   │       ├── 📁 screen/
│   │       │   └── ProjectListScreen.kt  # 목록 화면
│   │       ├── 📁 model/
│   │       │   └── ProjectListUiState.kt
│   │       └── 📁 viewmodel/
│   │           └── ProjectListViewModel.kt
│   │
│   ├── 📁 scout/                     # 인력 스카우트
│   │   ├── 📁 feature/
│   │   │   └── 📁 pages/
│   │   │       ├── WorkerListPage.kt      # 인력 목록 (AI 추천)
│   │   │       ├── ProposalListPage.kt    # 제안 관리
│   │   │       └── LocationSettingPage.kt # 위치 설정
│   │   └── 📁 presentation/
│   │       ├── 📁 screen/
│   │       │   └── CompanyScoutScreen.kt  # 메인 화면
│   │       ├── 📁 component/
│   │       │   ├── WorkerCard.kt          # 인력 카드
│   │       │   ├── ProposalCard.kt        # 제안 카드
│   │       │   ├── WorkerDetailBottomSheet.kt # 상세 정보
│   │       │   ├── ScoutTabBar.kt         # 탭바
│   │       │   └── EmptyState.kt          # 빈 상태
│   │       └── 📁 viewmodel/
│   │           └── CompanyScoutViewModel.kt
│   │
│   ├── 📁 money/                     # 자금 관리
│   │   ├── 📁 data/
│   │   │   └── CompanyMoneySharedEvent.kt
│   │   ├── 📁 feature/
│   │   │   └── 📁 dialog/
│   │   │       └── 📁 screen/
│   │   │           ├── PaymentConfirmationDialog.kt     # 개별 지급 확인
│   │   │           ├── BulkPaymentConfirmationDialog.kt # 일괄 지급
│   │   │           ├── PendingPaymentsDialog.kt         # 대기중 지급
│   │   │           ├── CompletedPaymentDetailDialog.kt  # 완료 상세
│   │   │           ├── CompletedProjectDetailDialog.kt  # 프로젝트 상세
│   │   │           ├── CompletedAmountDialog.kt         # 완료 금액
│   │   │           ├── ProjectListDialog.kt             # 프로젝트 선택
│   │   │           └── ProjectWorkerListDialog.kt       # 인력 목록
│   │   └── 📁 presentation/
│   │       ├── 📁 screen/
│   │       │   └── CompanyMoneyScreen.kt  # 메인 화면
│   │       ├── 📁 component/
│   │       │   ├── ProjectPaymentCard.kt        # 프로젝트 정산 카드
│   │       │   ├── ProjectPaymentFilterBar.kt   # 필터바
│   │       │   ├── ProjectPaymentSummaryCard.kt # 요약 카드
│   │       │   ├── SavingsIndicator.kt          # 절감액 표시 ⭐
│   │       │   ├── PaymentCard.kt               # 지급 카드
│   │       │   ├── PaymentFilterBar.kt          # 지급 필터
│   │       │   ├── PaymentSummaryCard.kt        # 지급 요약
│   │       │   ├── DepositButton.kt             # 입금 버튼
│   │       │   ├── EmptyMoneyState.kt           # 빈 상태
│   │       │   └── ScrollBar.kt                 # 스크롤바
│   │       ├── 📁 model/
│   │       │   └── CompanyMoneySharedUiState.kt
│   │       └── 📁 viewmodel/
│   │           ├── CompanyMoneyViewModel.kt
│   │           └── CompanyMoneySharedViewModel.kt
│   │
│   ├── 📁 info/                      # 사업자 정보
│   │   ├── 📁 data/
│   │   │   ├── ProfileData.kt        # 프로필 데이터
│   │   │   ├── AnnouncementData.kt   # 공지사항
│   │   │   ├── NewAnnouncementData.kt
│   │   │   └── TermsAndPoliciesData.kt # 약관
│   │   ├── 📁 feature/
│   │   │   └── 📁 dialog/
│   │   │       └── 📁 screen/
│   │   │           ├── ProfileEditDialog.kt         # 프로필 수정
│   │   │           ├── AnnouncementDetailDialog.kt  # 공지 상세
│   │   │           ├── TermsCategoryDialog.kt       # 약관 카테고리
│   │   │           └── TermsDetailDialog.kt         # 약관 상세
│   │   └── 📁 presentation/
│   │       ├── 📁 screen/
│   │       │   ├── MyInfoScreen.kt              # 내 정보
│   │       │   ├── AnnouncementScreen.kt        # 공지사항
│   │       │   ├── CustomerServiceScreen.kt     # 고객센터
│   │       │   ├── NotificationSettingsScreen.kt # 알림 설정
│   │       │   └── TermsAndPoliciesScreen.kt    # 약관
│   │       ├── 📁 component/
│   │       │   ├── HeaderSection.kt     # 헤더
│   │       │   ├── PremiumBanner.kt     # 프리미엄 배너
│   │       │   ├── QuickMenu.kt         # 빠른 메뉴
│   │       │   ├── SavingsCard.kt       # 절감액 카드
│   │       │   ├── SettingsMenu.kt      # 설정 메뉴
│   │       │   └── StatsGrid.kt         # 통계 그리드
│   │       └── 📁 viewmodel/
│   │           └── CompanyInfoViewModel.kt
│   │
│   └── 📁 shared/                    # 공유 상태
│       ├── CompanyMainViewModel.kt
│       ├── CompanyMainUiState.kt
│       └── CompanyMainEvent.kt
```

---

## 3. 인증 모듈 (Auth)

### 📱 회원가입 플로우 (3단계)

#### Page 1: SMS 인증
**파일**: `CompanyJoinPage1Screen.kt`
```kotlin
주요 기능:
- SMS 인증번호 발송 (60초 카운트다운)
- 재발송 기능 (3회 제한)
- 인증번호 6자리 자동 포커싱
- 실시간 유효성 검사
```

**컴포넌트**:
- `_PhoneNumberInput.kt`: 전화번호 입력 (010-XXXX-XXXX 형식)
- `_VerificationCodeInput.kt`: 6자리 인증번호 입력

#### Page 2: 사업자 정보
**파일**: `CompanyJoinPage2Screen.kt`
```kotlin
필수 입력:
- 사업자등록번호 (10자리, API 검증)
- 대표자명 (실명 확인)
- 상호명
- 업종 선택 (건설업 세부 20개 분류)
- 사업장 주소 (카카오 주소 API)
```

#### Page 3: 추가 정보 및 약관
**파일**: `CompanyJoinPage3Screen.kt`
```kotlin
입력 정보:
- 보험 가입 정보 (산재/고용보험)
- 로그인 ID/비밀번호 설정
- 이메일 주소
- 약관 동의 (필수 3개, 선택 2개)
- 마케팅 수신 동의
```

### 🔐 로그인
**파일**: `CompanyLoginPage1Screen.kt`
```kotlin
기능:
- ID/비밀번호 로그인
- 자동 로그인 (토큰 저장)
- 생체 인증 (지문/Face ID)
- 간편 로그인 (카카오, 네이버)
- 비밀번호 찾기
- 5회 실패 시 계정 잠금
```

### 📊 상태 관리
- `CompanyJoinSharedViewModel.kt`: 회원가입 전체 상태 관리
- `CompanyLoginSharedViewModel.kt`: 로그인 상태 관리
- Event-driven 아키텍처 (sealed class 사용)

---

## 4. 프로젝트 관리 (ProjectList)

### 📋 프로젝트 목록
**파일**: `ProjectListScreen.kt`

#### 탭 구성
```kotlin
1. 전체
   - 모든 프로젝트 표시
   - 총 개수 표시

2. 모집중 (RECRUITING)
   - 긴급 모집 배지 🚨
   - 마감 D-1 알림

3. 진행중 (IN_PROGRESS)
   - 날짜 기반 진행률 표시 ⭐
   - 경과 일수/전체 일수
   - 진행 퍼센트

4. 완료 (COMPLETED)
   - 완료된 프로젝트
   - 100% 진행률
```

#### 프로젝트 카드 표시 정보 ⭐ **변경**
```kotlin
ProjectCard 표시 항목:
✅ 프로젝트 이름
✅ 작업장소
✅ 착공일
✅ 준공일
✅ 날짜 기반 진행도 (새로운 기능)

❌ 제거된 항목:
- 회사명
- 일당 정보
- 인원 정보 (현재/최대)
```

#### 진행도 계산 방식 ⭐ **새로운 기능**
```kotlin
날짜 기반 진행도:
- 총 일수 = 준공일 - 착공일
- 경과 일수 = 오늘 - 착공일
- 진행률 = (경과 일수 / 총 일수) * 100

상태 표시:
- 시작 전: "시작 전" (0%)
- 진행 중: "진행중 (15/30일)"
- 완료: "완료" (100%)

색상 규칙:
- 0-79%: 파란색 (0xFF4B7BFF)
- 80-99%: 노란색 (0xFFFFC107)
- 100%: 초록색 (0xFF4CAF50)
```

### 🆕 프로젝트 생성 ⭐ **업데이트**
**파일**: `ProjectCreateDialog.kt`

#### 크기 및 UI 변경
```kotlin
다이얼로그 크기:
- 화면 너비의 95%
- 화면 높이의 80%
- 스크롤 가능
- 파란색 헤더 + 닫기 버튼
```

#### 입력 필드 (4개로 간소화)
```kotlin
1. 프로젝트 이름 *
   - 텍스트 입력
   - 예: "강남 오피스텔 신축 공사"

2. 착공일 *
   - DatePickerDialog 사용
   - 캘린더 아이콘
   - ISO 형식 (YYYY-MM-DD)

3. 준공일 *
   - DatePickerDialog 사용
   - 캘린더 아이콘
   - ISO 형식 (YYYY-MM-DD)

4. 작업장소 *
   - LocationPickerDialog 사용
   - 카카오맵 API 연동 준비
   - 주소 검색 기능
   - 상세 주소 선택 옵션
```

#### 새로운 다이얼로그 컴포넌트
```kotlin
DatePickerDialog:
- Material3 DatePicker 사용
- 날짜 선택 후 자동 포맷팅

LocationPickerDialog:
- 지도 보기 영역
- 주소 검색바
- "이 위치로 선택" 버튼
- 위도/경도 저장
```

**제거된 필드**:
- ~~인력 정보~~
- ~~급여 정보~~
- ~~부가 혜택~~

### ✅ 출근 관리
**파일**: `AttendanceCheckScreen.kt`

#### 출근 체크 방식
```kotlin
1. QR 코드 스캔
   - 근로자별 고유 QR
   - 실시간 검증
   - 중복 체크 방지

2. GPS 인증
   - 현장 반경 100m 내
   - 위치 정확도 표시
   - 실내/실외 구분

3. 수동 체크
   - 관리자 권한 필요
   - 사유 입력 필수
```

**상태 표시**:
- ✅ 정상 출근 (08:00)
- ⏰ 지각 (08:30) - 30분 단위
- ❌ 결근
- 🚪 조퇴 (15:00)
- 📝 비고 사항

### 👷 근로자 관리
**파일**: `WorkerManagementScreen.kt`, `WorkerInfoScreen.kt`

#### 날짜별 관리 탭
```kotlin
1. 지원자 관리
   - 지원자 목록 (프로필, 경력)
   - 일괄 승인/거절
   - 대기자 관리

2. 확정 인력
   - 연락처 일괄 발송
   - 출근 예정 알림
   - 교체 인력 지정

3. 출근/퇴근
   - 실시간 출근 현황
   - QR/GPS 체크인
   - 근무 시간 자동 계산
```

#### 근로자 평가
```kotlin
평가 항목 (5점 척도):
- 성실성 ⭐⭐⭐⭐⭐
- 숙련도 ⭐⭐⭐⭐☆
- 협업 ⭐⭐⭐⭐⭐
- 시간 준수 ⭐⭐⭐⭐☆
- 안전 수칙 ⭐⭐⭐⭐⭐

추가 기능:
- 블랙리스트 등록
- 우수 근로자 표시
- 재고용 의사 (80%)
```

### 💰 정산 관리
**파일**: `PaymentSummaryScreen.kt`, `CheckoutScreen.kt`

#### 정산 프로세스
```kotlin
1. 근무 확정
   - 출근 기록 확인
   - 근무 시간 계산
   - 추가 근무 확인

2. 급여 계산
   - 기본급: 150,000원
   - 시간외: +30,000원
   - 주말: +50,000원
   - 공제: -5,000원
   - 수수료(5%): -11,250원
   - 최종: 213,750원

3. 송금 처리
   - 개별/일괄 선택
   - 계좌 정보 확인
   - 송금 예약
```

---

## 5. 인력 스카우트 (Scout)

### 🔍 인력 검색
**파일**: `WorkerListPage.kt`

#### AI 추천 필터 ⭐
```kotlin
AI 매칭 알고리즘:
1. 과거 채용 패턴 분석
   - 자주 채용한 직종
   - 선호 경력대
   - 평균 평점

2. 프로젝트 유형 매칭
   - 유사 프로젝트 경험
   - 필요 기술 보유
   - 거리 적합도

3. 성과 기반 추천
   - 출근율 95% 이상
   - 평점 4.5 이상
   - 재고용률 80% 이상

매칭 점수: 0-100점
- 90점 이상: 최적 매칭 🏆
- 70-89점: 우수 매칭 ⭐
- 50-69점: 보통 매칭
```

#### 일반 필터
```kotlin
거리 필터:
- 1km 이내 (도보 가능)
- 3km 이내 (대중교통)
- 5km 이내 (자차 필수)
- 10km 이내 (원거리)

직종 필터:
- 목수 (10년 이상)
- 철근공 (5-10년)
- 조공 (1-5년)
- 용접공
- 배관공
- 전기공
- 미장공

기타 필터:
- 희망 일당 범위
- 가능 요일
- 보유 자격증
- 보험 가입 여부
```

### 👤 인력 상세 정보
**파일**: `WorkerDetailBottomSheet.kt`

#### 표시 정보
```kotlin
기본 정보:
- 프로필 사진
- 이름/나이/성별
- 거주지 (거리 3.2km)
- 연락처 (010-XXXX-XXXX)

경력 정보:
- 주 직종: 목수 (15년)
- 보조 가능: 철근공, 조공
- 주요 프로젝트:
  * 롯데타워 (2023.01-06)
  * 잠실 아파트 (2022.08-12)
  * 판교 오피스 (2022.03-07)

평가 정보:
- 평균 평점: ⭐ 4.8/5.0 (152건)
- 출근율: 98% (245/250일)
- 재고용률: 85% (17/20회)
- 최근 리뷰:
  "성실하고 실력이 뛰어남"
  "시간 약속 철저"

인증 뱃지:
🏆 우수 근로자 (상위 5%)
✓ 신원 인증 완료
📋 건설기능사 보유
🛡️ 4대보험 가입

자격증:
- 건축목공기능사 (2015.03)
- 거푸집기능사 (2018.07)
- 안전교육 이수 (2024.01)
```

### 📨 제안 관리
**파일**: `ProposalListPage.kt`

#### 제안 상태별 관리
```kotlin
대기중 (PENDING) - 노란색:
- 근로자 미확인
- 48시간 자동 만료
- 푸시 알림 재발송

검토중 (VIEWED) - 파란색:
- 근로자가 확인함
- 마지막 확인: 10분 전
- 관심 표시 가능

수락 (ACCEPTED) - 초록색:
- 채용 확정
- 출근 안내 발송
- 계약서 작성

거절 (REJECTED) - 빨간색:
- 거절 사유 확인
- 대체 인력 추천
- 재제안 가능 (7일 후)

만료 (EXPIRED) - 회색:
- 48시간 무응답
- 자동 만료 처리
- 통계에서 제외
```

#### 제안 기능
```kotlin
일괄 제안:
- 최대 20명 동시 발송
- 프로젝트별 그룹 관리
- 예약 발송 (특정 시간)

제안 템플릿:
1. 긴급 모집용
2. 장기 프로젝트용
3. 우수 인력 특별 대우
4. 신규 인력 환영

급여 협상:
- 기본 제시: 150,000원
- 협상 가능 범위: ±20,000원
- 자동 승인 설정
```

### 📍 위치 설정
**파일**: `LocationSettingPage.kt`

#### 위치 관리
```kotlin
현재 위치:
- GPS 자동 인식
- 주소 직접 입력
- 지도에서 선택
- 최근 위치 10개 저장

검색 반경:
- 1km (도보 15분)
- 3km (대중교통 30분)
- 5km (자차 20분)
- 10km (원거리)
- 전체 (제한 없음)

다중 현장:
- 최대 5개 현장 등록
- 현장별 검색 설정
- 빠른 전환 기능

SharedPreferences 저장:
- 마지막 설정 자동 저장
- 앱 재시작 시 복원
```

---

## 6. 자금 관리 (Money)

### 💰 메인 대시보드
**파일**: `CompanyMoneyScreen.kt`

#### 핵심 지표 표시
```kotlin
상단 요약 카드:
┌─────────────────────────────┐
│ 이번 달 총 지출            │
│ ₩15,250,000                │
│ 전월 대비 +12%             │
└─────────────────────────────┘

┌─────────────────────────────┐
│ 대기중 정산                │
│ ₩3,200,000 (15명)          │
│ [일괄 지급] 버튼            │
└─────────────────────────────┘

┌─────────────────────────────┐
│ 💚 절감된 수수료            │
│ ₩762,500                   │
│ 기존 대비 50% 절감!        │
└─────────────────────────────┘
```

### 💚 수수료 절감 시스템 ⭐
**파일**: `SavingsIndicator.kt`, `SavingsHighlightCard.kt`

#### 절감액 계산 및 표시
```kotlin
실시간 절감액 표시:
┌─────────────────────────────────────┐
│ 📊 직직직 수수료 혜택               │
│                                     │
│ 기존 인력사무소    직직직 플랫폼    │
│    수수료 10%        수수료 5%      │
│   ₩1,525,000        ₩762,500       │
│                                     │
│ 💚 총 절감액: ₩762,500 (50% 절감)  │
└─────────────────────────────────────┘

DetailedSavingsCard 컴포넌트:
- 프로젝트별 절감 내역
- 월별 누적 절감액
- 연간 예상 절감액 (₩18,300,000)
- 경쟁사 대비 차트

절감액 애니메이션:
- 숫자 카운트업 효과
- 초록색 강조 표시
- 축하 이모티콘 🎉
```

#### 절감 통계
```kotlin
월간 리포트:
- 프로젝트 수: 12개
- 총 인력: 180명
- 총 지급액: ₩15,250,000
- 기존 수수료(10%): ₩1,525,000
- 직직직 수수료(5%): ₩762,500
- 절감액: ₩762,500
- 절감률: 50%

연간 예상:
- 예상 절감액: ₩18,300,000
- 프로젝트당 평균: ₩63,541
- 인력당 평균: ₩4,236
```

### 📋 프로젝트별 정산
**파일**: `ProjectPaymentCard.kt`, `ProjectPaymentFilterBar.kt`

#### 정산 카드 UI
```kotlin
ProjectPaymentCard:
┌─────────────────────────────────┐
│ 강남 오피스텔 신축 공사          │
│ 위치: 강남구 역삼동              │
│ 기간: 2025.08.01 - 08.07         │
│                                 │
│ 인력: 15명                      │
│ 총 급여: ₩2,250,000             │
│ 수수료(5%): ₩112,500            │
│ 절감액: ₩112,500 💚             │
│                                 │
│ [상세보기] [일괄지급]           │
└─────────────────────────────────┘

필터 옵션:
- 상태: 대기중/처리중/완료
- 기간: 오늘/이번주/이번달/전체
- 프로젝트: 전체/개별 선택
- 정렬: 날짜순/금액순/인원순
```

### 💸 일괄 지급 시스템
**파일**: `BulkPaymentConfirmationDialog.kt`

#### 일괄 지급 프로세스
```kotlin
1단계: 지급 대상 선택
┌─────────────────────────────────┐
│ 일괄 지급 대상                  │
│                                 │
│ ☑ 김철수 (목수) - ₩180,000     │
│ ☑ 이영희 (철근공) - ₩165,000   │
│ ☑ 박민수 (조공) - ₩120,000     │
│ ... 12명 더보기                 │
│                                 │
│ 선택: 15명 / 총액: ₩2,250,000   │
└─────────────────────────────────┘

2단계: 최종 확인
┌─────────────────────────────────┐
│ 🔔 일괄 지급 최종 확인          │
│                                 │
│ 지급 인원: 15명                 │
│ 총 지급액: ₩2,250,000           │
│ 수수료(5%): ₩112,500            │
│ 실 지급액: ₩2,137,500           │
│                                 │
│ 절감된 수수료: ₩112,500 💚      │
│                                 │
│ 비밀번호: [******]              │
│                                 │
│ [취소] [확인 및 송금]           │
└─────────────────────────────────┘

3단계: 송금 진행
- 실시간 진행률 표시
- 개별 송금 상태 업데이트
- 실패 건 자동 재시도
- 완료 알림 발송
```

### 📊 지급 내역 관리
**파일**: `CompletedPaymentDetailDialog.kt`, `CompletedProjectDetailDialog.kt`

#### 상세 내역 표시
```kotlin
CompletedPaymentDetailDialog:
┌─────────────────────────────────┐
│ 지급 완료 상세                  │
│                                 │
│ 수령인: 김철수                  │
│ 계좌: 국민은행 ****-****-1234   │
│ 금액: ₩180,000                  │
│ 송금일시: 2025.08.07 15:30      │
│ 상태: ✅ 완료                   │
│                                 │
│ 프로젝트: 강남 오피스텔         │
│ 근무일: 2025.08.01-07 (7일)     │
│ 직종: 목수                      │
│                                 │
│ 세금계산서: 발행완료            │
│ [다운로드]                      │
└─────────────────────────────────┘

검색 및 필터:
- 기간별 (일/주/월/년)
- 프로젝트별
- 근로자별
- 상태별 (완료/실패/취소)
- 금액 범위
```

---

## 7. 사업자 정보 (Info)

### 👤 마이페이지
**파일**: `MyInfoScreen.kt`

#### 프로필 섹션
```kotlin
HeaderSection 컴포넌트:
┌─────────────────────────────────┐
│ [프로필 이미지]                 │
│                                 │
│ (주)건설왕                      │
│ 대표: 홍길동                    │
│ ⭐ 4.8 (리뷰 152개)             │
│                                 │
│ 인증 뱃지:                      │
│ ✓ 사업자 인증                  │
│ ✓ 보험 가입                    │
│ ✓ 우수 사업자                  │
│                                 │
│ [프로필 수정]                   │
└─────────────────────────────────┘
```

### 📊 통계 대시보드
**파일**: `StatsGrid.kt`

#### 통계 그리드 표시
```kotlin
StatsGrid 레이아웃:
┌─────────┬─────────┐
│ 총 채용 │ 평균평점│
│  523명  │  4.8점  │
├─────────┼─────────┤
│재고용률 │ 응답률  │
│  85%    │  92%    │
├─────────┼─────────┤
│월 지출  │절감액   │
│₩15.2M   │₩762K    │
└─────────┴─────────┘

상세 통계:
- 직종별 채용 차트
- 월별 지출 트렌드
- 평점 변화 그래프
- 절감액 누적 차트
```

### 💎 프리미엄 기능
**파일**: `PremiumBanner.kt`

#### 프리미엄 혜택
```kotlin
PremiumBanner UI:
┌─────────────────────────────────┐
│ 👑 프리미엄 멤버십              │
│                                 │
│ 특별 혜택:                      │
│ • 수수료 3% (추가 40% 할인)     │
│ • AI 매칭 우선권                │
│ • 긴급 모집 무제한              │
│ • 전담 매니저 배정              │
│ • 상세 분석 리포트              │
│                                 │
│ 월 ₩99,000 → ₩49,000 (첫달)    │
│                                 │
│ [자세히 보기] [지금 가입]       │
└─────────────────────────────────┘

플랜 비교:
- BASIC: 5% 수수료, 기본 기능
- STANDARD: 4% 수수료, AI 매칭
- PREMIUM: 3% 수수료, 모든 기능
```

### 📢 공지사항
**파일**: `AnnouncementScreen.kt`, `AnnouncementDetailDialog.kt`

#### 공지사항 관리
```kotlin
공지 카테고리:
- 📌 중요 공지 (상단 고정)
- 📢 시스템 점검
- 🎉 이벤트/프로모션
- 📋 업데이트 소식
- ℹ️ 이용 안내

NewAnnouncementData:
- 읽지 않은 공지 뱃지
- 푸시 알림 연동
- 중요도별 색상 구분
```

### ⚙️ 설정
**파일**: `NotificationSettingsScreen.kt`

#### 알림 설정
```kotlin
알림 카테고리:
┌─────────────────────────────────┐
│ 알림 설정                       │
│                                 │
│ 출근 알림              [ON] 🔔  │
│ 제안 응답              [ON] 🔔  │
│ 정산 완료              [ON] 🔔  │
│ 긴급 모집              [OFF]    │
│ 마케팅                 [OFF]    │
│                                 │
│ 방해 금지 시간                  │
│ 22:00 - 07:00         [ON]      │
└─────────────────────────────────┘
```

---

## 8. 공통 컴포넌트 (Common)

### 🧭 네비게이션
**파일**: `CompanyBottomBar.kt`

```kotlin
하단 네비게이션 구성:
┌────┬────┬────┬────┐
│ 📋 │ 🔍 │ 💰 │ ℹ️ │
├────┼────┼────┼────┤
│목록│스카웃│자금│정보│
└────┴────┴────┴────┘

enum class CompanyBottomNavItem:
- PROJECTS: 프로젝트 목록
- SCOUT: 인력 스카우트  
- MONEY: 자금 관리
- INFO: 사업자 정보
```

### 🎨 공통 UI 컴포넌트
```kotlin
CommonButton.kt:
- Primary/Secondary 스타일
- 로딩 상태 지원
- 비활성화 상태

CommonTextInput.kt:
- 에러 메시지 표시
- 카운터 표시 (0/100)
- 아이콘 지원

CommonTopBar.kt:
- 뒤로가기 버튼
- 제목 중앙 정렬
- 액션 버튼 (최대 2개)
```

### 📱 상태 관리
**파일**: `CompanyMainViewModel.kt`, `CompanySharedViewModel.kt`

```kotlin
전역 상태:
- 사용자 정보
- 네비게이션 상태
- 알림 목록
- 에러 처리

SharedViewModel 패턴:
- 화면 간 데이터 공유
- 단일 진실 공급원
- StateFlow 사용
```

---

## 9. API 데이터 형식

### 🔐 인증 API

#### SMS 인증
```kotlin
// Request
POST /api/auth/sms/send
{
  "phoneNumber": "010-1234-5678"
}

// Response
{
  "success": true,
  "verificationId": "uuid",
  "expiresAt": 1234567890
}
```

#### 회원가입
```kotlin
// Request
POST /api/auth/company/register
{
  "tempToken": "sms_token",
  "businessNumber": "123-45-67890",
  "companyName": "(주)건설왕",
  "representativeName": "홍길동",
  "businessType": "종합건설업",
  "businessAddress": "서울시 강남구...",
  "loginId": "company123",
  "password": "encrypted_password",
  "email": "admin@company.com",
  "insuranceInfo": {
    "hasInsurance": true,
    "insuranceType": "산재보험",
    "insuranceNumber": "2024-12345"
  }
}
```

### 💼 프로젝트 API

#### 프로젝트 생성
```kotlin
// Request
POST /api/projects
{
  "title": "강남 오피스텔 신축",
  "location": {
    "roadAddress": "서울시 강남구 역삼동 123",
    "latitude": 37.123456,
    "longitude": 127.123456
  },
  "workPeriod": {
    "startDate": "2025-08-01",
    "endDate": "2025-08-31",
    "workDays": ["MON", "TUE", "WED", "THU", "FRI"],
    "workStartTime": "08:00",
    "workEndTime": "18:00"
  },
  "jobRequirements": [
    {
      "jobCategoryId": "carpenter",
      "requiredCount": 5,
      "skillLevel": "EXPERT",
      "preferredExperience": 10
    }
  ],
  "paymentInfo": {
    "wageType": "DAILY",
    "wage": 150000,
    "overtimePay": 30000,
    "weekendPay": 50000,
    "paymentMethod": "WEEKLY"
  },
  "additionalInfo": {
    "provideMeals": true,
    "provideTransport": true,
    "isUrgent": false
  }
}
```

### 🔍 스카우트 API

#### AI 인력 추천
```kotlin
// Request
GET /api/scout/workers?useAiRecommendation=true&projectId=123

// Response
{
  "success": true,
  "data": [
    {
      "id": "worker_001",
      "name": "김철수",
      "age": 45,
      "primaryJob": "목수",
      "experience": 15,
      "rating": 4.8,
      "attendanceRate": 98.5,
      "distance": 3.2,
      "aiMatchScore": 95.5,  // AI 매칭 점수
      "matchReasons": [
        "과거 유사 프로젝트 3회 참여",
        "출근율 최상위 5%",
        "재고용 의사 100%"
      ]
    }
  ]
}
```

### 💰 정산 API

#### 절감액 통계
```kotlin
// Request  
GET /api/payments/savings-stats?period=MONTHLY

// Response
{
  "success": true,
  "stats": {
    "totalProjects": 12,
    "totalWorkers": 180,
    "totalPayments": 15250000,
    "originalServiceFee": 1525000,  // 10%
    "currentServiceFee": 762500,    // 5%
    "totalSaved": 762500,           // 절감액
    "savingsRate": 50.0,
    "projectedYearlySavings": 18300000,
    "chartData": [
      {
        "date": "2025-08",
        "saved": 762500,
        "projects": 12
      }
    ]
  }
}
```

---

## 📌 핵심 차별화 기능 요약

### 1. 💚 수수료 50% 절감
- 기존 10% → 직직직 5%
- 실시간 절감액 표시
- 월간/연간 절감 리포트
- 프로젝트별 절감 내역

### 2. 🤖 AI 인력 매칭
- 과거 채용 패턴 학습
- 95점 이상 최적 매칭
- 프로젝트별 맞춤 추천
- 성공률 예측

### 3. ✅ 실시간 출근 관리
- QR/GPS 이중 인증
- 실시간 출근 현황
- 자동 근무 시간 계산
- 대리 출근 방지

### 4. 💸 일괄 정산 시스템
- 원클릭 다중 송금
- 2단계 보안 확인
- 자동 세금계산서
- 실패 건 자동 재시도

### 5. 📅 간소화된 프로젝트 생성 ⭐ **NEW**
- 4개 필수 정보만 입력 (이름, 착공일, 준공일, 장소)
- 대형 다이얼로그 UI (화면 95%)
- 날짜 선택기 통합
- 지도 API 연동 준비

### 6. 📊 날짜 기반 진행도 ⭐ **NEW**
- 인원 대신 날짜로 계산
- 착공일/준공일 기준
- 색상 단계별 표시 (파랑→노랑→초록)
- 자동 상태 업데이트

---

## 🛠️ 기술 스택

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Navigation**: Compose Destinations
- **State Management**: StateFlow + SharedFlow
- **DI**: Koin
- **Network**: Retrofit + OkHttp
- **Local Storage**: Room + DataStore
- **Map**: Kakao Map SDK
- **Image**: Coil
- **Animation**: Lottie

---

## 📱 지원 환경

- **최소 Android**: API 24 (Android 7.0)
- **권장 Android**: API 30+ (Android 11+)
- **화면 크기**: 5.0" ~ 7.0"
- **방향**: Portrait only
- **언어**: 한국어

---

## 🔒 보안 사항

- JWT 토큰 기반 인증
- 생체 인증 지원
- SSL/TLS 암호화
- 민감 정보 암호화 저장
- 2단계 인증 (송금 시)