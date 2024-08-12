plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.kickymaulana.tandatangandigital"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kickymaulana.tandatangandigital"
        minSdk = 24
        targetSdk = 34
        versionCode = 9
        versionName = "1.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.android.networking)
    implementation(libs.android.spinkit)
    implementation(libs.circleimageview)
    implementation(libs.library)
    implementation(libs.pdfviewpager.library)
    implementation(libs.permissionmanager)
    implementation(libs.filepicker)
    implementation(libs.androidfilepicker)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
