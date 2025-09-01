package com.billcorea.jikgong.modules

import android.util.Log
import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.api.service.AuthApi
import com.billcorea.jikgong.api.service.JoinApi
// import com.billcorea.jikgong.api.service.ProjectApi
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
  
  // BuildConfig 확인을 위한 로깅
  factory {
    try {
      Log.d("NetworkModule", "BASE_URL: ${BuildConfig.BASE_URL}")
      Log.d("NetworkModule", "KAKAO_REST_API available: ${::BuildConfig.name}")
    } catch (e: Exception) {
      Log.e("NetworkModule", "BuildConfig access error", e)
    }
  }
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
    val baseUrl = try {
      if (BuildConfig.BASE_URL.isNullOrBlank()) {
        "https://www.jikgong.p-e.kr/"
      } else {
        BuildConfig.BASE_URL
      }
    } catch (e: Exception) {
      Log.e("NetworkModule", "BuildConfig.BASE_URL not available, using fallback", e)
      "https://www.jikgong.p-e.kr/"
    }
    
    Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(get())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  // API Services
  single<JoinApi> {
    get<Retrofit>().create(JoinApi::class.java)
  }
  single<AuthApi> {
    get<Retrofit>().create(AuthApi::class.java)
  }

  // 프로젝트 관련 API (향후 구현 시 주석 해제)
  // single<ProjectApi> {
  //   get<Retrofit>().create(ProjectApi::class.java)
  // }
}