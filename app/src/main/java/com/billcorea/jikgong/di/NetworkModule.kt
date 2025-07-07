package com.billcorea.jikgong.di

import android.util.Log
import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.api.service.JoinApi
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
  single {
    OkHttpClient.Builder()
      .connectTimeout(10, TimeUnit.SECONDS)     // 30초 → 10초로 단축
      .readTimeout(15, TimeUnit.SECONDS)        // 30초 → 15초로 단축
      .writeTimeout(15, TimeUnit.SECONDS)       // 30초 → 15초로 단축
      .addInterceptor { chain ->
        try {
          val response = chain.proceed(chain.request())
          response
        } catch (e: Exception) {
          Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(408) // Request Timeout
            .message("Network Timeout")
            .body(ResponseBody.create(null, "Network timeout occurred"))
            .build()
        }
      }
      .addInterceptor(HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
        level = HttpLoggingInterceptor.Level.BASIC
      })
      // 재시도 설정 추가
      .retryOnConnectionFailure(true)
      .build()
  }

  single {
    Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .client(get())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  single<JoinApi> {
    get<Retrofit>().create(JoinApi::class.java)
  }
}