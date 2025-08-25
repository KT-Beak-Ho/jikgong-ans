package com.billcorea.jikgong.network.interceptor

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import com.billcorea.jikgong.network.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * 공통 헤더 인터셉터
 * 모든 API 요청에 공통 헤더를 추가
 */
class HeaderInterceptor(
  private val context: Context
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()

    // 앱 버전 정보 가져오기
    val appVersion = getAppVersion()

    // 디바이스 ID 가져오기 (UUID 사용)
    val deviceId = getDeviceId()

    // User-Agent 생성
    val userAgent = createUserAgent(appVersion)

    // 공통 헤더 추가
    val modifiedRequest = originalRequest.newBuilder()
      .header(NetworkConfig.HEADER_CONTENT_TYPE, NetworkConfig.CONTENT_TYPE_JSON)
      .header(NetworkConfig.HEADER_ACCEPT, NetworkConfig.CONTENT_TYPE_JSON)
      .header(NetworkConfig.HEADER_USER_AGENT, userAgent)
      .header(NetworkConfig.HEADER_DEVICE_ID, deviceId)
      .header(NetworkConfig.HEADER_APP_VERSION, appVersion)
      .header(NetworkConfig.HEADER_PLATFORM, NetworkConfig.PLATFORM_ANDROID)
      .header("Accept-Language", Locale.getDefault().language)
      .build()

    return chain.proceed(modifiedRequest)
  }

  /**
   * 앱 버전 정보 가져오기
   */
  private fun getAppVersion(): String {
    return try {
      val packageInfo: PackageInfo = context.packageManager
        .getPackageInfo(context.packageName, 0)
      packageInfo.versionName ?: "1.0.0"
    } catch (e: Exception) {
      "1.0.0"
    }
  }

  /**
   * 디바이스 ID 가져오기
   */
  private fun getDeviceId(): String {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var deviceId = sharedPreferences.getString(NetworkConfig.PREF_DEVICE_ID, null)

    if (deviceId == null) {
      deviceId = UUID.randomUUID().toString()
      sharedPreferences.edit()
        .putString(NetworkConfig.PREF_DEVICE_ID, deviceId)
        .apply()
    }

    return deviceId
  }

  /**
   * User-Agent 문자열 생성
   */
  private fun createUserAgent(appVersion: String): String {
    val deviceModel = Build.MODEL
    val osVersion = Build.VERSION.RELEASE
    val sdkVersion = Build.VERSION.SDK_INT

    return "Jikgong/$appVersion (Android $osVersion; SDK $sdkVersion; $deviceModel)"
  }
}