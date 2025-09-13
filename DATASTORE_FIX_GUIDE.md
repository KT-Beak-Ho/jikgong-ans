# DataStore Unresolved Reference 오류 해결 가이드

## 문제 상황
- 빌드는 성공하지만 IDE에서 unresolved reference 오류 표시
- 실행에는 문제가 없음

## 원인
Android Studio의 인덱싱 문제로, 실제 코드는 정상이지만 IDE가 제대로 인식하지 못하는 상황

## 해결 방법

### 1. Android Studio에서 캐시 초기화
1. **File → Invalidate Caches...**
2. **Invalidate and Restart** 선택
3. Android Studio 재시작 후 인덱싱 완료 대기

### 2. Gradle Sync
1. **File → Sync Project with Gradle Files**
2. 또는 상단 툴바의 코끼리 아이콘 클릭

### 3. Clean & Rebuild
```bash
./gradlew clean
./gradlew build
```

### 4. IDE 캐시 수동 삭제 (위 방법이 안 될 경우)
```bash
# Windows
rm -rf .gradle
rm -rf .idea/caches
rm -rf .idea/libraries
rm -rf %USERPROFILE%/.gradle/caches

# 이후 Android Studio 재시작
```

## API 연결과의 관계
**API 연결과는 무관합니다.** 이 오류는:
- DataStore 자체의 문제가 아님
- API 연결 여부와 관계없음
- 순수하게 IDE 인덱싱 문제

## 확인 사항
- `BUILD SUCCESSFUL` 메시지가 나오면 코드는 정상
- 앱 실행 시 정상 작동하면 문제없음
- IDE 오류 표시는 무시 가능

## 주의사항
만약 계속 문제가 발생한다면:
1. Android Studio 업데이트 확인
2. Kotlin 플러그인 업데이트 확인
3. Gradle 버전 호환성 확인