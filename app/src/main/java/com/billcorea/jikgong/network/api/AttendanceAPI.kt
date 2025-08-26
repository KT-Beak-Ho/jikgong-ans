package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.attendance.*
import com.billcorea.jikgong.network.model.location.LocationData
import retrofit2.Response
import retrofit2.http.*

/**
 * 출퇴근/근태 관련 API 인터페이스
 * - 출퇴근 체크
 * - 근태 조회
 * - 근태 인증
 */
interface AttendanceAPI {

  // ===== 출퇴근 체크 =====

  @POST("api/attendance/check-in")
  suspend fun checkIn(
    @Header("Authorization") token: String,
    @Body checkIn: CheckInRequest
  ): Response<BaseResponse<AttendanceData>>

  @POST("api/attendance/check-out")
  suspend fun checkOut(
    @Header("Authorization") token: String,
    @Body checkOut: CheckOutRequest
  ): Response<BaseResponse<AttendanceData>>

  // ===== 근태 조회 =====

  @GET("api/attendance/{id}")
  suspend fun getAttendance(
    @Header("Authorization") token: String,
    @Path("id") attendanceId: String
  ): Response<BaseResponse<AttendanceData>>

  @GET("api/attendance/worker/{workerId}")
  suspend fun getWorkerAttendance(
    @Header("Authorization") token: String,
    @Path("workerId") workerId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?
  ): Response<BaseResponse<List<AttendanceData>>>

  @GET("api/attendance/project/{projectId}")
  suspend fun getProjectAttendance(
    @Header("Authorization") token: String,
    @Path("projectId") projectId: String,
    @Query("date") date: String
  ): Response<BaseResponse<List<AttendanceData>>>

  @GET("api/attendance/company/{companyId}")
  suspend fun getCompanyAttendance(
    @Header("Authorization") token: String,
    @Path("companyId") companyId: String,
    @Query("date") date: String
  ): Response<BaseResponse<List<AttendanceData>>>

  // ===== 근태 인증 =====

  @POST("api/attendance/verify")
  suspend fun verifyAttendance(
    @Header("Authorization") token: String,
    @Body verification: AttendanceVerification
  ): Response<DefaultResponse>

  @POST("api/attendance/verify-location")
  suspend fun verifyLocation(
    @Header("Authorization") token: String,
    @Body location: LocationVerificationRequest
  ): Response<BaseResponse<LocationVerificationResult>>

  // ===== 근태 통계 =====

  @GET("api/attendance/stats/worker/{workerId}")
  suspend fun getWorkerAttendanceStats(
    @Header("Authorization") token: String,
    @Path("workerId") workerId: String,
    @Query("period") period: String
  ): Response<BaseResponse<AttendanceStats>>

  @GET("api/attendance/stats/project/{projectId}")
  suspend fun getProjectAttendanceStats(
    @Header("Authorization") token: String,
    @Path("projectId") projectId: String,
    @Query("period") period: String
  ): Response<BaseResponse<AttendanceStats>>

  // ===== 휴게 시간 =====

  @POST("api/attendance/{id}/break-start")
  suspend fun startBreak(
    @Header("Authorization") token: String,
    @Path("id") attendanceId: String
  ): Response<BaseResponse<BreakRecord>>

  @POST("api/attendance/{id}/break-end")
  suspend fun endBreak(
    @Header("Authorization") token: String,
    @Path("id") attendanceId: String,
    @Body breakId: String
  ): Response<BaseResponse<BreakRecord>>
}