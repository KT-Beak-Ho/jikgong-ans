package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.company.*
import com.billcorea.jikgong.network.model.project.ProjectData
import com.billcorea.jikgong.network.model.worker.WorkerData
import com.billcorea.jikgong.network.model.job.JobRegistrationData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 기업 관련 API 인터페이스
 * - 기업 등록/관리
 * - 기업 정보 조회
 * - 기업 프로젝트/근로자 관리
 * - 기업 통계
 *
 * 모든 데이터 모델은 CompanyModels.kt에 정의됨
 */
interface CompanyAPI {

  // ===== 기업 등록/관리 =====

  @POST("api/companies/register")
  suspend fun registerCompany(
    @Body company: CompanyData
  ): Response<BaseResponse<CompanyData>>

  @GET("api/companies/{id}")
  suspend fun getCompany(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyData>>

  @PUT("api/companies/{id}")
  suspend fun updateCompany(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body company: CompanyData
  ): Response<BaseResponse<CompanyData>>

  @DELETE("api/companies/{id}")
  suspend fun deleteCompany(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("reason") reason: String?
  ): Response<DefaultResponse>

  @POST("api/companies/{id}/verify")
  suspend fun verifyCompany(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body verification: CompanyVerification
  ): Response<BaseResponse<CompanyData>>

  // ===== 기업 검색 =====

  @GET("api/companies/search")
  suspend fun searchCompanies(
    @Query("keyword") keyword: String?,
    @Query("region") region: String?,
    @Query("businessType") businessType: String?,
    @Query("verified") verified: Boolean?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<CompanyData>>

  @GET("api/companies/nearby")
  suspend fun getNearbyCompanies(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("radius") radius: Int = 5000,
    @Query("businessType") businessType: String?
  ): Response<BaseResponse<List<CompanyData>>>

  // ===== 기업 프로젝트 관리 =====

  @GET("api/companies/{id}/projects")
  suspend fun getCompanyProjects(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<ProjectData>>

  @POST("api/companies/{id}/projects")
  suspend fun createCompanyProject(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body project: ProjectData
  ): Response<BaseResponse<ProjectData>>

  @GET("api/companies/{id}/projects/active")
  suspend fun getActiveProjects(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<List<ProjectData>>>

  // ===== 기업 일자리 관리 =====

  @GET("api/companies/{id}/jobs")
  suspend fun getCompanyJobs(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobRegistrationData>>

  @POST("api/companies/{id}/jobs")
  suspend fun createCompanyJob(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body job: JobRegistrationData
  ): Response<BaseResponse<JobRegistrationData>>

  // ===== 기업 근로자 관리 =====

  @GET("api/companies/{id}/workers")
  suspend fun getCompanyWorkers(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerData>>

  @POST("api/companies/{id}/workers/saved")
  suspend fun saveWorker(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body workerId: String
  ): Response<DefaultResponse>

  @DELETE("api/companies/{id}/workers/saved/{workerId}")
  suspend fun unsaveWorker(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Path("workerId") workerId: String
  ): Response<DefaultResponse>

  @GET("api/companies/{id}/workers/saved")
  suspend fun getSavedWorkers(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerData>>

  @POST("api/companies/{id}/workers/blacklist")
  suspend fun blacklistWorker(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body request: BlacklistRequest
  ): Response<DefaultResponse>

  @GET("api/companies/{id}/workers/blacklist")
  suspend fun getBlacklistedWorkers(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<List<BlacklistedWorker>>>

  // ===== 기업 통계 =====

  @GET("api/companies/{id}/stats")
  suspend fun getCompanyStats(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("period") period: String = "monthly"
  ): Response<BaseResponse<CompanyStats>>

  @GET("api/companies/{id}/financial")
  suspend fun getFinancialStats(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("year") year: Int,
    @Query("month") month: Int?
  ): Response<BaseResponse<FinancialStats>>

  @GET("api/companies/{id}/performance")
  suspend fun getPerformanceMetrics(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("startDate") startDate: String,
    @Query("endDate") endDate: String
  ): Response<BaseResponse<PerformanceMetrics>>

  // ===== 기업 리뷰/평가 =====

  @GET("api/companies/{id}/reviews")
  suspend fun getCompanyReviews(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<CompanyReview>>

  @POST("api/companies/{id}/reviews")
  suspend fun createCompanyReview(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body review: CompanyReviewRequest
  ): Response<BaseResponse<CompanyReview>>

  @GET("api/companies/{id}/rating")
  suspend fun getCompanyRating(
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyRating>>

  // ===== 기업 문서/인증 =====

  @Multipart
  @POST("api/companies/{id}/documents")
  suspend fun uploadCompanyDocument(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Part document: MultipartBody.Part,
    @Part("type") type: RequestBody,
    @Part("description") description: RequestBody?
  ): Response<BaseResponse<CompanyDocument>>

  @GET("api/companies/{id}/documents")
  suspend fun getCompanyDocuments(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<List<CompanyDocument>>>

  @GET("api/companies/{id}/certifications")
  suspend fun getCompanyCertifications(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<List<CompanyCertification>>>

  // ===== 기업 알림 설정 =====

  @GET("api/companies/{id}/notifications/settings")
  suspend fun getNotificationSettings(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyNotificationSettings>>

  @PUT("api/companies/{id}/notifications/settings")
  suspend fun updateNotificationSettings(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body settings: CompanyNotificationSettings
  ): Response<BaseResponse<CompanyNotificationSettings>>

  // ===== 파트너 관리 =====

  @GET("api/companies/{id}/partners")
  suspend fun getPartnerCompanies(
    @Header("Authorization") token: String,
    @Path("id") companyId: String
  ): Response<BaseResponse<List<PartnerCompany>>>

  @POST("api/companies/{id}/partners")
  suspend fun addPartnerCompany(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Body partnerId: String
  ): Response<DefaultResponse>

  @DELETE("api/companies/{id}/partners/{partnerId}")
  suspend fun removePartnerCompany(
    @Header("Authorization") token: String,
    @Path("id") companyId: String,
    @Path("partnerId") partnerId: String
  ): Response<DefaultResponse>
}