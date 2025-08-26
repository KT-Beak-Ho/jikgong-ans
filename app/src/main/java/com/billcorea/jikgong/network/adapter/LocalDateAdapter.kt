package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * LocalDate 타입을 JSON으로 직렬화/역직렬화하는 어댑터
 *
 * 변환 예시:
 * - JSON → Kotlin: "2025-01-27" → LocalDate(2025, 1, 27)
 * - Kotlin → JSON: LocalDate(2025, 1, 27) → "2025-01-27"
 *
 * 지원 포맷:
 * - 기본: yyyy-MM-dd (ISO-8601)
 * - 대체: yyyy/MM/dd, yyyyMMdd
 */
class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  companion object {
    // 기본 포맷 (ISO-8601)
    private val DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE

    // 대체 포맷들 (서버가 다른 포맷을 사용할 경우)
    private val ALTERNATIVE_FORMATTERS = listOf(
      DateTimeFormatter.ofPattern("yyyy-MM-dd"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd"),
      DateTimeFormatter.ofPattern("yyyyMMdd"),
      DateTimeFormatter.ofPattern("dd-MM-yyyy"),
      DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )
  }

  /**
   * LocalDate를 JSON 문자열로 직렬화
   *
   * @param src 변환할 LocalDate 객체
   * @param typeOfSrc 소스 타입
   * @param context 직렬화 컨텍스트
   * @return JSON 문자열 요소
   */
  override fun serialize(
    src: LocalDate?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return if (src != null) {
      // LocalDate를 ISO-8601 포맷 문자열로 변환
      JsonPrimitive(src.format(DEFAULT_FORMATTER))
    } else {
      JsonNull.INSTANCE
    }
  }

  /**
   * JSON 문자열을 LocalDate로 역직렬화
   *
   * @param json JSON 요소
   * @param typeOfT 대상 타입
   * @param context 역직렬화 컨텍스트
   * @return LocalDate 객체
   * @throws JsonParseException 파싱 실패 시
   */
  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalDate? {
    if (json == null || json.isJsonNull) {
      return null
    }

    val jsonString = json.asString
    if (jsonString.isNullOrBlank()) {
      return null
    }

    // 먼저 기본 포맷으로 시도
    try {
      return LocalDate.parse(jsonString, DEFAULT_FORMATTER)
    } catch (e: DateTimeParseException) {
      // 기본 포맷 실패 시 대체 포맷들 시도
      for (formatter in ALTERNATIVE_FORMATTERS) {
        try {
          return LocalDate.parse(jsonString, formatter)
        } catch (ignored: DateTimeParseException) {
          // 다음 포맷 시도
        }
      }
    }

    // 모든 포맷 실패 시 예외 발생
    throw JsonParseException(
      "Unable to parse date: $jsonString. " +
        "Expected format: yyyy-MM-dd (ISO-8601)"
    )
  }

  /**
   * 날짜 문자열 검증
   *
   * @param dateString 검증할 날짜 문자열
   * @return 유효한 날짜면 true
   */
  fun isValidDate(dateString: String): Boolean {
    return try {
      LocalDate.parse(dateString, DEFAULT_FORMATTER)
      true
    } catch (e: DateTimeParseException) {
      ALTERNATIVE_FORMATTERS.any { formatter ->
        try {
          LocalDate.parse(dateString, formatter)
          true
        } catch (e: DateTimeParseException) {
          false
        }
      }
    }
  }

  /**
   * 날짜를 한국어 형식으로 포맷
   *
   * @param date LocalDate 객체
   * @return "2025년 1월 27일" 형식의 문자열
   */
  fun toKoreanFormat(date: LocalDate): String {
    return "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일"
  }
}