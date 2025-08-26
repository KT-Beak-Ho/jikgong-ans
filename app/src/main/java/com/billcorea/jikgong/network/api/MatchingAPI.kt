package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.matching.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 매칭 관련 API 인터페이스
 * - AI 매칭
 * - 수동 매칭
 * - 매칭 점수 계산
 * - 매칭 이력 관리
 *
 * 모든 데이터 모델은 MatchingModels.kt에 정의됨
 */
interface MatchingAPI {

  // ===== 매칭 생성/관리 =====

  @POST("api/matching/create")
  suspend fun createMatching(
    @Header("Authorization") token: String,
    @Body request: MatchingRequest
  ): Response<BaseResponse<WorkerMatchingData>>

  @GET("api/matching/{id}")
  suspend fun getMatching(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String
  ): Response<BaseResponse<WorkerMatchingData>>

  @PUT("api/matching/{id}")
  suspend fun updateMatching(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String,
    @Body matching: WorkerMatchingData
  ): Response<BaseResponse<WorkerMatchingData>>

  @DELETE("api/matching/{id}")
  suspend fun deleteMatching(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String,
    @Query("reason") reason: String?
  ): Response<DefaultResponse>

  @PUT("api/matching/{id}/status")
  suspend fun updateMatchingStatus(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String,
    @Body statusUpdate: MatchingStatusUpdate
  ): Response<BaseResponse<WorkerMatchingData>>

  // ===== 매칭 조회 =====

  @GET("api/matching/worker/{workerId}")
  suspend fun getWorkerMatchings(
    @Header("Authorization") token: String,
    @Path("workerId") workerId: String,
    @Query("status") status: String?,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerMatchingData>>

  @GET("api/matching/project/{projectId}")
  suspend fun getProjectMatchings(
    @Header("Authorization") token: String,
    @Path("projectId") projectId: String,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerMatchingData>>

  @GET("api/matching/company/{companyId}")
  suspend fun getCompanyMatchings(
    @Header("Authorization") token: String,
    @Path("companyId") companyId: String,
    @Query("status") status: String?,
    @Query("period") period: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<WorkerMatchingData>>

  // ===== 매칭 확정/거절 =====

  @POST("api/matching/{id}/confirm")
  suspend fun confirmMatching(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String,
    @Body confirmation: MatchingConfirmRequest
  ): Response<BaseResponse<WorkerMatchingData>>

  @POST("api/matching/{id}/reject")
  suspend fun rejectMatching(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String,
    @Body rejection: MatchingRejectRequest
  ): Response<BaseResponse<WorkerMatchingData>>

  @POST("api/matching/batch-confirm")
  suspend fun batchConfirmMatchings(
    @Header("Authorization") token: String,
    @Body request: BatchMatchingConfirmRequest
  ): Response<BaseResponse<BatchMatchingResult>>

  // ===== AI 매칭 =====

  @GET("api/matching/ai/suggestions")
  suspend fun getAISuggestions(
    @Header("Authorization") token: String,
    @Query("workerId") workerId: String?,
    @Query("projectId") projectId: String?,
    @Query("jobId") jobId: String?,
    @Query("limit") limit: Int = 10
  ): Response<BaseResponse<List<AISuggestion>>>

  @POST("api/matching/ai/auto-match")
  suspend fun autoMatch(
    @Header("Authorization") token: String,
    @Body request: AutoMatchRequest
  ): Response<BaseResponse<AutoMatchResult>>

  @GET("api/matching/ai/analysis/{matchingId}")
  suspend fun getMatchAnalysis(
    @Header("Authorization") token: String,
    @Path("matchingId") matchingId: String
  ): Response<BaseResponse<MatchAnalysis>>

  @POST("api/matching/ai/improve")
  suspend fun improveMatchingAlgorithm(
    @Header("Authorization") token: String,
    @Body feedback: MatchingFeedback
  ): Response<DefaultResponse>

  // ===== 매칭 점수 =====

  @POST("api/matching/calculate-score")
  suspend fun calculateMatchingScore(
    @Header("Authorization") token: String,
    @Body request: ScoreCalculationRequest
  ): Response<BaseResponse<MatchingScore>>

  @GET("api/matching/score-factors")
  suspend fun getScoreFactors(
    @Header("Authorization") token: String,
    @Query("workerId") workerId: String,
    @Query("projectId") projectId: String
  ): Response<BaseResponse<ScoreFactors>>

  @PUT("api/matching/score-weights")
  suspend fun updateScoreWeights(
    @Header("Authorization") token: String,
    @Body weights: ScoreWeights
  ): Response<BaseResponse<ScoreWeights>>

  // ===== 매칭 통계 =====

  @GET("api/matching/stats/overview")
  suspend fun getMatchingStats(
    @Header("Authorization") token: String,
    @Query("companyId") companyId: String?,
    @Query("period") period: String = "monthly"
  ): Response<BaseResponse<MatchingStatistics>>

  @GET("api/matching/stats/success-rate")
  suspend fun getSuccessRate(
    @Header("Authorization") token: String,
    @Query("entityType") entityType: String,
    @Query("entityId") entityId: String
  ): Response<BaseResponse<SuccessRateData>>

  @GET("api/matching/stats/trends")
  suspend fun getMatchingTrends(
    @Header("Authorization") token: String,
    @Query("startDate") startDate: String,
    @Query("endDate") endDate: String
  ): Response<BaseResponse<MatchingTrends>>

  // ===== 매칭 필터/조건 =====

  @POST("api/matching/filters")
  suspend fun saveMatchingFilter(
    @Header("Authorization") token: String,
    @Body filter: MatchingFilter
  ): Response<BaseResponse<MatchingFilter>>

  @GET("api/matching/filters")
  suspend fun getMatchingFilters(
    @Header("Authorization") token: String,
    @Query("userId") userId: String
  ): Response<BaseResponse<List<MatchingFilter>>>

  @DELETE("api/matching/filters/{filterId}")
  suspend fun deleteMatchingFilter(
    @Header("Authorization") token: String,
    @Path("filterId") filterId: String
  ): Response<DefaultResponse>

  // ===== 매칭 히스토리 =====

  @GET("api/matching/history")
  suspend fun getMatchingHistory(
    @Header("Authorization") token: String,
    @Query("userId") userId: String,
    @Query("userType") userType: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<MatchingHistory>>

  @GET("api/matching/repeated-matches")
  suspend fun getRepeatedMatches(
    @Header("Authorization") token: String,
    @Query("workerId") workerId: String,
    @Query("companyId") companyId: String
  ): Response<BaseResponse<List<RepeatedMatch>>>

  // ===== 매칭 알림 =====

  @POST("api/matching/{id}/notify")
  suspend fun sendMatchingNotification(
    @Header("Authorization") token: String,
    @Path("id") matchingId: String,
    @Body notification: MatchingNotification
  ): Response<DefaultResponse>

  @GET("api/matching/notifications/settings")
  suspend fun getNotificationSettings(
    @Header("Authorization") token: String,
    @Query("userId") userId: String
  ): Response<BaseResponse<MatchingNotificationSettings>>

  @PUT("api/matching/notifications/settings")
  suspend fun updateNotificationSettings(
    @Header("Authorization") token: String,
    @Body settings: MatchingNotificationSettings
  ): Response<BaseResponse<MatchingNotificationSettings>>
}