package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.project.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 프로젝트 관련 API 인터페이스
 */
interface ProjectAPI {

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

  @POST("projects/{id}/confirm-worker")
  suspend fun confirmWorker(
    @Path("id") projectId: String,
    @Body workerId: String
  ): Response<DefaultResponse>
}
