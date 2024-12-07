plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.planetze"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planetze"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        viewBinding = true
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
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation(libs.mpandroidchart)
    implementation(libs.opencsv)
    testImplementation(libs.mockito)
    implementation(libs.play.services.auth)
    implementation(libs.volley)
    implementation(libs.media3.common)
    implementation("com.stripe:stripe-android:21.2.0")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")
    implementation(libs.volley)
    implementation(libs.media3.common)
    implementation(libs.play.services.auth)
    testImplementation(libs.mockito)
    implementation("com.stripe:stripe-android:21.2.0")
    implementation ("com.google.code.gson:gson:2.8.8")

}
