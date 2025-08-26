package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.project.*
import com.billcorea.jikgong.network.model.worker.WorkerData
import com.billcorea.jikgong.network.model.attendance.AttendanceData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 프로젝트 관련 API 인터페이스
 * - 프로젝트 생성/관리
 * - 프로젝트 진행 관리
 * - 프로젝트 인력 관리
 * - 프로젝트 문서 관리
 */
interface ProjectAPI {

  // ===== 프로젝트 관리 =====

  @GET("api/projects")
  suspend fun getProjects(
    @Query("status") status: String?,
    @Query("category") category: String?,
    @Query("companyId") companyId: String?,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
    @Query("sort") sort: String = "createdAt,desc"
  ): Response<PagedResponse<ProjectData>>

  @GET("api/projects/{id}")
  suspend fun getProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectData>>

  @POST("api/projects")
  suspend fun createProject(
    @Header("Authorization") token: String,
    @Body project: ProjectData
  ): Response<BaseResponse<ProjectData>>

  @PUT("api/projects/{id}")
  suspend fun updateProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body project: ProjectData
  ): Response<BaseResponse<ProjectData>>

  @DELETE("api/projects/{id}")
  suspend fun deleteProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("reason") reason: String?
  ): Response<DefaultResponse>

  @PUT("api/projects/{id}/status")
  suspend fun updateProjectStatus(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body statusUpdate: ProjectStatusUpdate
  ): Response<BaseResponse<ProjectData>>

  // ===== 프로젝트 시작/종료 =====

  @POST("api/projects/{id}/start")
  suspend fun startProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body startRequest: ProjectStartRequest
  ): Response<BaseResponse<ProjectData>>

  @POST("api/projects/{id}/complete")
  suspend fun completeProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body completionRequest: ProjectCompletionRequest
  ): Response<BaseResponse<ProjectCompletion>>

  // ===== 프로젝트 진행 관리 =====

  @GET("api/projects/{id}/progress")
  suspend fun getProjectProgress(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectProgress>>

  @PUT("api/projects/{id}/progress")
  suspend fun updateProjectProgress(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body progress: ProgressUpdate
  ): Response<BaseResponse<ProjectProgress>>

  // ===== 프로젝트 신청 =====

  @POST("api/projects/{id}/apply")
  suspend fun applyToProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body application: ProjectApplicationRequest
  ): Response<BaseResponse<ProjectApplication>>

  @GET("api/projects/{id}/applications")
  suspend fun getProjectApplications(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("status") status: String?
  ): Response<BaseResponse<List<ProjectApplication>>>

  // ===== 프로젝트 인력 관리 =====

  @POST("api/projects/{id}/workers/confirm")
  suspend fun confirmWorker(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body confirmation: WorkerConfirmRequest
  ): Response<BaseResponse<ProjectWorker>>

  @GET("api/projects/{id}/workers")
  suspend fun getProjectWorkers(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<List<ProjectWorker>>>

  @POST("api/projects/{id}/workers/request")
  suspend fun requestAdditionalWorkers(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body request: AdditionalWorkersRequest
  ): Response<BaseResponse<WorkerRequestResult>>

  // ===== 프로젝트 일정 관리 =====

  @GET("api/projects/{id}/schedule")
  suspend fun getProjectSchedule(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectSchedule>>

  @PUT("api/projects/{id}/schedule")
  suspend fun updateProjectSchedule(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body schedule: ProjectSchedule
  ): Response<BaseResponse<ProjectSchedule>>

  @POST("api/projects/{id}/milestones")
  suspend fun addMilestone(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body milestone: ProjectMilestone
  ): Response<BaseResponse<ProjectMilestone>>

  @GET("api/projects/{id}/milestones")
  suspend fun getProjectMilestones(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<List<ProjectMilestone>>>

  // ===== 프로젝트 문서 관리 =====

  @Multipart
  @POST("api/projects/{id}/documents")
  suspend fun uploadProjectDocument(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Part document: MultipartBody.Part,
    @Part("type") type: RequestBody,
    @Part("description") description: RequestBody?
  ): Response<BaseResponse<ProjectDocument>>

  @GET("api/projects/{id}/documents")
  suspend fun getProjectDocuments(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("type") type: String?
  ): Response<BaseResponse<List<ProjectDocument>>>

  @DELETE("api/projects/{id}/documents/{documentId}")
  suspend fun deleteProjectDocument(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Path("documentId") documentId: String
  ): Response<DefaultResponse>

  // ===== 프로젝트 근태 관리 =====

  @GET("api/projects/{id}/attendance")
  suspend fun getProjectAttendance(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("date") date: String
  ): Response<BaseResponse<List<AttendanceData>>>

  @GET("api/projects/{id}/attendance/summary")
  suspend fun getAttendanceSummary(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("startDate") startDate: String,
    @Query("endDate") endDate: String
  ): Response<BaseResponse<ProjectAttendanceSummary>>

  // ===== 프로젝트 통계 =====

  @GET("api/projects/{id}/stats")
  suspend fun getProjectStats(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectStats>>

  @GET("api/projects/{id}/cost-analysis")
  suspend fun getCostAnalysis(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectCostAnalysis>>

  @GET("api/projects/{id}/performance")
  suspend fun getProjectPerformance(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<ProjectPerformance>>

  // ===== 프로젝트 평가 =====

  @POST("api/projects/{id}/evaluate")
  suspend fun evaluateProject(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body evaluation: ProjectEvaluation
  ): Response<BaseResponse<ProjectEvaluation>>

  @GET("api/projects/{id}/evaluations")
  suspend fun getProjectEvaluations(
    @Header("Authorization") token: String,
    @Path("id") projectId: String
  ): Response<BaseResponse<List<ProjectEvaluation>>>

  @POST("api/projects/{id}/feedback")
  suspend fun submitProjectFeedback(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body feedback: ProjectFeedback
  ): Response<BaseResponse<ProjectFeedback>>

  // ===== 프로젝트 보고서 =====

  @GET("api/projects/{id}/report")
  suspend fun getProjectReport(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("type") type: String
  ): Response<BaseResponse<ProjectReport>>

  @POST("api/projects/{id}/report/generate")
  suspend fun generateProjectReport(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body request: ReportGenerationRequest
  ): Response<BaseResponse<ProjectReport>>

  // ===== 프로젝트 알림 =====

  @GET("api/projects/{id}/notifications")
  suspend fun getProjectNotifications(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<ProjectNotification>>

  @POST("api/projects/{id}/notifications")
  suspend fun sendProjectNotification(
    @Header("Authorization") token: String,
    @Path("id") projectId: String,
    @Body notification: ProjectNotificationRequest
  ): Response<DefaultResponse>
}

// ProjectProgress 추가 정의 (ProjectModels.kt에 추가)
data class ProjectProgress(
  val projectId: String,
  val progressPercentage: Double,
  val currentPhase: String,
  val completedMilestones: Int,
  val totalMilestones: Int,
  val lastUpdated: String
)