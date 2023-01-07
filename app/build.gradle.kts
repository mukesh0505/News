import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdk

    signingConfigs {
        create("release") {
            //TODO Create signingConfig at the time of release
            val tmpFilePath = System.getProperty("user.home") + "/work/_temp/keystore/"
            val allFilesFromDir = File(tmpFilePath).listFiles()

            if (allFilesFromDir != null) {
                val keystoreFile = allFilesFromDir.first()
                keystoreFile.renameTo(file("keystore/production/news-debug-keystore"))
            }
            storeFile = file("../keystore/production/news-debug-keystore")
            storePassword = System.getenv("STORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
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
    lint {
        abortOnError = false
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