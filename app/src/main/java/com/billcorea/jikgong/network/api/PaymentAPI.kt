package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.payment.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 결제/정산 관련 API 인터페이스
 */
interface PaymentAPI {

  @POST("payments")
  suspend fun createPayment(
    @Body payment: PaymentData
  ): Response<BaseResponse<PaymentData>>

  @GET("payments/{id}")
  suspend fun getPayment(
    @Path("id") paymentId: String
  ): Response<BaseResponse<PaymentData>>

  @PUT("payments/{id}/status")
  suspend fun updatePaymentStatus(
    @Path("id") paymentId: String,
    @Body status: PaymentStatus
  ): Response<BaseResponse<PaymentData>>

  @GET("payments/worker/{workerId}")
  suspend fun getWorkerPayments(
    @Path("workerId") workerId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?
  ): Response<BaseResponse<List<PaymentData>>>

  @GET("payments/company/{companyId}")
  suspend fun getCompanyPayments(
    @Path("companyId") companyId: String,
    @Query("startDate") startDate: String?,
    @Query("endDate") endDate: String?
  ): Response<BaseResponse<List<PaymentData>>>

  @GET("payments/project/{projectId}")
  suspend fun getProjectPayments(
    @Path("projectId") projectId: String
  ): Response<BaseResponse<List<PaymentData>>>

  @POST("payments/{id}/process")
  suspend fun processPayment(
    @Path("id") paymentId: String
  ): Response<BaseResponse<PaymentData>>

  @POST("payments/batch-process")
  suspend fun processBatchPayments(
    @Body paymentIds: List<String>
  ): Response<BaseResponse<List<PaymentData>>>

  @GET("payments/statistics")
  suspend fun getPaymentStatistics(
    @Query("userId") userId: String,
    @Query("period") period: String
  ): Response<BaseResponse<PaymentStatistics>>
}

data class PaymentStatistics(
  val totalAmount: Long,
  val paidAmount: Long,
  val pendingAmount: Long,
  val averageAmount: Long,
  val paymentCount: Int
)
