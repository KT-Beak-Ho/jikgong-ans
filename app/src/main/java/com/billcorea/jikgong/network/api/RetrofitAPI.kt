package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.network.model.auth.*
import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.location.*
import com.billcorea.jikgong.network.model.worker.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitAPI {

  @POST("api/login")
  fun login(
    @Body body: LoginRequest
  ): Call<LoginResponse>

  @POST("api/join/sms-verification")
  fun smsVerification(
    @Body body: SmsVerificationRequest
  ): Call<SmsVerificationResponse>

  @POST("api/member-info/visaExpiryDate")
  fun visaExpiryDate(
    @Body body: VisaExpiryDateRequest
  ): Call<DefaultResponse>

  @POST("api/join/validation-phone")
  fun phoneValidation(
    @Body body: PhoneValidationRequest
  ): Call<PhoneValidationResponse>

  @POST("api/join/validation-loginId")
  fun loginIdValidation(
    @Body body: LoginIdValidationRequest
  ): Call<LoginIdValidationResponse>

  @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API}")
  @GET("v2/local/search/address.JSON")
  fun kakaoGeocoding(
    @Query("query") query: String
  ): Call<AddressFindResponse>

  @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API}")
  @GET("v2/local/geo/coord2address.JSON")
  fun findAddress(
    @Query("y") lat: Double,
    @Query("x") lon: Double
  ): Call<Coord2AddressResponse>

  @Multipart
  @POST("api/join/worker/join")
  fun registerWorker(
    @Part("request") request: RequestBody,
    @Part educationCertificateImage: MultipartBody.Part?,
    @Part workerCardImage: MultipartBody.Part?,
    @Part signatureImage: MultipartBody.Part?
  ): Call<RegisterWorkerResponse>
}