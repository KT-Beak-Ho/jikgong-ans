package com.billcorea.jikgong.presentation.company.main

import com.billcorea.jikgong.network.CompanyData
import com.billcorea.jikgong.network.CompanyType
import com.billcorea.jikgong.network.CompanyStatus
import com.billcorea.jikgong.network.NotificationInfo
import com.billcorea.jikgong.network.CompanyStats
import com.billcorea.jikgong.network.StatItem
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentData
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSummary
import com.billcorea.jikgong.presentation.company.main.scout.data.Worker
import com.billcorea.jikgong.presentation.company.main.scout.data.Proposal
import com.billcorea.jikgong.presentation.company.main.scout.data.ProposalStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.screen.SimpleProject
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * 통합된 회사 관련 샘플 데이터 팩토리
 * 모든 화면에서 일관된 데이터를 사용하기 위한 중앙화된 데이터 공급자
 */
object CompanyMockDataFactory {

    // ==================== 공통 기본 데이터 ====================
    
    /**
     * 일관된 작업자 기본 정보
     */
    data class BaseWorker(
        val id: String,
        val name: String,
        val primaryJobType: String,
        val secondaryJobTypes: List<String> = emptyList(),
        val experience: Int,
        val hourlyWage: Int, // 원/시간
        val dailyWage: String, // 표시용 일당
        val rating: Float,
        val completedProjects: Int,
        val introduction: String,
        val isAvailable: Boolean = true,
        val location: String = "서울특별시 강남구",
        val distance: Double = 0.0
    )

    /**
     * 프로젝트 기본 정보
     */
    data class BaseProject(
        val id: String,
        val title: String,
        val company: String,
        val location: String,
        val category: String,
        val status: String,
        val startDate: String,
        val endDate: String,
        val wage: Int,
        val currentApplicants: Int,
        val maxApplicants: Int,
        val isUrgent: Boolean = false,
        val isBookmarked: Boolean = false
    )

    // ==================== 기본 작업자 데이터 ====================
    
    private val baseWorkers = listOf(
        BaseWorker(
            id = "worker_001",
            name = "김철수",
            primaryJobType = "철근공",
            secondaryJobTypes = listOf("형틀목공"),
            experience = 5,
            hourlyWage = 18000,
            dailyWage = "일당 18만원",
            rating = 4.8f,
            completedProjects = 52,
            introduction = "성실하고 꼼꼼한 작업을 약속드립니다. 철근 작업 5년 경력으로 안전하고 정확한 시공이 가능합니다.",
            distance = 0.8
        ),
        BaseWorker(
            id = "worker_002", 
            name = "이영희",
            primaryJobType = "타일공",
            secondaryJobTypes = emptyList(),
            experience = 3,
            hourlyWage = 17000,
            dailyWage = "일당 15만원",
            rating = 4.5f,
            completedProjects = 28,
            introduction = "깔끔한 마감 처리가 장점입니다. 타일 시공 전문으로 꼼꼼한 작업을 보장합니다.",
            distance = 1.2
        ),
        BaseWorker(
            id = "worker_003",
            name = "박민수", 
            primaryJobType = "전기공",
            secondaryJobTypes = listOf("배관공"),
            experience = 8,
            hourlyWage = 22000,
            dailyWage = "일당 20만원", 
            rating = 4.9f,
            completedProjects = 103,
            introduction = "다년간의 경험으로 신속 정확한 작업 보장합니다. 전기·배관 작업의 전문가입니다.",
            isAvailable = false,
            distance = 2.5
        ),
        BaseWorker(
            id = "worker_004",
            name = "정수진",
            primaryJobType = "도장공",
            secondaryJobTypes = emptyList(),
            experience = 2,
            hourlyWage = 15000,
            dailyWage = "협의 가능",
            rating = 4.3f,
            completedProjects = 15,
            introduction = "꼼꼼한 작업으로 만족도 높은 결과물을 제공합니다. 도장 작업 전문입니다.",
            distance = 1.8
        ),
        BaseWorker(
            id = "worker_005",
            name = "최영호",
            primaryJobType = "조적공", 
            secondaryJobTypes = listOf("미장공"),
            experience = 10,
            hourlyWage = 20000,
            dailyWage = "일당 22만원",
            rating = 4.7f,
            completedProjects = 156,
            introduction = "20년 경력의 베테랑입니다. 조적과 미장 작업의 숙련된 전문가입니다.",
            distance = 3.2
        ),
        BaseWorker(
            id = "worker_006",
            name = "최수진",
            primaryJobType = "목수",
            secondaryJobTypes = emptyList(),
            experience = 4,
            hourlyWage = 19000,
            dailyWage = "일당 17만원",
            rating = 4.6f,
            completedProjects = 34,
            introduction = "정밀한 목공 작업을 전문으로 합니다.",
            distance = 1.5
        ),
        BaseWorker(
            id = "worker_007",
            name = "정대수",
            primaryJobType = "용접공",
            secondaryJobTypes = emptyList(),
            experience = 6,
            hourlyWage = 21000,
            dailyWage = "일당 19만원",
            rating = 4.7f,
            completedProjects = 67,
            introduction = "안전한 용접 작업을 보장합니다.",
            distance = 2.1
        ),
        BaseWorker(
            id = "worker_008",
            name = "송기원",
            primaryJobType = "보통인부",
            secondaryJobTypes = emptyList(),
            experience = 1,
            hourlyWage = 16000,
            dailyWage = "일당 14만원",
            rating = 4.2f,
            completedProjects = 8,
            introduction = "성실히 작업하겠습니다.",
            distance = 0.9
        )
    )

    // ==================== 기본 프로젝트 데이터 ====================
    
    private val baseProjects = listOf(
        BaseProject(
            id = "project_001",
            title = "강남구 아파트 신축공사",
            company = "대한건설(주)",
            location = "서울시 강남구 역삼동",
            category = "아파트 신축",
            status = "RECRUITING",
            startDate = "2025-08-08",
            endDate = "2025-09-20",
            wage = 200000,
            currentApplicants = 8,
            maxApplicants = 15,
            isUrgent = true
        ),
        BaseProject(
            id = "project_002", 
            title = "인천 물류센터 건설공사",
            company = "현대건설",
            location = "인천 연수구",
            category = "물류센터",
            status = "IN_PROGRESS",
            startDate = "2025-08-10",
            endDate = "2025-10-25",
            wage = 180000,
            currentApplicants = 12,
            maxApplicants = 12
        ),
        BaseProject(
            id = "project_003",
            title = "부산 교량 보수공사", 
            company = "태영건설",
            location = "부산 해운대구",
            category = "교량 보수",
            status = "COMPLETED",
            startDate = "2025-07-15",
            endDate = "2025-08-30",
            wage = 220000,
            currentApplicants = 20,
            maxApplicants = 20
        ),
        BaseProject(
            id = "project_004",
            title = "사하구 낙동5블럭 온도측정센터 신축공사",
            company = "삼성건설",
            location = "부산 사하구",
            category = "특수시설",
            status = "RECRUITING",
            startDate = "2025-09-01",
            endDate = "2025-11-15",
            wage = 250000,
            currentApplicants = 5,
            maxApplicants = 25,
            isUrgent = true
        ),
        BaseProject(
            id = "project_005",
            title = "대전 공장 건설공사",
            company = "롯데건설",
            location = "대전 유성구",
            category = "공장 건설",
            status = "PLANNING",
            startDate = "2025-09-15",
            endDate = "2025-12-20",
            wage = 190000,
            currentApplicants = 0,
            maxApplicants = 30
        ),
        BaseProject(
            id = "project_006",
            title = "광주 지하철 연장공사",
            company = "두산건설",
            location = "광주 광산구",
            category = "지하철 공사",
            status = "RECRUITING",
            startDate = "2025-10-01",
            endDate = "2025-12-30",
            wage = 230000,
            currentApplicants = 3,
            maxApplicants = 20,
            isUrgent = true,
            isBookmarked = false
        )
    )

    // ==================== 회사 정보 데이터 ====================
    
    fun getCompanyData(): CompanyData {
        return CompanyData(
            id = "company_001",
            name = "김직공건설",
            type = CompanyType.PREMIUM,
            status = CompanyStatus.ACTIVE,
            statusText = "기업회원 • 활성 사용자",
            monthlySavings = 3540000L,
            previousMonthGrowth = 28,
            targetAchievementRate = 112,
            savedWorkersCount = 32,
            notifications = NotificationInfo(
                unreadCount = 3,
                totalCount = 15
            ),
            stats = CompanyStats(
                automatedDocs = StatItem(
                    icon = "📄",
                    label = "서류 자동화",
                    value = 312,
                    unit = "건",
                    trendText = "100%"
                ),
                matchedWorkers = StatItem(
                    icon = "👷",
                    label = "매칭 인력",
                    value = 156,
                    unit = "명",
                    trendText = "98.5%"
                ),
                completedProjects = StatItem(
                    icon = "✅",
                    label = "완료 프로젝트",
                    value = 23,
                    unit = "개",
                    trendText = "100%"
                ),
                activeConstructionSites = StatItem(
                    icon = "🏗️",
                    label = "시공 현장",
                    value = 8,
                    unit = "곳",
                    isActive = true,
                    trendText = "활성"
                )
            )
        )
    }

    // ==================== 스카우트 데이터 ====================
    
    fun getScoutWorkers(): List<Worker> {
        return baseWorkers.map { baseWorker ->
            Worker(
                id = baseWorker.id,
                name = baseWorker.name,
                jobTypes = listOf(baseWorker.primaryJobType) + baseWorker.secondaryJobTypes,
                experience = baseWorker.experience,
                distance = baseWorker.distance,
                rating = baseWorker.rating,
                introduction = baseWorker.introduction,
                desiredWage = baseWorker.dailyWage,
                isAvailable = baseWorker.isAvailable,
                completedProjects = baseWorker.completedProjects
            )
        }
    }

    fun getScoutProposals(): List<Proposal> {
        return listOf(
            Proposal(
                id = "proposal_001",
                workerId = "worker_001",
                workerName = "김철수",
                proposedWage = "일당 20만원",
                message = "프로젝트에 꼭 필요한 인력입니다. 철근 작업 경험이 풍부합니다.",
                status = ProposalStatus.PENDING,
                createdAt = LocalDateTime.now().minusHours(2),
                respondedAt = null,
                jobTypes = listOf("철근공"),
                distance = "0.8km",
                workerPhone = null,
                rejectReason = null
            ),
            Proposal(
                id = "proposal_002",
                workerId = "worker_002",
                workerName = "이영희",
                proposedWage = "일당 18만원",
                message = "타일 작업 전문가로 깔끔한 시공이 가능합니다.",
                status = ProposalStatus.ACCEPTED,
                createdAt = LocalDateTime.now().minusDays(1),
                respondedAt = LocalDateTime.now().minusHours(3),
                jobTypes = listOf("타일공"),
                distance = "1.2km",
                workerPhone = "010-1234-5678",
                rejectReason = null
            ),
            Proposal(
                id = "proposal_003",
                workerId = "worker_003",
                workerName = "박민수",
                proposedWage = "일당 22만원",
                message = "전기 배관 작업을 함께 진행할 수 있습니다.",
                status = ProposalStatus.REJECTED,
                createdAt = LocalDateTime.now().minusDays(2),
                respondedAt = LocalDateTime.now().minusDays(1),
                jobTypes = listOf("전기공"),
                distance = "2.5km", 
                workerPhone = null,
                rejectReason = "일정이 맞지 않습니다"
            )
        )
    }

    // ==================== 프로젝트 목록 데이터 ====================
    
    fun getSimpleProjects(): List<SimpleProject> {
        return baseProjects.map { baseProject ->
            SimpleProject(
                id = baseProject.id,
                title = baseProject.title,
                company = baseProject.company,
                location = baseProject.location,
                category = baseProject.category,
                status = baseProject.status,
                startDate = baseProject.startDate,
                endDate = baseProject.endDate,
                wage = baseProject.wage,
                currentApplicants = baseProject.currentApplicants,
                maxApplicants = baseProject.maxApplicants,
                isUrgent = baseProject.isUrgent,
                isBookmarked = baseProject.isBookmarked
            )
        }
    }

    // ==================== 임금 관리 데이터 ====================
    
    fun getProjectPaymentSummary(): ProjectPaymentSummary {
        return ProjectPaymentSummary(
            totalProjects = 5,
            completedPayments = 3,
            pendingPayments = 2,
            totalAmount = 15750000L,
            paidAmount = 9450000L,
            pendingAmount = 6300000L,
            overdueCount = 0
        )
    }

    fun getEmptyProjectPaymentSummary(): ProjectPaymentSummary {
        return ProjectPaymentSummary(
            totalProjects = 0,
            completedPayments = 0,
            pendingPayments = 0,
            totalAmount = 0L,
            paidAmount = 0L,
            pendingAmount = 0L,
            overdueCount = 0
        )
    }

    fun getProjectPayments(): List<ProjectPaymentData> {
        return listOf(
            // 강남구 아파트 신축공사 프로젝트
            ProjectPaymentData(
                id = "payment_001",
                projectTitle = "강남구 아파트 신축공사",
                projectId = "project_001",
                company = "대한건설(주)",
                location = "서울시 강남구 역삼동",
                workDate = LocalDate.of(2025, 8, 15),
                status = ProjectPaymentStatus.COMPLETED,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_001",
                        workerName = "김철수",
                        jobType = "철근공",
                        hoursWorked = 8.0,
                        hourlyRate = 18000,
                        totalAmount = 144000,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusDays(5)
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_006",
                        workerName = "최수진",
                        jobType = "목수",
                        hoursWorked = 8.0,
                        hourlyRate = 19000,
                        totalAmount = 152000,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusDays(5)
                    )
                ),
                totalAmount = 296000,
                paidAmount = 296000,
                pendingAmount = 0,
                createdAt = LocalDateTime.now().minusDays(10),
                completedAt = LocalDateTime.now().minusDays(5)
            ),
            // 인천 물류센터 건설공사 프로젝트
            ProjectPaymentData(
                id = "payment_002",
                projectTitle = "인천 물류센터 건설공사",
                projectId = "project_002",
                company = "현대건설",
                location = "인천 연수구",
                workDate = LocalDate.of(2025, 8, 20),
                status = ProjectPaymentStatus.PENDING,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_002",
                        workerName = "이영희",
                        jobType = "타일공",
                        hoursWorked = 8.0,
                        hourlyRate = 17000,
                        totalAmount = 136000,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_007",
                        workerName = "정대수", 
                        jobType = "용접공",
                        hoursWorked = 8.0,
                        hourlyRate = 21000,
                        totalAmount = 168000,
                        isPaid = false,
                        paidAt = null
                    )
                ),
                totalAmount = 304000,
                paidAmount = 0,
                pendingAmount = 304000,
                createdAt = LocalDateTime.now().minusDays(3),
                completedAt = null
            ),
            // 부산 교량 보수공사 프로젝트
            ProjectPaymentData(
                id = "payment_003",
                projectTitle = "부산 교량 보수공사",
                projectId = "project_003", 
                company = "태영건설",
                location = "부산 해운대구",
                workDate = LocalDate.of(2025, 8, 18),
                status = ProjectPaymentStatus.COMPLETED,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_003",
                        workerName = "박민수",
                        jobType = "전기공",
                        hoursWorked = 8.0,
                        hourlyRate = 22000,
                        totalAmount = 176000,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusDays(2)
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_005",
                        workerName = "최영호",
                        jobType = "조적공",
                        hoursWorked = 8.0,
                        hourlyRate = 20000,
                        totalAmount = 160000,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusDays(2)
                    )
                ),
                totalAmount = 336000,
                paidAmount = 336000,
                pendingAmount = 0,
                createdAt = LocalDateTime.now().minusDays(7),
                completedAt = LocalDateTime.now().minusDays(2)
            ),
            // 사하구 온도측정센터 신축공사 프로젝트
            ProjectPaymentData(
                id = "payment_004",
                projectTitle = "사하구 낙동5블럭 온도측정센터 신축공사",
                projectId = "project_004",
                company = "삼성건설", 
                location = "부산 사하구",
                workDate = LocalDate.of(2025, 8, 22),
                status = ProjectPaymentStatus.PENDING,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_004",
                        workerName = "정수진",
                        jobType = "도장공",
                        hoursWorked = 8.0,
                        hourlyRate = 15000,
                        totalAmount = 120000,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_008",
                        workerName = "송기원",
                        jobType = "보통인부",
                        hoursWorked = 8.0,
                        hourlyRate = 16000,
                        totalAmount = 128000,
                        isPaid = false,
                        paidAt = null
                    )
                ),
                totalAmount = 248000,
                paidAmount = 0,
                pendingAmount = 248000,
                createdAt = LocalDateTime.now().minusDays(1),
                completedAt = null
            ),
            // 대전 공장 건설공사 프로젝트
            ProjectPaymentData(
                id = "payment_005",
                projectTitle = "대전 공장 건설공사",
                projectId = "project_005",
                company = "롯데건설",
                location = "대전 유성구",
                workDate = LocalDate.of(2025, 8, 25),
                status = ProjectPaymentStatus.OVERDUE,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_001",
                        workerName = "김철수",
                        jobType = "철근공",
                        hoursWorked = 8.0,
                        hourlyRate = 18000,
                        totalAmount = 144000,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker_002",
                        workerName = "이영희", 
                        jobType = "타일공",
                        hoursWorked = 6.0,
                        hourlyRate = 17000,
                        totalAmount = 102000,
                        isPaid = false,
                        paidAt = null
                    )
                ),
                totalAmount = 246000,
                paidAmount = 0,
                pendingAmount = 246000,
                createdAt = LocalDateTime.now().minusDays(8),
                completedAt = null
            )
        )
    }

    // ==================== 유틸리티 함수 ====================
    
    /**
     * 통화 포맷팅 유틸리티
     */
    fun formatCurrency(amount: Long): String {
        return "₩${String.format(Locale.KOREA, "%,d", amount)}"
    }

    /**
     * 작업자 ID로 기본 작업자 정보 조회
     */
    fun getWorkerById(workerId: String): BaseWorker? {
        return baseWorkers.find { it.id == workerId }
    }

    /**
     * 프로젝트 ID로 기본 프로젝트 정보 조회
     */
    fun getProjectById(projectId: String): BaseProject? {
        return baseProjects.find { it.id == projectId }
    }

    /**
     * 직종별 작업자 필터링
     */
    fun getWorkersByJobType(jobType: String): List<BaseWorker> {
        return baseWorkers.filter { 
            it.primaryJobType == jobType || it.secondaryJobTypes.contains(jobType)
        }
    }

    /**
     * 사용 가능한 작업자만 필터링
     */
    fun getAvailableWorkers(): List<BaseWorker> {
        return baseWorkers.filter { it.isAvailable }
    }

    /**
     * 프로젝트 상태별 필터링
     */
    fun getProjectsByStatus(status: String): List<BaseProject> {
        return baseProjects.filter { it.status == status }
    }
}