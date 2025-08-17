package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.main.common.repository.CompanyRepository
import com.billcorea.jikgong.presentation.company.main.common.repository.CompanyRepositoryImpl
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
  // Company 공통
  single<CompanyRepository> { CompanyRepositoryImpl() }

  // ProjectList
  single<ProjectRepository> { ProjectRepositoryImpl() }

  // ProjectCreate - ProjectRepository 주입
  single<ProjectCreateRepository> {
    ProjectCreateRepositoryImpl(get())  // get()으로 ProjectRepository 자동 주입
  }
}
