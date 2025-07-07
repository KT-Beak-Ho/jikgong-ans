package com.billcorea.jikgong.di

import com.billcorea.jikgong.api.repository.JoinRepository
import com.billcorea.jikgong.api.repository.JoinRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

  single<JoinRepository> {
    JoinRepositoryImpl(get())
  }
}