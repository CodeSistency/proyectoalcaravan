plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")

    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.proyectoalcaravan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectoalcaravan"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

//    java {
//        toolchain {
//            languageVersion.set(JavaLanguageVersion.of(8))
//
//        }
//    }





}

dependencies {

    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    //Room
    val room_version = "2.5.0"


    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
//    kapt("androidx.room:room-compiler:$room_version")

    ksp("androidx.room:room-compiler:2.5.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Jetpack
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation ("androidx.compose.ui:ui")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation ("androidx.compose.foundation:foundation")
    implementation ("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose:compose-bom:2022.10.00")
    implementation ("androidx.compose.runtime:runtime")
    implementation ("androidx.compose.ui:ui-tooling")
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

    //QRS
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.4.1")

    //Neumorphism
    implementation ("io.github.sridhar-sp:neumorphic:0.0.6")


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:okhttp:4.3.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.3.1")

    //Fragment
    implementation ("androidx.fragment:fragment-ktx:1.5.3")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.5.3")


    //Image Picker
    implementation ("com.github.dhaval2404:imagepicker:2.1")

    // CameraX
    implementation ("androidx.camera:camera-camera2:1.0.2")
    implementation ("androidx.camera:camera-lifecycle:1.0.2")
    implementation ("androidx.camera:camera-view:1.0.0-alpha31")

    //Scanner
    implementation ("com.google.mlkit:barcode-scanning:17.0.3")

    // Zxing
    implementation ("com.google.zxing:core:3.3.3")
    //QRS
    implementation("com.simonsickle:composed-barcodes:1.1.1")

    //Charts
    implementation ("co.yml:ycharts:2.1.0")

    //liveData
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.databinding:databinding-runtime:8.1.2")

    //Google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")

    //Glide
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")

    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.google.accompanist:accompanist-coil:0.8.0")

    //MPAandroidCharts
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Firebase
//   implementation platform("com.google.firebase:firebase-bom:32.5.0")

    platform("com.google.firebase:firebase-bom:32.5.0")

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.5.1")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation ("com.google.firebase:firebase-messaging-ktx:23.3.1")


    implementation("com.valentinilk.shimmer:compose-shimmer:1.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}