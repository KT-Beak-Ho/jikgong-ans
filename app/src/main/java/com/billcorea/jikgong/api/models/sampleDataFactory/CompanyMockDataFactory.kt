package com.billcorea.jikgong.api.models.sampleDataFactory

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ApplicantWorker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyStats
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyType
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ConfirmedWorker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.NotificationInfo
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentSummary
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentSummary
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Proposal
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProposalStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.StatItem
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WageType
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Worker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkerInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

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
        ),
        BaseProject(
            id = "project_007",
            title = "울산 공항 활주로 보수공사",
            company = "현대건설",
            location = "울산 중구",
            category = "공항 시설",
            status = "COMPLETED",
            startDate = "2025-06-01",
            endDate = "2025-07-30",
            wage = 240000,
            currentApplicants = 15,
            maxApplicants = 15,
            isUrgent = false,
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
                completedProjects = baseWorker.completedProjects,
                hourlyWage = baseWorker.hourlyWage,
                dailyWage = baseWorker.dailyWage,
                location = baseWorker.location
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
        val projects = getProjectPayments()
        
        val completedProjects = projects.filter { it.status == ProjectPaymentStatus.COMPLETED }
        val pendingProjects = projects.filter { it.status == ProjectPaymentStatus.PENDING }
        val overdueProjects = projects.filter { it.status == ProjectPaymentStatus.OVERDUE }
        
        val totalAmount = projects.sumOf { it.totalAmount }
        val paidAmount = projects.sumOf { it.paidAmount }
        val pendingAmount = projects.sumOf { it.pendingAmount }
        
        return ProjectPaymentSummary(
            totalProjects = projects.size,
            completedPayments = completedProjects.size,
            pendingPayments = pendingProjects.size,
            totalAmount = totalAmount,
            paidAmount = paidAmount,
            pendingAmount = pendingAmount,
            overdueCount = overdueProjects.size,
            totalSavingsAmount = 5420000L, // 직직직 사용하면서 총 절약한 금액 (예시)
            monthlySavingsAmount = 850000L // 이번달 절약한 금액 (예시)
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
            overdueCount = 0,
            totalSavingsAmount = 0L,
            monthlySavingsAmount = 0L
        )
    }

    // ==================== 동적 은행 및 계좌 정보 생성 ====================
    
    private val bankNames = listOf(
        "국민은행", "신한은행", "우리은행", "하나은행", 
        "농협은행", "기업은행", "카카오뱅크", "토스뱅크"
    )
    
    /**
     * 작업자 ID를 기반으로 일관된 은행/계좌 정보 생성
     */
    private fun generateBankInfo(workerId: String, workerName: String): Pair<String, String> {
        val random = Random(workerId.hashCode())
        val bankName = bankNames[random.nextInt(bankNames.size)]
        
        // 은행별 계좌번호 패턴 생성
        val accountNumber = when (bankName) {
            "국민은행" -> "${random.nextInt(100, 1000)}-${random.nextInt(10, 100)}-${random.nextInt(100000, 1000000)}"
            "신한은행" -> "${random.nextInt(100, 1000)}-${random.nextInt(10, 100)}-${random.nextInt(100000, 1000000)}"
            "우리은행" -> "${random.nextInt(1000, 10000)}-${random.nextInt(100, 1000)}-${random.nextInt(100000, 1000000)}"
            "하나은행" -> "${random.nextInt(100, 1000)}-${random.nextInt(100, 1000)}-${random.nextInt(10000, 100000)}-${random.nextInt(10, 100)}"
            "농협은행" -> "${random.nextInt(100, 1000)}-${random.nextInt(1000, 10000)}-${random.nextInt(10, 100)}-${random.nextInt(10, 100)}"
            "기업은행" -> "${random.nextInt(100, 1000)}-${random.nextInt(100, 1000)}-${random.nextInt(10, 100)}-${random.nextInt(100, 1000)}"
            "카카오뱅크" -> "${random.nextInt(1000, 10000)}-${random.nextInt(10, 100)}-${random.nextInt(100000, 1000000)}"
            "토스뱅크" -> "${random.nextInt(1000, 10000)}-${random.nextInt(100000, 1000000)}"
            else -> "${random.nextInt(100, 1000)}-${random.nextInt(10, 100)}-${random.nextInt(100000, 1000000)}"
        }
        
        return Pair(bankName, accountNumber)
    }
    
    /**
     * WorkerPaymentInfo 생성 헬퍼 함수
     */
    private fun generateWorkerPaymentInfo(
        workerId: String,
        workerName: String,
        jobType: String,
        hoursWorked: Double,
        hourlyRate: Int,
        isPaid: Boolean,
        paidAt: LocalDateTime?
    ): ProjectPaymentData.WorkerPaymentInfo {
        val (bankName, accountNumber) = generateBankInfo(workerId, workerName)
        val totalAmount = (hoursWorked * hourlyRate).toLong()
        
        return ProjectPaymentData.WorkerPaymentInfo(
            workerId = workerId,
            workerName = workerName,
            jobType = jobType,
            hoursWorked = hoursWorked,
            hourlyRate = hourlyRate,
            totalAmount = totalAmount,
            isPaid = isPaid,
            paidAt = paidAt,
            bankName = bankName,
            accountNumber = accountNumber
        )
    }

    fun getProjectPayments(): List<ProjectPaymentData> {
        return listOf(
            // 강남구 아파트 신축공사 프로젝트 - 지급 완료
            run {
                val workers = listOf(
                    generateWorkerPaymentInfo("worker_001", "김철수", "철근공", 8.0, 18000, true, LocalDateTime.now().minusDays(5)),
                    generateWorkerPaymentInfo("worker_006", "최수진", "목수", 8.0, 19000, true, LocalDateTime.now().minusDays(5))
                )
                val totalAmount = workers.sumOf { it.totalAmount }
                
                ProjectPaymentData(
                    id = "payment_001",
                    projectTitle = "강남구 아파트 신축공사",
                    projectId = "project_001",
                    company = "대한건설(주)",
                    location = "서울시 강남구 역삼동",
                    workDate = LocalDate.of(2025, 8, 15),
                    status = ProjectPaymentStatus.COMPLETED,
                    workers = workers,
                    totalAmount = totalAmount,
                    paidAmount = totalAmount,
                    pendingAmount = 0,
                    createdAt = LocalDateTime.now().minusDays(10),
                    completedAt = LocalDateTime.now().minusDays(5)
                )
            },
            // 인천 물류센터 건설공사 프로젝트 - 지급 대기
            run {
                val workers = listOf(
                    generateWorkerPaymentInfo("worker_002", "이영희", "타일공", 8.0, 17000, false, null),
                    generateWorkerPaymentInfo("worker_007", "정대수", "용접공", 8.0, 21000, false, null),
                    generateWorkerPaymentInfo("worker_008", "송기원", "보통인부", 8.0, 16000, false, null)
                )
                val totalAmount = workers.sumOf { it.totalAmount }
                
                ProjectPaymentData(
                    id = "payment_002",
                    projectTitle = "인천 물류센터 건설공사",
                    projectId = "project_002",
                    company = "현대건설",
                    location = "인천 연수구",
                    workDate = LocalDate.of(2025, 8, 20),
                    status = ProjectPaymentStatus.PENDING,
                    workers = workers,
                    totalAmount = totalAmount,
                    paidAmount = 0,
                    pendingAmount = totalAmount,
                    createdAt = LocalDateTime.now().minusDays(3),
                    completedAt = null
                )
            },
            // 부산 교량 보수공사 프로젝트 - 지급 완료
            run {
                val workers = listOf(
                    generateWorkerPaymentInfo("worker_003", "박민수", "전기공", 8.0, 22000, true, LocalDateTime.now().minusDays(2)),
                    generateWorkerPaymentInfo("worker_005", "최영호", "조적공", 8.0, 20000, true, LocalDateTime.now().minusDays(2)),
                    generateWorkerPaymentInfo("worker_001", "김철수", "철근공", 6.0, 18000, true, LocalDateTime.now().minusDays(2))
                )
                val totalAmount = workers.sumOf { it.totalAmount }
                
                ProjectPaymentData(
                    id = "payment_003",
                    projectTitle = "부산 교량 보수공사",
                    projectId = "project_003",
                    company = "태영건설",
                    location = "부산 해운대구",
                    workDate = LocalDate.of(2025, 8, 18),
                    status = ProjectPaymentStatus.COMPLETED,
                    workers = workers,
                    totalAmount = totalAmount,
                    paidAmount = totalAmount,
                    pendingAmount = 0,
                    createdAt = LocalDateTime.now().minusDays(7),
                    completedAt = LocalDateTime.now().minusDays(2)
                )
            },
            // 사하구 온도측정센터 신축공사 프로젝트 - 지급 대기
            run {
                val workers = listOf(
                    generateWorkerPaymentInfo("worker_004", "정수진", "도장공", 8.0, 15000, false, null),
                    generateWorkerPaymentInfo("worker_002", "이영희", "타일공", 7.0, 17000, false, null)
                )
                val totalAmount = workers.sumOf { it.totalAmount }
                
                ProjectPaymentData(
                    id = "payment_004",
                    projectTitle = "사하구 낙동5블럭 온도측정센터 신축공사",
                    projectId = "project_004",
                    company = "삼성건설",
                    location = "부산 사하구",
                    workDate = LocalDate.of(2025, 8, 22),
                    status = ProjectPaymentStatus.PENDING,
                    workers = workers,
                    totalAmount = totalAmount,
                    paidAmount = 0,
                    pendingAmount = totalAmount,
                    createdAt = LocalDateTime.now().minusDays(1),
                    completedAt = null
                )
            },
            // 대전 공장 건설공사 프로젝트 - 연체
            run {
                val workers = listOf(
                    generateWorkerPaymentInfo("worker_006", "최수진", "목수", 8.0, 19000, false, null),
                    generateWorkerPaymentInfo("worker_007", "정대수", "용접공", 8.0, 21000, false, null),
                    generateWorkerPaymentInfo("worker_008", "송기원", "보통인부", 8.0, 16000, false, null)
                )
                val totalAmount = workers.sumOf { it.totalAmount }
                
                ProjectPaymentData(
                    id = "payment_005",
                    projectTitle = "대전 공장 건설공사",
                    projectId = "project_005",
                    company = "롯데건설",
                    location = "대전 유성구",
                    workDate = LocalDate.of(2025, 7, 25),
                    status = ProjectPaymentStatus.OVERDUE,
                    workers = workers,
                    totalAmount = totalAmount,
                    paidAmount = 0,
                    pendingAmount = totalAmount,
                    createdAt = LocalDateTime.now().minusDays(15),
                    completedAt = null
                )
            },
            // 광주 지하철 연장공사 프로젝트 - 지급 완료
            run {
                val workers = listOf(
                    generateWorkerPaymentInfo("worker_003", "박민수", "전기공", 8.0, 22000, true, LocalDateTime.now().minusDays(1)),
                    generateWorkerPaymentInfo("worker_004", "정수진", "도장공", 8.0, 15000, true, LocalDateTime.now().minusDays(1))
                )
                val totalAmount = workers.sumOf { it.totalAmount }
                
                ProjectPaymentData(
                    id = "payment_006",
                    projectTitle = "광주 지하철 연장공사",
                    projectId = "project_006",
                    company = "두산건설",
                    location = "광주 광산구",
                    workDate = LocalDate.of(2025, 8, 25),
                    status = ProjectPaymentStatus.COMPLETED,
                    workers = workers,
                    totalAmount = totalAmount,
                    paidAmount = totalAmount,
                    pendingAmount = 0,
                    createdAt = LocalDateTime.now().minusDays(5),
                    completedAt = LocalDateTime.now().minusDays(1)
                )
            }
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

    // ==================== 확정 근로자 관련 데이터 ====================
    
    // ==================== 날짜별 인원 수 결정 알고리즘 ====================
    
    /**
     * 날짜별 적정 인원 수를 결정하는 알고리즘
     * - 주중(월~목): 4-8명 (기본 작업량)
     * - 금요일: 6-10명 (주 마감으로 인한 증가)
     * - 토요일: 2-5명 (휴일 근무로 감소)
     * - 일요일: 0-3명 (최소 인원 또는 휴무)
     */
    /**
     * 개선된 확정 근로자 수 결정 알고리즘
     * - 날짜별 일관성을 보장하기 위해 동일한 시드 사용
     * - 요일별로 다른 패턴 적용
     * - 주중(월-목): 4~8명
     * - 금요일: 6~10명 (주말 준비로 많음)
     * - 토요일: 2~5명 (적당히 근무)
     * - 일요일: 0~3명 (휴일로 적음)
     */
    private fun calculateOptimalWorkerCount(dateString: String): Int {
        try {
            val date = LocalDate.parse(dateString)
            val dayOfWeek = date.dayOfWeek.value // 1=월요일, 7=일요일
            val dayOfMonth = date.dayOfMonth
            
            // 날짜를 시드로 사용하여 일관된 결과 보장
            val dateHash = dateString.hashCode()
            val random = Random(dateHash)
            
            return when (dayOfWeek) {
                1, 2, 3, 4 -> { // 월-목 (주중)
                    val baseRange = 4..8
                    val baseCount = random.nextInt(baseRange.first, baseRange.last + 1)
                    // 특정 날짜에 추가 인력 (프로젝트 진행도에 따른 증가)
                    if (dayOfMonth % 7 == 0) minOf(10, baseCount + 2) else baseCount
                }
                5 -> { // 금요일
                    val baseRange = 6..10
                    random.nextInt(baseRange.first, baseRange.last + 1)
                }
                6 -> { // 토요일
                    val baseRange = 2..5
                    random.nextInt(baseRange.first, baseRange.last + 1)
                }
                7 -> { // 일요일
                    // 70% 확률로 0명, 30% 확률로 1~3명
                    if (random.nextFloat() < 0.7f) 0 else random.nextInt(1, 4)
                }
                else -> 5 // 기본값
            }
        } catch (e: Exception) {
            return 5 // 파싱 실패 시 기본값
        }
    }
    
    /**
     * 기본 근로자 풀 - 여기서 동적으로 선택
     */
    private val workerPool = listOf(
        ConfirmedWorker(
            "1", "김철수", 30, "남", 5, 95, 24, "010-1234-5678", "철근공",
            "고급", "실외", listOf("철근기능사", "건설기계조종사"), 0.8, 4.8f, "2025-07-28"
        ),
        ConfirmedWorker(
            "2", "이영희", 28, "여", 3, 88, 18, "010-2345-6789", "타일공",
            "중급", "실내", listOf("타일기능사"), 1.2, 4.5f, "2025-07-30"
        ),
        ConfirmedWorker(
            "3", "박민수", 35, "남", 8, 92, 35, "010-3456-7890", "전기공",
            "고급", "혼합", listOf("전기기능사", "전기공사기사"), 2.5, 4.9f, "2025-07-25"
        ),
        ConfirmedWorker(
            "4", "정수연", 25, "여", 2, 85, 12, "010-4567-8901", "도장공",
            "초급", "실내", emptyList(), 1.8, 4.2f, "2025-07-29"
        ),
        ConfirmedWorker(
            "5", "최동현", 42, "남", 12, 98, 48, "010-5678-9012", "조적공",
            "고급", "실외", listOf("조적기능사", "건축기사"), 0.5, 4.9f, "2025-07-26"
        ),
        ConfirmedWorker(
            "6", "한미영", 33, "여", 6, 90, 28, "010-6789-0123", "목수",
            "고급", "혼합", listOf("목재창호제작기능사"), 1.5, 4.7f, "2025-07-27"
        ),
        ConfirmedWorker(
            "7", "장준호", 29, "남", 4, 75, 20, "010-7890-1234", "용접공",
            "중급", "실외", listOf("용접기능사"), 2.1, 4.3f, "2025-07-31"
        ),
        ConfirmedWorker(
            "8", "윤서진", 31, "여", 7, 93, 32, "010-8901-2345", "미장공",
            "고급", "실내", listOf("미장기능사"), 1.3, 4.6f, "2025-07-24"
        ),
        ConfirmedWorker(
            "9", "오민규", 27, "남", 3, 87, 15, "010-9012-3456", "철근공",
            "중급", "실외", listOf("철근기능사"), 0.9, 4.4f, "2025-07-30"
        ),
        ConfirmedWorker(
            "10", "신지혜", 29, "여", 4, 91, 21, "010-0123-4567", "타일공",
            "중급", "실내", listOf("타일기능사"), 1.1, 4.5f, "2025-07-28"
        ),
        ConfirmedWorker(
            "11", "강태현", 38, "남", 9, 89, 38, "010-1357-2468", "전기공",
            "고급", "혼합", listOf("전기기능사", "전기공사산업기사"), 2.3, 4.8f, "2025-07-23"
        ),
        ConfirmedWorker(
            "12", "조미라", 32, "여", 6, 94, 25, "010-2468-1357", "도장공",
            "중급", "실내", listOf("도장기능사"), 1.7, 4.6f, "2025-07-26"
        ),
        ConfirmedWorker(
            "13", "임성호", 34, "남", 7, 96, 30, "010-3691-4725", "조적공",
            "고급", "실외", listOf("조적기능사"), 0.7, 4.7f, "2025-07-25"
        ),
        ConfirmedWorker(
            "14", "황수진", 26, "여", 2, 83, 11, "010-5836-7419", "미장공",
            "초급", "실내", emptyList(), 1.9, 4.1f, "2025-07-31"
        ),
        ConfirmedWorker(
            "15", "노현준", 31, "남", 5, 88, 22, "010-7410-8529", "용접공",
            "중급", "실외", listOf("용접기능사"), 2.0, 4.4f, "2025-07-29"
        ),
        ConfirmedWorker(
            "16", "서예린", 28, "여", 4, 92, 17, "010-9630-1478", "목수",
            "중급", "혼합", listOf("목재창호제작기능사"), 1.4, 4.5f, "2025-07-27"
        ),
        ConfirmedWorker(
            "17", "문재혁", 36, "남", 8, 85, 33, "010-2583-6947", "철근공",
            "고급", "실외", listOf("철근기능사", "건설기계조종사"), 0.6, 4.6f, "2025-07-24"
        ),
        ConfirmedWorker(
            "18", "유소영", 30, "여", 5, 90, 23, "010-1472-5836", "타일공",
            "중급", "실내", listOf("타일기능사"), 1.0, 4.5f, "2025-07-28"
        ),
        ConfirmedWorker(
            "19", "안동수", 33, "남", 6, 87, 27, "010-3695-8147", "전기공",
            "중급", "혼합", listOf("전기기능사"), 2.2, 4.3f, "2025-07-26"
        ),
        ConfirmedWorker(
            "20", "김나연", 29, "여", 4, 89, 19, "010-7418-2963", "도장공",
            "중급", "실내", listOf("도장기능사"), 1.6, 4.4f, "2025-07-30"
        ),
        ConfirmedWorker(
            "21", "정호영", 37, "남", 9, 93, 36, "010-9517-4628", "조적공",
            "고급", "실외", listOf("조적기능사", "건축기사"), 0.4, 4.8f, "2025-07-22"
        ),
        ConfirmedWorker(
            "22", "배성민", 32, "남", 6, 91, 29, "010-1234-9876", "미장공",
            "중급", "실내", listOf("미장기능사"), 1.1, 4.6f, "2025-07-27"
        ),
        ConfirmedWorker(
            "23", "송미경", 27, "여", 3, 86, 16, "010-9876-5432", "목수",
            "초급", "혼합", emptyList(), 1.8, 4.2f, "2025-07-29"
        ),
        ConfirmedWorker(
            "24", "홍준식", 34, "남", 7, 88, 31, "010-5432-1098", "용접공",
            "고급", "실외", listOf("용접기능사", "특수용접기능사"), 1.9, 4.5f, "2025-07-25"
        ),
        ConfirmedWorker(
            "25", "차은지", 26, "여", 2, 84, 13, "010-8765-4321", "도장공",
            "초급", "실내", emptyList(), 2.0, 4.0f, "2025-07-31"
        )
    )
    
    // 캐시된 데이터를 저장할 변수
    private var _confirmedWorkersByDateCache: Map<String, List<ConfirmedWorker>>? = null
    
    /**
     * 날짜별 확정 근로자 데이터 반환 (동적 인원 수 적용, 캐시됨)
     */
    fun getConfirmedWorkersByDate(): Map<String, List<ConfirmedWorker>> {
        // 캐시된 데이터가 있으면 반환
        _confirmedWorkersByDateCache?.let { return it }
        
        println("CompanyMockDataFactory: Generating confirmed workers data (first time)")
        
        val dateRange = listOf(
            "2025-08-01", "2025-08-02", "2025-08-03", "2025-08-04", 
            "2025-08-05", "2025-08-06", "2025-08-07", "2025-08-08",
            "2025-08-09", "2025-08-10", "2025-08-11", "2025-08-12",
            "2025-08-13", "2025-08-14", "2025-08-15", "2025-08-16",
            "2025-08-17", "2025-08-18", "2025-08-19", "2025-08-20"
        )
        
        val result = dateRange.associateWith { date ->
            val workerCount = calculateOptimalWorkerCount(date)
            println("CompanyMockDataFactory: date=$date, workerCount=$workerCount")
            if (workerCount == 0) {
                emptyList()
            } else {
                // 날짜를 시드로 사용하여 일관된 랜덤 선택
                val random = Random(date.hashCode())
                val selectedWorkers = workerPool.shuffled(random).take(workerCount)
                println("CompanyMockDataFactory: selected workers for $date: ${selectedWorkers.map { it.name }}")
                selectedWorkers
            }
        }
        
        // 캐시에 저장
        _confirmedWorkersByDateCache = result
        return result
    }
    
    /**
     * 개선된 지원자 인원 수 결정 알고리즘
     * - 일반적으로 확정 근로자 수의 1.5~3배
     * - 주중에 지원자가 더 많음
     * - 일요일에는 지원자가 적음
     * - 확정자와 동일한 시드 기반으로 일관성 보장
     */
    private fun calculateOptimalApplicantCount(dateString: String): Int {
        try {
            val date = LocalDate.parse(dateString)
            val dayOfWeek = date.dayOfWeek.value // 1=월요일, 7=일요일
            val confirmedCount = calculateOptimalWorkerCount(dateString)
            
            // 확정자와 다른 시드 사용하여 독립적이지만 일관된 결과
            val dateHash = dateString.hashCode()
            val random = Random(dateHash + 1000)
            
            val multiplier = when (dayOfWeek) {
                1, 2, 3, 4 -> random.nextFloat() * (3.0f - 2.0f) + 2.0f // 주중: 2.0~3.0배
                5 -> random.nextFloat() * (3.5f - 2.5f) + 2.5f // 금요일: 2.5~3.5배
                6 -> random.nextFloat() * (2.5f - 1.5f) + 1.5f // 토요일: 1.5~2.5배
                7 -> if (confirmedCount == 0) 0.0f else random.nextFloat() * (2.0f - 1.0f) + 1.0f // 일요일: 1.0~2.0배 또는 0
                else -> 2.0f
            }
            
            val baseCount = (confirmedCount * multiplier).toInt()
            // 추가 변동성 (±2명)
            val variation = random.nextInt(-2, 3)
            return maxOf(0, baseCount + variation)
        } catch (e: Exception) {
            return 3 // 기본값
        }
    }
    
    /**
     * 지원자 풀
     */
    private val applicantPool = listOf(
        ApplicantWorker(
            "1", "홍길동", 28, "남", 3, 85, 15, "010-1111-2222", "철근공",
            "중급", "실외", listOf("철근기능사"), 1.5, 4.3f, "2025-07-20"
        ),
        ApplicantWorker(
            "2", "김영희", 32, "여", 5, 92, 22, "010-2222-3333", "타일공",
            "고급", "실내", listOf("타일기능사"), 0.9, 4.7f, "2025-07-18"
        ),
        ApplicantWorker(
            "3", "박철수", 29, "남", 2, 78, 12, "010-3333-4444", "목수",
            "초급", "혼합", emptyList(), 2.1, 4.0f, "2025-07-22"
        ),
        ApplicantWorker(
            "4", "이민수", 35, "남", 7, 88, 28, "010-4444-5555", "전기공",
            "고급", "혼합", listOf("전기기능사"), 1.8, 4.5f, "2025-07-15"
        ),
        ApplicantWorker(
            "5", "정수현", 27, "여", 4, 95, 18, "010-5555-6666", "도장공",
            "중급", "실내", listOf("도장기능사"), 1.2, 4.8f, "2025-07-19"
        ),
        ApplicantWorker(
            "6", "강민호", 30, "남", 6, 82, 25, "010-6666-7777", "조적공",
            "중급", "실외", listOf("조적기능사"), 1.7, 4.2f, "2025-07-16"
        ),
        ApplicantWorker(
            "7", "송유진", 26, "여", 2, 89, 10, "010-7777-8888", "미장공",
            "초급", "실내", emptyList(), 2.3, 4.4f, "2025-07-21"
        ),
        ApplicantWorker(
            "8", "김태준", 33, "남", 8, 76, 32, "010-8888-9999", "용접공",
            "고급", "실외", listOf("용접기능사", "특수용접기능사"), 1.1, 4.6f, "2025-07-14"
        ),
        ApplicantWorker(
            "9", "이소영", 29, "여", 3, 93, 16, "010-9999-0000", "타일공",
            "중급", "실내", listOf("타일기능사"), 1.4, 4.5f, "2025-07-17"
        ),
        ApplicantWorker(
            "10", "박지훈", 31, "남", 5, 87, 20, "010-0000-1111", "철근공",
            "중급", "실외", listOf("철근기능사"), 1.6, 4.4f, "2025-07-18"
        ),
        ApplicantWorker(
            "11", "전민주", 24, "여", 1, 80, 8, "010-2244-6688", "도장공",
            "초급", "실내", emptyList(), 2.5, 3.8f, "2025-07-23"
        ),
        ApplicantWorker(
            "12", "서준호", 33, "남", 6, 91, 24, "010-3366-9922", "목수",
            "고급", "혼합", listOf("목재창호제작기능사"), 1.3, 4.6f, "2025-07-15"
        ),
        ApplicantWorker(
            "13", "양지은", 29, "여", 4, 86, 18, "010-5577-1100", "미장공",
            "중급", "실내", listOf("미장기능사"), 1.9, 4.3f, "2025-07-19"
        ),
        ApplicantWorker(
            "14", "최하나", 31, "여", 4, 90, 19, "010-1122-3344", "전기공",
            "중급", "혼합", listOf("전기기능사"), 2.0, 4.4f, "2025-07-17"
        ),
        ApplicantWorker(
            "15", "조성민", 27, "남", 3, 83, 14, "010-7788-3344", "조적공",
            "초급", "실외", emptyList(), 1.8, 4.1f, "2025-07-20"
        ),
        ApplicantWorker(
            "16", "한예슬", 30, "여", 5, 94, 21, "010-9900-5566", "타일공",
            "고급", "실내", listOf("타일기능사"), 1.0, 4.7f, "2025-07-16"
        ),
        ApplicantWorker(
            "17", "이동욱", 32, "남", 6, 87, 26, "010-1133-7799", "용접공",
            "중급", "실외", listOf("용접기능사"), 1.7, 4.3f, "2025-07-18"
        ),
        ApplicantWorker(
            "18", "김수빈", 25, "여", 2, 81, 9, "010-4477-2255", "목수",
            "초급", "혼합", emptyList(), 2.2, 3.9f, "2025-07-22"
        ),
        ApplicantWorker(
            "19", "박태준", 28, "남", 4, 89, 17, "010-6611-4477", "철근공",
            "중급", "실외", listOf("철근기능사"), 1.5, 4.4f, "2025-07-19"
        ),
        ApplicantWorker(
            "20", "오다은", 31, "여", 5, 92, 23, "010-8833-6699", "도장공",
            "고급", "실내", listOf("도장기능사"), 1.1, 4.6f, "2025-07-17"
        ),
        ApplicantWorker(
            "21", "신우진", 35, "남", 8, 85, 31, "010-2200-8844", "전기공",
            "고급", "혼합", listOf("전기기능사", "전기공사산업기사"), 1.4, 4.5f, "2025-07-14"
        ),
        ApplicantWorker(
            "22", "임소정", 26, "여", 3, 88, 13, "010-5544-1177", "미장공",
            "초급", "실내", emptyList(), 2.1, 4.2f, "2025-07-21"
        ),
        ApplicantWorker(
            "23", "윤재호", 29, "남", 4, 86, 19, "010-7766-3300", "조적공",
            "중급", "실외", listOf("조적기능사"), 1.6, 4.3f, "2025-07-18"
        ),
        ApplicantWorker(
            "24", "최유리", 33, "여", 7, 93, 28, "010-9988-5522", "목수",
            "고급", "혼합", listOf("목재창호제작기능사"), 0.8, 4.8f, "2025-07-15"
        ),
        ApplicantWorker(
            "25", "구민석", 34, "남", 6, 84, 24, "010-1111-5555", "용접공",
            "중급", "실외", listOf("용접기능사"), 1.9, 4.2f, "2025-07-16"
        ),
        ApplicantWorker(
            "26", "노수아", 25, "여", 2, 91, 11, "010-2222-6666", "타일공",
            "초급", "실내", emptyList(), 2.4, 4.3f, "2025-07-22"
        ),
        ApplicantWorker(
            "27", "마준영", 31, "남", 5, 88, 22, "010-3333-7777", "철근공",
            "중급", "실외", listOf("철근기능사"), 1.3, 4.4f, "2025-07-17"
        ),
        ApplicantWorker(
            "28", "손지연", 28, "여", 3, 86, 17, "010-4444-8888", "도장공",
            "초급", "실내", emptyList(), 2.0, 4.1f, "2025-07-20"
        ),
        ApplicantWorker(
            "29", "유태호", 36, "남", 8, 90, 34, "010-5555-9999", "전기공",
            "고급", "혼합", listOf("전기기능사", "전기공사기사"), 1.2, 4.7f, "2025-07-13"
        ),
        ApplicantWorker(
            "30", "진민지", 27, "여", 4, 87, 19, "010-6666-0000", "미장공",
            "중급", "실내", listOf("미장기능사"), 1.7, 4.3f, "2025-07-18"
        )
    )
    
    /**
     * 날짜별 지원자 데이터 반환 (동적 인원 수 적용)
     */
    // 캐시된 지원자 데이터를 저장할 변수
    private var _applicantWorkersByDateCache: Map<String, List<ApplicantWorker>>? = null
    
    fun getApplicantWorkersByDate(): Map<String, List<ApplicantWorker>> {
        // 캐시된 데이터가 있으면 반환
        _applicantWorkersByDateCache?.let { return it }
        
        println("CompanyMockDataFactory: Generating applicant workers data (first time)")
        
        val dateRange = listOf(
            "2025-08-01", "2025-08-02", "2025-08-03", "2025-08-04", 
            "2025-08-05", "2025-08-06", "2025-08-07", "2025-08-08",
            "2025-08-09", "2025-08-10", "2025-08-11", "2025-08-12",
            "2025-08-13", "2025-08-14", "2025-08-15", "2025-08-16",
            "2025-08-17", "2025-08-18", "2025-08-19", "2025-08-20"
        )
        
        val result = dateRange.associateWith { date ->
            val applicantCount = calculateOptimalApplicantCount(date)
            println("CompanyMockDataFactory: applicant date=$date, count=$applicantCount")
            if (applicantCount == 0) {
                emptyList()
            } else {
                // 날짜를 시드로 사용하여 일관된 랜덤 선택 (확정자와 다른 시드 사용)
                val random = Random(date.hashCode() + 1000)
                val selectedApplicants = applicantPool.shuffled(random).take(applicantCount)
                println("CompanyMockDataFactory: selected applicants for $date: ${selectedApplicants.map { it.name }}")
                selectedApplicants
            }
        }
        
        // 캐시에 저장
        _applicantWorkersByDateCache = result
        return result
    }
    
    /**
     * 캐시 무효화 - 테스트나 데이터 갱신 시 사용
     */
    fun clearCache() {
        println("CompanyMockDataFactory: Clearing all caches")
        _confirmedWorkersByDateCache = null
        _applicantWorkersByDateCache = null
    }
    
    /**
     * 데이터 일관성 테스트 - 날짜별 데이터가 제대로 다른지 확인
     */
    fun testDataConsistency() {
        println("=== CompanyMockDataFactory Data Consistency Test ===")
        val confirmedData = getConfirmedWorkersByDate()
        val applicantData = getApplicantWorkersByDate()
        
        val testDates = listOf("2025-08-01", "2025-08-02", "2025-08-04", "2025-08-07")
        
        testDates.forEach { date ->
            val confirmedWorkers = confirmedData[date] ?: emptyList()
            val applicantWorkers = applicantData[date] ?: emptyList()
            println("Date: $date")
            println("  Confirmed: ${confirmedWorkers.size} workers - ${confirmedWorkers.map { it.name }}")
            println("  Applicants: ${applicantWorkers.size} workers - ${applicantWorkers.map { it.name }}")
        }
        println("============================================")
    }

    // ==================== Payment 관련 샘플 데이터 ====================
    
    fun getSampleWorkerInfos(): List<WorkerInfo> {
        return baseWorkers.map { baseWorker ->
            WorkerInfo(
                id = baseWorker.id,
                name = baseWorker.name,
                phone = "010-${(1000..9999).random()}-${(1000..9999).random()}",
                jobType = baseWorker.primaryJobType,
                experienceLevel = when {
                    baseWorker.experience >= 10 -> "전문"
                    baseWorker.experience >= 5 -> "숙련"
                    else -> "초급"
                }
            )
        }
    }

    fun getSamplePayments(): List<PaymentData> {
        val workers = getSampleWorkerInfos()
        val today = LocalDate.now()

        return listOf(
            PaymentData(
                id = "payment1",
                projectId = "project_001",
                projectTitle = "강남구 아파트 신축공사",
                worker = workers[0],
                workDate = today.minusDays(1),
                startTime = "08:00",
                endTime = "18:00",
                totalHours = 9.0,
                wageType = WageType.DAILY,
                wagePerHour = 16000,
                totalWage = 150000L,
                deductions = 7500L,
                finalAmount = 142500L,
                status = PaymentStatus.PENDING,
                notes = "정상 근무 완료"
            )
        )
    }

    fun getSamplePaymentSummary(): PaymentSummary {
        val payments = getSamplePayments()
        val pending = payments.filter { it.status == PaymentStatus.PENDING }
        val completed = payments.filter { it.status == PaymentStatus.COMPLETED }

        return PaymentSummary(
            totalPayments = payments.size,
            totalAmount = payments.sumOf { it.finalAmount },
            pendingCount = pending.size,
            pendingAmount = pending.sumOf { it.finalAmount },
            completedCount = completed.size,
            completedAmount = completed.sumOf { it.finalAmount },
            monthlyTotal = 15000000L,
            weeklyTotal = 3500000L
        )
    }
}