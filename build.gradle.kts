
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val agpVersion: String by project
        val kotlinVersion: String by project
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    ext {
        set("compileSdkVersion", 30)
        set("minSdkVersion", 21)
        set("targetSdkVersion", 30)
    }

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
    kotlin("multiplatform")
}

group = "com.linecorp.abc"
version = "0.1.7"

val isSnapshotUpload = false
val gitRepositoryName = "abc-${project.name}"

kotlin {
    val enableGranularSourceSetsMetadata = project.extra["kotlin.mpp.enableGranularSourceSetsMetadata"]?.toString()?.toBoolean() ?: false
    if (enableGranularSourceSetsMetadata) {
        val iosTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64
        iosTarget("ios") {
            val main by compilations.getting {
                val h3 by cinterops.creating {
                    defFile(project.file("cinterop/h3/h3.def"))
                    includeDirs(project.file("cinterop/h3"))
                }
            }
            main.enableEndorsedLibs = true
        }
    } else {
        ios {
            val main by compilations.getting {
                val h3 by cinterops.creating {
                    defFile(project.file("cinterop/h3/h3.def"))
                    includeDirs(project.file("cinterop/h3"))
                }
            }
            main.enableEndorsedLibs = true
        }
    }

    android {
        publishAllLibraryVariants()
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.uber:h3:3.7.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
                implementation("org.mockito.kotlin:mockito-kotlin:2.2.10")
                implementation("ch.qos.logback:logback-classic:1.2.3")
                implementation("org.robolectric:robolectric:4.3")
                implementation("org.json:json:20210307")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    val compileSdkVersion = project.ext.get("compileSdkVersion") as Int
    val minSdkVersion = project.ext.get("minSdkVersion") as Int
    val targetSdkVersion = project.ext.get("targetSdkVersion") as Int

    compileSdk = compileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

publishing {
    publications {
        create<MavenPublication>("abcH3") {
            if (isSnapshotUpload) {
                from(components.findByName("debug"))
            } else {
                from(components.findByName("release"))
            }

            groupId = project.group.toString()
            artifactId = project.name
            version = if (isSnapshotUpload) "${project.version}-SNAPSHOT" else project.version.toString()

            pom {
                name.set(artifactId)
                description.set("A library to convert Uber's H3 geo-index to LatLng vertices for Kotlin Multiplatform Mobile iOS and android")
                url.set("https://github.com/line/$gitRepositoryName")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("LINE Corporation")
                        email.set("dl_oss_dev@linecorp.com")
                        url.set("https://engineering.linecorp.com/en/")
                    }
                    developer {
                        id.set("pisces")
                        name.set("Steve Kim")
                        email.set("pisces@linecorp.com")
                    }
                }

                scm {
                    connection.set("scm:git@github.com:line/$gitRepositoryName.git")
                    developerConnection.set("scm:git:ssh://github.com:line/$gitRepositoryName.git")
                    url.set("http://github.com/line/$gitRepositoryName")
                }
            }
        }
    }
    repositories {
        maven {
            name = "MavenCentral"
            url = if (isSnapshotUpload) {
                uri("https://oss.sonatype.org/content/repositories/snapshots/")
            } else {
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            }

            val sonatypeUsername: String? by project
            val sonatypePassword: String? by project

            println("sonatypeUsername, sonatypePassword -> $sonatypeUsername, ${sonatypePassword?.masked()}")

            credentials {
                username = sonatypeUsername ?: ""
                password = sonatypePassword ?: ""
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project

    println("signingKey, signingPassword -> ${signingKey?.slice(0..9)}, ${signingPassword?.masked()}")

    isRequired = !isSnapshotUpload
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["abcH3"])
}

fun String.masked() = map { "*" }.joinToString("")