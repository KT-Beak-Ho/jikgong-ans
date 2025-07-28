package com.billcorea.jikgong.di

import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.api.repository.join.JoinRepositoryImpl
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.api.repository.login.LoginRepositoryImpl
// import com.billcorea.jikgong.api.repository.project.ProjectRegistrationRepository
// import com.billcorea.jikgong.api.repository.project.ProjectRegistrationRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

  single<JoinRepository> { JoinRepositoryImpl(get()) }
  single<LoginRepository> { LoginRepositoryImpl(get()) }

  // 프로젝트 관련 Repository (향후 구현 시 주석 해제)
  // single<ProjectRegistrationRepository> { ProjectRegistrationRepositoryImpl(get()) }
}