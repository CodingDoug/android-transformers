plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("com.hyperaware.transformer.plugin")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.hyperaware.transformers"
        minSdkVersion(16)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(group = "androidx.appcompat", name = "appcompat", version = "1.2.0")
    implementation(group = "androidx.constraintlayout", name = "constraintlayout", version = "2.0.4")

    implementation(group = "com.hyperaware.transformer", name = "transformer-module", version = "0.1")

    implementation(group = "commons-io", name = "commons-io", version = "2.8.0")
}

// The MyExtension extension added by the transform can be configured here.
//
//transform {
//    logVisits = true
//    logInstrumentation = true
//}
