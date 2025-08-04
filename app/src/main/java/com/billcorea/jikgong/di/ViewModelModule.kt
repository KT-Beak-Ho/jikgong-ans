package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import com.billcorea.jikgong.presentation.company.main.projectlist.viewmodel.ProjectListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

  // Repository
  single<ProjectRepository> { ProjectRepositoryImpl() }

  // ProjectListViewModel만 등록
  viewModel { ProjectListViewModel(get()) }

  // 다른 ViewModel들은 나중에 필요할 때 추가
}