package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * LocalTime 타입을 JSON으로 직렬화/역직렬화하는 어댑터
 *
 * 변환 예시:
 * - JSON → Kotlin: "14:30:00" → LocalTime(14, 30, 0)
 * - Kotlin → JSON: LocalTime(14, 30, 0) → "14:30:00"
 *
 * 지원 포맷:
 * - 기본: HH:mm:ss
 * - 간단: HH:mm
 * - 12시간: hh:mm a
 */
class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

  companion object {
    // 기본 포맷 (24시간 형식)
    private val DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME

    // 대체 포맷들
    private val ALTERNATIVE_FORMATTERS = listOf(
      DateTimeFormatter.ofPattern("HH:mm:ss"),
      DateTimeFormatter.ofPattern("HH:mm"),
      DateTimeFormatter.ofPattern("HHmm"),
      DateTimeFormatter.ofPattern("H:mm"),
      DateTimeFormatter.ofPattern("hh:mm a"),    // 12시간 형식 (오전/오후)
      DateTimeFormatter.ofPattern("hh:mm:ss a")
    )

    // 표시용 포맷터
    private val DISPLAY_FORMATTER_24 = DateTimeFormatter.ofPattern("HH:mm")
    private val DISPLAY_FORMATTER_12 = DateTimeFormatter.ofPattern("a hh:mm")
    private val KOREAN_FORMATTER = DateTimeFormatter.ofPattern("a h시 m분")
  }

  /**
   * LocalTime을 JSON 문자열로 직렬화
   *
   * @param src 변환할 LocalTime 객체
   * @param typeOfSrc 소스 타입
   * @param context 직렬화 컨텍스트
   * @return JSON 문자열 요소
   */
  override fun serialize(
    src: LocalTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return if (src != null) {
      // LocalTime을 HH:mm:ss 포맷 문자열로 변환
      JsonPrimitive(src.format(DEFAULT_FORMATTER))
    } else {
      JsonNull.INSTANCE
    }
  }

  /**
   * JSON 문자열을 LocalTime으로 역직렬화
   *
   * @param json JSON 요소
   * @param typeOfT 대상 타입
   * @param context 역직렬화 컨텍스트
   * @return LocalTime 객체
   * @throws JsonParseException 파싱 실패 시
   */
  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalTime? {
    if (json == null || json.isJsonNull) {
      return null
    }

    val jsonString = json.asString
    if (jsonString.isNullOrBlank()) {
      return null
    }

    // 특수 케이스 처리
    val normalizedString = normalizeTimeString(jsonString)

    // 먼저 기본 포맷으로 시도
    try {
      return LocalTime.parse(normalizedString, DEFAULT_FORMATTER)
    } catch (e: DateTimeParseException) {
      // 기본 포맷 실패 시 대체 포맷들 시도
      for (formatter in ALTERNATIVE_FORMATTERS) {
        try {
          return LocalTime.parse(normalizedString, formatter)
        } catch (ignored: DateTimeParseException) {
          // 다음 포맷 시도
        }
      }
    }

    // 모든 포맷 실패 시 예외 발생
    throw JsonParseException(
      "Unable to parse time: $jsonString. " +
        "Expected format: HH:mm:ss or HH:mm"
    )
  }

  /**
   * 시간 문자열 정규화
   *
   * @param timeString 원본 시간 문자열
   * @return 정규화된 시간 문자열
   */
  private fun normalizeTimeString(timeString: String): String {
    var normalized = timeString.trim()

    // 한국어 오전/오후 처리
    normalized = normalized
      .replace("오전", "AM")
      .replace("오후", "PM")
      .replace("시", ":")
      .replace("분", "")
      .replace("초", "")

    // 초가 없는 경우 추가
    if (normalized.matches(Regex("^\\d{1,2}:\\d{2}$"))) {
      normalized += ":00"
    }

    return normalized
  }

  /**
   * 시간을 12시간 형식으로 변환
   *
   * @param time LocalTime 객체
   * @return "오후 2:30" 형식의 문자열
   */
  fun to12HourFormat(time: LocalTime): String {
    val hour = time.hour
    val minute = time.minute
    val period = if (hour < 12) "오전" else "오후"
    val displayHour = when (hour) {
      0 -> 12
      in 1..12 -> hour
      else -> hour - 12
    }
    return String.format("%s %d:%02d", period, displayHour, minute)
  }

  /**
   * 시간을 한국어 형식으로 변환
   *
   * @param time LocalTime 객체
   * @return "오후 2시 30분" 형식의 문자열
   */
  fun toKoreanFormat(time: LocalTime): String {
    val hour = time.hour
    val minute = time.minute
    val period = if (hour < 12) "오전" else "오후"
    val displayHour = when (hour) {
      0 -> 12
      in 1..12 -> hour
      else -> hour - 12
    }

    return if (minute == 0) {
      "$period ${displayHour}시"
    } else {
      "$period ${displayHour}시 ${minute}분"
    }
  }

  /**
   * 작업 시간 계산
   *
   * @param startTime 시작 시간
   * @param endTime 종료 시간
   * @return 작업 시간 (시간 단위)
   */
  fun calculateWorkHours(startTime: LocalTime, endTime: LocalTime): Double {
    val duration = java.time.Duration.between(startTime, endTime)
    return duration.toMinutes() / 60.0
  }

  /**
   * 휴게 시간을 고려한 실제 작업 시간 계산
   *
   * @param startTime 시작 시간
   * @param endTime 종료 시간
   * @param breakMinutes 휴게 시간 (분)
   * @return 실제 작업 시간 (시간 단위)
   */
  fun calculateActualWorkHours(
    startTime: LocalTime,
    endTime: LocalTime,
    breakMinutes: Int = 60
  ): Double {
    val totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes()
    val actualMinutes = totalMinutes - breakMinutes
    return actualMinutes / 60.0
  }

  /**
   * 시간 검증
   *
   * @param timeString 검증할 시간 문자열
   * @return 유효한 시간이면 true
   */
  fun isValidTime(timeString: String): Boolean {
    val normalized = normalizeTimeString(timeString)

    // 기본 포맷 시도
    try {
      LocalTime.parse(normalized, DEFAULT_FORMATTER)
      return true
    } catch (e: DateTimeParseException) {
      // 대체 포맷들 시도
      return ALTERNATIVE_FORMATTERS.any { formatter ->
        try {
          LocalTime.parse(normalized, formatter)
          true
        } catch (e: DateTimeParseException) {
          false
        }
      }
    }
  }

  /**
   * 근무 시간대 확인
   *
   * @param time 확인할 시간
   * @return 시간대 (오전/오후/야간/심야)
   */
  fun getWorkPeriod(time: LocalTime): String {
    return when (time.hour) {
      in 6..11 -> "오전"
      in 12..17 -> "오후"
      in 18..21 -> "야간"
      else -> "심야"
    }
  }
}