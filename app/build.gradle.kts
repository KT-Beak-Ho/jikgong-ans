import org.jetbrains.kotlin.konan.properties.Properties
val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.billcorea.jikgong"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.billcorea.jikgong"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // NDK ABI 필터 수정 - 모든 주요 아키텍처 지원
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64", "x86", "armeabi-v7a")
        }

        // 만약 NDK를 사용하지 않는다면 위의 ndk 블록을 완전히 삭제하세요
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField("String", "KAKAO_REST_API", '"' + properties["KAKAO_REST_API"].toString() + '"')
            buildConfigField("String", "KAKAO_API",  '"' + properties["KAKAO_API"].toString() + '"')
            //  Base Url 생성
            buildConfigField("String", "BASE_URL",  '"' + properties["BASE_URL"].toString() + '"')
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "KAKAO_REST_API", '"' + properties["KAKAO_REST_API"].toString() + '"')
            buildConfigField("String", "KAKAO_API",  '"' + properties["KAKAO_API"].toString() + '"')
            //  Base Url 생성
            buildConfigField("String", "BASE_URL",  '"' + properties["BASE_URL"].toString() + '"')
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Compose 컴파일러 버전 명시 (Kotlin 2.0.21과 호환)
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    // ===== 네트워크 관련 의존성 (정리됨) =====
    // Retrofit (이미 OkHttp와 Gson 포함)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp Logging Interceptor만 추가 (디버깅용)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 중복 제거 - Retrofit에 이미 포함됨
    // implementation("com.squareup.okhttp3:okhttp:4.12.0") // 제거
    // implementation("com.google.code.gson:gson:2.10.1") // 제거

    // ===== AndroidX Core =====
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ===== Compose =====
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.runtime.livedata)

    // ===== Location Services =====
    implementation(libs.play.services.location)

    // ===== Media =====
    implementation(libs.androidx.media3.common.ktx)

    // ===== Testing =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ===== Dependency Injection =====
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // ===== UI Components =====
    // Material Icons
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    // ===== Navigation =====
    implementation(libs.androidx.navigation.compose)

    // Raamcosta Compose Destinations
    implementation(libs.raamcosta.core)
    ksp(libs.raamcosta.ksp)

    // ===== UI Libraries =====
    // Date Picker
    implementation(libs.snapper)

    // Bottom Sheet Dialog
    implementation(libs.bottomsheetdialog.compose)

    // Material Dialogs
    implementation(libs.dialogs.core)
    implementation(libs.dialogs.lifecycle)

    // WebView
    implementation(libs.accompanist.webview)

    // ===== Maps & Navigation =====
    // Kakao Map
    implementation(libs.kakao.android)
    implementation(libs.kakao.v2.navi)

    // ===== Permissions =====
    implementation(libs.google.accompanist.permissions)
    
    // ===== Image Loading =====
    // Glide
    // glide
    implementation (libs.glide)
    ksp (libs.glide.compiler)

    // ===== DataStore =====
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}