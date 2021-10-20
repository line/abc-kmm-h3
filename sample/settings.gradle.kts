pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                val agpVersion: String by settings
                useModule("com.android.tools.build:gradle:$agpVersion")
            }
        }
    }
}
rootProject.name = "sample"

include(":androidApp")
include(":shared")