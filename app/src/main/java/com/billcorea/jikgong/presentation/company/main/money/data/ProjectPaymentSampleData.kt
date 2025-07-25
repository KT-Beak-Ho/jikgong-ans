package com.billcorea.jikgong.presentation.company.main.money.data

import java.time.LocalDate
import java.time.LocalDateTime

object ProjectPaymentSampleData {

    fun getSampleProjectPayments(): List<ProjectPaymentData> {
        val today = LocalDate.now()

        return listOf(
            ProjectPaymentData(
                id = "project1",
                projectTitle = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
                projectLocation = "부산 사하구",
                workers = listOf(
                    WorkerPaymentInfo(
                        workerId = "worker1",
                        workerName = "김철수",
                        jobType = "보통인부",
                        workDate = today.minusDays(1),
                        workHours = 8.0,
                        hourlyWage = 16250,
                        totalAmount = 130000L,
                        deductionAmount = 6500L, // 5% 수수료
                        finalAmount = 123500L,
                        status = ProjectPaymentStatus.PENDING
                    ),
                    WorkerPaymentInfo(
                        workerId = "worker2",
                        workerName = "이영희",
                        jobType = "철근공",
                        workDate = today.minusDays(1),
                        workHours = 8.0,
                        hourlyWage = 22500,
                        totalAmount = 180000L,
                        deductionAmount = 9000L,
                        finalAmount = 171000L,
                        status = ProjectPaymentStatus.PENDING
                    ),
                    WorkerPaymentInfo(
                        workerId = "worker3",
                        workerName = "박민수",
                        jobType = "작업반장",
                        workDate = today.minusDays(1),
                        workHours = 9.0,
                        hourlyWage = 22222,
                        totalAmount = 200000L,
                        deductionAmount = 10000L,
                        finalAmount = 190000L,
                        status = ProjectPaymentStatus.COMPLETED,
                        paymentDate = LocalDateTime.now().minusHours(2)
                    )
                ),
                totalAmount = 510000L,
                serviceFeeReduction = 25500L, // 5% 절감 (기존 10% - 현재 5%)
                originalServiceFee = 51000L,  // 10%
                currentServiceFee = 25500L,   // 5%
                totalSavings = 25500L,
                status = ProjectPaymentStatus.PENDING,
                workStartDate = today.minusDays(1),
                workEndDate = today.minusDays(1),
                notes = "1월 05일 09:30 작업 완료"
            ),

            ProjectPaymentData(
                id = "project2",
                projectTitle = "인천 물류센터 건설",
                projectLocation = "인천 연수구",
                workers = listOf(
                    WorkerPaymentInfo(
                        workerId = "worker4",
                        workerName = "최수진",
                        jobType = "콘크리트공",
                        workDate = today.minusDays(2),
                        workHours = 8.0,
                        hourlyWage = 21250,
                        totalAmount = 170000L,
                        deductionAmount = 8500L,
                        finalAmount = 161500L,
                        status = ProjectPaymentStatus.COMPLETED,
                        paymentDate = LocalDateTime.now().minusDays(1)
                    ),
                    WorkerPaymentInfo(
                        workerId = "worker5",
                        workerName = "정대수",
                        jobType = "보통인부",
                        workDate = today.minusDays(2),
                        workHours = 8.0,
                        hourlyWage = 16250,
                        totalAmount = 130000L,
                        deductionAmount = 6500L,
                        finalAmount = 123500L,
                        status = ProjectPaymentStatus.COMPLETED,
                        paymentDate = LocalDateTime.now().minusDays(1)
                    )
                ),
                totalAmount = 300000L,
                serviceFeeReduction = 15000L,
                originalServiceFee = 30000L,
                currentServiceFee = 15000L,
                totalSavings = 15000L,
                status = ProjectPaymentStatus.COMPLETED,
                workStartDate = today.minusDays(2),
                workEndDate = today.minusDays(2),
                notes = "작업 완료"
            ),

            ProjectPaymentData(
                id = "project3",
                projectTitle = "부산 교량 보수 공사",
                projectLocation = "부산 해운대구",
                workers = listOf(
                    WorkerPaymentInfo(
                        workerId = "worker6",
                        workerName = "송기원",
                        jobType = "용접공",
                        workDate = today,
                        workHours = 8.0,
                        hourlyWage = 26250,
                        totalAmount = 210000L,
                        deductionAmount = 10500L,
                        finalAmount = 199500L,
                        status = ProjectPaymentStatus.PROCESSING
                    )
                ),
                totalAmount = 210000L,
                serviceFeeReduction = 10500L,
                originalServiceFee = 21000L,
                currentServiceFee = 10500L,
                totalSavings = 10500L,
                status = ProjectPaymentStatus.PROCESSING,
                workStartDate = today,
                workEndDate = today,
                notes = "진행 중"
            ),

            ProjectPaymentData(
                id = "project4",
                projectTitle = "강남구 오피스텔 신축공사",
                projectLocation = "서울 강남구",
                workers = listOf(
                    WorkerPaymentInfo(
                        workerId = "worker7",
                        workerName = "한지민",
                        jobType = "보통인부",
                        workDate = today.minusDays(3),
                        workHours = 8.0,
                        hourlyWage = 17500,
                        totalAmount = 140000L,
                        deductionAmount = 7000L,
                        finalAmount = 133000L,
                        status = ProjectPaymentStatus.FAILED
                    ),
                    WorkerPaymentInfo(
                        workerId = "worker8",
                        workerName = "윤성호",
                        jobType = "타일공",
                        workDate = today.minusDays(3),
                        workHours = 8.0,
                        hourlyWage = 25000,
                        totalAmount = 200000L,
                        deductionAmount = 10000L,
                        finalAmount = 190000L,
                        status = ProjectPaymentStatus.FAILED
                    )
                ),
                totalAmount = 340000L,
                serviceFeeReduction = 17000L,
                originalServiceFee = 34000L,
                currentServiceFee = 17000L,
                totalSavings = 17000L,
                status = ProjectPaymentStatus.FAILED,
                workStartDate = today.minusDays(3),
                workEndDate = today.minusDays(3),
                notes = "계좌 정보 오류로 재처리 필요"
            ),

            ProjectPaymentData(
                id = "project5",
                projectTitle = "대전 공장 건설",
                projectLocation = "대전 유성구",
                workers = listOf(
                    WorkerPaymentInfo(
                        workerId = "worker9",
                        workerName = "조현우",
                        jobType = "보통인부",
                        workDate = today.minusDays(4),
                        workHours = 8.0,
                        hourlyWage = 16000,
                        totalAmount = 128000L,
                        deductionAmount = 6400L,
                        finalAmount = 121600L,
                        status = ProjectPaymentStatus.COMPLETED,
                        paymentDate = LocalDateTime.now().minusDays(3)
                    ),
                    WorkerPaymentInfo(
                        workerId = "worker10",
                        workerName = "김혜진",
                        jobType = "전기공",
                        workDate = today.minusDays(4),
                        workHours = 8.0,
                        hourlyWage = 23750,
                        totalAmount = 190000L,
                        deductionAmount = 9500L,
                        finalAmount = 180500L,
                        status = ProjectPaymentStatus.COMPLETED,
                        paymentDate = LocalDateTime.now().minusDays(3)
                    ),
                    WorkerPaymentInfo(
                        workerId = "worker11",
                        workerName = "서준혁",
                        jobType = "배관공",
                        workDate = today.minusDays(4),
                        workHours = 8.0,
                        hourlyWage = 22500,
                        totalAmount = 180000L,
                        deductionAmount = 9000L,
                        finalAmount = 171000L,
                        status = ProjectPaymentStatus.COMPLETED,
                        paymentDate = LocalDateTime.now().minusDays(3)
                    )
                ),
                totalAmount = 498000L,
                serviceFeeReduction = 24900L,
                originalServiceFee = 49800L,
                currentServiceFee = 24900L,
                totalSavings = 24900L,
                status = ProjectPaymentStatus.COMPLETED,
                workStartDate = today.minusDays(4),
                workEndDate = today.minusDays(4),
                notes = "모든 작업자 지급 완료"
            )
        )
    }

    fun getSampleProjectPaymentSummary(): ProjectPaymentSummary {
        val projects = getSampleProjectPayments()
        val pending = projects.filter { it.status == ProjectPaymentStatus.PENDING }
        val completed = projects.filter { it.status == ProjectPaymentStatus.COMPLETED }

        return ProjectPaymentSummary(
            totalProjects = projects.size,
            totalAmount = projects.sumOf { it.totalAmount },
            pendingProjects = pending.size,
            pendingAmount = pending.sumOf { it.totalAmount },
            completedProjects = completed.size,
            completedAmount = completed.sumOf { it.totalAmount },
            monthlyTotal = 13293800L,
            totalServiceFeeSavings = projects.sumOf { it.totalSavings },
            averageSavingsPerProject = if (projects.isNotEmpty()) {
                projects.sumOf { it.totalSavings } / projects.size
            } else 0L
        )
    }

    // 빈 상태 테스트용 함수
    fun getEmptyProjectPayments(): List<ProjectPaymentData> {
        return emptyList()
    }

    fun getEmptyProjectPaymentSummary(): ProjectPaymentSummary {
        return ProjectPaymentSummary(
            totalProjects = 0,
            totalAmount = 0L,
            pendingProjects = 0,
            pendingAmount = 0L,
            completedProjects = 0,
            completedAmount = 0L,
            monthlyTotal = 0L,
            totalServiceFeeSavings = 0L,
            averageSavingsPerProject = 0L
        )
    }
}