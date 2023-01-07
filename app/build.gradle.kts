import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

val productionKeystoreProperties = Properties().apply {
    load(
        FileInputStream(
            File(
                rootProject.rootDir,
                "keystore/production/keystore.properties"
            )
        )
    )
}

val debugKeystoreProperties = Properties().apply {
    load(
        FileInputStream(
            File(
                rootProject.rootDir,
                "keystore/debug/keystore.properties"
            )
        )
    )
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdk

    signingConfigs {
        getByName("debug") {
            storeFile = file("../keystore/debug/news-debug-keystore")
            storePassword = debugKeystoreProperties.getProperty("STORE_PASSWORD")
            keyAlias = debugKeystoreProperties.getProperty("KEY_ALIAS")
            keyPassword = debugKeystoreProperties.getProperty("KEY_PASSWORD")
        }
        create("release") {
            //TODO Create signingConfig at the time of release
            storeFile = file("../keystore/production/news-debug-keystore")
            storePassword = productionKeystoreProperties.getProperty("STORE_PASSWORD")
            keyAlias = productionKeystoreProperties.getProperty("KEY_ALIAS")
            keyPassword = productionKeystoreProperties.getProperty("KEY_PASSWORD")
        }
    }

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(Libs.coreKtx)
    implementation(Libs.lifeCycleRuntimeKtx)
    implementation(Libs.activityCompose)
    implementation(Libs.composeUi)
    implementation(Libs.composeUiToolPreview)
    implementation(Libs.composeMaterial)
    testImplementation(Libs.jUnit)
    androidTestImplementation(Libs.androidTestExtJunit)
    androidTestImplementation(Libs.espresso)
    androidTestImplementation(Libs.composeUiJunitTest)
    debugImplementation(Libs.composeUiTooling)
    debugImplementation(Libs.composeUiManifest)

    implementation(Libs.hiltAndroid)
    kapt(Libs.hiltCompiler)
    implementation(Libs.androidXViewModelKtx)
}