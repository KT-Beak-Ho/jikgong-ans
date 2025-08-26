package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.network.model.auth.*
import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.location.*
import com.billcorea.jikgong.network.model.worker.*
import com.billcorea.jikgong.network.model.company.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 레거시 API 인터페이스 (통합)
 * - 기존 동기 호출을 지원하면서 점진적으로 suspend 함수로 마이그레이션
 * - Kakao 지도 API 포함
 * - 파일 업로드 및 기타 유틸리티 API 포함
 */
interface RetrofitAPI {

  // ============================================
  // 인증 관련 (동기 -> 비동기 마이그레이션)
  // ============================================

  @POST("api/login")
  suspend fun login(
    @Body body: LoginRequest
  ): Response<LoginResponse>

  @POST("api/join/sms-verification")
  suspend fun smsVerification(
    @Body body: SmsVerificationRequest
  ): Response<SmsVerificationResponse>

  @POST("api/member-info/visaExpiryDate")
  suspend fun visaExpiryDate(
    @Body body: VisaExpiryDateRequest
  ): Response<DefaultResponse>

  @POST("api/join/validation-phone")
  suspend fun phoneValidation(
    @Body body: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  @POST("api/join/validation-loginId")
  suspend fun loginIdValidation(
    @Body body: LoginIdValidationRequest
  ): Response<LoginIdValidationResponse>

  @POST("api/join/validation-email")
  suspend fun emailValidation(
    @Body body: EmailValidationRequest
  ): Response<EmailValidationResponse>

  // ============================================
  // Kakao 지도 API
  // ============================================

  @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API}")
  @GET("v2/local/search/address.JSON")
  suspend fun kakaoGeocoding(
    @Query("query") query: String,
    @Query("page") page: Int = 1,
    @Query("size") size: Int = 10
  ): Response<AddressFindResponse>

  @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API}")
  @GET("v2/local/geo/coord2address.JSON")
  suspend fun findAddress(
    @Query("y") lat: Double,
    @Query("x") lon: Double
  ): Response<Coord2AddressResponse>

  @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API}")
  @GET("v2/local/geo/transcoord.JSON")
  suspend fun convertCoordinate(
    @Query("x") x: Double,
    @Query("y") y: Double,
    @Query("input_coord") inputCoord: String,
    @Query("output_coord") outputCoord: String
  ): Response<CoordinateConversionResponse>

  @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API}")
  @GET("v2/local/search/keyword.JSON")
  suspend fun searchKeyword(
    @Query("query") query: String,
    @Query("category_group_code") categoryGroupCode: String? = null,
    @Query("x") x: Double? = null,
    @Query("y") y: Double? = null,
    @Query("radius") radius: Int? = null,
    @Query("page") page: Int = 1,
    @Query("size") size: Int = 15
  ): Response<KeywordSearchResponse>

  // ============================================
  // 작업자 등록 (Multipart)
  // ============================================

  @Multipart
  @POST("api/join/worker/join")
  suspend fun registerWorker(
    @Part("request") request: RequestBody,
    @Part educationCertificateImage: MultipartBody.Part?,
    @Part workerCardImage: MultipartBody.Part?,
    @Part signatureImage: MultipartBody.Part?
  ): Response<RegisterWorkerResponse>

  @Multipart
  @POST("api/join/company/join")
  suspend fun registerCompany(
    @Part("request") request: RequestBody,
    @Part businessLicenseImage: MultipartBody.Part?,
    @Part constructionLicenseImage: MultipartBody.Part?,
    @Part companySealImage: MultipartBody.Part?
  ): Response<RegisterCompanyResponse>

  // ============================================
  // 파일 업로드
  // ============================================

  @Multipart
  @POST("api/files/upload")
  suspend fun uploadFile(
    @Header("Authorization") token: String,
    @Part file: MultipartBody.Part,
    @Part("type") type: RequestBody
  ): Response<BaseResponse<FileUploadResponse>>

  @POST("api/files/upload-multiple")
  suspend fun uploadMultipleFiles(
    @Header("Authorization") token: String,
    @Body files: List<MultipartBody.Part>
  ): Response<BaseResponse<List<FileUploadResponse>>>

  @GET("api/files/{fileId}")
  suspend fun getFile(
    @Header("Authorization") token: String,
    @Path("fileId") fileId: String
  ): Response<BaseResponse<FileData>>

  @DELETE("api/files/{fileId}")
  suspend fun deleteFile(
    @Header("Authorization") token: String,
    @Path("fileId") fileId: String
  ): Response<DefaultResponse>

  // ============================================
  // 기타 유틸리티 API
  // ============================================

  @GET("api/config/app-version")
  suspend fun getAppVersion(): Response<BaseResponse<AppVersionInfo>>

  @GET("api/config/terms")
  suspend fun getTermsAndConditions(
    @Query("type") type: String
  ): Response<BaseResponse<TermsData>>

  @GET("api/config/notices")
  suspend fun getNotices(
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<NoticeData>>

  @GET("api/config/banners")
  suspend fun getBanners(
    @Query("position") position: String? = null
  ): Response<BaseResponse<List<BannerData>>>

  @GET("api/config/faqs")
  suspend fun getFAQs(
    @Query("category") category: String? = null,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<FAQData>>

  // ============================================
  // 푸시 알림
  // ============================================

  @POST("api/notifications/register-token")
  suspend fun registerPushToken(
    @Header("Authorization") token: String,
    @Body request: PushTokenRequest
  ): Response<DefaultResponse>

  @DELETE("api/notifications/unregister-token")
  suspend fun unregisterPushToken(
    @Header("Authorization") token: String,
    @Body request: PushTokenRequest
  ): Response<DefaultResponse>

  @GET("api/notifications")
  suspend fun getNotifications(
    @Header("Authorization") token: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<NotificationData>>

  @PUT("api/notifications/{id}/read")
  suspend fun markNotificationAsRead(
    @Header("Authorization") token: String,
    @Path("id") notificationId: String
  ): Response<DefaultResponse>

  @PUT("api/notifications/read-all")
  suspend fun markAllNotificationsAsRead(
    @Header("Authorization") token: String
  ): Response<DefaultResponse>

  // ============================================
  // 지역 코드
  // ============================================

  @GET("api/regions")
  suspend fun getRegions(
    @Query("level") level: Int? = null,
    @Query("parentCode") parentCode: String? = null
  ): Response<BaseResponse<List<RegionApiData>>>

  @GET("api/regions/search")
  suspend fun searchRegion(
    @Query("keyword") keyword: String
  ): Response<BaseResponse<List<RegionApiData>>>

  // ============================================
  // 통계 API
  // ============================================

  @GET("api/stats/dashboard")
  suspend fun getDashboardStats(
    @Header("Authorization") token: String,
    @Query("userType") userType: String,
    @Query("period") period: String = "monthly"
  ): Response<BaseResponse<DashboardStats>>

  @GET("api/stats/market")
  suspend fun getMarketStats(
    @Query("region") region: String? = null,
    @Query("jobType") jobType: String? = null
  ): Response<BaseResponse<MarketStats>>
}