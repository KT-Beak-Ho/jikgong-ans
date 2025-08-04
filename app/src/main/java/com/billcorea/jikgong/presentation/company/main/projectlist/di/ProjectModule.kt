package com.billcorea.jikgong.presentation.company.main.projectlist.di

import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepository
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProjectModule {

  @Binds
  @Singleton
  abstract fun bindProjectRepository(
    projectRepositoryImpl: ProjectRepositoryImpl
  ): ProjectRepository
}