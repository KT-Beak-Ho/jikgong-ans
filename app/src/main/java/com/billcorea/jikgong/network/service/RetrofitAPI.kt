package com.billcorea.jikgong.network.service

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.network.auth.*
import com.billcorea.jikgong.network.location.*
import com.billcorea.jikgong.network.common.DefaultResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.lang.reflect.Type

interface RetrofitAPI {

    @POST("api/login")
    fun login(
        @Body body: LoginRequest
    ): Call<LoginResponse>

    @POST("api/join/sms-verification")
    fun smsVerification(
        @Body body: SmsVerificationRequest
    ) : Call<SmsVerificationResponse>

    @POST("api/member-info/visaExpiryDate")
    fun visaExpiryDate(
        @Body body: VisaExpiryDateRequest
    ) : Call <DefaultResponse>

    @POST("api/join/validation-phone")
    fun phoneValidation(
        @Body body: PhoneValidationRequest
    ) : Call<PhoneValidationResponse>

    @POST("api/join/validation-loginId")
    fun loginIdValidation(
        @Body body: LoginIdValidationRequest
    ) : Call<LoginIdValidationResponse>

    @Headers("Authorization: KakaoAK " + BuildConfig.KAKAO_REST_API)
    @GET("v2/local/search/address.JSON")
    fun kakaoGeocoding(
        @Query("query") query: String
    ) : Call<AddressFindResponse>

    @Headers("Authorization: KakaoAK " + BuildConfig.KAKAO_REST_API)
    @GET("v2/local/geo/coord2address.JSON")
    fun findAddress(
        @Query("y") lat : Double,
        @Query("x") lon : Double
    ) : Call<Coord2AddressResponse>

    @Multipart
    @POST("api/join/worker/join")
    fun registerWorker(
        @Part("request") request: RequestBody,
        @Part educationCertificateImage: MultipartBody.Part?,
        @Part workerCardImage: MultipartBody.Part?,
        @Part signatureImage: MultipartBody.Part?
    ): Call<RegisterWorkerResponse>

    companion object {
        var baseURL="http://59.21.223.137:8080/"
        var kakaoURL="https://dapi.kakao.com/"
        private val client = OkHttpClient.Builder().build()
        val gson : Gson =   GsonBuilder().setLenient().create();
        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object :
                Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                    try{
                        nextResponseBodyConverter.convert(value)
                    }catch (e:Exception){
                        e.printStackTrace()
                        null
                    }
                } else{
                    null
                }
            }
        }

        fun create(): RetrofitAPI {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(RetrofitAPI::class.java)
        }

        @OptIn(UnstableApi::class)
        fun createKakao(): RetrofitAPI {
            return Retrofit.Builder()
                .baseUrl(kakaoURL)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(RetrofitAPI::class.java)
        }
    }
}