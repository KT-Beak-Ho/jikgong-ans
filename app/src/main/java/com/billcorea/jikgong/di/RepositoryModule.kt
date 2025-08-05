package com.billcorea.jikgong.di

import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.api.repository.join.JoinRepositoryImpl
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.api.repository.login.LoginRepositoryImpl
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

  single<JoinRepository> { JoinRepositoryImpl(get()) }
  single<LoginRepository> { LoginRepositoryImpl(get()) }

  // 프로젝트 생성 Repository 추가
  single<ProjectCreateRepository> { ProjectCreateRepositoryImpl() }
}