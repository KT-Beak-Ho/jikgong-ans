package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.payment.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 결제/정산 관련 API 인터페이스
 * - 급여 지급
 * - 정산 관리
 * - 결제 내역
 */
interface PaymentAPI {

  // ===== 결제 생성/조회 =====

  @POST("api/payments")
  suspend fun createPayment(
    @Header("Authorization") token: String,
    @Body payment: PaymentData
  ): Response<BaseResponse<PaymentData>>

  @GET("api/payments/{id}")
  suspend fun getPayment(
    @Header("Authorization") token: String,
    @Path("id") paymentId: String
  ): Response<BaseResponse<PaymentData>>

  @PUT("api/payments/{id}")
  suspend fun updatePayment(
    @Header("Authorization") token: String,
    @Path("id") paymentId: String,
    @Body payment: PaymentData
  ): Response<BaseResponse<PaymentData>>

  @DELETE("api/payments/{id}")
  suspend fun cancelPayment(
    @Header("Authorization") token: String,
    @Path("id") paymentId: String,
    @Body reason: CancelReason
  ): Response<DefaultResponse>

  // ===== 결제 상태 관리 =====

  @PUT("api/payments/{id}/status")
  suspend fun updatePaymentStatus(
    @Header("Authorization") token: String,
    @Path("id") paymentId: String,
    @Body status: PaymentStatusUpdate
  ): Response<BaseResponse<PaymentData>>

  // ===== 결제 내역 조회 =====

  @GET("api/payments")
  suspend fun getPayments(
    @Header("Authorization") token: String,
    @Query("status") status: String?,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<PaymentData>>

  @GET("api/payments/worker/{workerId}")
  suspend fun getWorkerPayments(
    @Header("Authorization") token: String,
    @Path("workerId") workerId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<PaymentData>>

  @GET("api/payments/company/{companyId}")
  suspend fun getCompanyPayments(
    @Header("Authorization") token: String,
    @Path("companyId") companyId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?,
    @Query("status") status: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<PaymentData>>

  @GET("api/payments/project/{projectId}")
  suspend fun getProjectPayments(
    @Header("Authorization") token: String,
    @Path("projectId") projectId: String,
    @Query("status") status: String?
  ): Response<BaseResponse<List<PaymentData>>>

  // ===== 결제 처리 =====

  @POST("api/payments/{id}/process")
  suspend fun processPayment(
    @Header("Authorization") token: String,
    @Path("id") paymentId: String,
    @Body processRequest: PaymentProcessRequest? = null
  ): Response<BaseResponse<PaymentData>>

  @POST("api/payments/batch-process")
  suspend fun processBatchPayments(
    @Header("Authorization") token: String,
    @Body request: BatchPaymentRequest
  ): Response<BaseResponse<BatchPaymentResult>>

  // ===== 정산 =====

  @POST("api/payments/settlement/create")
  suspend fun createSettlement(
    @Header("Authorization") token: String,
    @Body settlement: SettlementRequest
  ): Response<BaseResponse<SettlementData>>

  @GET("api/payments/settlement/{id}")
  suspend fun getSettlement(
    @Header("Authorization") token: String,
    @Path("id") settlementId: String
  ): Response<BaseResponse<SettlementData>>

  @POST("api/payments/settlement/{id}/approve")
  suspend fun approveSettlement(
    @Header("Authorization") token: String,
    @Path("id") settlementId: String
  ): Response<BaseResponse<SettlementData>>

  // ===== 통계 =====

  @GET("api/payments/statistics")
  suspend fun getPaymentStatistics(
    @Header("Authorization") token: String,
    @Query("userId") userId: String,
    @Query("userType") userType: String,
    @Query("period") period: String
  ): Response<BaseResponse<PaymentStatistics>>

  @GET("api/payments/summary/monthly")
  suspend fun getMonthlySummary(
    @Header("Authorization") token: String,
    @Query("year") year: Int,
    @Query("month") month: Int,
    @Query("userId") userId: String
  ): Response<BaseResponse<MonthlySummary>>

  // ===== 송금 =====

  @POST("api/payments/transfer")
  suspend fun transferMoney(
    @Header("Authorization") token: String,
    @Body transfer: TransferRequest
  ): Response<BaseResponse<TransferResult>>

  @GET("api/payments/transfer/history")
  suspend fun getTransferHistory(
    @Header("Authorization") token: String,
    @Query("userId") userId: String,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<TransferData>>
}