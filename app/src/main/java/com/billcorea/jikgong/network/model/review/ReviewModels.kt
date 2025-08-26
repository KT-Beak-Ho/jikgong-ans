package com.billcorea.jikgong.network.model.review

import com.billcorea.jikgong.network.model.common.*

/**
 * 리뷰 데이터 기본 클래스
 */
sealed class ReviewData {
  abstract val id: String
  abstract val reviewerId: String
  abstract val projectId: String
  abstract val rating: Double
  abstract val comment: String
  abstract val tags: List<String>
  abstract val createdAt: String
}

/**
 * 노동자 리뷰 데이터
 */
data class WorkerReviewData(
  override val id: String,
  override val reviewerId: String,
  override val projectId: String,
  override val rating: Double,
  override val comment: String,
  override val tags: List<String>,
  override val createdAt: String,

  val workerId: String,
  val punctuality: Int,
  val skillLevel: Int,
  val attitude: Int,
  val teamwork: Int,
  val wouldRehire: Boolean
) : ReviewData()

/**
 * 기업 리뷰 데이터
 */
data class CompanyReviewData(
  override val id: String,
  override val reviewerId: String,
  override val projectId: String,
  override val rating: Double,
  override val comment: String,
  override val tags: List<String>,
  override val createdAt: String,

  val companyId: String,
  val paymentTimeliness: Int,
  val workEnvironment: Int,
  val safetyManagement: Int,
  val communication: Int,
  val wouldWorkAgain: Boolean
) : ReviewData()
