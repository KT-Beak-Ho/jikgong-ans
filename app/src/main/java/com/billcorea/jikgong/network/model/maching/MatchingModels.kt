package com.billcorea.jikgong.network.model.matching

import com.billcorea.jikgong.network.model.common.*

/**
 * 노동자 매칭 데이터
 */
data class WorkerMatchingData(
  val id: String,
  val workerId: String,
  val projectId: String,
  val jobId: String,
  val matchingScore: MatchingScore,
  val status: MatchingStatus,
  val requestedAt: String,
  val confirmedAt: String? = null,
  val rejectedAt: String? = null,
  val rejectionReason: String? = null,
  val priority: Int = 0,
  val isAutoMatched: Boolean = false
)

/**
 * 매칭 점수
 */
data class MatchingScore(
  val totalScore: Double,
  val distanceScore: Double,
  val experienceScore: Double,
  val wageScore: Double,
  val skillScore: Double,
  val availabilityScore: Double,
  val weights: ScoreWeights
)

/**
 * 점수 가중치
 */
data class ScoreWeights(
  val distance: Double = 0.25,
  val experience: Double = 0.25,
  val wage: Double = 0.20,
  val skill: Double = 0.20,
  val availability: Double = 0.10
)

/**
 * 매칭 요청
 */
data class MatchingRequest(
  val workerId: String,
  val projectId: String,
  val jobId: String,
  val message: String? = null
)

/**
 * 매칭 결과
 */
data class MatchingResult(
  val matchingId: String,
  val success: Boolean,
  val message: String,
  val matchingScore: Double? = null
)
