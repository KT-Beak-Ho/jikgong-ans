package com.billcorea.jikgong.utils

import android.app.Application
import com.billcorea.jikgong.BuildConfig
import com.billcorea.jikgong.di.networkModule
import com.billcorea.jikgong.di.repositoryModule
import com.billcorea.jikgong.di.viewModelModule
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JikGongApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JikGongApp)
            modules(
                appModule,
                //  추가
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API)
        KakaoSdk.init(this, BuildConfig.KAKAO_API)

    }
}