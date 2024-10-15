plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.autotranstandaloneinspection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.autotranstandaloneinspection"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        repositories {
            //google()
            //mavenCentral()
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
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    tasks.withType<Test>{
        useJUnitPlatform(){
            excludeTags("develop", "restdocs")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.register<Test>("unitTest") {
        group = "verification"
        useJUnitPlatform {
            excludeTags("develop", "context", "restdocs")
        }
    }

    tasks.register<Test>("contextTest") {
        group = "verification"
        useJUnitPlatform {
            includeTags("context")
        }
    }

    tasks.register<Test>("restDocsTest") {
        group = "verification"
        useJUnitPlatform {
            includeTags("restdocs")
        }
    }
}


dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.compose.ui:ui:1.6.0-beta02")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling:1.6.0-beta02")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0-rc01")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0-beta02")
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.activity:activity-compose:1.5.1")

    //hitt - dependency injection
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    kapt("com.google.dagger:hilt-compiler:2.48")

    //hilt - local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptTest("com.google.dagger:hilt-compiler:2.48")

    //hilt - instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.48")

    //navigation
    implementation( "androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.navigation:navigation-runtime-ktx:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    //CSV Reader
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Retrofit
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //viewModelScope
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")

    //android schedulers
    implementation ("io.reactivex.rxjava2:rxjava:2.2.9")
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.1")

    //mockito
    // testImplementation 'android.arch.core:core-testing:1.1.1'
    testImplementation ("org.mockito:mockito-inline:5.2.0")

    //Testing
    testImplementation ("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    androidTestImplementation ("androidx.test:core-ktx:1.5.0")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")



    //Room DB
    val room_version = "2.6.1"
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")


    //Kotlin extension and room support
    implementation("androidx.room:room-ktx:$room_version")

    //room test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    //camera
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")
    implementation ("com.google.mlkit:barcode-scanning:17.3.0")

    //permissions library
    implementation ("androidx.activity:activity-ktx:1.8.2")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
    //implementation ("com.google.android.libraries.maps:maps:3.3.0")
    //implementation ("com.github.galcyurio:want-to-apply-dark-mode-plugin:1.0.0-alpha01")

    // Permissions Library
    implementation ("com.google.accompanist:accompanist-permissions:0.23.1")

    //camera
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation("io.coil-kt:coil-compose:1.4.0")

    //implementation("com.github.bumptech.glide:glide:4.12.0")
    //ksp("com.github.bumptech.glide:ksp:4.12.0")

    //Credemtial manager
    implementation("androidx.credentials:credentials:<latest version>")
    implementation("androidx.credentials:credentials-play-services-auth:<latest version>")
    //implementation("com.google.android.libraries.identity.googleid:googleid:<latest version>")

    //material 3
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-beta03")

    //aditional
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.compose.foundation:foundation:1.7.3")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")

    implementation("com.mikepenz:iconics-core:5.3.0")
}
