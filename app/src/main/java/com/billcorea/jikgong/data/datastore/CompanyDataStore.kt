package com.billcorea.jikgong.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// DataStore 인스턴스 생성
private val Context.companyDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "company_preferences"
)

/**
 * 사업자(Company) 정보를 관리하는 DataStore
 */
class CompanyDataStore(private val context: Context) {
    
    // Preference Keys
    private object Keys {
        val COMPANY_ID = longPreferencesKey("company_id")
        val COMPANY_NAME = stringPreferencesKey("company_name")
        val BUSINESS_NUMBER = stringPreferencesKey("business_number")
        val REPRESENTATIVE_NAME = stringPreferencesKey("representative_name")
        val BUSINESS_TYPE = stringPreferencesKey("business_type")
        val BUSINESS_ADDRESS = stringPreferencesKey("business_address")
        val EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone")
        val LOGIN_ID = stringPreferencesKey("login_id")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val MEMBERSHIP_TYPE = stringPreferencesKey("membership_type")
    }
    
    // 회사 정보 데이터 클래스
    data class CompanyInfo(
        val companyId: Long = 0L,
        val companyName: String = "",
        val businessNumber: String = "",
        val representativeName: String = "",
        val businessType: String = "",
        val businessAddress: String = "",
        val email: String = "",
        val phone: String = "",
        val loginId: String = "",
        val membershipType: String = "BASIC"
    )
    
    // 인증 토큰 데이터 클래스
    data class AuthTokens(
        val accessToken: String = "",
        val refreshToken: String = "",
        val isLoggedIn: Boolean = false
    )
    
    // DataStore 인스턴스
    private val dataStore = context.companyDataStore
    
    // 회사 정보 저장
    suspend fun saveCompanyInfo(companyInfo: CompanyInfo) {
        dataStore.edit { preferences ->
            preferences[Keys.COMPANY_ID] = companyInfo.companyId
            preferences[Keys.COMPANY_NAME] = companyInfo.companyName
            preferences[Keys.BUSINESS_NUMBER] = companyInfo.businessNumber
            preferences[Keys.REPRESENTATIVE_NAME] = companyInfo.representativeName
            preferences[Keys.BUSINESS_TYPE] = companyInfo.businessType
            preferences[Keys.BUSINESS_ADDRESS] = companyInfo.businessAddress
            preferences[Keys.EMAIL] = companyInfo.email
            preferences[Keys.PHONE] = companyInfo.phone
            preferences[Keys.LOGIN_ID] = companyInfo.loginId
            preferences[Keys.MEMBERSHIP_TYPE] = companyInfo.membershipType
        }
    }
    
    // 회사 정보 조회
    val companyInfo: Flow<CompanyInfo> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            CompanyInfo(
                companyId = preferences[Keys.COMPANY_ID] ?: 0L,
                companyName = preferences[Keys.COMPANY_NAME] ?: "",
                businessNumber = preferences[Keys.BUSINESS_NUMBER] ?: "",
                representativeName = preferences[Keys.REPRESENTATIVE_NAME] ?: "",
                businessType = preferences[Keys.BUSINESS_TYPE] ?: "",
                businessAddress = preferences[Keys.BUSINESS_ADDRESS] ?: "",
                email = preferences[Keys.EMAIL] ?: "",
                phone = preferences[Keys.PHONE] ?: "",
                loginId = preferences[Keys.LOGIN_ID] ?: "",
                membershipType = preferences[Keys.MEMBERSHIP_TYPE] ?: "BASIC"
            )
        }
    
    // 회사명만 조회
    val companyName: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[Keys.COMPANY_NAME] ?: ""
        }
    
    // 인증 토큰 저장
    suspend fun saveAuthTokens(tokens: AuthTokens) {
        dataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] = tokens.accessToken
            preferences[Keys.REFRESH_TOKEN] = tokens.refreshToken
            preferences[Keys.IS_LOGGED_IN] = tokens.isLoggedIn
        }
    }
    
    // 인증 토큰 조회
    val authTokens: Flow<AuthTokens> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            AuthTokens(
                accessToken = preferences[Keys.ACCESS_TOKEN] ?: "",
                refreshToken = preferences[Keys.REFRESH_TOKEN] ?: "",
                isLoggedIn = preferences[Keys.IS_LOGGED_IN] ?: false
            )
        }
    
    // 로그인 상태 조회
    val isLoggedIn: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[Keys.IS_LOGGED_IN] ?: false
        }
    
    // 로그아웃 (데이터 초기화)
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] = ""
            preferences[Keys.REFRESH_TOKEN] = ""
            preferences[Keys.IS_LOGGED_IN] = false
        }
    }
    
    // 전체 데이터 삭제
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    // 특정 키의 값 업데이트
    suspend fun updateCompanyName(companyName: String) {
        dataStore.edit { preferences ->
            preferences[Keys.COMPANY_NAME] = companyName
        }
    }
    
    suspend fun updateEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[Keys.EMAIL] = email
        }
    }
    
    suspend fun updatePhone(phone: String) {
        dataStore.edit { preferences ->
            preferences[Keys.PHONE] = phone
        }
    }
    
    suspend fun updateBusinessAddress(address: String) {
        dataStore.edit { preferences ->
            preferences[Keys.BUSINESS_ADDRESS] = address
        }
    }
}