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

        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField("String", "KAKAO_REST_API", '"' + properties["KAKAO_REST_API"].toString() + '"')
            buildConfigField("String", "KAKAO_API",  '"' + properties["KAKAO_API"].toString() + '"')
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "KAKAO_REST_API", '"' + properties["KAKAO_REST_API"].toString() + '"')
            buildConfigField("String", "KAKAO_API",  '"' + properties["KAKAO_API"].toString() + '"')
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
}

dependencies {

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.location)
    implementation(libs.androidx.media3.common.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // material icons
    implementation (libs.androidx.material.icons.core)
    implementation (libs.androidx.material.icons.extended)

    // navigation
    implementation(libs.androidx.navigation.compose)
    // raamcosta compose destination
    implementation(libs.raamcosta.core)
    ksp(libs.raamcosta.ksp)

    // date picker ref ...
    // https://github.com/commandiron/WheelPickerCompose
    implementation (libs.snapper)

    // bottomsheetdialog
    implementation (libs.bottomsheetdialog.compose)

    // material dialogs :  이 diaglog 는 res/values 의 colors, themes 의 영향을 받음.
    // https://github.com/afollestad/material-dialogs/tree/main
    implementation (libs.dialogs.core)
    implementation (libs.dialogs.lifecycle)

    // rest api (retrofit / json)
    // retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // webView
    implementation (libs.accompanist.webview)

    // kakao map
    implementation(libs.kakao.android)
    implementation(libs.kakao.v2.navi)

    //권한획득
    implementation(libs.google.accompanist.permissions)

    // glide
    implementation (libs.glide)
    ksp (libs.glide.compiler)

}