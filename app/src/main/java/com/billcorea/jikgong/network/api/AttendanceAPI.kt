package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.attendance.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 출퇴근/근태 관련 API 인터페이스
 */
interface AttendanceAPI {

  @POST("attendance/check-in")
  suspend fun checkIn(
    @Body checkIn: CheckInRequest
  ): Response<BaseResponse<AttendanceData>>

  @POST("attendance/check-out")
  suspend fun checkOut(
    @Body checkOut: CheckOutRequest
  ): Response<BaseResponse<AttendanceData>>

  @GET("attendance/{id}")
  suspend fun getAttendance(
    @Path("id") attendanceId: String
  ): Response<BaseResponse<AttendanceData>>

  @GET("attendance/worker/{workerId}")
  suspend fun getWorkerAttendance(
    @Path("workerId") workerId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?
  ): Response<BaseResponse<List<AttendanceData>>>

  @GET("attendance/project/{projectId}")
  suspend fun getProjectAttendance(
    @Path("projectId") projectId: String,
    @Query("date") date: String
  ): Response<BaseResponse<List<AttendanceData>>>

  @POST("attendance/verify")
  suspend fun verifyAttendance(
    @Body verification: AttendanceVerification
  ): Response<DefaultResponse>
}

data class CheckInRequest(
  val workerId: String,
  val projectId: String,
  val location: LocationData,
  val photo: String?,
  val verificationMethod: VerificationMethod
)

data class CheckOutRequest(
  val attendanceId: String,
  val location: LocationData,
  val photo: String?
)

data class AttendanceVerification(
  val attendanceId: String,
  val verificationMethod: VerificationMethod,
  val verificationData: String
)