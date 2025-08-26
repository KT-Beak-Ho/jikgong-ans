
package com.billcorea.jikgong.network.model.common

import com.google.gson.annotations.SerializedName

// ===== 기본 응답 모델 =====
data class BaseResponse<T>(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("data")
  val data: T? = null,

  @SerializedName("timestamp")
  val timestamp: String? = null
)

data class PagedResponse<T>(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("data")
  val data: PageData<T>? = null
)

data class PageData<T>(
  @SerializedName("content")
  val content: List<T>,

  @SerializedName("totalElements")
  val totalElements: Long,

  @SerializedName("totalPages")
  val totalPages: Int,

  @SerializedName("number")
  val number: Int,

  @SerializedName("size")
  val size: Int,

  @SerializedName("first")
  val first: Boolean,

  @SerializedName("last")
  val last: Boolean
)

// DefaultResponse - 단일 정의
data class DefaultResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("success")
  val success: Boolean = true,

  @SerializedName("timestamp")
  val timestamp: String? = null
)

data class ErrorResponse(
  @SerializedName("code")
  val code: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("errors")
  val errors: List<FieldError>? = null,

  @SerializedName("timestamp")
  val timestamp: String? = null
)

// FieldError - 단일 정의 (RegisterModels에서 이동)
data class FieldError(
  @SerializedName("field")
  val field: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("rejectedValue")
  val rejectedValue: Any? = null
)

// ===== 파일 관련 =====
data class FileUploadResponse(
  @SerializedName("fileId")
  val fileId: String,

  @SerializedName("fileName")
  val fileName: String,

  @SerializedName("fileUrl")
  val fileUrl: String,

  @SerializedName("fileSize")
  val fileSize: Long,

  @SerializedName("mimeType")
  val mimeType: String
)

data class FileData(
  @SerializedName("fileId")
  val fileId: String,

  @SerializedName("fileName")
  val fileName: String,

  @SerializedName("fileUrl")
  val fileUrl: String,

  @SerializedName("fileSize")
  val fileSize: Long,

  @SerializedName("mimeType")
  val mimeType: String,

  @SerializedName("uploadedAt")
  val uploadedAt: String,

  @SerializedName("uploadedBy")
  val uploadedBy: String
)

// ===== 공통 Enum들 (단일 정의) =====
enum class UserRole {
  WORKER,
  COMPANY,
  ADMIN
}

enum class Gender {
  MALE,
  FEMALE,
  OTHER
}

enum class JobType {
  CONSTRUCTION,
  ELECTRIC,
  PLUMBING,
  PAINTING,
  LANDSCAPING,
  CLEANING,
  MOVING,
  OTHER
}

enum class WorkType {
  DAILY,
  WEEKLY,
  MONTHLY,
  PROJECT
}

enum class WageType {
  HOURLY,
  DAILY,
  MONTHLY,
  PROJECT
}

// NotificationType - 단일 정의 (Enums.kt와 RetrofitModels.kt 중복 제거)
enum class NotificationType {
  PROJECT_START,
  PROJECT_END,
  WORKER_NEEDED,
  SCHEDULE_CHANGE,
  SAFETY_ALERT,
  PAYMENT_NOTICE,
  GENERAL,
  JOB_MATCH,
  APPLICATION_STATUS,
  URGENT,
  SYSTEM
}

// ===== 앱 설정 관련 =====
data class AppVersionInfo(
  @SerializedName("currentVersion")
  val currentVersion: String,

  @SerializedName("minimumVersion")
  val minimumVersion: String,

  @SerializedName("forceUpdate")
  val forceUpdate: Boolean,

  @SerializedName("updateUrl")
  val updateUrl: String,

  @SerializedName("releaseNotes")
  val releaseNotes: String
)

data class TermsData(
  @SerializedName("type")
  val type: String,

  @SerializedName("version")
  val version: String,

  @SerializedName("content")
  val content: String,

  @SerializedName("required")
  val required: Boolean,

  @SerializedName("effectiveDate")
  val effectiveDate: String
)

data class NoticeData(
  @SerializedName("id")
  val id: String,

  @SerializedName("title")
  val title: String,

  @SerializedName("content")
  val content: String,

  @SerializedName("type")
  val type: String,

  @SerializedName("important")
  val important: Boolean,

  @SerializedName("createdAt")
  val createdAt: String
)

data class BannerData(
  @SerializedName("id")
  val id: String,

  @SerializedName("imageUrl")
  val imageUrl: String,

  @SerializedName("linkUrl")
  val linkUrl: String? = null,

  @SerializedName("position")
  val position: String,

  @SerializedName("priority")
  val priority: Int,

  @SerializedName("startDate")
  val startDate: String,

  @SerializedName("endDate")
  val endDate: String
)

data class FAQData(
  @SerializedName("id")
  val id: String,

  @SerializedName("category")
  val category: String,

  @SerializedName("question")
  val question: String,

  @SerializedName("answer")
  val answer: String,

  @SerializedName("order")
  val order: Int
)

// ===== 푸시 알림 관련 =====
data class PushTokenRequest(
  @SerializedName("token")
  val token: String,

  @SerializedName("deviceType")
  val deviceType: String = "ANDROID"
)

data class NotificationData(
  @SerializedName("id")
  val id: String,

  @SerializedName("title")
  val title: String,

  @SerializedName("message")
  val message: String,

  @SerializedName("type")
  val type: String,

  @SerializedName("data")
  val data: Map<String, String>? = null,

  @SerializedName("read")
  val read: Boolean,

  @SerializedName("createdAt")
  val createdAt: String
)

// ===== 지역 관련 =====
data class RegionApiData(
  @SerializedName("code")
  val code: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("level")
  val level: Int,

  @SerializedName("parentCode")
  val parentCode: String? = null,

  @SerializedName("fullName")
  val fullName: String
)

// ===== 통계 관련 =====
data class DashboardStats(
  @SerializedName("period")
  val period: String,

  @SerializedName("totalProjects")
  val totalProjects: Int,

  @SerializedName("activeProjects")
  val activeProjects: Int,

  @SerializedName("totalWorkers")
  val totalWorkers: Int,

  @SerializedName("totalRevenue")
  val totalRevenue: Long,

  @SerializedName("growthRate")
  val growthRate: Double,

  @SerializedName("charts")
  val charts: Map<String, List<ChartData>>
)

data class ChartData(
  @SerializedName("label")
  val label: String,

  @SerializedName("value")
  val value: Double,

  @SerializedName("date")
  val date: String? = null
)

data class MarketStats(
  @SerializedName("averageWage")
  val averageWage: Long,

  @SerializedName("totalJobs")
  val totalJobs: Int,

  @SerializedName("totalWorkers")
  val totalWorkers: Int,

  @SerializedName("demandSupplyRatio")
  val demandSupplyRatio: Double,

  @SerializedName("trends")
  val trends: List<TrendData>
)

data class TrendData(
  @SerializedName("period")
  val period: String,

  @SerializedName("metric")
  val metric: String,

  @SerializedName("value")
  val value: Double,

  @SerializedName("change")
  val change: Double
)