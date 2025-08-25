package com.billcorea.jikgong.network.model.job

import com.billcorea.jikgong.network.model.common.*

/**
 * 일자리 등록 데이터
 */
data class JobRegistrationData(
  // ===== 기본 정보 =====
  val id: String,
  val projectId: String,
  val companyId: String,
  val title: String,
  val jobType: JobType,
  val isUrgent: Boolean = false,
  val deadline: String,
  val status: JobStatus,

  // ===== 근무 조건 =====
  val workDate: String,
  val workStartTime: String,
  val workEndTime: String,
  val location: String,
  val detailLocation: String? = null,
  val workIntensity: WorkIntensity,
  val riskLevel: RiskLevel,

  // ===== 요구사항 =====
  val experienceLevel: ExperienceLevel,
  val minExperienceYears: Int,
  val ageLimit: AgeRange? = null,
  val preferredGender: Gender? = null,
  val requiredDocuments: List<String>,
  val needInterview: Boolean = false,
  val needHealthCheck: Boolean = false,

  // ===== 제공 사항 =====
  val provisions: JobProvisions,

  // ===== 기타 =====
  val preparationItems: List<String>,
  val environmentPhotos: List<String>,
  val probationPeriod: Int? = null,
  val contactPerson: ContactPerson,
  val applicants: List<String> = emptyList(),
  val viewCount: Int = 0,
  val createdAt: String,
  val updatedAt: String
)

/**
 * 일자리 제공 사항
 */
data class JobProvisions(
  val hasPickup: Boolean,
  val pickupLocation: String? = null,
  val parkingSpace: ParkingSpace,
  val parkingDescription: String? = null,
  val hasAccommodation: Boolean,
  val meals: MealProvision,
  val providesTools: Boolean,
  val providesWorkClothes: Boolean,
  val insurance: List<InsuranceType>
)

/**
 * 주차 공간
 */
data class ParkingSpace(
  val available: Boolean,
  val isFree: Boolean,
  val description: String? = null
)

/**
 * 식사 제공
 */
data class MealProvision(
  val breakfast: Boolean,
  val lunch: Boolean,
  val dinner: Boolean
)

/**
 * 담당자 정보
 */
data class ContactPerson(
  val name: String,
  val position: String,
  val phone: String,
  val email: String? = null
)

/**
 * 연령 범위
 */
data class AgeRange(
  val min: Int,
  val max: Int
)
