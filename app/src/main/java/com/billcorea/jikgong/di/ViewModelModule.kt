package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedViewModel
import com.billcorea.jikgong.presentation.company.main.info.shared.CompanyInfoSharedViewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedViewModel
//import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedViewModel
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  // 기업 ViewModels
  viewModel { CompanyJoinSharedViewModel(get()) }
  viewModel { CompanyLoginSharedViewModel(get()) }
  viewModel { CompanyInfoSharedViewModel() }
  //viewModel { CompanyMoneySharedViewModel() }

  // 프로젝트 관련 ViewModels
  viewModel { ProjectRegistrationSharedViewModel() }

  // 근로자 ViewModels
  viewModel { WorkerJoinSharedViewModel() }
}