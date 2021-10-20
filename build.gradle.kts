import org.jetbrains.kotlin.cli.common.toBooleanLenient

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
    kotlin("multiplatform")
}

val libVersion = "0.1.7"
val isSnapshotUpload = false

group = "com.linecorp"
version = libVersion

kotlin {
    android {
        publishAllLibraryVariants()
    }

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

    sourceSets {
        val commonMain by getting {
            dependencies {
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

val isMavenLocal = System.getProperty("maven.local").toBooleanLenient() ?: false
if (!isMavenLocal) {
    publishing {
        publications {
            create<MavenPublication>("NaverRepo") {
                if (isSnapshotUpload) {
                    from(components.findByName("debug"))
                } else {
                    from(components.findByName("release"))
                }

                groupId = project.group.toString()
                artifactId = artifactId
                version = if (isSnapshotUpload) "$libVersion-SNAPSHOT" else libVersion

                pom {
                    name.set("$groupId:$artifactId")
                    url.set("https://github.com/line/${project.name}")
                    description.set("h3 for Kotlin Multiplatform")

                    developers {
                        developer {
                            id.set("pisces")
                            name.set("Steve Kim")
                            email.set("pisces@linecorp.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:ssh://github.com/line/${project.name}.git")
                        developerConnection.set("scm:git:ssh://github.com/line/${project.name}.git")
                        url.set("http://github.com/line/${project.name}")
                    }
                }
            }
        }
        repositories {
            maven {
                isAllowInsecureProtocol = true
                url = if (isSnapshotUpload) {
                    uri("http://repo.navercorp.com/m2-snapshot-repository")
                } else {
                    uri("http://repo.navercorp.com/maven2")
                }

                credentials {
                    username = System.getProperty("maven.username") ?: ""
                    password = System.getProperty("maven.password") ?: ""
                }
            }
        }
    }
}