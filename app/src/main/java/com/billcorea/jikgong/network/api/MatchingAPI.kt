package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.matching.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 매칭 관련 API 인터페이스
 */
interface MatchingAPI {

  @POST("matching/create")
  suspend fun createMatching(
    @Body request: MatchingRequest
  ): Response<BaseResponse<WorkerMatchingData>>

  @GET("matching/{id}")
  suspend fun getMatching(
    @Path("id") matchingId: String
  ): Response<BaseResponse<WorkerMatchingData>>

  @PUT("matching/{id}/status")
  suspend fun updateMatchingStatus(
    @Path("id") matchingId: String,
    @Body status: MatchingStatus,
    @Query("reason") reason: String?
  ): Response<BaseResponse<WorkerMatchingData>>

  @GET("matching/worker/{workerId}")
  suspend fun getWorkerMatchings(
    @Path("workerId") workerId: String
  ): Response<BaseResponse<List<WorkerMatchingData>>>

  @GET("matching/project/{projectId}")
  suspend fun getProjectMatchings(
    @Path("projectId") projectId: String
  ): Response<BaseResponse<List<WorkerMatchingData>>>

  @POST("matching/confirm")
  suspend fun confirmMatching(
    @Body request: MatchingConfirmRequest
  ): Response<DefaultResponse>

  @GET("matching/ai/suggestions")
  suspend fun getAISuggestions(
    @Query("workerId") workerId: String,
    @Query("limit") limit: Int = 10
  ): Response<BaseResponse<List<WorkerMatchingData>>>

  @POST("matching/calculate-score")
  suspend fun calculateMatchingScore(
    @Query("workerId") workerId: String,
    @Query("projectId") projectId: String
  ): Response<BaseResponse<MatchingScore>>
}