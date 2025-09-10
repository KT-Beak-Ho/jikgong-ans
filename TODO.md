# 📋 직직직 사업자(Company) 앱 통합 TODO 리스트 v3.0

> **최종 업데이트**: 2025-01-15  
> **총 TODO 항목**: 35개 (완료: 7개)  
> **예상 총 소요시간**: 61시간 (약 8-10일)  
> **완료된 작업**: 중복 파일 정리, 메모리 누수 해결, MVVM 패턴 적용, Money 화면 네비게이션

---

## 🚨 긴급 - 기술적 이슈 (즉시 해결 필요)

### 1. ✅ 중복 파일 정리 (완료)
- **문제**: `ProjectCreateDialog.kt` 파일이 2개 존재했음
  - `/projectlist/projectcreate/screen/ProjectCreateDialog.kt` (새 버전 - 4필드) ✅ 유지
  - `/projectlist/feature/create/screen/ProjectCreateDialog.kt` (구 버전) ❌ 삭제 완료
- **해결**: 구 버전 제거 완료, import 충돌 없음 확인
- **완료 시간**: 2025-01-15

### 2. ✅ 메모리 누수 위험 해결 (완료)

#### 2-1. ✅ MapLocationDialog.kt 정리 (완료)
- **위치**: `MapLocationDialog.kt`
- **문제**: LaunchedEffect 7개 사용, cleanup 미확인
- **해결**: DisposableEffect로 변경, onDispose 추가
- **완료**: MapLocationDialogOptimized.kt 생성, MapLocationViewModel 추가
- **완료 시간**: 2025-01-15

#### 2-2. ✅ CompanyScoutScreen.kt 최적화 (완료)
- **위치**: `CompanyScoutScreen.kt`
- **문제**: LaunchedEffect 17개로 과도한 Side Effect
- **해결**: MapLocationViewModel로 로직 이동, DisposableEffect 적용
- **완료 시간**: 2025-01-15

#### 2-3. ✅ JobCreationScreen.kt 상태 관리 개선 (완료)
- **위치**: `JobCreationScreen.kt`
- **문제**: remember 상태 38개로 복잡한 상태 관리
- **해결**: ViewModel 생성 및 상태 분리
- **완료**: JobCreationViewModel, 데이터 클래스, 섹션별 컴포넌트 생성
- **완료 시간**: 2025-01-15

---

## 🔴 우선순위 1 - 네비게이션 (화면 이동)

### 3. ✅ Money 화면 네비게이션 (완료)
- **파일**: `CompanyMoneyScreen.kt`
- **필요 기능**:
  - 검색 기능 구현 ✅
  - 프로젝트 생성 화면으로 이동 ✅
- **완료**: CompanyMoneyScreenEnhanced.kt 생성, 실시간 검색, FAB 버튼 추가
- **완료 시간**: 2025-01-15

### 4. ✅ Scout 화면 네비게이션 (완료)
- **파일**: `CompanyScoutViewModel.kt`
- **필요 기능**: 근로자 상세 화면 네비게이션
- **완료**: 네비게이션 이벤트 추가, 전화 걸기 기능 구현
- **완료 시간**: 2025-01-15

### 5. ✅ Info 화면 네비게이션 (완료)
- **파일**: `CompanyInfoScreen.kt`
- **필요 기능**: 
  - 프로필 수정 화면 이동
  - 알림 설정 화면 이동
  - 약관 및 정책 화면 이동
  - 로그아웃 처리
- **완료**: CompanyInfoScreenEnhanced.kt 생성, 모든 네비게이션 메서드 구현
- **완료 시간**: 2025-01-15

---

## 🟢 우선순위 2 - UI 기능 구현

### 13. 지도 다이얼로그
- **파일**: `ProjectCreateDialog.kt`
- **라인**: 134
- **현재 상태**: 아이콘 버튼만 있고 클릭 이벤트 없음
- **필요 작업**: LocationPickerDialog 컴포넌트 생성 및 연결
- **예상 시간**: 4시간

### 14. 폼 검증 로직
- **파일**: `JobCreationScreen.kt`
- **라인**: 229
- **현재 상태**: 임시 검증 로직만 존재
- **필요 작업**: 모든 필수 필드에 대한 완전한 검증 구현
- **예상 시간**: 2시간

### 15. 근무 시작시간 설정
- **파일**: `JobCreationScreen.kt`
- **라인**: 602, 605
- **현재 상태**: 오전 8시로 하드코딩
- **필요 작업**: State 변수 추가 및 TimePicker Dialog 연동
- **예상 시간**: 2시간

### 16. 근무 종료시간 설정
- **파일**: `JobCreationScreen.kt`
- **라인**: 603, 608
- **현재 상태**: 오후 6시로 하드코딩
- **필요 작업**: State 변수 추가 및 TimePicker Dialog 연동
- **예상 시간**: 2시간

### 17. 사진 추가 기능
- **파일**: `JobCreationScreen.kt`
- **라인**: 641
- **현재 상태**: UI만 있고 기능 없음
- **필요 작업**: 갤러리/카메라 선택 다이얼로그 및 이미지 업로드
- **예상 시간**: 4시간

### 18. 픽업장소 추가 필드
- **파일**: `JobCreationScreen.kt`
- **라인**: 683
- **현재 상태**: 주석으로만 표시
- **필요 작업**: 픽업 제공 시 표시될 추가 입력 필드 구현
- **예상 시간**: 2시간

### 19. 픽업장소 선택 기능
- **파일**: `JobCreationScreen.kt`
- **라인**: 686
- **현재 상태**: 버튼만 있고 기능 없음
- **필요 작업**: 지도에서 픽업 위치 선택 기능
- **예상 시간**: 3시간

### 20. 전화 걸기 기능 (고객센터)
- **파일**: `CustomerServiceScreen.kt`
- **라인**: 89
- **현재 상태**: 클릭 이벤트 없음
- **필요 작업**: Intent.ACTION_DIAL로 전화 앱 실행
- **예상 시간**: 30분

### 21. 문의하기 네비게이션
- **파일**: `CustomerServiceScreen.kt`
- **라인**: 201
- **현재 상태**: 클릭 이벤트 없음
- **필요 작업**: 문의하기 화면으로 네비게이션
- **예상 시간**: 1시간

### 22. 전화 걸기 기능 (제안 카드)
- **파일**: `ProposalCard.kt`
- **라인**: 233
- **현재 상태**: 클릭 영역만 있고 기능 없음
- **필요 작업**: 근로자에게 전화 연결
- **예상 시간**: 30분

---

## 🔵 우선순위 3 - Mock 데이터 제거  

### 23. Scout Mock 데이터 제거
- **대상**: `CompanyMockDataFactory.getScoutWorkers()`
- **필요 작업**: 실제 API 연동으로 교체
- **예상 시간**: 3시간

### 24. Money Mock 데이터 제거
- **대상**: `getSamplePayments()`
- **필요 작업**: 실제 정산 API 연동
- **예상 시간**: 3시간

### 25. Info 하드코딩 제거
- **대상**: 프로필, 통계 하드코딩 데이터
- **필요 작업**: 서버에서 실제 데이터 조회
- **예상 시간**: 2시간

---

## 🟡 우선순위 4 - API 연동

### 6. 회원가입 ID 중복 확인
- **파일**: `CompanyJoinSharedViewModel.kt`
- **라인**: 213
- **현재 상태**: Mock 데이터로 항상 "사용 가능" 반환
- **필요 작업**: 실제 서버 API `/api/company/check-id` 연동
- **예상 시간**: 2시간

### 7. 회원가입 Email 중복 확인
- **파일**: `CompanyJoinSharedViewModel.kt`
- **라인**: 301
- **현재 상태**: Mock 데이터로 항상 "사용 가능" 반환
- **필요 작업**: 실제 서버 API `/api/company/check-email` 연동
- **예상 시간**: 2시간

### 8. 회원가입 검증 로직
- **파일**: `CompanyJoinPage2Screen.kt`
- **라인**: 127
- **현재 상태**: 클라이언트 측 검증만 존재
- **필요 작업**: 서버 측 ID/Email 검증 결과를 UI에 반영
- **예상 시간**: 3시간

### 9. 회사 정보 연동
- **파일**: `ProjectCreateViewModel.kt`
- **라인**: 129
- **현재 상태**: `company = "대림건설(주)"` 하드코딩
- **필요 작업**: DataStore/SharedPreferences에서 실제 회사 정보 조회
- **예상 시간**: 1시간

### 10. 지원자 수락/거절 API
- **파일**: `WorkerManagementScreen.kt`
- **라인**: 616
- **현재 상태**: 주석 처리된 TODO
- **필요 작업**: 지원자 수락/거절 API 호출 및 상태 업데이트
- **예상 시간**: 3시간

### 11. 정산 변경 요청 API
- **파일**: `PaymentSummaryScreen.kt`
- **라인**: 134
- **현재 상태**: 버튼만 있고 기능 없음
- **필요 작업**: 변경 요청 다이얼로그 및 API 연동
- **예상 시간**: 3시간

### 12. 입금 처리 API
- **파일**: `PaymentSummaryScreen.kt`
- **라인**: 146
- **현재 상태**: 버튼만 있고 기능 없음
- **필요 작업**: 실제 송금 API 연동 및 결과 처리
- **예상 시간**: 4시간

---

## 📊 구현 로드맵

### 🚀 Sprint 1 (Week 1): 기술적 이슈 해결
**목표**: 안정성 확보  
**소요시간**: 10시간
- [x] 중복 파일 정리 (1h) ✅
- [x] MapLocationDialog 메모리 누수 해결 (2h) ✅
- [x] CompanyScoutScreen Side Effect 정리 (3h) ✅
- [x] JobCreationScreen ViewModel 분리 (4h) ✅

### 🚀 Sprint 2 (Week 2): 네비게이션 & 핵심 API
**목표**: 기본 동작 구현  
**소요시간**: 20시간
- [x] 모든 네비게이션 구현 (5h) ✅ (Money, Scout, Info 완료)
- [ ] 회원가입 API 연동 (7h)
- [ ] 회사 정보 연동 (1h)
- [ ] 지원자 관리 API (3h)
- [ ] 정산 API (4h)

### 🚀 Sprint 3 (Week 3): UI 기능 완성
**목표**: 사용자 경험 개선  
**소요시간**: 18시간
- [ ] 지도 다이얼로그 (4h)
- [ ] 시간 선택 기능 (4h)
- [ ] 사진/픽업 기능 (6h)
- [ ] 폼 검증 (2h)
- [ ] 전화 기능 (1h)
- [ ] 문의 네비게이션 (1h)

### 🚀 Sprint 4 (Week 4): 마무리
**목표**: 완성도 향상  
**소요시간**: 13시간
- [ ] Mock 데이터 제거 (8h)
- [ ] 에러 처리 강화 (3h)
- [ ] 성능 최적화 (2h)

---

## ⚠️ 위험 요소 및 대응 방안

### 🔴 HIGH: 메모리 누수
- **위험**: 앱 크래시, 성능 저하
- **대응**: LaunchedEffect cleanup, DisposableEffect 사용

### 🟡 MEDIUM: 상태 관리 복잡도
- **위험**: 버그 증가, 유지보수 어려움
- **대응**: ViewModel 분리, State Hoisting

### 🟢 LOW: 중복 코드
- **위험**: 혼란, 잘못된 수정
- **대응**: 즉시 제거, 단일 파일 유지

---

## ✅ 완료 체크리스트

### 기능 테스트
- [ ] 회원가입 전체 플로우
- [ ] 프로젝트 CRUD 동작
- [ ] 인력 스카우트 프로세스
- [ ] 정산 및 송금 기능
- [ ] 모든 네비게이션 경로

### 성능 테스트
- [ ] 메모리 사용량 (Memory Profiler)
- [ ] API 응답 시간 (< 2초)
- [ ] UI 렌더링 (60fps 유지)

### 사용성 테스트
- [ ] 에러 메시지 적절성
- [ ] 로딩 상태 표시
- [ ] 빈 상태 처리
- [ ] 오프라인 처리

---

## 📌 참고사항

### 개발 원칙
1. **Mock 우선순위**: Scout → Money → Info 순으로 실제 API 전환
2. **UI 우선순위**: 지도 → 시간 → 사진 → 전화
3. **테스트**: 각 Sprint 완료 시 통합 테스트 필수
4. **문서화**: 구현 완료 시 COMPANY_API_FORMAT.md 업데이트

### 기술 스택
- **Architecture**: MVVM + Clean Architecture
- **UI**: Jetpack Compose
- **Navigation**: Compose Destinations
- **Network**: Retrofit + OkHttp
- **Local Storage**: DataStore (SharedPreferences 대체)

---

**문서 버전**: v3.0 (통합본)  
**최종 업데이트**: 2025-01-15  
**작성자**: 직직직 개발팀  
**상태**: 🚧 진행중