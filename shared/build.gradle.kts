plugins {
    alias(libs.plugins.jinada.android.library)
    alias(libs.plugins.jinada.android.compose)
}

android {
    namespace = "com.youngsik.shared"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
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
}

dependencies {
    implementation(libs.bundles.location)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.viewbinding)
}