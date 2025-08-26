package com.billcorea.jikgong.network.model.payment

import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * Payment 샘플 데이터 생성기
 * 개발 및 테스트용
 */
object PaymentSampleData {

  private val workerNames = listOf(
    "김철수", "이영희", "박민수", "정수연", "최준혁",
    "강지영", "조민준", "윤서연", "장현우", "임수진"
  )

  private val jobTypes = listOf(
    "철근공", "타일공", "도배공", "전기공", "배관공",
    "목공", "미장공", "방수공", "용접공", "조적공"
  )

  private val projectNames = listOf(
    "아파트 신축공사 A동", "오피스텔 리모델링", "단독주택 인테리어",
    "상가 전기 공사", "아파트 배관 교체", "빌라 신축 공사",
    "공장 증축 공사", "학교 리모델링", "병원 인테리어", "호텔 신축"
  )

  private val experienceLevels = listOf("초급", "중급", "고급", "전문가")

  /**
   * 샘플 PaymentUIData 리스트 생성
   */
  fun getSamplePayments(count: Int = 20): List<PaymentUIData> {
    return List(count) { index ->
      createSamplePayment(index)
    }
  }

  /**
   * 샘플 PaymentUIData 생성
   */
  private fun createSamplePayment(index: Int): PaymentUIData {
    val random = Random(index)
    val workDate = LocalDate.now().minusDays(random.nextLong(0, 30))
    val status = PaymentUIStatus.values()[random.nextInt(PaymentUIStatus.values().size)]
    val workerName = workerNames[random.nextInt(workerNames.size)]
    val jobType = jobTypes[random.nextInt(jobTypes.size)]
    val projectName = projectNames[random.nextInt(projectNames.size)]
    val experienceLevel = experienceLevels[random.nextInt(experienceLevels.size)]

    val hourlyWage = (20000 + random.nextInt(15) * 1000)
    val workHours = 8.0 + random.nextDouble() * 2
    val totalWage = (hourlyWage * workHours).toLong()
    val deductions = (totalWage * 0.1).toLong()
    val finalAmount = totalWage - deductions

    return PaymentUIData(
      id = "payment_${index + 1}",
      projectId = "project_${random.nextInt(1, 10)}",
      projectTitle = projectName,
      worker = WorkerUIInfo(
        id = "worker_${index + 1}",
        name = workerName,
        phone = "010-${random.nextInt(1000, 9999)}-${random.nextInt(1000, 9999)}",
        jobType = jobType,
        experienceLevel = experienceLevel,
        profileImageUrl = null
      ),
      workDate = workDate,
      startTime = "${8 + random.nextInt(2)}:00",
      endTime = "${17 + random.nextInt(3)}:00",
      totalHours = workHours,
      wageType = WageUIType.values()[random.nextInt(WageUIType.values().size)],
      wagePerHour = hourlyWage,
      totalWage = totalWage,
      deductions = deductions,
      finalAmount = finalAmount,
      status = status,
      paymentDate = if (status == PaymentUIStatus.COMPLETED) {
        LocalDateTime.now().minusDays(random.nextLong(0, 10))
      } else null,
      createdAt = LocalDateTime.now().minusDays(random.nextLong(0, 30)),
      updatedAt = LocalDateTime.now().minusDays(random.nextLong(0, 5)),
      notes = if (random.nextBoolean()) "${jobType} 작업 완료" else ""
    )
  }

  /**
   * 샘플 PaymentUISummary 생성
   */
  fun getSamplePaymentSummary(): PaymentUISummary {
    val payments = getSamplePayments(100)

    return PaymentUISummary(
      totalPayments = payments.size,
      totalAmount = payments.sumOf { it.finalAmount },
      pendingCount = payments.count { it.status == PaymentUIStatus.PENDING },
      pendingAmount = payments.filter { it.status == PaymentUIStatus.PENDING }
        .sumOf { it.finalAmount },
      completedCount = payments.count { it.status == PaymentUIStatus.COMPLETED },
      completedAmount = payments.filter { it.status == PaymentUIStatus.COMPLETED }
        .sumOf { it.finalAmount },
      overdueCount = payments.count { it.status == PaymentUIStatus.OVERDUE },
      overdueAmount = payments.filter { it.status == PaymentUIStatus.OVERDUE }
        .sumOf { it.finalAmount },
      monthlyTotal = payments.filter {
        it.workDate.month == LocalDate.now().month
      }.sumOf { it.finalAmount },
      weeklyTotal = payments.filter {
        it.workDate.isAfter(LocalDate.now().minusWeeks(1))
      }.sumOf { it.finalAmount },
      totalPendingAmount = payments.filter { it.status == PaymentUIStatus.PENDING }
        .sumOf { it.finalAmount },
      totalCompletedAmount = payments.filter { it.status == PaymentUIStatus.COMPLETED }
        .sumOf { it.finalAmount },
      thisMonthAmount = payments.filter {
        it.workDate.month == LocalDate.now().month
      }.sumOf { it.finalAmount }
    )
  }

  /**
   * 네트워크용 샘플 PaymentData 생성
   */
  fun getSampleNetworkPayments(count: Int = 10): List<PaymentData> {
    return List(count) { index ->
      createSampleNetworkPayment(index)
    }
  }

  /**
   * 샘플 PaymentData 생성 (네트워크 모델)
   */
  private fun createSampleNetworkPayment(index: Int): PaymentData {
    val random = Random(index)
    val baseAmount = (150000L + random.nextLong(100000L))
    val deductionTotal = (baseAmount * 0.1).toLong()

    return PaymentData(
      id = "payment_${index + 1}",
      projectId = "project_${random.nextInt(1, 10)}",
      workerId = "worker_${index + 1}",
      companyId = "company_001",
      baseAmount = baseAmount,
      workHours = 8.0 + random.nextDouble() * 2,
      overtimeHours = if (random.nextBoolean()) random.nextDouble() * 2 else 0.0,
      deductions = PaymentDeductions(
        incomeTax = (deductionTotal * 0.3).toLong(),
        nationalPension = (deductionTotal * 0.2).toLong(),
        healthInsurance = (deductionTotal * 0.25).toLong(),
        employmentInsurance = (deductionTotal * 0.15).toLong(),
        other = (deductionTotal * 0.1).toLong(),
        total = deductionTotal
      ),
      bonuses = PaymentBonuses(
        performance = if (random.nextBoolean()) 50000L else 0L,
        overtime = if (random.nextBoolean()) 30000L else 0L,
        weekend = 0L,
        night = 0L,
        special = 0L,
        total = 80000L
      ),
      finalAmount = baseAmount - deductionTotal + 80000L,
      paymentMethod = PaymentMethod.values()[random.nextInt(PaymentMethod.values().size)],
      paymentStatus = PaymentStatus.values()[random.nextInt(PaymentStatus.values().size)],
      scheduledDate = LocalDate.now().plusDays(random.nextLong(-30, 30)).toString(),
      actualPaymentDate = if (random.nextBoolean()) {
        LocalDateTime.now().minusDays(random.nextLong(0, 10)).toString()
      } else null,
      bankTransferInfo = if (random.nextBoolean()) {
        BankTransferInfo(
          bankName = listOf("국민은행", "신한은행", "우리은행", "하나은행")[random.nextInt(4)],
          accountNumber = "123-456-${random.nextInt(100000, 999999)}",
          accountHolder = workerNames[random.nextInt(workerNames.size)],
          transactionId = "TX${System.currentTimeMillis()}"
        )
      } else null,
      invoice = null,
      notes = if (random.nextBoolean()) "정상 처리" else null,
      createdAt = LocalDateTime.now().minusDays(random.nextLong(0, 30)).toString(),
      updatedAt = LocalDateTime.now().toString()
    )
  }
}