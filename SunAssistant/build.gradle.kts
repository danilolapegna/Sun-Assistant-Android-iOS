plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
}

android {

    lint {
        abortOnError = false
        quiet = true
    }
    namespace = "com.sunassistant"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.sunassistant"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "1.3.12"

        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.3"
        }
        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            debug {
                applicationIdSuffix = ".debug"
                isDebuggable = true
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.7.4")
    implementation("androidx.compose.ui:ui-tooling:1.7.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.4")
    implementation("androidx.compose.foundation:foundation:1.7.4")
    implementation("androidx.compose.material:material:1.7.4")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.4")
}
