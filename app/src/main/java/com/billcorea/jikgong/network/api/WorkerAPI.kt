package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.worker.*
import com.billcorea.jikgong.network.model.review.WorkerReviewData
import com.billcorea.jikgong.network.model.project.ProjectData
import com.billcorea.jikgong.network.model.job.JobRegistrationData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 노동자 관련 API 인터페이스
 * - 노동자 등록/관리
 * - 노동자 프로필
 * - 노동자 스카우트
 * - 노동자 경력/평가
 *
 * 모든 데이터 모델은 WorkerModels.kt에 정의됨
 */
interface WorkerAPI {

  // ===== 노동자 등록/관리 =====

  @Multipart
  @POST("api/workers/register")
  suspend fun registerWorker(
    @Part("request") request: RequestBody,
    @Part profileImage: MultipartBody.Part?,
    @Part educationCertificate: MultipartBody.Part?,
    @Part workerCard: MultipartBody.Part?,
    @Part signature: MultipartBody.Part?
  ): Response<BaseResponse<RegisterWorkerResponse>>

  @GET("api/workers/{id}")
  suspend fun getWorker(
    @Path("id") workerId: String
  ): Response<BaseResponse<WorkerData>>

  @PUT("api/workers/{id}")
  suspend fun updateWorker(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body worker: WorkerData
  ): Response<BaseResponse<WorkerData>>

  @DELETE("api/workers/{id}")
  suspend fun deleteWorker(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("reason") reason: String?
  ): Response<DefaultResponse>

  @POST("api/workers/{id}/verify")
  suspend fun verifyWorker(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body verification: WorkerVerification
  ): Response<BaseResponse<WorkerData>>

  @PUT("api/workers/{id}/status")
  suspend fun updateWorkerStatus(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body status: WorkerStatusUpdate
  ): Response<BaseResponse<WorkerData>>

  // ===== 노동자 검색 =====

  @GET("api/workers/search")
  suspend fun searchWorkers(
    @Query("jobType") jobType: String?,
    @Query("location") location: String?,
    @Query("experienceLevel") experienceLevel: String?,
    @Query("skills") skills: List<String>?,
    @Query("minWage") minWage: Long?,
    @Query("maxWage") maxWage: Long?,
    @Query("available") available: Boolean?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
    @Query("sort") sort: String = "rating,desc"
  ): Response<PagedResponse<WorkerData>>

  @GET("api/workers/nearby")
  suspend fun getNearbyWorkers(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("radius") radius: Int = 5000,
    @Query("jobType") jobType: String?,
    @Query("available") available: Boolean = true
  ): Response<BaseResponse<List<WorkerData>>>

  @GET("api/workers/recommended")
  suspend fun getRecommendedWorkers(
    @Header("Authorization") token: String,
    @Query("projectId") projectId: String?,
    @Query("jobId") jobId: String?,
    @Query("limit") limit: Int = 10
  ): Response<BaseResponse<List<RecommendedWorker>>>

  // ===== 노동자 프로필 =====

  @GET("api/workers/{id}/profile")
  suspend fun getWorkerProfile(
    @Path("id") workerId: String
  ): Response<BaseResponse<WorkerProfile>>

  @PUT("api/workers/{id}/profile")
  suspend fun updateWorkerProfile(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body profile: WorkerProfile
  ): Response<BaseResponse<WorkerProfile>>

  @Multipart
  @POST("api/workers/{id}/profile/photo")
  suspend fun updateProfilePhoto(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Part photo: MultipartBody.Part
  ): Response<BaseResponse<PhotoUpdateResponse>>

  @GET("api/workers/{id}/public-profile")
  suspend fun getPublicProfile(
    @Path("id") workerId: String
  ): Response<BaseResponse<PublicWorkerProfile>>

  // ===== 스카우트 프로필 =====

  @POST("api/workers/{id}/scout-profile")
  suspend fun createScoutProfile(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body profile: ScoutProfile
  ): Response<BaseResponse<ScoutProfile>>

  @PUT("api/workers/{id}/scout-profile")
  suspend fun updateScoutProfile(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body profile: ScoutProfile
  ): Response<BaseResponse<ScoutProfile>>

  @GET("api/workers/scout-profiles")
  suspend fun getScoutProfiles(
    @QueryMap filters: Map<String, String>,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<ScoutProfileData>>

  @POST("api/workers/{id}/scout-profile/toggle")
  suspend fun toggleScoutProfile(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("isPublic") isPublic: Boolean
  ): Response<BaseResponse<ScoutProfile>>

  // ===== 노동자 경력 =====

  @GET("api/workers/{id}/experience")
  suspend fun getWorkerExperience(
    @Path("id") workerId: String
  ): Response<BaseResponse<List<WorkExperience>>>

  @POST("api/workers/{id}/experience")
  suspend fun addWorkExperience(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body experience: WorkExperience
  ): Response<BaseResponse<WorkExperience>>

  @PUT("api/workers/{id}/experience/{experienceId}")
  suspend fun updateWorkExperience(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Path("experienceId") experienceId: String,
    @Body experience: WorkExperience
  ): Response<BaseResponse<WorkExperience>>

  @DELETE("api/workers/{id}/experience/{experienceId}")
  suspend fun deleteWorkExperience(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Path("experienceId") experienceId: String
  ): Response<DefaultResponse>

  // ===== 노동자 자격증 =====

  @GET("api/workers/{id}/certificates")
  suspend fun getWorkerCertificates(
    @Path("id") workerId: String
  ): Response<BaseResponse<List<Certificate>>>

  @Multipart
  @POST("api/workers/{id}/certificates")
  suspend fun addCertificate(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Part("certificate") certificate: RequestBody,
    @Part image: MultipartBody.Part?
  ): Response<BaseResponse<Certificate>>

  @DELETE("api/workers/{id}/certificates/{certificateId}")
  suspend fun deleteCertificate(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Path("certificateId") certificateId: String
  ): Response<DefaultResponse>

  @POST("api/workers/{id}/certificates/verify")
  suspend fun verifyCertificate(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body verification: CertificateVerification
  ): Response<BaseResponse<Certificate>>

  // ===== 노동자 활동 =====

  @GET("api/workers/{id}/activity")
  suspend fun getWorkerActivity(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerActivity>>

  @GET("api/workers/{id}/projects")
  suspend fun getWorkerProjects(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<ProjectData>>

  @GET("api/workers/{id}/current-job")
  suspend fun getCurrentJob(
    @Header("Authorization") token: String,
    @Path("id") workerId: String
  ): Response<BaseResponse<JobRegistrationData?>>

  // ===== 노동자 평가/리뷰 =====

  @GET("api/workers/{id}/reviews")
  suspend fun getWorkerReviews(
    @Path("id") workerId: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerReviewData>>

  @POST("api/workers/{id}/reviews")
  suspend fun createWorkerReview(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body review: WorkerReviewRequest
  ): Response<BaseResponse<WorkerReviewData>>

  @GET("api/workers/{id}/rating")
  suspend fun getWorkerRating(
    @Path("id") workerId: String
  ): Response<BaseResponse<WorkerRating>>

  @GET("api/workers/{id}/performance")
  suspend fun getWorkerPerformance(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("period") period: String = "6months"
  ): Response<BaseResponse<WorkerPerformance>>

  // ===== 노동자 통계 =====

  @GET("api/workers/{id}/stats")
  suspend fun getWorkerStats(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("period") period: String = "monthly"
  ): Response<BaseResponse<WorkerStatistics>>

  @GET("api/workers/{id}/income")
  suspend fun getIncomeStats(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("year") year: Int,
    @Query("month") month: Int?
  ): Response<BaseResponse<IncomeStatistics>>

  @GET("api/workers/{id}/work-history")
  suspend fun getWorkHistory(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkHistory>>

  // ===== 노동자 설정 =====

  @GET("api/workers/{id}/preferences")
  suspend fun getWorkerPreferences(
    @Header("Authorization") token: String,
    @Path("id") workerId: String
  ): Response<BaseResponse<WorkerPreferences>>

  @PUT("api/workers/{id}/preferences")
  suspend fun updateWorkerPreferences(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body preferences: WorkerPreferences
  ): Response<BaseResponse<WorkerPreferences>>

  @GET("api/workers/{id}/notification-settings")
  suspend fun getNotificationSettings(
    @Header("Authorization") token: String,
    @Path("id") workerId: String
  ): Response<BaseResponse<NotificationSettings>>

  @PUT("api/workers/{id}/notification-settings")
  suspend fun updateNotificationSettings(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Body settings: NotificationSettings
  ): Response<BaseResponse<NotificationSettings>>

  // ===== 노동자 문서 =====

  @Multipart
  @POST("api/workers/{id}/documents")
  suspend fun uploadDocument(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Part document: MultipartBody.Part,
    @Part("type") type: RequestBody,
    @Part("description") description: RequestBody?
  ): Response<BaseResponse<WorkerDocument>>

  @GET("api/workers/{id}/documents")
  suspend fun getWorkerDocuments(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Query("type") type: String?
  ): Response<BaseResponse<List<WorkerDocument>>>

  @DELETE("api/workers/{id}/documents/{documentId}")
  suspend fun deleteDocument(
    @Header("Authorization") token: String,
    @Path("id") workerId: String,
    @Path("documentId") documentId: String
  ): Response<DefaultResponse>

  // ===== 노동자 블랙리스트 =====

  @GET("api/workers/{id}/blacklist-status")
  suspend fun getBlacklistStatus(
    @Header("Authorization") token: String,
    @Path("id") workerId: String
  ): Response<BaseResponse<BlacklistStatus>>

  @GET("api/workers/blacklist/check")
  suspend fun checkBlacklist(
    @Header("Authorization") token: String,
    @Query("workerIds") workerIds: List<String>
  ): Response<BaseResponse<List<BlacklistCheckResult>>>
}