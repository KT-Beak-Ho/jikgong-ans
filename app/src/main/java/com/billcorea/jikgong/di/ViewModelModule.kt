package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.viewmodel.ProjectCreateViewModel
// 기존 ViewModel imports...
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  // 기존 ViewModel들...

  // 프로젝트 생성 ViewModel 추가
  viewModel { ProjectCreateViewModel(get()) }
}