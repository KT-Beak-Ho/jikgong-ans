package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.job.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 일자리 관련 API 인터페이스
 */
interface JobAPI {

  @GET("jobs")
  suspend fun getJobs(
    @Query("jobType") jobType: String?,
    @Query("location") location: String?,
    @Query("urgent") urgent: Boolean?,
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

  @PUT("jobs/{id}")
  suspend fun updateJob(
    @Path("id") jobId: String,
    @Body job: JobRegistrationData
  ): Response<BaseResponse<JobRegistrationData>>

  @DELETE("jobs/{id}")
  suspend fun deleteJob(
    @Path("id") jobId: String
  ): Response<DefaultResponse>

  @POST("jobs/{id}/apply")
  suspend fun applyToJob(
    @Path("id") jobId: String,
    @Body workerId: String
  ): Response<DefaultResponse>

  @GET("jobs/{id}/applicants")
  suspend fun getJobApplicants(
    @Path("id") jobId: String
  ): Response<BaseResponse<List<WorkerData>>>
}

// ================================================================================
// 📁 network/api/MatchingAPI.kt
// ================================================================================
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
