package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * LocalDateTime Gson 어댑터
 */
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

  override fun serialize(
    src: LocalDateTime?,
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
  ): LocalDateTime? {
    return if (json == null || json.isJsonNull) {
      null
    } else {
      try {
        LocalDateTime.parse(json.asString, formatter)
      } catch (e: Exception) {
        try {
          LocalDateTime.parse(json.asString)
        } catch (e2: Exception) {
          null
        }
      }
    }
  }
}