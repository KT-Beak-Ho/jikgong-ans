package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project
import kotlinx.coroutines.delay

class ProjectCreateRepositoryImpl : ProjectCreateRepository {

  override suspend fun createProject(project: Project): Result<Project> {
    return try {
      // API 호출 시뮬레이션
      delay(1500)

      // 성공 시뮬레이션
      Result.success(project.copy(id = System.currentTimeMillis().toString()))
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}