import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val baseApiUrl: String = System.getenv("API_URL") ?: gradleLocalProperties(
    rootDir
).getProperty("API_URL")

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.devfutech.souvenir"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String", "BASE_URL", "\"${baseApiUrl}\""
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.32")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.4")

    //Livedata
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.33-beta")
    kapt("com.google.dagger:hilt-android-compiler:2.33-beta")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("androidx.hilt:hilt-compiler:1.0.0-beta01")

    // Retrofit + GSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-beta03")

    // Room
    implementation("androidx.room:room-runtime:2.3.0-rc01")
    implementation("androidx.room:room-ktx:2.3.0-rc01")
    kapt("androidx.room:room-compiler:2.3.0-rc01")

    //Google API
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")

    //COIL Image Load
    implementation("io.coil-kt:coil:1.1.1")

    //Shimmer Facebook
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //Maps
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")

    //Chuck
    debugImplementation("com.github.chuckerteam.chucker:library:3.4.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.4.0")

    //HTTP Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //Hyperion Check
    debugImplementation("com.willowtreeapps.hyperion:hyperion-core:0.9.31")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-build-config:0.9.31")
    debugImplementation("com.willowtreeapps.hyperion:hyperion-phoenix:0.9.31")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}