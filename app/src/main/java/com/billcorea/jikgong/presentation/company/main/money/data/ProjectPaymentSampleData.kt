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
                projectId = "proj_001",
                company = "삼성건설",
                location = "부산 사하구",
                workDate = today.minusDays(1),
                status = ProjectPaymentStatus.PENDING,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker1",
                        workerName = "김철수",
                        jobType = "보통인부",
                        hoursWorked = 8.0,
                        hourlyRate = 16250,
                        totalAmount = 130000L,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker2",
                        workerName = "이영희",
                        jobType = "철근공",
                        hoursWorked = 8.0,
                        hourlyRate = 22500,
                        totalAmount = 180000L,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker3",
                        workerName = "박민수",
                        jobType = "작업반장",
                        hoursWorked = 9.0,
                        hourlyRate = 22222,
                        totalAmount = 200000L,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusHours(2)
                    )
                ),
                totalAmount = 510000L,
                paidAmount = 200000L,
                pendingAmount = 310000L,
                createdAt = LocalDateTime.now().minusDays(1)
            ),

            ProjectPaymentData(
                id = "project2",
                projectTitle = "인천 물류센터 건설",
                projectId = "proj_002",
                company = "현대건설",
                location = "인천 연수구",
                workDate = today.minusDays(2),
                status = ProjectPaymentStatus.COMPLETED,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker4",
                        workerName = "최수진",
                        jobType = "콘크리트공",
                        hoursWorked = 8.0,
                        hourlyRate = 21250,
                        totalAmount = 170000L,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusDays(1)
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker5",
                        workerName = "정대수",
                        jobType = "보통인부",
                        hoursWorked = 8.0,
                        hourlyRate = 16250,
                        totalAmount = 130000L,
                        isPaid = true,
                        paidAt = LocalDateTime.now().minusDays(1)
                    )
                ),
                totalAmount = 300000L,
                paidAmount = 300000L,
                pendingAmount = 0L,
                createdAt = LocalDateTime.now().minusDays(2),
                completedAt = LocalDateTime.now().minusDays(1)
            ),

            ProjectPaymentData(
                id = "project3",
                projectTitle = "부산 교량 보수 공사",
                projectId = "proj_003",
                company = "태영건설",
                location = "부산 해운대구",
                workDate = today,
                status = ProjectPaymentStatus.PROCESSING,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker6",
                        workerName = "송기원",
                        jobType = "용접공",
                        hoursWorked = 8.0,
                        hourlyRate = 26250,
                        totalAmount = 210000L,
                        isPaid = false,
                        paidAt = null
                    )
                ),
                totalAmount = 210000L,
                paidAmount = 0L,
                pendingAmount = 210000L,
                createdAt = LocalDateTime.now()
            ),

            ProjectPaymentData(
                id = "project4",
                projectTitle = "강남구 오피스텔 신축공사",
                projectId = "proj_004",
                company = "대한건설(주)",
                location = "서울 강남구",
                workDate = today.minusDays(3),
                status = ProjectPaymentStatus.FAILED,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker7",
                        workerName = "한지민",
                        jobType = "보통인부",
                        hoursWorked = 8.0,
                        hourlyRate = 17500,
                        totalAmount = 140000L,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker8",
                        workerName = "윤성호",
                        jobType = "타일공",
                        hoursWorked = 8.0,
                        hourlyRate = 25000,
                        totalAmount = 200000L,
                        isPaid = false,
                        paidAt = null
                    )
                ),
                totalAmount = 340000L,
                paidAmount = 0L,
                pendingAmount = 340000L,
                createdAt = LocalDateTime.now().minusDays(3)
            ),

            ProjectPaymentData(
                id = "project5",
                projectTitle = "대전 공장 건설",
                projectId = "proj_005",
                company = "롯데건설",
                location = "대전 유성구",
                workDate = today.minusDays(7), // 연체 시나리오
                status = ProjectPaymentStatus.OVERDUE,
                workers = listOf(
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker9",
                        workerName = "조현우",
                        jobType = "보통인부",
                        hoursWorked = 8.0,
                        hourlyRate = 16000,
                        totalAmount = 128000L,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker10",
                        workerName = "김혜진",
                        jobType = "전기공",
                        hoursWorked = 8.0,
                        hourlyRate = 23750,
                        totalAmount = 190000L,
                        isPaid = false,
                        paidAt = null
                    ),
                    ProjectPaymentData.WorkerPaymentInfo(
                        workerId = "worker11",
                        workerName = "서준혁",
                        jobType = "배관공",
                        hoursWorked = 8.0,
                        hourlyRate = 22500,
                        totalAmount = 180000L,
                        isPaid = false,
                        paidAt = null
                    )
                ),
                totalAmount = 498000L,
                paidAmount = 0L,
                pendingAmount = 498000L,
                createdAt = LocalDateTime.now().minusDays(7)
            )
        )
    }

    fun getSampleProjectPaymentSummary(): ProjectPaymentSummary {
        val projects = getSampleProjectPayments()
        val pending = projects.filter { it.status == ProjectPaymentStatus.PENDING }
        val completed = projects.filter { it.status == ProjectPaymentStatus.COMPLETED }
        val overdue = projects.filter { it.status == ProjectPaymentStatus.OVERDUE }

        return ProjectPaymentSummary(
            totalProjects = projects.size,
            completedPayments = completed.size,
            pendingPayments = pending.size,
            totalAmount = projects.sumOf { it.totalAmount },
            paidAmount = projects.sumOf { it.paidAmount },
            pendingAmount = projects.sumOf { it.pendingAmount },
            overdueCount = overdue.size
        )
    }

    // 빈 상태 테스트용 함수
    fun getEmptyProjectPayments(): List<ProjectPaymentData> {
        return emptyList()
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
}