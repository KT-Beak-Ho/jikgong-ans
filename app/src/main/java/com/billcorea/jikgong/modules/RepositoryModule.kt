package com.billcorea.jikgong.modules

import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.api.repository.join.JoinRepositoryImpl
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.api.repository.login.LoginRepositoryImpl
import com.billcorea.jikgong.api.repository.company.main.common.CompanyRepository
import com.billcorea.jikgong.api.repository.company.main.common.CompanyRepositoryImpl
import com.billcorea.jikgong.api.repository.company.main.projectList.ProjectRepository
import com.billcorea.jikgong.api.repository.company.main.projectList.ProjectRepositoryImpl
import com.billcorea.jikgong.api.repository.company.main.projectList.projectCreate.ProjectCreateRepository
import com.billcorea.jikgong.api.repository.company.main.projectList.projectCreate.ProjectCreateRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
  // Auth
  single<JoinRepository> { JoinRepositoryImpl(get()) }  // JoinApi 주입
  single<LoginRepository> { LoginRepositoryImpl(get()) } // AuthApi 주입

  // Company 공통
  single<CompanyRepository> { CompanyRepositoryImpl() }

  // ProjectList
  single<ProjectRepository> { ProjectRepositoryImpl() }

  // ProjectCreate - ProjectRepository 주입
  single<ProjectCreateRepository> {
    ProjectCreateRepositoryImpl(get())  // get()으로 ProjectRepository 자동 주입
  }
}
