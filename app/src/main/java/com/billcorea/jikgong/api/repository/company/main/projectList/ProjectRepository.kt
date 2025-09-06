package com.billcorea.jikgong.api.repository.company.main.projectList

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project

interface ProjectRepository {
  suspend fun getProjects(): List<Project>
  suspend fun getProjectById(projectId: String): Project?
  suspend fun toggleBookmark(projectId: String): Boolean
  suspend fun createProject(project: Project): Boolean
  suspend fun updateProject(project: Project): Boolean
  suspend fun deleteProject(projectId: String): Boolean
}