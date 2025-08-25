package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * LocalDate Gson 어댑터
 */
class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  override fun serialize(
    src: LocalDate?,
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
  ): LocalDate? {
    return if (json == null || json.isJsonNull) {
      null
    } else {
      try {
        LocalDate.parse(json.asString, formatter)
      } catch (e: Exception) {
        try {
          LocalDate.parse(json.asString)
        } catch (e2: Exception) {
          null
        }
      }
    }
  }
}