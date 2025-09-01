package com.billcorea.jikgong.api.repository.company.main.common

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class CompanyRepositoryImpl : CompanyRepository {

  private val _companyData = MutableStateFlow(CompanyData())

  override fun getCompanyData(): Flow<CompanyData> = _companyData.asStateFlow()

  override suspend fun updateCompanyData(data: CompanyData) {
    _companyData.value = data
  }

  override suspend fun clearNotifications() {
    delay(200)
    _companyData.update { current ->
      current.copy(
        notifications = current.notifications.copy(unreadCount = 0)
      )
    }
  }

  override suspend fun refreshFromServer() {
    delay(1000)
    // 실제로는 RetrofitAPI를 사용하여 서버에서 데이터 가져오기
    _companyData.value = CompanyData()
  }
}