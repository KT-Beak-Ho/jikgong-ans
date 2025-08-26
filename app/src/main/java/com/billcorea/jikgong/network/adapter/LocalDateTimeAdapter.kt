package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * LocalDateTime 타입을 JSON으로 직렬화/역직렬화하는 어댑터
 *
 * 변환 예시:
 * - JSON → Kotlin: "2025-01-27T14:30:00" → LocalDateTime
 * - Kotlin → JSON: LocalDateTime → "2025-01-27T14:30:00"
 *
 * 지원 포맷:
 * - 기본: yyyy-MM-dd'T'HH:mm:ss (ISO-8601)
 * - 시간대 포함: yyyy-MM-dd'T'HH:mm:ss'Z'
 * - 밀리초 포함: yyyy-MM-dd'T'HH:mm:ss.SSS
 */
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  companion object {
    // 기본 포맷 (ISO-8601)
    private val DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // 대체 포맷들
    private val ALTERNATIVE_FORMATTERS = listOf(
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
      DateTimeFormatter.ISO_DATE_TIME,
      DateTimeFormatter.ISO_INSTANT
    )

    // 한국 시간대
    private val KOREA_ZONE_ID = ZoneId.of("Asia/Seoul")

    // 표시용 포맷터
    private val DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
    private val SIMPLE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd HH:mm")
  }

  /**
   * LocalDateTime을 JSON 문자열로 직렬화
   *
   * @param src 변환할 LocalDateTime 객체
   * @param typeOfSrc 소스 타입
   * @param context 직렬화 컨텍스트
   * @return JSON 문자열 요소
   */
  override fun serialize(
    src: LocalDateTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return if (src != null) {
      // LocalDateTime을 ISO-8601 포맷 문자열로 변환
      JsonPrimitive(src.format(DEFAULT_FORMATTER))
    } else {
      JsonNull.INSTANCE
    }
  }

  /**
   * JSON 문자열을 LocalDateTime으로 역직렬화
   *
   * @param json JSON 요소
   * @param typeOfT 대상 타입
   * @param context 역직렬화 컨텍스트
   * @return LocalDateTime 객체
   * @throws JsonParseException 파싱 실패 시
   */
  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalDateTime? {
    if (json == null || json.isJsonNull) {
      return null
    }

    val jsonString = json.asString
    if (jsonString.isNullOrBlank()) {
      return null
    }

    // Unix timestamp 처리 (숫자로만 구성된 경우)
    if (jsonString.all { it.isDigit() }) {
      return try {
        val timestamp = jsonString.toLong()
        // 밀리초 단위 타임스탬프를 LocalDateTime으로 변환
        LocalDateTime.ofInstant(
          java.time.Instant.ofEpochMilli(timestamp),
          KOREA_ZONE_ID
        )
      } catch (e: Exception) {
        null
      }
    }

    // Z(UTC) 시간대 표시가 있는 경우 처리
    if (jsonString.endsWith("Z")) {
      return try {
        val instant = java.time.Instant.parse(jsonString)
        LocalDateTime.ofInstant(instant, KOREA_ZONE_ID)
      } catch (e: Exception) {
        null
      }
    }

    // 먼저 기본 포맷으로 시도
    try {
      return LocalDateTime.parse(jsonString, DEFAULT_FORMATTER)
    } catch (e: DateTimeParseException) {
      // 기본 포맷 실패 시 대체 포맷들 시도
      for (formatter in ALTERNATIVE_FORMATTERS) {
        try {
          return LocalDateTime.parse(jsonString, formatter)
        } catch (ignored: DateTimeParseException) {
          // 다음 포맷 시도
        }
      }
    }

    // 모든 포맷 실패 시 예외 발생
    throw JsonParseException(
      "Unable to parse datetime: $jsonString. " +
        "Expected format: yyyy-MM-dd'T'HH:mm:ss (ISO-8601)"
    )
  }

  /**
   * LocalDateTime을 한국 시간대로 변환
   *
   * @param dateTime UTC LocalDateTime
   * @return 한국 시간대 LocalDateTime
   */
  fun toKoreanTime(dateTime: LocalDateTime): LocalDateTime {
    return dateTime.atZone(ZoneId.of("UTC"))
      .withZoneSameInstant(KOREA_ZONE_ID)
      .toLocalDateTime()
  }

  /**
   * 현재 한국 시간 가져오기
   *
   * @return 현재 한국 시간
   */
  fun nowInKorea(): LocalDateTime {
    return LocalDateTime.now(KOREA_ZONE_ID)
  }

  /**
   * 날짜시간을 사용자 친화적 형식으로 변환
   *
   * @param dateTime LocalDateTime 객체
   * @param simple 간단한 형식 여부
   * @return 포맷된 문자열
   */
  fun toDisplayFormat(dateTime: LocalDateTime, simple: Boolean = false): String {
    return if (simple) {
      dateTime.format(SIMPLE_FORMATTER)  // "01/27 14:30"
    } else {
      dateTime.format(DISPLAY_FORMATTER)  // "2025년 01월 27일 14시 30분"
    }
  }

  /**
   * 상대 시간 표시 (예: "3분 전", "2시간 전")
   *
   * @param dateTime 비교할 시간
   * @return 상대 시간 문자열
   */
  fun toRelativeTime(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now(KOREA_ZONE_ID)
    val duration = java.time.Duration.between(dateTime, now)

    return when {
      duration.toDays() > 0 -> "${duration.toDays()}일 전"
      duration.toHours() > 0 -> "${duration.toHours()}시간 전"
      duration.toMinutes() > 0 -> "${duration.toMinutes()}분 전"
      duration.seconds > 0 -> "${duration.seconds}초 전"
      else -> "방금 전"
    }
  }

  /**
   * 날짜시간 검증
   *
   * @param dateTimeString 검증할 날짜시간 문자열
   * @return 유효한 날짜시간이면 true
   */
  fun isValidDateTime(dateTimeString: String): Boolean {
    // 기본 포맷 시도
    try {
      LocalDateTime.parse(dateTimeString, DEFAULT_FORMATTER)
      return true
    } catch (e: DateTimeParseException) {
      // 대체 포맷들 시도
      return ALTERNATIVE_FORMATTERS.any { formatter ->
        try {
          LocalDateTime.parse(dateTimeString, formatter)
          true
        } catch (e: DateTimeParseException) {
          false
        }
      }
    }
  }
}