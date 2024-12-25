plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.zerocarbon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.zerocarbon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.play.services.auth)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.firebase.auth)
    implementation(libs.prolificinteractive.material.calendarview)
    implementation(libs.mpandroidchart)
    implementation(libs.opencsv)
    testImplementation(libs.mockito)
    implementation(libs.play.services.auth)
    implementation(libs.volley)
    implementation(libs.media3.common)
    implementation(libs.stripe.android)
    implementation(libs.core)
    implementation(libs.volley)
    implementation(libs.media3.common)
    implementation(libs.play.services.auth)
    testImplementation(libs.mockito)
    implementation(libs.gson)
    implementation(libs.generativeai)
    implementation(libs.guava)
    implementation(libs.reactive.streams)
}
