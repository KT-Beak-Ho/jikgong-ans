package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDateTime

data class Project(
  val id: String,
  val title: String,
  val description: String,
  val location: String,
  val category: ProjectCategory,
  val status: ProjectStatus,
  val dailyWage: Int,
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val requiredWorkers: Int,
  val appliedWorkers: Int,
  val companyName: String,
  val contactNumber: String,
  val requirements: List<String>,
  val benefits: List<String>,
  val isBookmarked: Boolean,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)

