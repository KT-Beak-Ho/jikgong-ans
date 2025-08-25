package com.billcorea.jikgong.network.adapter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * LocalDate Gson 어댑터
 */
class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

  override fun serialize(
    src: LocalDate?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(src?.format(formatter))
  }

  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LocalDate? {
    return json?.asString?.let {
      LocalDate.parse(it, formatter)
    }
  }
}