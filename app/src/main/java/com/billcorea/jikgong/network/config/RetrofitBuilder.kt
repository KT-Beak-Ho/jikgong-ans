package com.billcorea.jikgong.network.config

import com.billcorea.jikgong.network.api.RetrofitAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetrofitBuilder {

  var baseURL = "https://www.jikgong.p-e.kr/"
  var kakaoURL = "https://dapi.kakao.com/"

  private val client = OkHttpClient.Builder().build()

  val gson: Gson = GsonBuilder().setLenient().create()

  val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object :
      Converter<ResponseBody, Any?> {
      val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
      override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
        try {
          nextResponseBodyConverter.convert(value)
        } catch (e: Exception) {
          e.printStackTrace()
          null
        }
      } else {
        null
      }
    }
  }

  fun create(): RetrofitAPI {
    return Retrofit.Builder()
      .baseUrl(baseURL)
      .addConverterFactory(nullOnEmptyConverterFactory)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(client)
      .build()
      .create(RetrofitAPI::class.java)
  }

  fun createKakao(): RetrofitAPI {
    return Retrofit.Builder()
      .baseUrl(kakaoURL)
      .addConverterFactory(nullOnEmptyConverterFactory)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(client)
      .build()
      .create(RetrofitAPI::class.java)
  }
}