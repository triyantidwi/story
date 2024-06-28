plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.dicoding.storyappsubmission"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.storyappsubmission"
        minSdk = 24
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
        }

        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
    }

    dependencies {
        implementation("androidx.paging:paging-runtime-ktx:3.1.1")

        implementation("androidx.datastore:datastore-preferences:1.0.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
        implementation("androidx.activity:activity-ktx:1.9.0")

        implementation("com.github.bumptech.glide:glide:4.15.0")

        implementation("androidx.core:core-ktx:1.9.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.android.gms:play-services-maps:18.2.0")

        implementation("androidx.room:room-runtime:2.5.0")
        implementation("androidx.room:room-ktx:2.5.0")
        ksp("androidx.room:room-compiler:2.5.0")
        implementation ("androidx.room:room-paging:2.5.0")

        testImplementation("junit:junit:4.13.2")
        testImplementation("androidx.core:core-testing:1.13.1")
        testImplementation("androidx.arch.core:core-testing:2.1.0")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
        testImplementation("org.mockito:mockito-core:3.11.2")
        testImplementation("org.mockito:mockito-inline:3.11.2")

        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.core:core-testing:1.13.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
        androidTestImplementation("org.mockito:mockito-android:3.11.2")
    }
}