// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
    id("io.sentry.android.gradle") version "3.12.0" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.4.20" apply false
    id("androidx.room") version "2.6.0" apply false
    id("androidx.navigation.safeargs") version "2.5.3" apply false
}