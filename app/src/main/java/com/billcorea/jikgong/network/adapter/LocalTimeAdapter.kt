package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * LocalTime Gson 어댑터
 */
class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

  private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

  override fun serialize(
    src: LocalTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(src?.format(formatter))
  }

  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalTime? {
    return json?.asString?.let {
      LocalTime.parse(it, formatter)
    }
  }
}