
package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedViewModel
import com.billcorea.jikgong.presentation.company.main.info.shared.CompanyInfoSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  // 기존 ViewModels
  viewModel { CompanyJoinSharedViewModel(get()) }
  viewModel { CompanyLoginSharedViewModel(get()) }

  viewModel { CompanyInfoSharedViewModel() }
  viewModel { CompanyMoneySharedViewModel() }
}