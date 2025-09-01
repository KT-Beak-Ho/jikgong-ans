package com.billcorea.jikgong.api.repository.company.main.common

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyData
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
  fun getCompanyData(): Flow<CompanyData>
  suspend fun updateCompanyData(data: CompanyData)
  suspend fun clearNotifications()
  suspend fun refreshFromServer()
}