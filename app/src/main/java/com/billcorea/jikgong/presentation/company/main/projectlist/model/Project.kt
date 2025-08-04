package com.billcorea.jikgong.presentation.company.main.projectlist.model

import java.time.LocalDateTime

data class Project(
  val id: String,
  val title: String,
  val company: String,
  val location: String,
  val category: String,
  val status: String, // RECRUITING, IN_PROGRESS, COMPLETED
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val wage: Int,
  val description: String,
  val requirements: List<String>,
  val benefits: List<String>,
  val currentApplicants: Int,
  val maxApplicants: Int,
  val isUrgent: Boolean = false,
  val isBookmarked: Boolean = false,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
)