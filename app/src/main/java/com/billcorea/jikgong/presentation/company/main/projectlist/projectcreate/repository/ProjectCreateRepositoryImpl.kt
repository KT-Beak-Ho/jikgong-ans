package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository

import com.billcorea.jikgong.presentation.company.main.projectlist.model.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import kotlinx.coroutines.delay

class ProjectCreateRepositoryImpl(
  private val projectRepository: ProjectRepository
) : ProjectCreateRepository {

  override suspend fun createProject(project: Project): Result<Project> {
    return try {
      delay(1000) // 네트워크 시뮬레이션
      val success = projectRepository.createProject(project)
      if (success) {
        Result.success(project)
      } else {
        Result.failure(Exception("프로젝트 생성에 실패했습니다"))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}