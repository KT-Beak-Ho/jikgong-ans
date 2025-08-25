package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * LocalTime Gson 어댑터
 */
class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

  private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
  private val shortFormatter = DateTimeFormatter.ofPattern("HH:mm")

  override fun serialize(
    src: LocalTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return if (src == null) {
      JsonNull.INSTANCE
    } else {
      JsonPrimitive(formatter.format(src))
    }
  }

  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalTime? {
    return if (json == null || json.isJsonNull) {
      null
    } else {
      val timeString = json.asString
      try {
        LocalTime.parse(timeString, formatter)
      } catch (e: Exception) {
        try {
          LocalTime.parse(timeString, shortFormatter)
        } catch (e2: Exception) {
          try {
            LocalTime.parse(timeString)
          } catch (e3: Exception) {
            null
          }
        }
      }
    }
  }
}