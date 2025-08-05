package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepositoryImpl
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import org.koin.dsl.module

/**
 * Koin을 사용하는 경우의 Repository Module
 */
val repositoryModule = module {
  // ProjectRepository 싱글톤으로 제공
  single<ProjectRepository> { ProjectRepositoryImpl() }

  // ProjectCreateRepository 제공 (ProjectRepository 주입)
  single<ProjectCreateRepository> { ProjectCreateRepositoryImpl(get()) }
}