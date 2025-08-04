package com.billcorea.jikgong.presentation.company.main.projectlist.data

import java.time.LocalDateTime

data class Project(
  val id: String,
  val title: String,
  val company: String,
  val location: String,
  val category: String,
  val status: String,
  val startDate: LocalDateTime,
  val endDate: LocalDateTime,
  val wage: Int,
  val description: String,
  val requirements: List<String> = emptyList(),
  val benefits: List<String> = emptyList(),
  val currentApplicants: Int,
  val maxApplicants: Int,
  val isBookmarked: Boolean,
  val isUrgent: Boolean = false,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)
