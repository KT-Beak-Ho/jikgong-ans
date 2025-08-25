package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.worker.*
import com.billcorea.jikgong.network.model.review.WorkerReviewData  // 추가된 import
import retrofit2.Response
import retrofit2.http.*

/**
 * 노동자 관련 API 인터페이스
 */
interface WorkerAPI {

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

  @GET("workers/{id}/activity")
  suspend fun getWorkerActivity(
    @Path("id") workerId: String
  ): Response<BaseResponse<List<WorkerActivity>>>

  @GET("workers/{id}/reviews")
  suspend fun getWorkerReviews(
    @Path("id") workerId: String
  ): Response<BaseResponse<List<WorkerReviewData>>>

  @POST("workers/{id}/scout-profile")
  suspend fun updateScoutProfile(
    @Path("id") workerId: String,
    @Body profile: ScoutProfile
  ): Response<BaseResponse<ScoutProfile>>

  @GET("workers/scout-profiles")
  suspend fun getScoutProfiles(
    @QueryMap filters: Map<String, String>
  ): Response<PagedResponse<WorkerData>>
}