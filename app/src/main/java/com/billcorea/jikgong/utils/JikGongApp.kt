package com.billcorea.jikgong.utils

import android.app.Application
import com.billcorea.jikgong.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JikGongApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JikGongApp)
            modules(appModule)
        }
        KakaoMapSdk.init(this,BuildConfig.KAKAO_API)
        KakaoSdk.init(this, BuildConfig.KAKAO_API)
    }
}