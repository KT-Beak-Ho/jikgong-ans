package com.billcorea.jikgong.network.config

import com.billcorea.jikgong.BuildConfig
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
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Retrofit 빌더 및 API 인스턴스 생성 관리
 */
object RetrofitBuilder {

  // Base URLs
  private const val BASE_URL = "https://www.jikgong.p-e.kr/"
  private const val KAKAO_URL = "https://dapi.kakao.com/"

  // Timeout 설정
  private const val CONNECT_TIMEOUT = 30L
  private const val READ_TIMEOUT = 30L
  private const val WRITE_TIMEOUT = 30L

  /**
   * Gson 인스턴스 생성 with Date/Time Adapters
   */
  val gson: Gson = GsonBuilder()
    .setLenient()
    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
    .setPrettyPrinting()
    .serializeNulls()
    .create()

  /**
   * OkHttpClient 생성
   */
  private fun createOkHttpClient(
    authToken: String? = null,
    isDebug: Boolean = BuildConfig.DEBUG
  ): OkHttpClient {
    return OkHttpClient.Builder().apply {
      // Timeout 설정
      connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
      readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
      writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

      // Interceptor 추가
      addInterceptor(HeaderInterceptor())

      // Auth Token이 있는 경우 String으로 전달
      authToken?.let {
        addInterceptor(AuthInterceptor(it))
      }

      // Error Interceptor
      addInterceptor(ErrorInterceptor())

      // Logging (디버그 모드에서만)
      if (isDebug) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BODY
        }
        addInterceptor(loggingInterceptor)
      }

      // 재시도 설정
      retryOnConnectionFailure(true)
    }.build()
  }

  /**
   * 빈 응답 처리를 위한 Converter Factory
   */
  private val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(
      type: Type,
      annotations: Array<out Annotation>,
      retrofit: Retrofit
    ) = object : Converter<ResponseBody, Any?> {
      val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(
        converterFactory(),
        type,
        annotations
      )

      override fun convert(value: ResponseBody) =
        if (value.contentLength() != 0L) {
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

  /**
   * Retrofit 인스턴스 생성
   */
  private fun createRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(nullOnEmptyConverterFactory)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
  }

  // ===== API 인스턴스 생성 메서드 =====

  /**
   * 기본 API 생성
   */
  fun <T> createApi(
    apiClass: Class<T>,
    authToken: String? = null
  ): T {
    val client = createOkHttpClient(authToken)
    val retrofit = createRetrofit(BASE_URL, client)
    return retrofit.create(apiClass)
  }

  /**
   * Kakao API 생성
   */
  fun <T> createKakaoApi(apiClass: Class<T>): T {
    val client = createOkHttpClient()
    val retrofit = createRetrofit(KAKAO_URL, client)
    return retrofit.create(apiClass)
  }

  // ===== 각 API별 인스턴스 생성 편의 메서드 =====

  fun createRetrofitAPI(authToken: String? = null): RetrofitAPI =
    createApi(RetrofitAPI::class.java, authToken)

  fun createKakaoRetrofitAPI(): RetrofitAPI =
    createKakaoApi(RetrofitAPI::class.java)

  fun createAuthAPI(): AuthAPI =
    createApi(AuthAPI::class.java)

  fun createAttendanceAPI(authToken: String): AttendanceAPI =
    createApi(AttendanceAPI::class.java, authToken)

  fun createCompanyAPI(authToken: String? = null): CompanyAPI =
    createApi(CompanyAPI::class.java, authToken)

  fun createJobAPI(authToken: String? = null): JobAPI =
    createApi(JobAPI::class.java, authToken)

  fun createMatchingAPI(authToken: String): MatchingAPI =
    createApi(MatchingAPI::class.java, authToken)

  fun createPaymentAPI(authToken: String): PaymentAPI =
    createApi(PaymentAPI::class.java, authToken)

  fun createProjectAPI(authToken: String): ProjectAPI =
    createApi(ProjectAPI::class.java, authToken)

  fun createWorkerAPI(authToken: String? = null): WorkerAPI =
    createApi(WorkerAPI::class.java, authToken)

  // ===== 레거시 지원 (점진적 마이그레이션) =====

  @Deprecated("Use createRetrofitAPI() instead", ReplaceWith("createRetrofitAPI()"))
  fun create(): RetrofitAPI = createRetrofitAPI()

  @Deprecated("Use createKakaoRetrofitAPI() instead", ReplaceWith("createKakaoRetrofitAPI()"))
  fun createKakao(): RetrofitAPI = createKakaoRetrofitAPI()
}