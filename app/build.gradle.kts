plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.marcel.eatbymark"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.marcel.eatbymark"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }
    kotlinOptions {
        jvmTarget = "20"
    }
    kotlin {
        jvmToolchain(20) // MockK requires this
    }
}

dependencies {
    // -- APP -- //
    // Android
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    // Jetpack compose
    implementation(platform(libs.compose))
    implementation(libs.compose.activity)
    implementation(libs.compose.material3)
    implementation(libs.compose.paging)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)
    // Crash Report
    implementation(libs.acra.mail.sender)
    // Coroutines
    implementation(libs.android.coroutines)
    // Database
    implementation(libs.room)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    // Dependency Injection
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    // Images
    implementation(libs.coil)
    // Navigation
    implementation(libs.navigation.compose)
    // Network
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.moshi.converter)
    implementation(libs.okhttp.logger)
    implementation(libs.retrofit)

    // -- TESTING --
    // Android
    androidTestImplementation(libs.androidx.junit)
    // Compose
    //androidTestImplementation(libs.compose.test)
    debugImplementation(libs.compose.test.manifest)
    // Coroutines
    testImplementation(libs.coroutines.test)
    // Espresso
    androidTestImplementation(libs.androidx.espresso.core)
    // Hilt
    testImplementation(libs.hilt.test)
    kspTest(libs.hilt.compiler)
    // JUnit
    testImplementation(libs.junit)
    // MockK
    testImplementation(libs.mockk)

    // Alts
    //implementation(libs.gson)
}