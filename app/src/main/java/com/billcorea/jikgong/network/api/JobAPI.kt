package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.job.*
import com.billcorea.jikgong.network.model.worker.WorkerData
import com.billcorea.jikgong.network.model.matching.AutoMatchCriteria  // matching 모델에서 import
import retrofit2.Response
import retrofit2.http.*

/**
 * 일자리 관련 API 인터페이스
 * - 일자리 등록/관리
 * - 일자리 검색
 * - 일자리 지원
 * - 일자리 매칭
 */
interface JobAPI {

  // ===== 일자리 관리 =====

  @GET("api/jobs")
  suspend fun getJobs(
    @Query("jobType") jobType: String?,
    @Query("location") location: String?,
    @Query("wageType") wageType: String?,
    @Query("minWage") minWage: Long?,
    @Query("maxWage") maxWage: Long?,
    @Query("urgent") urgent: Boolean?,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
    @Query("sort") sort: String = "createdAt,desc"
  ): Response<PagedResponse<JobRegistrationData>>

  @GET("api/jobs/{id}")
  suspend fun getJob(
    @Path("id") jobId: String
  ): Response<BaseResponse<JobRegistrationData>>

  @POST("api/jobs")
  suspend fun createJob(
    @Header("Authorization") token: String,
    @Body job: JobRegistrationData
  ): Response<BaseResponse<JobRegistrationData>>

  @PUT("api/jobs/{id}")
  suspend fun updateJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body job: JobRegistrationData
  ): Response<BaseResponse<JobRegistrationData>>

  @DELETE("api/jobs/{id}")
  suspend fun deleteJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Query("reason") reason: String?
  ): Response<DefaultResponse>

  @PUT("api/jobs/{id}/status")
  suspend fun updateJobStatus(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body status: JobStatusUpdate
  ): Response<BaseResponse<JobRegistrationData>>

  // ===== 일자리 검색 =====

  @GET("api/jobs/search")
  suspend fun searchJobs(
    @Query("keyword") keyword: String,
    @QueryMap filters: Map<String, String>,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobRegistrationData>>

  @GET("api/jobs/nearby")
  suspend fun getNearbyJobs(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("radius") radius: Int = 5000,
    @Query("jobType") jobType: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobRegistrationData>>

  @GET("api/jobs/recommended")
  suspend fun getRecommendedJobs(
    @Header("Authorization") token: String,
    @Query("workerId") workerId: String,
    @Query("limit") limit: Int = 10
  ): Response<BaseResponse<List<JobRegistrationData>>>

  @GET("api/jobs/similar/{id}")
  suspend fun getSimilarJobs(
    @Path("id") jobId: String,
    @Query("limit") limit: Int = 5
  ): Response<BaseResponse<List<JobRegistrationData>>>

  // ===== 일자리 지원 =====

  @POST("api/jobs/{id}/apply")
  suspend fun applyToJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body application: JobApplicationRequest
  ): Response<BaseResponse<JobApplication>>

  @DELETE("api/jobs/{id}/apply")
  suspend fun cancelApplication(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Query("applicationId") applicationId: String,
    @Query("reason") reason: String?
  ): Response<DefaultResponse>

  @GET("api/jobs/{id}/applicants")
  suspend fun getJobApplicants(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobApplication>>

  @PUT("api/jobs/applications/{applicationId}/status")
  suspend fun updateApplicationStatus(
    @Header("Authorization") token: String,
    @Path("applicationId") applicationId: String,
    @Body status: ApplicationStatusUpdate
  ): Response<BaseResponse<JobApplication>>

  // ===== 일자리 지원 관리 (근로자) =====

  @GET("api/jobs/my-applications")
  suspend fun getMyApplications(
    @Header("Authorization") token: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobApplication>>

  @GET("api/jobs/applications/{applicationId}")
  suspend fun getApplication(
    @Header("Authorization") token: String,
    @Path("applicationId") applicationId: String
  ): Response<BaseResponse<JobApplication>>

  // ===== 일자리 북마크 =====

  @POST("api/jobs/{id}/bookmark")
  suspend fun bookmarkJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String
  ): Response<DefaultResponse>

  @DELETE("api/jobs/{id}/bookmark")
  suspend fun unbookmarkJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String
  ): Response<DefaultResponse>

  @GET("api/jobs/bookmarks")
  suspend fun getBookmarkedJobs(
    @Header("Authorization") token: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobRegistrationData>>

  // ===== 일자리 통계 =====

  @GET("api/jobs/{id}/stats")
  suspend fun getJobStats(
    @Path("id") jobId: String
  ): Response<BaseResponse<JobStats>>

  @GET("api/jobs/stats/market")
  suspend fun getMarketStats(
    @Query("region") region: String?,
    @Query("jobType") jobType: String?,
    @Query("period") period: String = "weekly"
  ): Response<BaseResponse<JobMarketStats>>

  // ===== 일자리 매칭 =====

  @POST("api/jobs/{id}/match")
  suspend fun matchWorkerToJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body workerId: String
  ): Response<BaseResponse<JobMatchResult>>

  @GET("api/jobs/{id}/match-candidates")
  suspend fun getMatchCandidates(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Query("limit") limit: Int = 20
  ): Response<BaseResponse<List<MatchCandidate>>>

  @POST("api/jobs/{id}/auto-match")
  suspend fun autoMatchJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body criteria: AutoMatchCriteria  // MatchingModels에서 import
  ): Response<BaseResponse<List<JobMatchResult>>>

  // ===== 일자리 알림 =====

  @POST("api/jobs/{id}/notify")
  suspend fun sendJobNotification(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body notification: JobNotificationRequest
  ): Response<DefaultResponse>

  @GET("api/jobs/alerts")
  suspend fun getJobAlerts(
    @Header("Authorization") token: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<JobAlert>>

  @POST("api/jobs/alerts")
  suspend fun createJobAlert(
    @Header("Authorization") token: String,
    @Body alert: JobAlertRequest
  ): Response<BaseResponse<JobAlert>>

  @DELETE("api/jobs/alerts/{alertId}")
  suspend fun deleteJobAlert(
    @Header("Authorization") token: String,
    @Path("alertId") alertId: String
  ): Response<DefaultResponse>

  // ===== 일자리 평가 =====

  @POST("api/jobs/{id}/complete")
  suspend fun completeJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body completion: JobCompletionRequest
  ): Response<BaseResponse<JobCompletion>>

  @POST("api/jobs/{id}/evaluate")
  suspend fun evaluateJob(
    @Header("Authorization") token: String,
    @Path("id") jobId: String,
    @Body evaluation: JobEvaluationRequest
  ): Response<BaseResponse<JobEvaluation>>
}