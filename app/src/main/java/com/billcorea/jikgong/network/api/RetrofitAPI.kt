package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.auth.*
import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.worker.*
import com.billcorea.jikgong.network.model.company.*
import com.billcorea.jikgong.network.model.project.*
import com.billcorea.jikgong.network.model.job.*
import com.billcorea.jikgong.network.model.location.*
import com.billcorea.jikgong.network.model.payment.*
import com.billcorea.jikgong.network.model.attendance.*
import com.billcorea.jikgong.network.model.matching.*  // MatchingConfirmRequest 포함
import com.billcorea.jikgong.network.model.review.*    // WorkerReviewData, CompanyReviewData 포함
import com.billcorea.jikgong.network.model.notification.*
import com.billcorea.jikgong.network.model.chat.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 메인 Retrofit API 인터페이스
 * 모든 API 엔드포인트 정의
 */
interface RetrofitAPI {

  companion object {
    lateinit var instance: RetrofitAPI
  }

  // ===== 인증 관련 =====
  @POST("auth/login")
  suspend fun login(
    @Body request: LoginRequest
  ): Response<BaseResponse<LoginResponse>>

  @POST("auth/logout")
  suspend fun logout(
    @Header("Authorization") token: String
  ): Response<DefaultResponse>

  @POST("auth/refresh")
  suspend fun refreshToken(
    @Body request: RefreshTokenRequest
  ): Response<BaseResponse<TokenData>>

  @POST("auth/validate-id")
  suspend fun validateLoginId(
    @Body request: LoginIdValidationRequest
  ): Response<LoginIdValidationResponse>

  @POST("auth/validate-phone")
  suspend fun validatePhone(
    @Body request: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  @POST("auth/send-sms")
  suspend fun sendSmsVerification(
    @Body request: PhoneValidationRequest
  ): Response<PhoneValidationResponse>

  @POST("auth/verify-sms")
  suspend fun verifySms(
    @Body request: SmsVerificationRequest
  ): Response<SmsVerificationResponse>

  // ===== 노동자 관련 =====
  @POST("workers/register")
  suspend fun registerWorker(
    @Body request: RegisterWorkerRequest
  ): Response<BaseResponse<RegisterWorkerResponse>>

  @GET("workers/{id}")
  suspend fun getWorker(
    @Path("id") workerId: String
  ): Response<BaseResponse<WorkerData>>

  @PUT("workers/{id}")
  suspend fun updateWorker(
    @Path("id") workerId: String,
    @Body worker: WorkerData
  ): Response<BaseResponse<WorkerData>>

  @DELETE("workers/{id}")
  suspend fun deleteWorker(
    @Path("id") workerId: String
  ): Response<DefaultResponse>

  @GET("workers/search")
  suspend fun searchWorkers(
    @Query("jobType") jobType: String?,
    @Query("location") location: String?,
    @Query("experienceLevel") experienceLevel: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerData>>

  // ===== 기업 관련 =====
  @POST("companies/register")
  suspend fun registerCompany(
    @Body company: CompanyData
  ): Response<BaseResponse<CompanyData>>

  @GET("companies/{id}")
  suspend fun getCompany(
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyData>>

  @PUT("companies/{id}")
  suspend fun updateCompany(
    @Path("id") companyId: String,
    @Body company: CompanyData
  ): Response<BaseResponse<CompanyData>>

  @GET("companies/{id}/stats")
  suspend fun getCompanyStats(
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyStats>>

  @GET("companies/{id}/projects")
  suspend fun getCompanyProjects(
    @Path("id") companyId: String,
    @Query("status") status: String?
  ): Response<BaseResponse<List<ProjectData>>>

  // ===== 프로젝트 관련 =====
  @GET("projects")
  suspend fun getProjects(
    @Query("status") status: String?,
    @Query("category") category: String?,
    @Query("companyId") companyId: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<ProjectData>>

  @GET("projects/{id}")
  suspend fun getProject(
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectData>>

  @POST("projects")
  suspend fun createProject(
    @Body project: ProjectData
  ): Response<BaseResponse<ProjectData>>

  @PUT("projects/{id}")
  suspend fun updateProject(
    @Path("id") projectId: String,
    @Body project: ProjectData
  ): Response<BaseResponse<ProjectData>>

  @DELETE("projects/{id}")
  suspend fun deleteProject(
    @Path("id") projectId: String
  ): Response<DefaultResponse>

  @POST("projects/{id}/apply")
  suspend fun applyToProject(
    @Path("id") projectId: String,
    @Body workerId: String
  ): Response<DefaultResponse>

  @GET("projects/{id}/applicants")
  suspend fun getProjectApplicants(
    @Path("id") projectId: String
  ): Response<BaseResponse<List<WorkerData>>>

  // ===== 일자리 관련 =====
  @POST("jobs/register")
  suspend fun registerJob(
    @Body job: JobRegistrationData
  ): Response<BaseResponse<JobRegistrationData>>

  @GET("jobs")
  suspend fun getJobs(
    @Query("type") type: String?,
    @Query("location") location: String?,
    @Query("wage") minWage: Int?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobRegistrationData>>

  @GET("jobs/{id}")
  suspend fun getJob(
    @Path("id") jobId: String
  ): Response<BaseResponse<JobRegistrationData>>

  // ===== 매칭 관련 =====
  @POST("matching/request")
  suspend fun requestMatching(
    @Body request: MatchingRequest
  ): Response<BaseResponse<MatchingResult>>

  @GET("matching/workers")
  suspend fun getMatchingWorkers(
    @Query("projectId") projectId: String,
    @Query("jobType") jobType: String?
  ): Response<BaseResponse<List<WorkerMatchingData>>>

  @POST("matching/confirm")
  suspend fun confirmMatching(
    @Body request: MatchingConfirmRequest
  ): Response<DefaultResponse>

  // ===== 결제 관련 =====
  @POST("payments/create")
  suspend fun createPayment(
    @Body payment: PaymentData
  ): Response<BaseResponse<PaymentData>>

  @GET("payments/{id}")
  suspend fun getPayment(
    @Path("id") paymentId: String
  ): Response<BaseResponse<PaymentData>>

  @GET("payments/company/{companyId}")
  suspend fun getCompanyPayments(
    @Path("companyId") companyId: String,
    @Query("status") status: String?
  ): Response<BaseResponse<List<PaymentData>>>

  // ===== 근태 관련 =====
  @POST("attendance/check-in")
  suspend fun checkIn(
    @Body request: CheckInRequest
  ): Response<BaseResponse<AttendanceData>>

  @POST("attendance/check-out")
  suspend fun checkOut(
    @Body request: CheckOutRequest
  ): Response<BaseResponse<AttendanceData>>

  @GET("attendance/{workerId}")
  suspend fun getAttendanceHistory(
    @Path("workerId") workerId: String,
    @Query("from") fromDate: String,
    @Query("to") toDate: String
  ): Response<BaseResponse<List<AttendanceData>>>

  // ===== 리뷰 관련 =====
  @POST("reviews/worker")
  suspend fun createWorkerReview(
    @Body review: WorkerReviewData
  ): Response<BaseResponse<WorkerReviewData>>

  @POST("reviews/company")
  suspend fun createCompanyReview(
    @Body review: CompanyReviewData
  ): Response<BaseResponse<CompanyReviewData>>

  @GET("reviews/worker/{workerId}")
  suspend fun getWorkerReviews(
    @Path("workerId") workerId: String
  ): Response<BaseResponse<List<WorkerReviewData>>>

  @GET("reviews/company/{companyId}")
  suspend fun getCompanyReviews(
    @Path("companyId") companyId: String
  ): Response<BaseResponse<List<CompanyReviewData>>>

  // ===== 알림 관련 =====
  @GET("notifications")
  suspend fun getNotifications(
    @Query("userId") userId: String,
    @Query("unreadOnly") unreadOnly: Boolean = false
  ): Response<BaseResponse<List<NotificationData>>>

  @PUT("notifications/{id}/read")
  suspend fun markNotificationAsRead(
    @Path("id") notificationId: String
  ): Response<DefaultResponse>

  @PUT("notifications/settings")
  suspend fun updateNotificationSettings(
    @Body settings: NotificationSettings
  ): Response<BaseResponse<NotificationSettings>>

  // ===== 채팅 관련 =====
  @GET("chat/rooms")
  suspend fun getChatRooms(
    @Query("userId") userId: String
  ): Response<BaseResponse<List<ChatRoomData>>>

  @GET("chat/messages/{roomId}")
  suspend fun getChatMessages(
    @Path("roomId") roomId: String,
    @Query("lastMessageId") lastMessageId: String?
  ): Response<BaseResponse<List<ChatMessageData>>>

  @POST("chat/send")
  suspend fun sendChatMessage(
    @Body message: ChatMessageData
  ): Response<BaseResponse<ChatMessageData>>

  // ===== 위치 관련 =====
  @GET("location/search")
  suspend fun searchAddress(
    @Query("keyword") keyword: String
  ): Response<AddressFindResponse>

  @GET("location/coord2address")
  suspend fun getAddressFromCoordinates(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double
  ): Response<Coord2AddressResponse>
}