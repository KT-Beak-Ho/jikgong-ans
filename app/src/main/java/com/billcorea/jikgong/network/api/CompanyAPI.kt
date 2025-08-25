package com.billcorea.jikgong.network.api

import com.billcorea.jikgong.network.model.common.*
import com.billcorea.jikgong.network.model.company.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 기업 관련 API 인터페이스
 */
interface CompanyAPI {

  @POST("companies/register")
  suspend fun registerCompany(
    @Body company: CompanyData
  ): Response<BaseResponse<CompanyData>>

  @GET("companies/{id}")
  suspend fun getCompany(
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyData>>

  @PUT("companies/{id}")
  suspend fun updateCompany(
    @Path("id") companyId: String,
    @Body company: CompanyData
  ): Response<BaseResponse<CompanyData>>

  @DELETE("companies/{id}")
  suspend fun deleteCompany(
    @Path("id") companyId: String
  ): Response<DefaultResponse>

  @GET("companies/search")
  suspend fun searchCompanies(
    @Query("keyword") keyword: String?,
    @Query("region") region: String?,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20
  ): Response<PagedResponse<CompanyData>>

  @GET("companies/{id}/stats")
  suspend fun getCompanyStats(
    @Path("id") companyId: String
  ): Response<BaseResponse<CompanyStats>>

  @GET("companies/{id}/projects")
  suspend fun getCompanyProjects(
    @Path("id") companyId: String,
    @Query("status") status: String?
  ): Response<BaseResponse<List<ProjectData>>>

  @GET("companies/{id}/workers")
  suspend fun getCompanyWorkers(
    @Path("id") companyId: String
  ): Response<BaseResponse<List<WorkerData>>>
}