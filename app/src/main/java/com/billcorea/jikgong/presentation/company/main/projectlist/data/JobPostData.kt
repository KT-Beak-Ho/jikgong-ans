package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDate
import java.time.LocalDateTime

data class ExistingJob(
    val id: String,
    val title: String,
    val workPeriod: String,
    val dailyWage: Int
)

data class PreviousJobPost(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val wage: Int,
    val workPeriod: String,
    val maxWorkers: Int,
    val completedDate: LocalDate,
    val totalApplicants: Int
)

data class TempSavePost(
    val id: String,
    val title: String,
    val saveDate: LocalDateTime
)