// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //id("com.google.gms.google-services") version "4.4.2" apply false

    // KSP 버전을 Kotlin 2.0.21에 맞게 수정
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}

buildscript {
    // 필요한 경우 여기에 buildscript 의존성 추가
}