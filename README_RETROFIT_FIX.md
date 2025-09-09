# Retrofit 오류 해결 가이드

## 문제점
- `java.lang.IllegalArgumentException: Expected URL scheme 'http' or 'https' but no scheme was found for null`
- 기업 로그인 시 앱 종료 현상

## 해결된 사항

### 1. local.properties 설정 추가
```properties
# API Configuration
BASE_URL=http://59.21.223.137/
KAKAO_REST_API=YOUR_KAKAO_REST_API_KEY_HERE
KAKAO_API=YOUR_KAKAO_API_KEY_HERE
```

### 2. NetworkModule.kt 안전장치 추가
- BuildConfig.BASE_URL이 null이거나 빈 값일 때 fallback URL 사용
- 예외 처리 및 로깅 추가

### 3. Koin DI 설정 확인
- NetworkModule, RepositoryModule, ViewModelModule 모두 올바르게 설정됨
- JikgongApplication에서 Koin 모듈 등록 확인

## 빌드 및 실행 순서

1. **Clean Build 실행**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. **실제 API 키 설정** (옵션)
   - 카카오 개발자센터에서 실제 API 키 획득
   - `local.properties`의 `YOUR_KAKAO_REST_API_KEY_HERE`, `YOUR_KAKAO_API_KEY_HERE`를 실제 키로 교체

3. **디버깅 로그 확인**
   - Logcat에서 "NetworkModule" 태그로 BASE_URL 설정 확인
   - Retrofit 인스턴스 생성 성공 여부 확인

## 주의사항
- `local.properties` 파일은 버전 관리에 포함되지 않으므로 각 개발 환경에서 설정 필요
- KAKAO API 키가 없어도 BASE_URL 설정으로 Retrofit 오류는 해결됨
- 실제 로그인 기능은 현재 주석 처리되어 있어 로컬에서만 동작

## 테스트 방법
1. 앱 실행
2. "기업회원" 선택
3. 로그인 화면에서 임의 ID/비밀번호 입력
4. 로그인 버튼 클릭
5. 프로젝트 리스트 화면으로 정상 이동하면 해결 완료