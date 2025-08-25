package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * LocalDateTime Gson 어댑터
 */
class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  override fun serialize(
    src: LocalDateTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(src?.format(formatter))
  }

  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalDateTime? {
    return json?.asString?.let {
      LocalDateTime.parse(it, formatter)
    }
  }
}