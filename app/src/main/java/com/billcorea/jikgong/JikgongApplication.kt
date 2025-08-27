package com.billcorea.jikgong

import android.app.Application
import com.billcorea.jikgong.di.networkModule
import com.billcorea.jikgong.di.repositoryModule
import com.billcorea.jikgong.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JikgongApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
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