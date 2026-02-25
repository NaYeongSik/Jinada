plugins {
    alias(libs.plugins.jinada.android.library)
    alias(libs.plugins.jinada.android.hilt)
}

android {
    namespace = "com.youngsik.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":domain"))
    implementation(project(":shared"))

    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase.libraries)

    implementation(libs.bundles.location)
    implementation(libs.bundles.serialization)
    implementation(libs.geofire.android.common)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.androidx.datastore.preferences)
}