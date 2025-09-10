package com.billcorea.jikgong.modules

import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedViewModel
import com.billcorea.jikgong.presentation.company.main.common.CompanySharedViewModel
import com.billcorea.jikgong.presentation.company.main.info.presentation.viewmodel.CompanyInfoViewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.presentation.viewmodel.ProjectListViewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.viewmodel.ProjectCreateViewModel
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.presentation.worker.login.shared.WorkerLoginViewModel
import com.billcorea.jikgong.utils.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  // Main
  viewModel { MainViewModel() }

  // Worker Auth
  viewModel { WorkerJoinSharedViewModel() }
  viewModel { WorkerLoginViewModel() }

  // Company Auth
  viewModel { CompanyJoinSharedViewModel(get(), get()) }  // JoinRepository, Context 주입
  viewModel { CompanyLoginSharedViewModel(get()) } // LoginRepository 주입

  // Company 공통 (싱글톤으로 관리)
  single { CompanySharedViewModel(get()) }  // CompanyRepository 주입

  // Company Info
  viewModel { CompanyInfoViewModel(get()) }  // CompanySharedViewModel 주입

  // Company ProjectList
  viewModel { ProjectListViewModel(get()) }  // ProjectRepository 주입

  // Company ProjectCreate
  viewModel { ProjectCreateViewModel(get(), get()) }  // ProjectCreateRepository, Context 주입

  // 기타 기존 ViewModels...
}
