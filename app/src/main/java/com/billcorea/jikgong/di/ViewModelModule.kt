package com.billcorea.jikgong.di

import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import org.koin.dsl.module

val viewModelModule = module {

  // SharedViewModel을 싱글톤으로 등록 (앱 전체에서 공유)
  single {
    CompanyJoinSharedViewModel(get())
  }
}