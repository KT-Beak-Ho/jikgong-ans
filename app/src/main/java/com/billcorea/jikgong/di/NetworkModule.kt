package com.billcorea.jikgong.di

import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.api.service.JoinApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
  single {
    OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      //  api call 로깅
      //  요청/응답 헤더 + 바디 전체 로깅
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      })
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