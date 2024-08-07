plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.cryptowatcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cryptowatcher"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
 }

dependencies {

    implementation("androidx.navigation:navigation-compose:2.7.7")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("io.mockk:mockk-agent-jvm:1.13.10")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("com.google.truth:truth:1.1.4")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
