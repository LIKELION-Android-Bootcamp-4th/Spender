import org.gradle.kotlin.dsl.implementation
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
}

android {
    namespace = "com.example.spender"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.spender"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "NAVER_OCR_CLIENT_SECRET",
            localProperties.getProperty("NAVER_OCR_CLIENT_SECRET")
        )

        buildConfigField(
            "String",
            "DEFAULT_WEB_CLIENT_ID",
            localProperties.getProperty("DEFAULT_WEB_CLIENT_ID")
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            localProperties.getProperty("NAVER_CLIENT_ID")
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            localProperties.getProperty("NAVER_CLIENT_SECRET")
        )

        buildConfigField(
            "String",
            "KAKAO_APP_KEY",
            localProperties.getProperty("KAKAO_APP_KEY")
        )

        buildConfigField(
            "String",
            "KAKAO_REDIRECT_URL",
            localProperties.getProperty("KAKAO_REDIRECT_URL")
        )

        manifestPlaceholders["KAKAO_REDIRECT_SCHEME"] = localProperties.getProperty("KAKAO_REDIRECT_URL")?.replace("\"", "") ?: ""

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.transport.api)
    implementation(libs.transport.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //jetpack compose
    implementation(libs.compose.nav)
    implementation(libs.coroutines.core)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.lifecycle.viewmodel.ktx)

    // mp android chart
    implementation(libs.mpandroidchart)

    implementation(libs.androidx.material.icons.extended)

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-functions-ktx:21.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-base:18.3.0")
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("com.navercorp.nid:oauth:5.10.0")
    implementation ("com.kakao.sdk:v2-all:2.20.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.12.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:1.1.1")
    // For interop APIs with Material 3
    implementation("androidx.glance:glance-material3:1.1.1")
    // For interop APIs with Material 2
    implementation("androidx.glance:glance-material:1.1.1")

    // coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    //Google AdMob
    implementation("com.google.android.gms:play-services-ads:23.1.0")
}