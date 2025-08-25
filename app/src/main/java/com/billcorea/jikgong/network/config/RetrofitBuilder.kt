package com.billcorea.jikgong.network.config

import android.content.Context
import android.content.SharedPreferences
import com.billcorea.jikgong.network.adapter.LocalDateAdapter
import com.billcorea.jikgong.network.adapter.LocalDateTimeAdapter
import com.billcorea.jikgong.network.adapter.LocalTimeAdapter
import com.billcorea.jikgong.network.api.*
import com.billcorea.jikgong.network.interceptor.AuthInterceptor
import com.billcorea.jikgong.network.interceptor.ErrorInterceptor
import com.billcorea.jikgong.network.interceptor.HeaderInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Retrofit 인스턴스 빌더
 */
object RetrofitBuilder {

  private var retrofit: Retrofit? = null

  /**
   * Gson 인스턴스 생성
   */
  private fun createGson(): Gson {
    return GsonBuilder()
      .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
      .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
      .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
      .setLenient()
      .create()
  }

  /**
   * OkHttpClient 생성
   */
  private fun createOkHttpClient(
    context: Context,
    sharedPreferences: SharedPreferences
  ): OkHttpClient {
    return OkHttpClient.Builder().apply {
      // 타임아웃 설정
      connectTimeout(NetworkConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
      readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.SECONDS)
      writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)

      // 인터셉터 추가
      addInterceptor(HeaderInterceptor(context))
      addInterceptor(AuthInterceptor(sharedPreferences))
      addInterceptor(ErrorInterceptor())

      // 로깅 인터셉터 (디버그 모드에서만)
      if (com.billcorea.jikgong.BuildConfig.DEBUG) {
        addInterceptor(HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BODY
        })
      }

      // 재시도 설정
      retryOnConnectionFailure(true)
    }.build()
  }

  /**
   * Retrofit 인스턴스 생성
   */
  fun getInstance(
    context: Context,
    sharedPreferences: SharedPreferences
  ): Retrofit {
    if (retrofit == null) {
      retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .client(createOkHttpClient(context, sharedPreferences))
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .build()
    }
    return retrofit!!
  }

  /**
   * API 서비스 초기화
   */
  fun initializeApiServices(
    context: Context,
    sharedPreferences: SharedPreferences
  ): ApiServices {
    val retrofit = getInstance(context, sharedPreferences)

    return ApiServices(
      authAPI = retrofit.create(AuthAPI::class.java),
      workerAPI = retrofit.create(WorkerAPI::class.java),
      companyAPI = retrofit.create(CompanyAPI::class.java),
      projectAPI = retrofit.create(ProjectAPI::class.java),
      jobAPI = retrofit.create(JobAPI::class.java),
      matchingAPI = retrofit.create(MatchingAPI::class.java),
      paymentAPI = retrofit.create(PaymentAPI::class.java),
      attendanceAPI = retrofit.create(AttendanceAPI::class.java),
      retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    )
  }

  /**
   * Retrofit 인스턴스 재설정 (로그아웃 시 등)
   */
  fun reset() {
    retrofit = null
  }
}

/**
 * API 서비스 홀더
 */
data class ApiServices(
  val authAPI: AuthAPI,
  val workerAPI: WorkerAPI,
  val companyAPI: CompanyAPI,
  val projectAPI: ProjectAPI,
  val jobAPI: JobAPI,
  val matchingAPI: MatchingAPI,
  val paymentAPI: PaymentAPI,
  val attendanceAPI: AttendanceAPI,
  val retrofitAPI: RetrofitAPI
)