package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project

interface ProjectCreateRepository {
  suspend fun createProject(project: Project): Result<Project>
}
