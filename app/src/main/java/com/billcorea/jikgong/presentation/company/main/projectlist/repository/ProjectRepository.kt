// File: ProjectRepository.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.data.*

interface ProjectRepository {

  suspend fun getProjects(): List<Project>

  suspend fun getMoreProjects(currentSize: Int): List<Project>

  suspend fun updateBookmark(projectId: String, isBookmarked: Boolean)

  suspend fun applyToProject(projectId: String)

  suspend fun deleteProject(projectId: String)

  suspend fun createProject(project: Project): Project

  suspend fun updateProject(project: Project): Project

  suspend fun getProjectById(projectId: String): Project?
}

// File: ProjectRepositoryImpl.kt
