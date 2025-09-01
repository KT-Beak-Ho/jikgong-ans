package com.billcorea.jikgong.api.service

import com.billcorea.jikgong.api.models.auth.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>
    
    @POST("api/worker/join/send-sms")
    suspend fun sendSMS(
        @Body body: SendSMSRequest
    ): Response<SendSMSResponse>
    
    @POST("api/worker/join/verify-sms")
    suspend fun verifySMS(
        @Body body: VerifySMSRequest
    ): Response<VerifySMSResponse>
    
    @POST("api/worker/join/login-id-validation")
    suspend fun validateLoginId(
        @Body body: LoginIdValidationRequest
    ): Response<LoginIdValidationResponse>
    
    @POST("api/worker/join")
    suspend fun registerWorker(
        @Body body: RegisterWorker
    ): Response<RegisterWorkerResponse>
}