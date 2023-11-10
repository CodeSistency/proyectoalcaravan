buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.8" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
//    id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
}