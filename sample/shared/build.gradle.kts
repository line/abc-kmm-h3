import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

val h3Version: String by project
val h3VersionLib = "com.linecorp:abc-kmm-h3:$h3Version"
version = "0.1.1"

kotlin {
    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64
    iosTarget("ios") {
        binaries
            .filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>()
            .forEach {
                it.transitiveExport = true
                it.export(h3VersionLib)
            }
    }
    android()

    cocoapods {
        ios.deploymentTarget = "9.0"
        homepage = "https://github.com/line/abc-h3-kmm/sample/iosApp"
        summary = "Sample for h3-kmm"
        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(h3VersionLib)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(h3VersionLib)
                api(h3VersionLib)
                api("ch.qos.logback:logback-classic:1.2.3")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("androidx.test:core:1.0.0")
                implementation("androidx.test:runner:1.1.0")
                implementation("org.robolectric:robolectric:4.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(h3VersionLib)
                api(h3VersionLib)
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDir("src/androidMain/res")
    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}