package com.billcorea.jikgong

import android.app.Application
import com.billcorea.jikgong.modules.networkModule
import com.billcorea.jikgong.modules.repositoryModule
import com.billcorea.jikgong.modules.viewModelModule
import com.kakao.vectormap.KakaoMapSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JikgongApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // KakaoMap SDK 초기화 - 안전한 초기화
        try {
            KakaoMapSdk.init(this, BuildConfig.KAKAO_API)
        } catch (e: UnsatisfiedLinkError) {
            // 에뮬레이터나 아키텍처 불일치로 인한 오류는 무시
            android.util.Log.w("JikgongApplication", "KakaoMap SDK 초기화 실패: ${e.message}")
        } catch (e: Exception) {
            android.util.Log.e("JikgongApplication", "KakaoMap SDK 초기화 오류", e)
        }
        
        startKoin {
            androidLogger()
            androidContext(this@JikgongApplication)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}