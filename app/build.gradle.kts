plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

android {
    namespace = "tech.devbashar.interactivetaskmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "tech.devbashar.interactivetaskmanager"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "tech.devbashar.interactivetaskmanager.HiltTestRunner"
    }

    buildTypes {
        release {
            signingConfig  = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt)
    implementation(libs.hilt.viewmodel)
    androidTestImplementation(libs.hilt.viewmodel)
    androidTestImplementation(libs.hilt.android.test)
    kaptAndroidTest(libs.hilt.android.compiler)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.dataStore)
    implementation(libs.colorPicker)
    implementation(libs.materialKolor)
    kapt(libs.hilt.kapt)
    implementation(libs.foundation.pager)
    implementation(libs.feather.icon)
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
}

kapt {
    correctErrorTypes = true
}
