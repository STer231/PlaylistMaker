plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildFeatures {
            viewBinding = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
}

dependencies {
    val room_version = "2.6.0"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    implementation ("com.google.code.gson:gson:2.10")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.activity:activity-ktx:1.9.0")
    implementation ("io.insert-koin:koin-android:3.3.0")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
}