package com.billcorea.jikgong.presentation.company.main.common.repository

import com.billcorea.jikgong.network.CompanyData
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
  fun getCompanyData(): Flow<CompanyData>
  suspend fun updateCompanyData(data: CompanyData)
  suspend fun clearNotifications()
  suspend fun refreshFromServer()
}