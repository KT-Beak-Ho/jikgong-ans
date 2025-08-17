package com.billcorea.jikgong.data.model

import androidx.annotation.DrawableRes

data class Project(
  val id: String,
  val title: String,
  val location: String,
  val distance: String,
  val date: String,
  val time: String,
  val salary: String,
  val tags: List<String> = emptyList(),
  val applicants: Int = 0,
  @DrawableRes val imageRes: Int? = null,
  val isBookmarked: Boolean = false,
  val isUrgent: Boolean = false,
  val companyName: String = "",
  val projectType: ProjectType = ProjectType.CONSTRUCTION
)

enum class ProjectType {
  CONSTRUCTION, INTERIOR, LANDSCAPING, DEMOLITION, OTHER
}