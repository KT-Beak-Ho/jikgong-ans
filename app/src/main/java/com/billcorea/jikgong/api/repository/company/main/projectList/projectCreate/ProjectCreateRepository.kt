package com.billcorea.jikgong.api.repository.company.main.projectList.projectCreate

import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project

interface ProjectCreateRepository {
  suspend fun createProject(project: Project): Result<Project>
}
