package com.billcorea.jikgong.presentation.company.main.money.data

import java.time.LocalDate
import java.time.LocalDateTime

object PaymentSampleData {

    fun getSampleWorkers(): List<WorkerInfo> {
        return listOf(
            WorkerInfo(
                id = "worker1",
                name = "김철수",
                phone = "010-1234-5678",
                jobType = "보통인부",
                experienceLevel = "숙련"
            ),
            WorkerInfo(
                id = "worker2",
                name = "이영희",
                phone = "010-2345-6789",
                jobType = "철근공",
                experienceLevel = "전문"
            ),
            WorkerInfo(
                id = "worker3",
                name = "박민수",
                phone = "010-3456-7890",
                jobType = "작업반장",
                experienceLevel = "전문"
            ),
            WorkerInfo(
                id = "worker4",
                name = "최수진",
                phone = "010-4567-8901",
                jobType = "콘크리트공",
                experienceLevel = "숙련"
            )
        )
    }

    fun getSamplePayments(): List<PaymentData> {
        val workers = getSampleWorkers()
        val today = LocalDate.now()

        return listOf(
            PaymentData(
                id = "payment1",
                projectId = "project1",
                projectTitle = "강남구 아파트 신축 공사",
                worker = workers[0],
                workDate = today.minusDays(1),
                startTime = "08:00",
                endTime = "18:00",
                totalHours = 9.0,
                wageType = WageType.DAILY,
                wagePerHour = 16000,
                totalWage = 150000L,
                deductions = 7500L, // 5% 수수료
                finalAmount = 142500L,
                status = PaymentStatus.PENDING,
                notes = "정상 근무 완료"
            ),
            PaymentData(
                id = "payment2",
                projectId = "project1",
                projectTitle = "강남구 아파트 신축 공사",
                worker = workers[1],
                workDate = today.minusDays(1),
                startTime = "08:00",
                endTime = "17:00",
                totalHours = 8.0,
                wageType = WageType.DAILY,
                wagePerHour = 20000,
                totalWage = 180000L,
                deductions = 9000L,
                finalAmount = 171000L,
                status = PaymentStatus.PROCESSING,
                paymentDate = LocalDateTime.now().minusHours(2)
            ),
            PaymentData(
                id = "payment3",
                projectId = "project2",
                projectTitle = "인천 물류센터 건설",
                worker = workers[2],
                workDate = today.minusDays(2),
                startTime = "07:30",
                endTime = "17:30",
                totalHours = 9.0,
                wageType = WageType.DAILY,
                wagePerHour = 22000,
                totalWage = 200000L,
                deductions = 10000L,
                finalAmount = 190000L,
                status = PaymentStatus.COMPLETED,
                paymentDate = LocalDateTime.now().minusDays(1)
            ),
            PaymentData(
                id = "payment4",
                projectId = "project1",
                projectTitle = "강남구 아파트 신축 공사",
                worker = workers[3],
                workDate = today.minusDays(2),
                startTime = "08:00",
                endTime = "16:00",
                totalHours = 7.0,
                wageType = WageType.DAILY,
                wagePerHour = 18000,
                totalWage = 170000L,
                deductions = 8500L,
                finalAmount = 161500L,
                status = PaymentStatus.COMPLETED,
                paymentDate = LocalDateTime.now().minusDays(1)
            ),
            PaymentData(
                id = "payment5",
                projectId = "project3",
                projectTitle = "부산 교량 보수 공사",
                worker = workers[0],
                workDate = today.minusDays(3),
                startTime = "08:30",
                endTime = "17:00",
                totalHours = 7.5,
                wageType = WageType.DAILY,
                wagePerHour = 16000,
                totalWage = 150000L,
                deductions = 7500L,
                finalAmount = 142500L,
                status = PaymentStatus.FAILED,
                notes = "계좌 정보 오류로 재처리 필요"
            ),
            PaymentData(
                id = "payment6",
                projectId = "project1",
                projectTitle = "강남구 아파트 신축 공사",
                worker = workers[1],
                workDate = today.minusDays(3),
                startTime = "08:00",
                endTime = "18:00",
                totalHours = 9.0,
                wageType = WageType.DAILY,
                wagePerHour = 20000,
                totalWage = 180000L,
                deductions = 9000L,
                finalAmount = 171000L,
                status = PaymentStatus.PENDING
            ),
            PaymentData(
                id = "payment7",
                projectId = "project2",
                projectTitle = "인천 물류센터 건설",
                worker = workers[2],
                workDate = today.minusDays(4),
                startTime = "08:00",
                endTime = "17:00",
                totalHours = 8.0,
                wageType = WageType.DAILY,
                wagePerHour = 22000,
                totalWage = 200000L,
                deductions = 10000L,
                finalAmount = 190000L,
                status = PaymentStatus.COMPLETED,
                paymentDate = LocalDateTime.now().minusDays(3)
            ),
            PaymentData(
                id = "payment8",
                projectId = "project1",
                projectTitle = "강남구 아파트 신축 공사",
                worker = workers[3],
                workDate = today.minusDays(4),
                startTime = "08:00",
                endTime = "16:30",
                totalHours = 7.5,
                wageType = WageType.DAILY,
                wagePerHour = 18000,
                totalWage = 170000L,
                deductions = 8500L,
                finalAmount = 161500L,
                status = PaymentStatus.PROCESSING
            ),
            PaymentData(
                id = "payment9",
                projectId = "project3",
                projectTitle = "부산 교량 보수 공사",
                worker = workers[0],
                workDate = today.minusDays(5),
                startTime = "08:00",
                endTime = "17:30",
                totalHours = 8.5,
                wageType = WageType.DAILY,
                wagePerHour = 16000,
                totalWage = 150000L,
                deductions = 7500L,
                finalAmount = 142500L,
                status = PaymentStatus.COMPLETED,
                paymentDate = LocalDateTime.now().minusDays(4)
            ),
            PaymentData(
                id = "payment10",
                projectId = "project2",
                projectTitle = "인천 물류센터 건설",
                worker = workers[1],
                workDate = today.minusDays(5),
                startTime = "07:30",
                endTime = "17:30",
                totalHours = 9.0,
                wageType = WageType.DAILY,
                wagePerHour = 20000,
                totalWage = 180000L,
                deductions = 9000L,
                finalAmount = 171000L,
                status = PaymentStatus.PENDING
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