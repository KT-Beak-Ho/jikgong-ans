package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.auth.*
import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.worker.*
import com.billcorea.jikgong.network.model.company.*
import com.billcorea.jikgong.network.model.project.*
import com.billcorea.jikgong.network.model.job.*
import com.billcorea.jikgong.network.model.location.*
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

  @GET("workers/nearby")
  suspend fun getNearbyWorkers(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("radius") radius: Int = 5000
  ): Response<BaseResponse<List<WorkerData>>>

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

  @DELETE("companies/{id}")
  suspend fun deleteCompany(
    @Path("id") companyId: String
  ): Response<DefaultResponse>

  // ===== 프로젝트 관련 =====
  @GET("projects")
  suspend fun getProjects(
    @Query("status") status: String?,
    @Query("category") category: String?,
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

  // ===== 일자리 관련 =====
  @GET("jobs")
  suspend fun getJobs(
    @Query("jobType") jobType: String?,
    @Query("location") location: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobRegistrationData>>

  @GET("jobs/{id}")
  suspend fun getJob(
    @Path("id") jobId: String
  ): Response<BaseResponse<JobRegistrationData>>

  @POST("jobs")
  suspend fun createJob(
    @Body job: JobRegistrationData
  ): Response<BaseResponse<JobRegistrationData>>

  @POST("jobs/{id}/apply")
  suspend fun applyJob(
    @Path("id") jobId: String,
    @Body workerId: String
  ): Response<DefaultResponse>

  // ===== 위치 관련 =====
  @GET("location/address/search")
  suspend fun searchAddress(
    @Query("query") query: String
  ): Response<AddressFindResponse>

  @GET("location/coord-to-address")
  suspend fun coordToAddress(
    @Query("x") longitude: Double,
    @Query("y") latitude: Double
  ): Response<Coord2AddressResponse>
}