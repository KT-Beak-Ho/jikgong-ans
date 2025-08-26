package com.billcorea.jikgong.network.model.matching

import com.billcorea.jikgong.network.model.common.*

/**
 * 매칭 관련 모델 (완전판)
 */

// ============================================
// 매칭 데이터
// ============================================
data class WorkerMatchingData(
  val id: String,
  val workerId: String,
  val workerName: String,
  val projectId: String? = null,
  val jobId: String? = null,
  val companyId: String,
  val companyName: String,

  // ===== 매칭 정보 =====
  val matchingType: MatchingType,
  val matchingScore: MatchingScore,
  val status: MatchingStatus,
  val priority: MatchingPriority,

  // ===== 날짜 =====
  val requestedAt: String,
  val matchedAt: String? = null,
  val confirmedAt: String? = null,
  val completedAt: String? = null,

  // ===== 조건 =====
  val wage: Long,
  val workDate: String,
  val workStartTime: String,
  val workEndTime: String,
  val location: String,

  // ===== 추가 정보 =====
  val requirements: List<String> = emptyList(),
  val notes: String? = null,
  val rejectReason: String? = null,

  val createdAt: String,
  val updatedAt: String
)

// ============================================
// 매칭 요청
// ============================================
data class MatchingRequest(
  val workerId: String? = null,
  val projectId: String? = null,
  val jobId: String? = null,
  val companyId: String,
  val matchingType: MatchingType,
  val requirements: MatchingRequirements,
  val urgent: Boolean = false
)

data class MatchingRequirements(
  val jobType: JobType,
  val experienceLevel: ExperienceLevel,
  val skills: List<String>,
  val certifications: List<String>?,
  val wageRange: WageRange,
  val location: String,
  val workDate: String
)

data class WageRange(
  val min: Long,
  val max: Long
)

// ============================================
// 매칭 확정/거절
// ============================================
data class MatchingConfirmRequest(
  val confirmedBy: String,
  val finalWage: Long?,
  val notes: String?
)

data class MatchingRejectRequest(
  val rejectedBy: String,
  val reason: String,
  val detailReason: String?
)

// ============================================
// 매칭 점수
// ============================================
data class MatchingScore(
  val totalScore: Double,
  val skillScore: Double,
  val experienceScore: Double,
  val locationScore: Double,
  val wageScore: Double,
  val availabilityScore: Double,
  val reliabilityScore: Double
)

// ============================================
// 매칭 타입/상태
// ============================================
enum class MatchingType {
  AUTO,        // AI 자동 매칭
  MANUAL,      // 수동 매칭
  DIRECT,      // 직접 지원
  SCOUT,       // 스카우트
  RECOMMENDED  // 추천
}

enum class MatchingStatus {
  PENDING,     // 대기중
  REVIEWING,   // 검토중
  PROPOSED,    // 제안됨
  ACCEPTED,    // 수락됨
  REJECTED,    // 거절됨
  CONFIRMED,   // 확정됨
  IN_PROGRESS, // 진행중
  COMPLETED,   // 완료됨
  CANCELLED,   // 취소됨
  EXPIRED      // 만료됨
}

enum class MatchingPriority {
  LOW,
  NORMAL,
  HIGH,
  URGENT
}

// ============================================
// MatchingAPI에서 이동된 모델들
// ============================================
data class MatchingStatusUpdate(
  val status: MatchingStatus,
  val reason: String?,
  val updatedBy: String
)

data class AISuggestion(
  val id: String,
  val targetType: String,  // WORKER, PROJECT, JOB
  val targetId: String,
  val targetName: String,
  val matchScore: Double,
  val confidence: Double,
  val reasons: List<MatchReason>,
  val suggestedAt: String
)

data class MatchReason(
  val factor: String,
  val weight: Double,
  val description: String
)

data class AutoMatchRequest(
  val projectId: String?,
  val jobId: String?,
  val criteria: AutoMatchCriteria,
  val limit: Int = 10
)

data class AutoMatchCriteria(
  val minScore: Double,
  val maxDistance: Int?,
  val requiredSkills: List<String>?,
  val experienceLevel: String?,
  val wageRange: WageRange?,
  val excludeBlacklisted: Boolean
)

data class AutoMatchResult(
  val totalCandidates: Int,
  val matchedCount: Int,
  val matches: List<WorkerMatchingData>,
  val executionTime: Long
)

data class MatchAnalysis(
  val matchingId: String,
  val overallScore: Double,
  val strengthFactors: List<StrengthFactor>,
  val weaknessFactors: List<WeaknessFactor>,
  val recommendations: List<String>,
  val predictedSuccessRate: Double
)

data class StrengthFactor(
  val factor: String,
  val score: Double,
  val impact: String
)

data class WeaknessFactor(
  val factor: String,
  val score: Double,
  val suggestion: String
)

data class MatchingFeedback(
  val matchingId: String,
  val actualOutcome: String,
  val satisfactionScore: Int,
  val comments: String?
)

data class ScoreCalculationRequest(
  val workerId: String,
  val projectId: String?,
  val jobId: String?,
  val includeDetails: Boolean = false
)

data class ScoreFactors(
  val skillMatch: Double,
  val experienceMatch: Double,
  val locationProximity: Double,
  val wageCompatibility: Double,
  val availabilityMatch: Double,
  val pastPerformance: Double,
  val preferenceMatch: Double
)

data class ScoreWeights(
  val skillWeight: Double = 0.25,
  val experienceWeight: Double = 0.2,
  val locationWeight: Double = 0.15,
  val wageWeight: Double = 0.15,
  val availabilityWeight: Double = 0.1,
  val performanceWeight: Double = 0.1,
  val preferenceWeight: Double = 0.05
)

data class MatchingStatistics(
  val totalMatchings: Int,
  val successfulMatchings: Int,
  val pendingMatchings: Int,
  val rejectedMatchings: Int,
  val averageMatchScore: Double,
  val averageResponseTime: Double,
  val topMatchedWorkers: List<TopMatchedEntity>,
  val topMatchedProjects: List<TopMatchedEntity>
)

data class TopMatchedEntity(
  val id: String,
  val name: String,
  val matchCount: Int,
  val successRate: Double
)

data class SuccessRateData(
  val entityType: String,
  val entityId: String,
  val totalAttempts: Int,
  val successfulMatches: Int,
  val successRate: Double,
  val trend: String  // IMPROVING, STABLE, DECLINING
)

data class MatchingTrends(
  val period: String,
  val dailyMatches: List<DailyMatchData>,
  val peakDays: List<String>,
  val peakHours: List<Int>,
  val averageDailyMatches: Double,
  val growthRate: Double
)

data class DailyMatchData(
  val date: String,
  val matchCount: Int,
  val successCount: Int
)

data class MatchingFilter(
  val id: String?,
  val name: String,
  val userId: String,
  val criteria: FilterCriteria,
  val isActive: Boolean,
  val createdAt: String?
)

data class FilterCriteria(
  val jobTypes: List<JobType>?,
  val locations: List<String>?,
  val skills: List<String>?,
  val experienceRange: ExperienceRange?,
  val wageRange: WageRange?,
  val certifications: List<String>?
)

data class ExperienceRange(
  val min: Int,
  val max: Int
)

data class MatchingHistory(
  val id: String,
  val matchingId: String,
  val action: String,
  val performedBy: String,
  val timestamp: String,
  val details: String?
)

data class RepeatedMatch(
  val workerId: String,
  val workerName: String,
  val companyId: String,
  val companyName: String,
  val matchCount: Int,
  val lastMatchDate: String,
  val averageRating: Double,
  val totalWorkDays: Int
)

data class MatchingNotification(
  val type: MatchingNotificationType,
  val recipients: List<String>,
  val title: String,
  val message: String,
  val data: Map<String, String>?
)

enum class MatchingNotificationType {
  NEW_MATCH,
  MATCH_CONFIRMED,
  MATCH_REJECTED,
  MATCH_EXPIRED,
  HIGH_SCORE_MATCH
}

data class MatchingNotificationSettings(
  val userId: String,
  val newMatch: Boolean = true,
  val matchConfirmed: Boolean = true,
  val matchRejected: Boolean = true,
  val highScoreMatch: Boolean = true,
  val minScoreThreshold: Double = 0.8
)

data class BatchMatchingConfirmRequest(
  val matchingIds: List<String>,
  val confirmation: MatchingConfirmRequest
)

data class BatchMatchingResult(
  val totalCount: Int,
  val successCount: Int,
  val failedCount: Int,
  val results: List<MatchingOperationResult>
)

data class MatchingOperationResult(
  val matchingId: String,
  val success: Boolean,
  val message: String?
)