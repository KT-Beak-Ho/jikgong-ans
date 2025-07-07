package com.billcorea.jikgong.di

import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.api.repository.join.JoinRepositoryImpl
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.api.repository.login.LoginRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

  single<JoinRepository> {  JoinRepositoryImpl(get()) }
  single<LoginRepository> {  LoginRepositoryImpl(get()) }
}