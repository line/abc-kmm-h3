![abc-kmm-h3: A library to convert Uber's H3 geo-index to LatLng vertices for Kotlin Multiplatform Mobile iOS and android](images/cover.png)

[![Kotlin](https://img.shields.io/badge/kotlin-1.5.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![KMM](https://img.shields.io/badge/KMM-0.2.7-lightgreen.svg?logo=KMM)](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
[![AGP](https://img.shields.io/badge/AGP-7.0.1-green.svg?logo=AGP)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-blue.svg?logo=Gradle)](https://gradle.org)
[![Platform](https://img.shields.io/badge/platform-ios,android-lightgray.svg?style=flat)](https://img.shields.io/badge/platform-ios-lightgray.svg?style=flat)

A library to convert Uber's H3 geo-index to LatLng vertices for Kotlin Multiplatform Mobile iOS and android

## Example

### Get Geo Boundaries

Android
```kotlin
val h3Indexes = listOf(
    "87283082bffffff",
    "872830870ffffff",
    "872830820ffffff",
    "87283082effffff",
)
val polygons = H3.polygons(h3Indexes)

/*
[Polygon(h3Index=87283082bffffff, vertices=[LatLng(lat=37.78529347359727, lng=-122.41077092287513), LatLng(lat=37.79707086149341, lng=-122.40326874464051), LatLng(lat=37.80760100422449, lng=-122.41208776737979), ...
*/
```

iOS
```swift
let h3Indexes = [
    "87283082bffffff",
    "872830870ffffff",
    "872830820ffffff",
    "87283082effffff",
]

let polygons = H3.Companion().polygons(h3Indexes: h3Indexes)

print("polygons", polygons)

/*
[Polygon(h3Index=87283082bffffff, vertices=[LatLng(lat=37.78529347359727, lng=-122.41077092287512), LatLng(lat=37.79707086149341, lng=-122.4032687446405), LatLng(lat=37.80760100422449, lng=-122.41208776737977), ...
*/
```

## Installation

### Gradle Settings

Add this gradle settings into your `KMP(Kotlin Multiplatform Application)`

#### build.gradle.kts in root

```kotlin
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

#### build.gradle.kts in shared

```kotlin

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

val h3Lib = "com.linecorp:abc-kmm-h3:0.1.7"

kotlin {
    android()
    ios {
        binaries
            .filterIsInstance<Framework>()
            .forEach {
                it.baseName = "shared"
                it.transitiveExport = true
                it.export(h3Lib)
            }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(h3Lib)
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
                implementation("com.google.android.material:material:1.2.1")
                implementation(h3Lib)
                api(h3Lib)
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
                implementation(h3Lib)
                api(h3Lib)
            }
        }
        val iosTest by getting
    }
}
```

### Project Settings

Android
- Copy and paste jniLibs folder into src/main in your project

iOS
- Cocoapods

1. Create `Podfile` with below setting in your project root.

    ```bash
    use_frameworks!

    platform :ios, '10.0'

    install! 'cocoapods', :deterministic_uuids => false

    target 'iosApp' do
        pod 'shared', :path => '../shared/'
    end
    ```

2. Run command `pod install` on the terminal


## TODO
- [x] Get Geo Boundaries
