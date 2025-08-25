package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.job.*
import com.billcorea.jikgong.network.model.worker.WorkerData  // 추가된 import
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